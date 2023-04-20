package com.ustaz1505.easypm.database;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class Database {
    private final String url;
    public Database() throws Exception {

        url = "jdbc:sqlite:plugins/EasyPM/database.db";
        Class.forName("org.sqlite.JDBC").getDeclaredConstructor().newInstance();

        Connection c = getConnection();
        Statement s = c.createStatement();

        s.executeUpdate("CREATE TABLE IF NOT EXISTS \"epm_users\" (" +
                "\"id\" INTEGER NOT NULL UNIQUE, " +
                "\"username\" TEXT, " +
                "\"uuid\" TEXT, " +
                "PRIMARY KEY(\"id\" AUTOINCREMENT)" +
                ");");

        s.executeUpdate("CREATE TABLE IF NOT EXISTS \"epm_messages\" (" +
                "\"id\"INTEGER NOT NULL UNIQUE," +
                "\"time\"INTEGER NOT NULL," +
                "\"from\"INTEGER NOT NULL," +
                "\"to\"INTEGER NOT NULL," +
                "\"contents\"TEXT NOT NULL," +
                "PRIMARY KEY(\"id\" AUTOINCREMENT)" +
                ");");

        s.close();
        c.close();

    }

    public Connection getConnection() throws Exception {
        return DriverManager.getConnection(url);
    }

    public void addUser(String Username, String UUID) {
        try {
            Connection c = this.getConnection();
            Statement s = c.createStatement();
            s.executeUpdate("INSERT INTO epm_users ('username', 'uuid') VALUES ('" + Username + "', '" + UUID + "')");
            s.close();
            c.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public int countUsers(String Username) {
        try {
            Connection c = this.getConnection();
            Statement s = c.createStatement();
            ResultSet result = c.createStatement().executeQuery("SELECT COUNT(*) FROM epm_users WHERE username = '" + Username + "'");
            int temp = result.getInt(1);
            s.close();
            c.close();
            return temp;
        }
        catch(Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void addMessage(String Message, String From, String To) {
        try {
            Connection c = this.getConnection();
            Statement s = c.createStatement();
            long time = System.currentTimeMillis() / 1000L;
            s.executeUpdate("INSERT INTO epm_messages ('time', 'from', 'to', 'contents') VALUES (" + time + ", " + getUserID(From) + ", " + getUserID(To) + ", '" + Message +"')");
            s.close();
            c.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    public int getUserID(String Username) {
        try {
            Connection c = this.getConnection();
            Statement s = c.createStatement();
            ResultSet result = c.createStatement().executeQuery("SELECT id from epm_users WHERE username = '"  + Username + "'");
            int temp = result.getInt(1);
            s.close();
            c.close();
            return temp;
        }
        catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String getUserByID(int Id) {
        try {
            Connection c = this.getConnection();
            Statement s = c.createStatement();
            ResultSet result = c.createStatement().executeQuery("SELECT username from epm_users WHERE id = " + Id);
            String temp = result.getString(1);
            s.close();
            c.close();
            return temp;
        }
        catch(Exception e) {
            e.printStackTrace();
            return "404";
        }
    }

    public List<List<? extends Serializable>> getAllMessages(String Username) {
        List<List<? extends Serializable>> messages = new java.util.ArrayList<>();
        try {
            Connection c = this.getConnection();
            Statement s = c.createStatement();
            ResultSet resultSet = c.createStatement().executeQuery("SELECT * FROM epm_messages WHERE \"from\" = " + getUserID(Username) + " OR \"to\" = " + getUserID(Username));
            while(resultSet.next()) {
                List<? extends Serializable> message = List.of(
                        resultSet.getLong(2),
                        resultSet.getInt(3),
                        resultSet.getInt(4),
                        resultSet.getString(5)
                );
                messages.add(message);
            }
            s.close();
            c.close();
            return messages;
        }
        catch(Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }
}
