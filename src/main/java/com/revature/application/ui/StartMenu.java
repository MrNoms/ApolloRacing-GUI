package com.revature.application.ui;

import com.revature.application.services.UserService;
import com.revature.application.util.custom_exceptions.InvalidUserException;

import java.io.PrintStream;
import java.util.Scanner;

public class StartMenu implements IMenu{
    private final UserService mUserService;
    private final PrintStream cout = System.out;
    private final Scanner cin = new Scanner(System.in);
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

        cout.print(mUserService.NAMEREQ);
        while(true) {
            uName = cin.nextLine();
            try {
                mUserService.isValidUsername(uName);
                cout.println("MAKE SURE NO ONE HAS THIS NAME");
                break;
            }
            catch(InvalidUserException iu) {
                cout.print(iu.getMessage());
            }
        }
        cout.print(mUserService.PASSREQ);
        while(true) {
            pWord = cin.nextLine();
            try {
                if(mUserService.isValidPassword(pWord)) {
                    cout.print("Please confirm password: ");
                    if(pWord.equals(cin.nextLine())) break;
                    else cout.print("Passwords do not match.\nEnter: ");
                }
            }
            catch(InvalidUserException iu) {
                cout.print(iu.getMessage());
            }
        }

        cout.print("E-Mail: ");
        while(true) {
            eMail = cin.nextLine();
            try {
                if(mUserService.isValidEmail(eMail)) break;
            }
            catch(InvalidUserException iu) {
                cout.print(iu.getMessage());
            }
        }
        cout.print("Phone #: ");
        phone = cin.nextLine();

        cout.println("Need to have user confirm all entered information");

        cout.println("Add the user to the database");
    }
}
