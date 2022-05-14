package chat.server.gui;

import chat.server.core.ChatServer;
import chat.server.core.ChatServerListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler, ChatServerListener {
    private static final int POS_X = 100;
    private static final int POS_Y = 100;
    private static final int WIDTH = 200;
    private static final int HEIGHT = 100;

    private final ChatServer server = new ChatServer(this);
    private final JButton btnStart = new JButton("Start");
    private final JButton btnStop = new JButton("Stop");
    private final JPanel pannelTop = new JPanel(new GridLayout(1,2));
    private final JTextArea log = new JTextArea();

    private ServerGUI() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat server");
        setAlwaysOnTop(true);
        log.setEnabled(false);
        log.setLineWrap(true);
        //setLayout(new GridLayout(1, 2));
        JScrollPane scrollLog = new JScrollPane(log);
        btnStart.addActionListener(this);
        btnStop.addActionListener(this);
        pannelTop.add(btnStart);
        pannelTop.add(btnStop);
        add(pannelTop, BorderLayout.NORTH);
        add(scrollLog, BorderLayout.CENTER);
       // add(btnStart);
       // add(btnStop);
        setVisible(true);
    }

    public static void main(String[] args) {
        System.out.println("main started");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ServerGUI();
            }
        });
        System.out.println("main ended");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnStart) {
            server.start(8189);
        } else if (src == btnStop) {
            server.stop();
        } else {
            throw new RuntimeException("Action for component unimplemented");
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        String msg = "Exception in thread " + t.getName() +
                " " + e.getClass().getCanonicalName() +
                ": " + e.getMessage() +
                "\n\t" + e.getStackTrace()[0];
        JOptionPane.showMessageDialog(null, msg,
                "Exception", JOptionPane.ERROR_MESSAGE);
    }
    @Override
    public void onChatServerMessage(String msg){
        SwingUtilities.invokeLater(() -> {
          log.append(msg + "\n");
          log.setCaretPosition(log.getDocument().getLength());
        });
    }
}
