package com.revature.application.ui;

import com.revature.application.models.User;
import com.revature.application.services.UserService;
import com.revature.application.util.annotations.Inject;
import com.revature.application.util.custom_exceptions.InvalidUserException;
import com.sun.tools.javac.Main;

import java.io.PrintStream;
import java.util.Scanner;

public class StartMenu implements IMenu{
    @Inject
    private final UserService mUserService;
    @Inject
    public StartMenu(UserService us) { mUserService = us; }

    @Override
    public void start() {
        MENU: {
            while(true) {
                welcomeMessage();
                cout.print("\nEnter: ");
                String input = cin.nextLine();

                switch (input) {
                    case "1":
                        login();
                        break;
                    case "2":
                        signup();
                        break;
                    case "x":
                        goodbyeMessage();
                        break MENU; //break to this break label;
                    default:
                        cout.println("\nInvalid input.");
                        break;
                }
            }
        }
    }

    private void welcomeMessage() {
        cout.println("\nWelcome to Application\n" +
                "[1] Login\n" +
                "[2] Signup\n" +
                "[x] Exit\n");
    }

    private void goodbyeMessage() {
        cout.println("\nCome back soon.");
    }

    private void login() {
        cout.println("Needs implement");
    }

    private void signup() {
        cout.println("\nBeautiful! You on your way to... something...\n" +
                "Let's get started creating your store site profile.\n" +
                "Please create account credentials and provide contact information\n");

        String uName;
        String pWord;
        String eMail;
        String phone;
        credCreation:
        {
            while (true) {
                cout.print(mUserService.NAMEREQ);
                while (true) {
                    uName = cin.nextLine();
                    try {
                        mUserService.isValidUsername(uName);
                        cout.println("MAKE SURE NO ONE HAS THIS NAME");
                        break;
                    } catch (InvalidUserException iu) {
                        cout.print(iu.getMessage());
                    }
                }
                cout.print(mUserService.PASSREQ);
                while (true) {
                    pWord = cin.nextLine();
                    try {
                        if (mUserService.isValidPassword(pWord)) {
                            cout.print("Please confirm password: ");
                            if (pWord.equals(cin.nextLine())) break;
                            else cout.print("Passwords do not match.\nEnter: ");
                        }
                    } catch (InvalidUserException iu) {
                        cout.print(iu.getMessage());
                    }
                }

                cout.print("E-Mail: ");
                while (true) {
                    eMail = cin.nextLine();
                    try {
                        if (mUserService.isValidEmail(eMail)) break;
                    } catch (InvalidUserException iu) {
                        cout.print(iu.getMessage());
                    }
                }
                cout.print("Please enter Phone # with your country code if you it: ");
                while (true) {
                    phone = cin.nextLine();
                    try {
                        if (mUserService.isValidPhone(phone)) break;
                    } catch (InvalidUserException iu) {
                        cout.print(iu.getMessage());
                    }
                }

                credConfirm:
                {
                    while (true) {
                        cout.print("Please confirm your username and account information:\n" +
                                "Username: " + uName +
                                "\nEmail: " + eMail +
                                "\nPhone: " + phone +
                                "\nEnter y/n: ");
                        switch (cin.nextLine().toLowerCase()) {
                            case "y":
                                User newUser = new User(uName, pWord, eMail, phone);
                                try {
                                    mUserService.createUser(newUser);
                                    new MainMenu(newUser).start();
                                }
                                catch(Exception e) {
                                    cout.println("Account creation failed\n"+e.getMessage());
                                }
                                break credCreation;
                            case "x":
                                break credConfirm;
                            default:
                                cout.println("Invalid input.");
                                break;
                        }
                    }
                }
            }
        }
    }
}
