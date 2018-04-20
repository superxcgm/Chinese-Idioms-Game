package com.superxc.chineseIdioms.util;

import org.junit.Assert;
import org.junit.Test;

public class UtilTest {
    @Test
    public void MD5Test() {
        Assert.assertEquals("e10adc3949ba59abbe56e057f20f883e", Util.MD5("123456"));
    }
}
