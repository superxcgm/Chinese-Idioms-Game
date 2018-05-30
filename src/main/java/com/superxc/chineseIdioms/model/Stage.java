package com.superxc.chineseIdioms.model;

import com.superxc.chineseIdioms.util.AppConfigure;
import com.superxc.chineseIdioms.util.DB;
import com.superxc.chineseIdioms.util.Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stage {
    private static final String tableName = "stage";

    private int id;
    private List<Idiom> idioms;
    private int totalTime;

    public int getTotalTime() {
        return totalTime;
    }

    public int getId() {
        return id;
    }

    public List<Idiom> getIdioms() {
        return idioms;
    }

    private Stage() {
    }

    private Stage(int id, List<Idiom> idioms, int totalTime) {
        this.id = id;
        this.idioms = idioms;
        this.totalTime = totalTime;
    }

    public static Stage getStage(int stageId) {
        Connection connect = DB.getConnect();
        Stage stage = null;
        try {
            Statement statement = connect.createStatement();
            int totalTime = 0;
            // get totalTime;
            String sql = String.format("SELECT totalTime FROM level AS a JOIN %s AS b ON a.id=b.levelID WHERE b.id=%d", tableName, stageId);
            if (AppConfigure.getBooleanProperty("SQL_SHOW")) {
                System.out.println(sql);
            }
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                totalTime = resultSet.getInt("totalTime");
            } else {
                // TODO: 无法获得该关卡对应的难度的总时长
            }

            stage = new Stage(stageId, Idiom.getIdioms(stageId), totalTime);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DB.close(connect);
        return stage;
    }

    public static Map<Integer, Integer> getUserClearStages(User user) {
        if (user == User.createAnonymousUser()) {
            return LocaleClearStage.get();
        }
        // map: stageId -> starCount
        Map<Integer, Integer> mapStageIdToStarCount = new HashMap<>();

        Connection connect = DB.getConnect();
        try {
            Statement statement = connect.createStatement();
            String sql = String.format("SELECT stageId, starCount FROM clearStage WHERE userId=%d", user.getId());
            if (AppConfigure.getBooleanProperty("SQL_SHOW")) {
                System.out.println(sql);
            }
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                mapStageIdToStarCount.put(resultSet.getInt("stageId"),
                        resultSet.getInt("starCount"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        DB.close(connect);
        return  mapStageIdToStarCount;
    }

    public static void addClearStage(User user, int stageId, int starCount) {
        if (user == User.createAnonymousUser()) {
            LocaleClearStage.add(stageId, starCount);
            return;
        }
        Connection connect = DB.getConnect();
        try {
            Statement statement = connect.createStatement();
            String sql = String.format("INSERT INTO clearStage(userId, stageId, starCount) VALUES(%d, %d, %d)",
                    user.getId(), stageId, starCount);
            if (AppConfigure.getBooleanProperty("SQL_SHOW")) {
                System.out.println(sql);
            }
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DB.close(connect);
    }

    public static void updateClearStage(User user, int stageId, int starCount) {
        if (user == User.createAnonymousUser()) {
            LocaleClearStage.update(stageId, starCount);
            return;
        }
        Connection connect = DB.getConnect();
        try {
            Statement statement = connect.createStatement();
            String sql = String.format("UPDATE clearStage SET starCount=%d WHERE userId=%d AND stageId=%d",
                    starCount, user.getId(), stageId);
            if (AppConfigure.getBooleanProperty("SQL_SHOW")) {
                System.out.println(sql);
            }
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DB.close(connect);
    }
}
