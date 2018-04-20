package com.superxc.chineseIdioms.util;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class UtilTest {
//    @Ignore
    @Test
    public void MD5Test() {
        Assert.assertEquals("a2854d99bb0d12cd0bae0e536a0084a2", Util.MD5("123456"));
    }
}
