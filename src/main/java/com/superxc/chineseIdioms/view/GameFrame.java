package com.superxc.chineseIdioms.view;

import com.superxc.chineseIdioms.model.User;

public class GameFrame extends CenterableFrame{
    public GameFrame(ChoseStageFrame choseStageFrame, User user, int stage) {
        setSize(600, 400);
        moveToScreenCenter();
    }
}
