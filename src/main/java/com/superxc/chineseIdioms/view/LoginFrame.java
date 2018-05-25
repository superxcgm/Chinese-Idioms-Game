package com.superxc.chineseIdioms.view;

import com.superxc.chineseIdioms.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends BackgroundImageJFrame {

    public static final String BEACH_JPG = "/img/beach.jpg";
    private JTextField textFieldUsername;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("用户登录");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel northPanel = new TransparentJPanel();
        JPanel centerPanel = new TransparentJPanel();
        JPanel southPanel = new TransparentJPanel();

        initializeNorthComponent(northPanel);
        add(northPanel, BorderLayout.NORTH);

        initializeCenterComponent(centerPanel);
        add(centerPanel, BorderLayout.CENTER);

        initializeSouthComponent(southPanel);
        add(southPanel, BorderLayout.SOUTH);

        pack();

        // move JFrame to center of the screen
        setLocationRelativeTo(null);
        setResizable(false);
        setBackground(BEACH_JPG);
    }

    private void initializeNorthComponent(JPanel northPanel) {

        JLabel labelRegister = new JLabel("没有帐号？new一个");
        labelRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        labelRegister.addMouseListener(new LabelRegisterListener(this, labelRegister));
        northPanel.add(labelRegister);
    }

    private String getInputUsername() {
        return textFieldUsername.getText().trim();
    }

    private String getInputPassword() {
        return new String(passwordField.getPassword());
    }

    private void initializeCenterComponent(JPanel centerPanel) {
        centerPanel.setLayout(new GridBagLayout());

        centerPanel.add(new JLabel("用户名：", SwingConstants.RIGHT), new GBC(0, 0, 1, 1).setAnchor(GBC.EAST).setInsets(0, 5, 0, 0));
        textFieldUsername = new JTextField();
        centerPanel.add(textFieldUsername, new GBC(1, 0, 2, 1).setFill(GBC.HORIZONTAL).setWeight(100, 0));

        centerPanel.add(new JLabel("密码：", SwingConstants.RIGHT), new GBC(0, 1, 1, 1).setAnchor(GBC.EAST));
        passwordField = new JPasswordField();
        passwordField.addActionListener(loginWithUsernameAndPassword());
        centerPanel.add(passwordField, new GBC(1, 1, 2, 1).setFill(GBC.HORIZONTAL).setWeight(100, 0));
    }

    private void initializeSouthComponent(JPanel southPanel) {
        JButton buttonAnonymouseLogin = new JButton("匿名登录");
        buttonAnonymouseLogin.addActionListener(e -> login(User.createAnonymousUser()));
        southPanel.add(buttonAnonymouseLogin);

        JButton buttonLogin = new JButton("登录");
        buttonLogin.addActionListener(loginWithUsernameAndPassword());
        southPanel.add(buttonLogin);

        JButton buttonExit = new JButton("退出");
        buttonExit.addActionListener(e -> System.exit(0));
        southPanel.add(buttonExit);
    }

    private ActionListener loginWithUsernameAndPassword() {
        return e -> login(new User(getInputUsername(), getInputPassword(), 0, "", 0));
    }

    private void login(User incompleteUser) {
        User completeUser = User.login(incompleteUser);
        if (completeUser == null) {
            JOptionPane.showMessageDialog(this, "用户名不存在或者密码错误，请重新输入！", "登录失败", JOptionPane.INFORMATION_MESSAGE);
            passwordField.setText("");
            return;
        }
        this.setVisible(false);
        JFrame frame = new ChoseStageFrame(completeUser);
        frame.setVisible(true);
    }

    private class LabelRegisterListener extends MouseAdapter{

        private JLabel labelRegister;
        private JFrame loginFrame;
        private RegisterFrame registerFrame = null;


        public LabelRegisterListener(JFrame loginFrame, JLabel labelRegister) {
            this.loginFrame = loginFrame;
            this.labelRegister = labelRegister;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            labelRegister.setForeground(Color.PINK);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            labelRegister.setForeground(Color.black);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (registerFrame == null) {
                registerFrame = new RegisterFrame(loginFrame);
            }
            loginFrame.setVisible(false);
            // TODO: 这里会出现文本框还没完整清除，就显示出来，所以用户会看到一闪而过的清除过程
            registerFrame.clearAllTextField();
            registerFrame.setVisible(true);
        }
    }
}
