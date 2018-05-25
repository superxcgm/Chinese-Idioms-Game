package com.superxc.chineseIdioms.model;

public class AnonymousUser extends User{
    private static final AnonymousUser user = new AnonymousUser();

    private AnonymousUser() {
        super("anonymous", null, 0, "", 0);
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
