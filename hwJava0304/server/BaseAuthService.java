package server;

import java.sql.*;

public class BaseAuthService  implements AuthService  {

    private static Connection connection ;
    private static Statement stmt;
    private static PreparedStatement ps;

    @Override
    public void start() { }

    @Override
    public void stop() { }

    @Override
    public boolean changeNick(ClientHandler c, String newNick) {
        try {
            connect();
            ResultSet rs = stmt.executeQuery("SELECT nickname FROM users");
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()){
                if (rs.getString("nickname").equals(newNick)) return false;
            }
            stmt.executeUpdate("UPDATE users SET nickname  = '" + newNick + "' WHERE login like( '" + c.getName() + "');");

            connection.commit();
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return true;
        }finally {
            disconnect();
        }

    }

    public BaseAuthService() {}
    private static void connect() throws  Exception{
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:clients-db.sqlite");
        stmt = connection.createStatement();
    }

    private static void disconnect() {
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getNickByLoginPass(String login, String pass) {
        try {
            connect();
            ResultSet rs = stmt.executeQuery("SELECT nickname, login, password FROM users ");
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                if (rs.getString("login").equals(login) && rs.getString("password").equals(pass)) return rs.getString("nickname");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
        return null;
    }


}