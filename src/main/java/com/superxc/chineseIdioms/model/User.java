package com.superxc.chineseIdioms.model;

import com.superxc.chineseIdioms.util.AppConfigure;
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

    // TODO: 这里不清真，id应该是final类型的
    private int id;
    private final String username;
    private final String password;
    private int totalStar;

    public int getId() {
        return id;
    }

    public int getTotalStar() {
        return totalStar;
    }

    /**
     * username must only contain upper/lowerCase letter, digits, '-' or '_'
     * @param username
     * @param password
     */
    public User(String username, String password) {
        id = 0;
        this.username = username;
        this.password = password;
    }

    public User(String username, int totalStar) {
        this.username = username;
        this.totalStar = totalStar;
        password = null;
    }

    public User(int id, String username, String password) {
        this(username, password);
        this.id = id;
    }

    public User(User otherUser) {
        this.username = otherUser.username;
        this.password = otherUser.password;
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
            if (AppConfigure.getBooleanProperty("SQL_SHOW")) {
                System.out.println(sql);
            }
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
            if (AppConfigure.getBooleanProperty("SQL_SHOW")) {
                System.out.println(sql);
            }
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
            String sql = String.format("SELECT id, username, password FROM %s WHERE username='%s' AND password='%s'",
                    tableName, incompleteUser.getUsername(), incompleteUser.getPassword());
            if (AppConfigure.getBooleanProperty("SQL_SHOW")) {
                System.out.println(sql);
            }
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

    public static List<User> getTopNOrderByTotalStartDesc(int n) {
        List<User> userList = new ArrayList<>();

        Connection connection = DB.getConnect();
        try {
            Statement statement = connection.createStatement();
            String sql = String.format("SELECT username, SUM(starCount) AS totalStarCount FROM %s AS a JOIN clearStage AS b ON a.id=b.userID GROUP BY a.id ORDER BY totalStarCount DESC LIMIT 10",
                    tableName, n);
            if (AppConfigure.getBooleanProperty("SQL_SHOW")) {
                System.out.println(sql);
            }
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                userList.add(new User(
                        resultSet.getString("username"),
                        resultSet.getInt("totalStarCount")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    private static User resultGet(ResultSet resultSet) throws SQLException {

        return new User(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("password")
        );
    }


    public static String encryptPassword(String password) {
        return Util.MD5(password);
    }

    public int getMaxPassStageId() {
        Connection connect = DB.getConnect();
        int maxPassStageId = 0;

        try {
            Statement statement = connect.createStatement();
            String sql = String.format("SELECT MAX(stageId) AS maxStageId FROM clearStage WHERE userId=%d", getId());
            if (AppConfigure.getBooleanProperty("SQL_SHOW")) {
                System.out.println(sql);
            }
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                maxPassStageId = resultSet.getInt("maxStageId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DB.close(connect);
        return maxPassStageId;
    }

    public int getStageStarCount(int stageId) {
        Connection connect = DB.getConnect();
        int starCount = 0;
        try {
            Statement statement = connect.createStatement();
            String sql = String.format("SELECT starCount FROM clearStage WHERE userId=%d AND stageId=%d", getId(), stageId);
            if (AppConfigure.getBooleanProperty("SQL_SHOW")) {
                System.out.println(sql);
            }
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                starCount = resultSet.getInt("starCount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DB.close(connect);
        return starCount;
    }
}
