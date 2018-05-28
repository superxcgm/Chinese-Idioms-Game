package com.superxc.chineseIdioms.model;

import com.superxc.chineseIdioms.util.DB;
import com.superxc.chineseIdioms.util.Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private static final String tableName = "user";
    private static final String STAR_ICON = "✪";

    private final String username;
    private final String password;


    /**
     * username must only contain upper/lowerCase letter, digits, '-' or '_'
     * @param username
     * @param password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(User otherUser) {
        this.username = otherUser.username;
        this.password = otherUser.password;
    }

    public String getStageStarString(int stage) {
        switch (getStageStarCount(stage)) {
            case 0:
                return "";
            case 1:
                return STAR_ICON;
            case 2:
                return STAR_ICON + STAR_ICON;
            case 3:
                return STAR_ICON + STAR_ICON + STAR_ICON;
        }
        return null;
    }

    public void setStageStar(int stage, int starCount) {

    }

    public int getStageStarCount(int stage) {
        return 0;
    }

    public String getUsername() {
        return username;
    }

    private String getPassword() {
        return password;
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
        return true;
    }

    private boolean create() {
        boolean success = false;
        Connection connection = DB.getConnect();
        try {
            Statement statement = connection.createStatement();
            String sql = String.format("INSERT INTO %s (username, password) VALUES ('%s', '%s')",
                    tableName, getUsername(), getPassword());
             success = statement.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DB.close(connection);
        return success;
    }

    public boolean exist() {
        boolean isExist = false;
        Connection connect = DB.getConnect();
        try {
            Statement statement = connect.createStatement();
            String sql = String.format("SELECT * FROM %s WHERE username='%s'", tableName, getUsername());
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                isExist = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DB.close(connect);
        return isExist;
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
            // TODO: 这里需要预防SQL注入攻击
            String sql = String.format("SELECT username, password FROM %s WHERE username='%s' AND password='%s'",
                    tableName, incompleteUser.getUsername(), incompleteUser.getPassword());

            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                completeUser = resultGet(resultSet);
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

    public static List<User> getTopNOrderByProcessDesc(int n) {
        List<User> userList = new ArrayList<>();

        Connection connection = DB.getConnect();
        try {
            Statement statement = connection.createStatement();
            String sql = String.format("SELECT username, password, process, star, totalStars FROM %s ORDER BY process DESC LIMIT %d",
                    tableName, n);

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                userList.add(resultGet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public static List<User> getTopNOrderByTotalStartDesc(int n) {
        List<User> userList = new ArrayList<>();

        Connection connection = DB.getConnect();
        try {
            Statement statement = connection.createStatement();
            String sql = String.format("SELECT username, password, process, star, totalStars FROM %s ORDER BY totalStars DESC LIMIT %d",
                    tableName, n);

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                userList.add(resultGet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    private static User resultGet(ResultSet resultSet) throws SQLException {
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");

        return new User(username, password);
    }


    public static String encryptPassword(String password) {
        return Util.MD5(password);
    }
}
