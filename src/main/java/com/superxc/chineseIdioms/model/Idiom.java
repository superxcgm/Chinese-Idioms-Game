package com.superxc.chineseIdioms.model;

import com.superxc.chineseIdioms.util.DB;
import com.superxc.chineseIdioms.util.Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Idiom {
    static final String tableName = "idiom";
    private int groupId;
    private String value;
    private String description;

    private Idiom(int groupId, String value, String description) {
        this.groupId = groupId;
        this.value = value;
        this.description = description;
    }

    public static List<Idiom> getIdioms(int givenGroupId) {
        List<Idiom> idioms = new ArrayList<>();
        Connection connection = DB.getConnect();

        try {
            Statement statement = connection.createStatement();
            String sql = String.format("SELECT `value`,`description` FROM %s WHERE `groupId`=%d",
                    tableName, givenGroupId);
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                idioms.add(new Idiom(givenGroupId, resultSet.getString("value"), resultSet.getString("description")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DB.close(connection);
        return idioms;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public String[] getSplit() {
        return value.split("");
    }

    /**
     * 插入成语到数据库
     */
    public static void insertData(String filename) {
        String content = Util.fileReadToString(filename);
        if (content == null) {
            System.out.println("无法读取文件！");
            System.exit(0);
        }
        String[] lines = content.split("\n");

        Connection connection = DB.getConnect();

        List<Integer> unresolveLines = new ArrayList<>();
        List<Integer> insertErrorLines = new ArrayList<>();

        try {
            insertRecords(lines, connection.createStatement(), unresolveLines, insertErrorLines);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DB.close(connection);

        System.out.println("无法解析这些行：" + unresolveLines);
        System.out.println("这些行插入失败：" + insertErrorLines);
    }

    private static void insertRecords(String[] lines, Statement statement, List<Integer> unresolveLines, List<Integer> insertErrorLines) throws SQLException {

        for (int i = 0; i < lines.length; i++) {
            String[] elems = lines[i].split("\\|");
            if (elems.length != 3) {
                unresolveLines.add(i + 1);
                continue;
            }
            String sql = String.format("INSERT INTO %s(`groupId`, `value`, `description`) VALUES (%d, '%s', '%s')",
                    tableName, Integer.parseInt(elems[0]), elems[1], elems[2]);
            if (statement.executeUpdate(sql) > 0) {
                continue;
            }
            insertErrorLines.add(i + 1);
        }
    }
}
