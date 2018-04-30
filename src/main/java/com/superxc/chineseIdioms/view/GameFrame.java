package com.superxc.chineseIdioms.view;

import com.sun.tools.javac.util.Pair;
import com.superxc.chineseIdioms.model.Idiom;
import com.superxc.chineseIdioms.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;

public class GameFrame extends CenterableFrame{

    private static final String PROMPT_OFF = "提示：关";
    private static final int COLS = 7;
    private JLabel labelTimer;

    private Map<Integer, Idiom> idioms;

    private boolean prompt = false;

    private int currentPromptIndex = -1;

    private List<JButton> wordsClick = new ArrayList<>();

    private JLabel labelPrompt;

    private ChoseStageFrame choseStageFrame;

    private User user;

    private int stage;

    private int timeUsed = 0;
    private boolean SHUFFLE_ON = false;
    private Timer timer;

    public GameFrame(ChoseStageFrame choseStageFrame, User user, int stage) {
        this.choseStageFrame = choseStageFrame;
        this.user = user;
        this.stage = stage;

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                choseStageFrame.showFrame();
            }
        });

        setTitle("第" + stage + "关");
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        initIdioms(stage);

        JPanel northPanel = new JPanel();
        JPanel centerPanel = new JPanel();
        JPanel southPanel = new JPanel();

        initializeNorthPanel(northPanel);
        add(northPanel, BorderLayout.NORTH);

        initializeCenterPanel(centerPanel);
        add(centerPanel, BorderLayout.CENTER);

        initializeSouthPanel(southPanel);
        add(southPanel, BorderLayout.SOUTH);

        pack();
        moveToScreenCenter();
        setResizable(false);

        initTimer();
    }

    private void initializeNorthPanel(JPanel northPanel) {
        labelTimer = new JLabel();
        refreshLabelTimer();
        northPanel.add(labelTimer);
    }

    private void refreshLabelTimer() {
        labelTimer.setText("计时：" + timeUsed);
    }
    private void initTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeUsed++;
                refreshLabelTimer();
            }
        }, 0, 1000);
    }

    private void initIdioms(int stage) {
        idioms = new HashMap<>();
        int index = 0;
        for (Idiom idiom : Idiom.getIdioms(stage)) {
            idioms.put(index++, idiom);
        }
    }

    private void initializeSouthPanel(JPanel southPanel) {
        southPanel.setLayout(new BorderLayout());

        labelPrompt = new JLabel(PROMPT_OFF, JLabel.CENTER);
        labelPrompt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                refreshPrompt();
            }
        });
        southPanel.add(labelPrompt, BorderLayout.CENTER);

        JButton buttonPrompt = new JButton("提示");
        buttonPrompt.addActionListener(e -> {
            prompt = !prompt;
            if (prompt) {
                refreshPrompt();
            } else {
                labelPrompt.setText(PROMPT_OFF);
            }
        });
        southPanel.add(buttonPrompt, BorderLayout.SOUTH);
    }

    private void refreshPrompt() {
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
        int index = 0;
        List<JButton> btns = new ArrayList<>(idioms.size() * 4);
        for (Idiom idiom : idioms.values()) {
            String[] words = idiom.getSplit();
            for (String word : words) {
                JButton button = new JButton(word);
                index++;
                button.addActionListener(handleWordClick());
                btns.add(button);
            }
        }
        if (SHUFFLE_ON) {
            Collections.shuffle(btns);
        }
        btns.forEach(centerPanel::add);
    }

    private ActionListener handleWordClick() {
        return e -> {
            JButton button = (JButton) e.getSource();

            if (wordsClick.contains(button)) {
                setButtonActive(button, false);
                wordsClick.remove(button);
                return;
            }

            wordsClick.add(button);
            setButtonActive(button, true);
            if (wordsClick.size() != 4) {
                return;
            }

            String sIdiom = String.join("", wordsClick.stream().map(btn -> btn.getActionCommand()).collect(Collectors.toList()));
            Integer index;
            if ((index = guessRight(sIdiom)) != null) {
                if (idioms.get(index).getDescription().equals(labelPrompt.getText())) {
                    refreshPrompt();
                }
                wordsClick.forEach(btn -> btn.setVisible(false));
                idioms.remove(index);
                // TODO: 播放成功消除的音效

                if (idioms.size() == 0) {
                    if (user.getProcess() < stage) {
                        user.setProcess(stage);
                        user.save();
                    }
                    timer.cancel();
                    Object stringArray[] = {"主页", "下一关"};
                    int option = JOptionPane.showOptionDialog(this, "闯关成功！用时：" + timeUsed + "秒。", "成功！", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, stringArray, stringArray[1]);
                    if (option == JOptionPane.YES_OPTION) {
                        choseStageFrame.showFrame();
                        setVisible(false);
                    } else {
                        JFrame frame = new GameFrame(choseStageFrame, user, stage + 1);
                        frame.setVisible(true);
                        setVisible(false);
                    }
                }
            } else {
                // TODO: 播放消除失败的音效
            }
            wordsClick.forEach(btn -> {
                setButtonActive(btn, false);
            });
            wordsClick.clear();
        };
    }

    private void setButtonActive(JButton button, boolean active) {
        button.setBackground(active ? Color.green : getBackground());
        button.setOpaque(active);
        button.setBorderPainted(!active);
    }

    private Integer guessRight(String sIdiom) {
        for (Integer integer : idioms.keySet()) {
            if (sIdiom.equals(idioms.get(integer).getValue())) {
                return integer;
            }
        }
        return null;
    }

    private int getRows() {
        int rows = idioms.size() * 4 / COLS;
        if (idioms.size() % COLS != 0) {
            rows++;
        }
        return rows;
    }
}
