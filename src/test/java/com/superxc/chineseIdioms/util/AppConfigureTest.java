package com.superxc.chineseIdioms.util;

import org.junit.Assert;
import org.junit.Test;

public class AppConfigureTest {
    @Test
    public void getPropertyTest() throws Exception{
        Assert.assertEquals("haha", AppConfigure.getProperty("TEST"));
    }
}
