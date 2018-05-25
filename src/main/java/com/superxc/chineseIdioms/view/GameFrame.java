package com.superxc.chineseIdioms.view;

import com.superxc.chineseIdioms.model.Idiom;
import com.superxc.chineseIdioms.model.User;
import com.superxc.chineseIdioms.util.AppConfigure;

import javax.swing.*;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;

import static com.superxc.chineseIdioms.view.LoginFrame.BEACH_JPG;

public class GameFrame extends BackgroundImageJFrame {

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
    private final boolean SHUFFLE_ON;
    private Timer timer;
    private AudioClip audioClipSuccessEliminate;
    private AudioClip audioClipSuccess;
    private AudioClip audioClipFailedEliminate;

    public GameFrame(ChoseStageFrame choseStageFrame, User user, int stage) {

        loadResource();

        this.choseStageFrame = choseStageFrame;
        this.user = user;
        this.stage = stage;

        SHUFFLE_ON = AppConfigure.getBooleanProperty("GAME_SHUFFLE");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                choseStageFrame.showFrame();
            }
        });

        setTitle("第" + stage + "关");
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        initIdioms(stage);

        JPanel northPanel = new TransparentJPanel();
        JPanel centerPanel = new TransparentJPanel();
        JPanel southPanel = new TransparentJPanel();

        initializeNorthPanel(northPanel);
        add(northPanel, BorderLayout.NORTH);

        initializeCenterPanel(centerPanel);
        add(centerPanel, BorderLayout.CENTER);

        initializeSouthPanel(southPanel);
        add(southPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setBackground(BEACH_JPG);

        initTimer();
    }

    private void loadResource() {
        audioClipSuccessEliminate = JApplet.newAudioClip(GameFrame.class.getResource("/sound/eliminateSuccess.wav"));

        audioClipFailedEliminate = JApplet.newAudioClip(GameFrame.class.getResource("/sound/eliminateFail.wav"));

        audioClipSuccess = JApplet.newAudioClip(GameFrame.class.getResource("/sound/success.wav"));


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
        // TODO: 潜在的空指针异常
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

                audioClipSuccessEliminate.play();

                if (idioms.size() == 0) {
                    if (user.getProcess() < stage) {
                        user.setProcess(stage);
                        user.save();
                    }
                    timer.cancel();

                    audioClipSuccess.play();

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
                audioClipFailedEliminate.play();
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
