package com.superxc.chineseIdioms.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RegisterFrame extends CenterableFrame {
    private JFrame loginFrame;
    private JTextField textFieldUsername;
    private JPasswordField passwordField;
    private JPasswordField rePasswordField;

    public RegisterFrame(JFrame loginFrame) {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                hideCurrentFrameAndShowLoginFrame();
            }
        });
        this.loginFrame = loginFrame;
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        JPanel centerPanel = new JPanel();
        JPanel southPanel = new JPanel();

        initializeCenterPanel(centerPanel);
        add(centerPanel, BorderLayout.CENTER);

        initializeSouthPanel(southPanel);
        add(southPanel, BorderLayout.SOUTH);

        pack();
        moveToScreenCenter();
        setResizable(false);
    }

    private void initializeSouthPanel(JPanel southPanel) {
        JButton buttonRegister = new JButton("注册");
        southPanel.add(buttonRegister);

        JButton buttonCancel = new JButton("取消");
        buttonCancel.addActionListener(e -> hideCurrentFrameAndShowLoginFrame());
        southPanel.add(buttonCancel);
    }

    private void initializeCenterPanel(JPanel centerPanel) {
        centerPanel.setLayout(new GridBagLayout());

        centerPanel.add(new JLabel("用户名："), new GBC(0, 0, 1, 1).setAnchor(GBC.EAST));
        textFieldUsername = new JTextField();
        centerPanel.add(textFieldUsername, new GBC(1, 0, 2, 1).setFill(GBC.HORIZONTAL).setWeight(100, 0));

        centerPanel.add(new JLabel("密码："), new GBC(0, 1, 1, 1).setAnchor(GBC.EAST));
        passwordField = new JPasswordField();
        centerPanel.add(passwordField, new GBC(1, 1, 2, 1).setFill(GBC.HORIZONTAL).setWeight(100, 0));

        centerPanel.add(new JLabel("重复密码："), new GBC(0, 2, 1, 1).setAnchor(GBC.EAST).setInsets(0, 5, 0, 0));
        rePasswordField = new JPasswordField();
        centerPanel.add(rePasswordField, new GBC(1, 2, 2, 1).setFill(GBC.HORIZONTAL).setWeight(100, 0));
    }

    public void clearAllTextField() {
        textFieldUsername.setText("");
        passwordField.setText("");
        rePasswordField.setText("");
    }

    public void hideCurrentFrameAndShowLoginFrame() {
        setVisible(false);
        loginFrame.setVisible(true);
    }
}
