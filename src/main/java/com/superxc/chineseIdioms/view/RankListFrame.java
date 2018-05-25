package com.superxc.chineseIdioms.view;

import com.superxc.chineseIdioms.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RankListFrame extends JFrame {

    private List<User> ranklist = null;
    private JLabel labelRankList;

    public RankListFrame(ChoseStageFrame choseStageFrame) {
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        JPanel centerPanel = new JPanel();

        initializeCenterPanel(centerPanel);
        add(centerPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }

    private void initializeCenterPanel(JPanel centerPanel) {
        labelRankList = new JLabel();
        centerPanel.add(labelRankList);
    }

    public void showFrame() {
        ranklist = User.getTopNOrderByProcessDesc(10);

        refreshRankList(ranklist);

        setVisible(true);
        pack();
    }

    private void refreshRankList(List<User> ranklist) {
        StringBuilder stringBuilder = new StringBuilder("<html>\n" +
                "<body>\n" +
                "<table>\n" +
                "<thead>\n" +
                "<tr>\n" +
                "<th>排名</th>\n" +
                "<th>用户名</th>\n" +
                "<th>关卡</th>\n" +
                "</tr>\n" +
                "</thead>\n" +
                "<tbody>");

        for (int i = 0; i < Math.max(ranklist.size(), 10); i++) {
            User user;

            if (i >= ranklist.size()) {
                user = new User("暂无", null);
            } else {
                user = ranklist.get(i);
            }

            stringBuilder.append(String.format("<tr><td>%d</td><td>%s</td><td>%d</td></tr>", i + 1, user.getUsername(), user.getProcess()));
        }
        stringBuilder.append("</tbody>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>");
        labelRankList.setText(stringBuilder.toString());
        pack();
    }
}
