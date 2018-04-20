package com.superxc.chineseIdioms.model;

import com.superxc.chineseIdioms.exception.NotSaveableException;

public class AnonymousUser extends User{
    private static final AnonymousUser user = new AnonymousUser();

    private AnonymousUser() {
        super("anonymous", null);
    }

    @Override
    public boolean save() {
        // TODO: 将数据保存在本地
        return true;
    }

    public static AnonymousUser getAnonymousUser() {
        return user;
    }
}
