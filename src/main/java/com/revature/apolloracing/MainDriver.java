package com.revature.apolloracing;

import com.revature.apolloracing.daos.UserDAO;
import com.revature.apolloracing.services.UserService;
import com.revature.apolloracing.ui.StartMenu;
import com.revature.apolloracing.util.database.UserSchema;

public class MainDriver {
    public static void main(String[] args) {
        new StartMenu(new UserService(new UserDAO(new UserSchema()))).start();
    }
}