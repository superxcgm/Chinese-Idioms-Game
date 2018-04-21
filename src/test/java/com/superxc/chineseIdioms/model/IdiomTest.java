package com.superxc.chineseIdioms.model;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

public class IdiomTest {

    @Test
    public void idiomTest() {
        List<Idiom> idioms = Idiom.getIdioms(1);

        for (Idiom idiom : idioms) {
            System.out.println(idiom.getValue());
            System.out.println(idiom.getDescription());
        }
        Assert.assertEquals(10, idioms.size());
    }

    /**
     * 插入成语数据的时候使用
     */
    @Ignore
    @Test
    public void insertIdiomsIntoDB() {
        Idiom.insertData("/Users/yujiali/IdeaProjects/Chinese-Idioms-Game/chinese-idioms.txt");
    }
}
