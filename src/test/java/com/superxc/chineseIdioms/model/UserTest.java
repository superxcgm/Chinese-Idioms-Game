package com.superxc.chineseIdioms.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class UserTest {

    @Test
    public void userBasicTest() {
        User user = new User("superxc", "123456");
        Assert.assertEquals(user.getUsername(), "superxc");
    }

    @Test
    public void should_return_false_when_login_failure() {
        User user = new User("doNotExist", "123456");
        Assert.assertNull(User.login(user));
    }

    @Test
    public void should_return_true_when_login_success() {
        User user = new User("superxc", "123456");
        Assert.assertNotNull(User.login(user));
    }

    @Test
    public void should_return_true_when_save_add_new_user_success() {
        String username = "testuser" + System.currentTimeMillis() / 10;
        User user = new User(username, "123456");
        Assert.assertTrue(user.save());
    }

    @Test
    public void should_return_true_when_save_update_user_success() {
        User user = new User("superxc", "123456");
        Random random = new Random();
        user.setProcess(Math.abs(random.nextInt()) % 10);
        Assert.assertTrue(user.save());
    }

    @Test
    public void anonymousUserTest() {
        User anonymousUser = User.createAnonymousUser();
        Assert.assertEquals("anonymous", anonymousUser.getUsername());
    }
}
