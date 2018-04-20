package com.superxc.chineseIdioms.util;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "加载数据库驱动失败！", "错误", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
    public static Connection getConnect() {
        String db_url = AppConfigure.getProperty("DB_URL");
        String db_user = AppConfigure.getProperty("DB_USER");
        String db_password = AppConfigure.getProperty("DB_PASSWORD");
        try {
            return DriverManager.getConnection(db_url, db_user, db_password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "无法连接服务器！", "错误", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public static void close(Connection connection) {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
