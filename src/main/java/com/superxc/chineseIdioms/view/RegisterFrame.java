package com.superxc.chineseIdioms.view;

import com.superxc.chineseIdioms.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.superxc.chineseIdioms.view.LoginFrame.BEACH_JPG;

public class RegisterFrame extends BackgroundImageJFrame {
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

        JPanel centerPanel = new TransparentJPanel();
        JPanel southPanel = new TransparentJPanel();

        initializeCenterPanel(centerPanel);
        add(centerPanel, BorderLayout.CENTER);

        initializeSouthPanel(southPanel);
        add(southPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);

        setBackground(BEACH_JPG);
    }

    private String getUsername() {
        return textFieldUsername.getText().trim();
    }

    private String getPassword() {
        return new String(passwordField.getPassword());
    }

    private String getRePassword() {
        return new String(rePasswordField.getPassword());
    }

    private void initializeSouthPanel(JPanel southPanel) {
        JButton buttonRegister = new JButton("注册");
        buttonRegister.addActionListener(registerAction());
        southPanel.add(buttonRegister);

        JButton buttonCancel = new JButton("取消");
        buttonCancel.addActionListener(e -> hideCurrentFrameAndShowLoginFrame());
        southPanel.add(buttonCancel);
    }

    private ActionListener registerAction() {
        return e -> {
            String username = getUsername();
            String password = getPassword();
            String errorMsg = "";
            User user;
            try {
                if (username.length() == 0) {
                    errorMsg = "用户名不能为空!";
                    throwAnException();
                } else if (username.length() < 6) {
                    errorMsg = "用户名的长底不小于6位!";
                    throwAnException();
                } else if (!containsOnlyLetterAndDigit(username)) {
                    errorMsg = "用户名仅能包含数字和字母!";
                    throwAnException();
                } else if (password.length() == 0) {
                    errorMsg = "密码不能为空!";
                    throwAnException();
                } else if (password.length() < 6) {
                    errorMsg = "密码长度不能小于6位!";
                    throwAnException();
                } else if (!password.equals(getRePassword())) {
                    errorMsg = "两次输入的密码不一致!";
                    throwAnException();
                }
                user = new User(username, password);
                if (!user.save()) {
                    errorMsg = "注册失败！";
                    throwAnException();
                }
            } catch (Exception exp) {
                JOptionPane.showMessageDialog(this, errorMsg, "错误", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(this, "注册成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            hideCurrentFrameAndShowLoginFrame();
        };
    }

    private void throwAnException() throws Exception {
        throw new Exception();
    }

    private boolean containsOnlyLetterAndDigit(String str) {
        return str.matches("^[a-zA-Z0-9]{6,20}$");
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
        rePasswordField.addActionListener(registerAction());
        centerPanel.add(rePasswordField, new GBC(1, 2, 2, 1).setFill(GBC.HORIZONTAL).setWeight(100, 0));
    }

    public void clearAllTextField() {
        textFieldUsername.setText("");
        passwordField.setText("");
        rePasswordField.setText("");
    }

    private void hideCurrentFrameAndShowLoginFrame() {
        setVisible(false);
        loginFrame.setVisible(true);
    }
}
