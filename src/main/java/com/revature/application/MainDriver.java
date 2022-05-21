package com.revature.application;

import com.revature.application.services.UserService;
import com.revature.application.ui.StartMenu;

public class MainDriver {
    public static void main(String[] args) {
        new StartMenu(new UserService()).start();
    }
}