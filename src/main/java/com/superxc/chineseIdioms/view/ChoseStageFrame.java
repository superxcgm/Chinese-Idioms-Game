package com.superxc.chineseIdioms.view;

import com.superxc.chineseIdioms.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static com.superxc.chineseIdioms.view.LoginFrame.BEACH_JPG;

public class ChoseStageFrame extends BackgroundImageJFrame {

    // TODO: MAX_STAGE 应该从数据库获取
    private static final int MAX_STAGE = 30;
    private static final int COLS = 5;

    private List<JButton> btns = new ArrayList<>();

    private User user;

    private RankListFrame rankListFrame = null;

    public ChoseStageFrame(User user) {
        // TODO: 选关页面出现之前，应该先显示一个玩法介绍
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.user = user;

        setTitle("选择关卡");
        JPanel centerPanel = new TransparentJPanel();
        JPanel southPanel = new TransparentJPanel();

        initializeCenterPanel(centerPanel);
        add(centerPanel, BorderLayout.CENTER);

        initializeSouthPanel(southPanel);
        add(southPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setBackground(BEACH_JPG);
    }

    private void initializeSouthPanel(JPanel southPanel) {
        JButton buttonRankList = new JButton("排行榜");
        buttonRankList.addActionListener(e -> {
            if (rankListFrame == null) {
                rankListFrame = new RankListFrame(this);
            }
            rankListFrame.showFrame();
        });
        southPanel.add(buttonRankList);
    }

    private void initializeCenterPanel(JPanel centerPanel) {
        int rows = getRows();
        centerPanel.setLayout(new GridLayout(rows, COLS));
        for (int i = 1; i <= MAX_STAGE; i++) {
            JButton button = new JButton("<html><center>" + i + "</center>" + user.getStageStarString(i) + "</html>");
            button.setActionCommand(i + "");
            button.addActionListener(choseStageListener());
            centerPanel.add(button);
            btns.add(button);
        }
        updateStageStatus();
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

    public void showFrame() {
        setVisible(true);
        updateStageStatus();
    }

    private void updateStageStatus() {
        btns.forEach(btn -> {
            btn.setEnabled(true);
            int index = Integer.parseInt(btn.getActionCommand());
            btn.setText("<html><center>" + index + "</center>" + user.getStageStarString(index) + "</html>");
            if (Integer.parseInt(btn.getActionCommand()) > user.getProcess() + 1) {
                btn.setEnabled(false);
            }
        });
    }
}
