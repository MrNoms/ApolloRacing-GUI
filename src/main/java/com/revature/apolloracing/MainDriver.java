package com.revature.apolloracing;

import com.revature.apolloracing.daos.UserDAO;
import com.revature.apolloracing.services.UserService;
import com.revature.apolloracing.ui.StartMenu;

public class MainDriver {
    public static void main(String[] args) {
        new StartMenu(new UserService(new UserDAO())).start();
    }
}