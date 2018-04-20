package com.superxc.chineseIdioms.util;

import org.junit.Test;

import java.sql.Connection;

public class DBTest {
    @Test
    public void connectTest() {
        Connection connection = DB.getConnect();
        DB.close(connection);
    }
}
