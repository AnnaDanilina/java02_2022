package server;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String name;
    ExecutorService executorService;
    BufferedWriter writer;

    public ClientHandler(Server server, Socket socket, ExecutorService executorService, Logger logger) {
        try {
            this.server = server;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.name = "";
            this.writer = null;
            executorService.execute(() -> {
                try {
                    while (true) {
                        String str = in.readUTF();
                        if (str.startsWith("/auth")) {
                            String[] parts = str.split("\\s");
                            String nick = server.getAuthService().getNickByLoginPass(parts[1], parts[2]);
                            if (nick != null) {
                                if (!server.isNickBusy(nick)) {
                                    sendMsg("/authok " + nick);
                                    name = nick;
                                    server.broadcastMsg(name + " зашел в чат");
                                    server.subscribe(this);
                                    break;
                                } else sendMsg("Учетная запись уже используется");
                            } else {
                                sendMsg("Неверные логин/пароль");
                            }
                        }
                    }
                    writer = new BufferedWriter(new FileWriter("demo.txt", true));
                    while (true) {
                        String str = in.readUTF();
                      // System.out.println("from " + name + ": " + str);
                        // ЛОГИРОВАНИЕ
                        logger.log(Level.INFO,"from " + name + ": " + str);

                        if (str.startsWith("/")) {
                            if (str.equals("/end")) break;
                            if (str.startsWith("/w ")) {
                                String[] strar = str.split(" ", 3);
                                server.sendMsgToNick(this, strar[1], strar[2]);
                            }
                            if (str.startsWith("/changenick ")) {
                                String newNick = str.split("\\s")[1];
                                if (server.getAuthService().changeNick(this, newNick)) {
                                    changeNick(newNick);
                                } else {
                                    sendMsg("Указанный ник уже кем-то занят");
                                }
                            }
                        } else {
                            server.broadcastMsg(name + ": " + str);
                            server.broadcastMsg(name + ": " + str);
                            writer.newLine();
                            writer.write(name + ": " + str);
                            writer.flush();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    server.unsubscribe(this);
                    server.broadcastMsg(name + " вышел из чата");
                    try {
                        socket.close();
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (
                IOException e) {
            e.printStackTrace();
        }

    }

    public void changeNick(String newNick) {
        server.broadcastMsg(name + " сменил ник на " + newNick);
        name = newNick;
        sendMsg("/yournickis " + newNick);
        server.broadcastClientList();
    }


    public String getName() {
        return name;
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
