package com.superxc.chineseIdioms.util;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfigure {
    private static final Properties properties = new Properties();

    static {
        try {
            InputStream inputStream = AppConfigure.class.getClassLoader().getResourceAsStream("app.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "配置文件加载失败!", "错误", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static boolean getBooleanProperty(String key) {
        return getProperty(key).equals("true");
    }
}
