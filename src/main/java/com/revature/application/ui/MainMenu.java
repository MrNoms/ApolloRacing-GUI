package com.revature.application.ui;

import com.revature.application.models.User;

public class MainMenu implements IMenu {
    private final User mUser;

    public MainMenu(User user) {
        mUser = user;
    }

    @Override
    public void start() {
        System.out.println("Welcome to the main menu "+mUser);
    }
}
