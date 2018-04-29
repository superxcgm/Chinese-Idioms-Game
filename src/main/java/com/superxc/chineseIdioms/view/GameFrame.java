package com.superxc.chineseIdioms.view;

import com.superxc.chineseIdioms.model.Idiom;
import com.superxc.chineseIdioms.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Random;

public class GameFrame extends CenterableFrame{

    public static final String PROMPT_OFF = "提示：关";
    public static final int COLS = 7;
    private final JPanel centerPanel;
    private final JPanel southPanel;
    private final List<Idiom> idioms;
    private boolean prompt = false;
    private int currentPromptIndex = -1;

    public GameFrame(ChoseStageFrame choseStageFrame, User user, int stage) {

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                choseStageFrame.setVisible(true);
            }
        });

        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        centerPanel = new JPanel();
        southPanel = new JPanel();

        idioms = Idiom.getIdioms(stage);

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
        labelPrompt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                refreshPrompt(labelPrompt);
            }
        });
        southPanel.add(labelPrompt, BorderLayout.CENTER);

        JButton buttonPrompt = new JButton("提示");
        buttonPrompt.addActionListener(e -> {
            prompt = !prompt;
            if (prompt) {
                refreshPrompt(labelPrompt);
            } else {
                labelPrompt.setText(PROMPT_OFF);
            }
        });
        southPanel.add(buttonPrompt, BorderLayout.SOUTH);
    }

    private void refreshPrompt(JLabel labelPrompt) {
        if (!prompt) {
            return;
        }

        int promptIndex;
        do {
            promptIndex = (new Random()).nextInt(idioms.size());
        } while (currentPromptIndex == promptIndex);

        currentPromptIndex = promptIndex;
        labelPrompt.setText(idioms.get(currentPromptIndex).getDescription());
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
