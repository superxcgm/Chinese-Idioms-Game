package com.superxc.chineseIdioms.view;

import com.superxc.chineseIdioms.model.User;

import javax.swing.*;

public class ChoseStageFrame extends CenterableFrame {
    public ChoseStageFrame(User user) {
        setSize(800, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        moveToScreenCenter();
    }
}
