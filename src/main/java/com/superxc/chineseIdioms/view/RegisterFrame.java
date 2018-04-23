package com.superxc.chineseIdioms.view;

import javax.swing.*;

public class RegisterFrame extends CenterableFrame {
    private JFrame loginFrame;

    public RegisterFrame(JFrame loginFrame) {
        this.loginFrame = loginFrame;

        pack();
        moveToScreenCenter();
    }

    public void clearAllTextField() {

    }
}
