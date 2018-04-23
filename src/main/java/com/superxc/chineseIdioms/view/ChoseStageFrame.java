package com.superxc.chineseIdioms.view;

import com.superxc.chineseIdioms.model.User;

import javax.swing.*;

public class ChoseStageFrame extends JFrame {
    public ChoseStageFrame(User user) {
        setSize(800, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        moveToScreenCenter();
    }
    private void moveToScreenCenter() {
        setLocationRelativeTo(null);
    }
}
