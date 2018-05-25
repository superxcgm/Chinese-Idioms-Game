package com.superxc.chineseIdioms.view;

import javax.swing.*;

public class BackgroundImageJFrame extends JFrame{
     protected void setBackground(String imgName) {
        ((JPanel)this.getContentPane()).setOpaque(false);
        String imgUrl = LoginFrame.class.getResource(imgName).getFile();
        ImageIcon imageIcon = new ImageIcon(imgUrl);

        JLabel background = new JLabel(imageIcon);
        this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
        background.setBounds(0, 0, getWidth(), getHeight());
    }
}
