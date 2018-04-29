package com.superxc.chineseIdioms.view;

import com.superxc.chineseIdioms.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ChoseStageFrame extends CenterableFrame {

    // TODO: MAX_STAGE 应该从数据库获取
    private static final int MAX_STAGE = 20;
    public static final int COLS = 5;
    private JPanel centerPanel;
    private User user;

    public ChoseStageFrame(User user) {
        // TODO: 选关页面出现之前，应该先显示一个玩法介绍
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.user = user;

        setTitle("选择关卡");
        centerPanel = new JPanel();

        initializeCenterPanel(centerPanel);
        add(centerPanel, BorderLayout.CENTER);

        pack();
        moveToScreenCenter();
    }

    private void initializeCenterPanel(JPanel centerPanel) {
        int rows = getRows();
        centerPanel.setLayout(new GridLayout(rows, COLS));
        for (int i = 1; i <= MAX_STAGE; i++) {
            JButton button = new JButton(i + "");
            button.addActionListener(choseStageListener());
            if (i > user.getProcess() + 1) {
                button.setEnabled(false);
            }
            centerPanel.add(button);
        }
    }

    private int getRows() {
        int rows = MAX_STAGE / COLS;
        if (MAX_STAGE % COLS != 0) {
            rows++;
        }
        return rows;
    }

    private ActionListener choseStageListener() {
        return e -> {
            JFrame gameFrame = new GameFrame(this, user, Integer.parseInt(e.getActionCommand()));
            setVisible(false);
            gameFrame.setVisible(true);
        };
    }
}
