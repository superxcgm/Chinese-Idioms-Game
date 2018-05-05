package com.superxc.chineseIdioms.util;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class UtilTest {
//    @Ignore
    @Test
    public void MD5Test() {
        Assert.assertEquals("a2854d99bb0d12cd0bae0e536a0084a2", Util.MD5("123456"));
    }

    @Test
    public void fileReadToStringTest() throws Exception{

        String filename = this.getClass().getResource("/test_file.xc").getFile();
        File file = new File(filename);
        PrintStream printStream = new PrintStream(new FileOutputStream(file));

        String str = "世界，你好！\n好";
        printStream.print(str);
        printStream.close();

        Assert.assertEquals(str, Util.fileReadToString(filename));
    }
}
