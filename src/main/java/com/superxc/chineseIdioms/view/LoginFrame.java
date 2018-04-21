package com.superxc.chineseIdioms.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends JFrame{

    public LoginFrame() {
        setTitle("用户登录");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel northPanel = new JPanel();
        JPanel centerPanel = new JPanel();
        JPanel southPanel = new JPanel();

        initializeNorthComponent(northPanel);
        add(northPanel, BorderLayout.NORTH);

        initializeCenterComponent(centerPanel);
        add(centerPanel, BorderLayout.CENTER);

        initializeSouthComponent(southPanel);
        add(southPanel, BorderLayout.SOUTH);

        pack();

        moveToScreenCenter();
        setResizable(false);
    }

    private void moveToScreenCenter() {
        setLocationRelativeTo(null);
    }

    private void initializeNorthComponent(JPanel northPanel) {
        JLabel labelRegister = new JLabel("没有帐号？new一个");
        labelRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // 和窗口背景色溶为一体的底边框
        labelRegister.setBorder(BorderFactory.createMatteBorder(0, 0,1, 0, getBackground()));

        labelRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                labelRegister.setBorder(BorderFactory.createMatteBorder(0, 0,1, 0, Color.BLUE));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labelRegister.setBorder(BorderFactory.createMatteBorder(0, 0,1, 0, getBackground()));
            }
        });
        // TODO: 鼠标放上去的时候，加一条下划线，然后鼠标变成手
        northPanel.add(labelRegister);
    }

    private void initializeCenterComponent(JPanel centerPanel) {
        // TODO: 改用网格组布局
        centerPanel.setLayout(new GridLayout(2, 2));

        centerPanel.add(new JLabel("用户名：", SwingConstants.RIGHT));
        JTextField textFieldUsername = new JTextField();
        centerPanel.add(textFieldUsername);

        centerPanel.add(new JLabel("密码：", SwingConstants.RIGHT));
        JPasswordField passwordField = new JPasswordField();
        centerPanel.add(passwordField);
    }

    private void initializeSouthComponent(JPanel southPanel) {
        JButton buttonAnonymouseLogin = new JButton("匿名登录");
        southPanel.add(buttonAnonymouseLogin);

        JButton buttonLogin = new JButton("登录");
        southPanel.add(buttonLogin);

        JButton buttonExit = new JButton("退出");
        buttonExit.addActionListener(e -> System.exit(0));
        southPanel.add(buttonExit);
    }
}
