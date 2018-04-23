package com.superxc.chineseIdioms.model;

import com.superxc.chineseIdioms.util.DB;
import com.superxc.chineseIdioms.util.Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class User {
    static final String tableName = "user";

    private final String username;
    private final String password;
    private int process;


    /**
     * name must only contain upper/lowerCase letter, digits, '-' or '_'
     * @param name
     * @param password_raw
     */
    public User(String name, String password_raw) {
        username = name;
        password = Util.MD5(password_raw);
        process = 0;
    }

    public User(User otherUser) {
        this.username = otherUser.username;
        this.password = otherUser.password;
        this.process = otherUser.process;
    }

    public String getUsername() {
        return username;
    }

    private String getPassword() {
        return password;
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        // TODO: 对传入的process进行处理
        this.process = process;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        User otherUser = (User) obj;

        return Objects.equals(username, otherUser.username) &&
                Objects.equals(password, otherUser.password);
    }

    public boolean save() {
        boolean saveSuccess = false;
        if (exist()) {
            return update();
        } else {
            return create();
        }
    }

    private boolean update() {
        boolean success = false;
        Connection connection = DB.getConnect();
        try {
            Statement statement = connection.createStatement();
            String sql = String.format("UPDATE %s SET process=%d WHERE username='%s'",
                    tableName, getProcess(), getUsername());
            success = statement.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DB.close(connection);
        return success;
    }

    private boolean create() {
        boolean success = false;
        Connection connection = DB.getConnect();
        try {
            Statement statement = connection.createStatement();
            String sql = String.format("INSERT INTO %s (username, password, process) VALUES ('%s', '%s', %d)",
                    tableName, getUsername(), getPassword(), getProcess());
             success = statement.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DB.close(connection);
        return success;
    }

    private boolean exist() {
        return User.login(this) != null;
    }

    /**
     *
     * @param incompleteUser contains only username and password
     * @return return completeUser if login success, or return null if login failure.
     */
    public static User login(User incompleteUser) {
        if (incompleteUser == createAnonymousUser()) {
            return incompleteUser;
        }
        Connection connection = DB.getConnect();
        User completeUser = null;
        try {
            Statement statement = connection.createStatement();
            String sql = String.format("SELECT username, password, process FROM %s WHERE username='%s' AND password='%s'",
                    tableName, incompleteUser.getUsername(), incompleteUser.getPassword());

            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                completeUser = new User(incompleteUser);
                completeUser.setProcess(resultSet.getInt("process"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DB.close(connection);
        return completeUser;
    }

    public static User createAnonymousUser() {
        return AnonymousUser.getAnonymousUser();
    }
}
