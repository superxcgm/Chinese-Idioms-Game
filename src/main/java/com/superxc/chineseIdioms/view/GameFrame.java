package com.superxc.chineseIdioms.view;

import com.superxc.chineseIdioms.model.Idiom;
import com.superxc.chineseIdioms.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameFrame extends CenterableFrame{

    public static final String PROMPT_OFF = "提示：关";
    public static final int COLS = 7;
    private final JPanel centerPanel;
    private final JPanel southPanel;
    private final List<Idiom> idioms;

    public GameFrame(ChoseStageFrame choseStageFrame, User user, int stage) {

        centerPanel = new JPanel();
        southPanel = new JPanel();

        idioms = Idiom.getIdioms(stage);
        System.out.println("stage: " + stage);

        initializeCenterPanel(centerPanel);
        add(centerPanel, BorderLayout.CENTER);

        initializeSouthPanel(southPanel);
        add(southPanel, BorderLayout.SOUTH);

        pack();
        moveToScreenCenter();
        setResizable(false);
    }

    private void initializeSouthPanel(JPanel southPanel) {
        southPanel.setLayout(new BorderLayout());

        JLabel labelPrompt = new JLabel(PROMPT_OFF, JLabel.CENTER);
        southPanel.add(labelPrompt, BorderLayout.CENTER);
        JButton buttonPrompt = new JButton("提示");
        southPanel.add(buttonPrompt, BorderLayout.SOUTH);
    }

    private void initializeCenterPanel(JPanel centerPanel) {
        int rows = getRows();
        centerPanel.setLayout(new GridLayout(rows, COLS));
        for (Idiom idiom : idioms) {
            String[] words = idiom.getSplit();
            for (String word : words) {
                JButton button = new JButton(word);
                button.addActionListener(e -> {
                    System.out.println("click : " + e.getActionCommand());
                });
                centerPanel.add(button);
            }
        }
    }

    private int getRows() {
        int rows = idioms.size() * 4 / COLS;
        if (idioms.size() % COLS != 0) {
            rows++;
        }
        return rows;
    }
}
