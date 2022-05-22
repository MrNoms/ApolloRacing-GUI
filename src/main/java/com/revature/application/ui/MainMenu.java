package com.revature.application.ui;

import com.revature.application.models.User;
import com.revature.application.util.annotations.Inject;

public class MainMenu implements IMenu {
    @Inject
    private final User mUser;
    @Inject
    public MainMenu(User user) {
        mUser = user;
    }

    @Override
    public void start() {
        cout.println("Welcome to the main menu "+mUser.getUserName()+
                "\nThere ain't nuthin here for you yet");
    }
}
