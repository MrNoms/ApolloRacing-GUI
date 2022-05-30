package com.revature.apolloracing.ui;

import com.revature.apolloracing.models.User;
import com.revature.apolloracing.models.User.UserStatus;
import com.revature.apolloracing.util.annotations.Inject;

public class MainMenu implements IMenu {
    @Inject
    private final User mUser;
    @Inject
    private final UserStatus permission;
    @Inject
    public MainMenu(User user) {
        mUser = user;
        permission = user.getRole();
    }

    @Override
    public void start() {
        MENU:
        {
            while(true) {
                welcomeMessage();
                cout.print("\nEnter: ");
                String input = cin.nextLine();

                switch (input) {
                    case "1":
                        cout.println("Needs implement. Show all items currently in stock\n" +
                                "in pages and allow searching by location or item name");
                        break;
                    case "2":
                        cout.println("Needs implement. Show the users cart and prompt order creation.");
                        break;
                    case "3":
                        cout.println("Needs implement. Query the database to allow user to see\n" +
                                "list of orders and view their contents.");
                        break;
                    case "4":
                        cout.println("Needs implement. Prompt user to confirm password then give\n" +
                                "options for what to change.");
                        break;
                    case "5":
                        cout.println("Needs implement. Prompt user for which security features to change.");
                        break;
                    case "x":
                        break MENU;
                    case "6":
                        if(permission.equals(UserStatus.ADMIN)) {
                            cout.println("Needs implement. Search for customers by username or email.\n" +
                                    "Match a regex");
                            break;
                        }
                    case "7":
                        if(permission.equals(UserStatus.ADMIN)) {
                            cout.println("Needs implement. Admin will enter store location to be added\n" +
                                    "to database");
                            break;
                        }
                    case "8":
                        if(permission.equals(UserStatus.ADMIN)) {
                            cout.println("Needs implement. Admin will add received supply to the appropriate\n" +
                                    "location");
                            break;
                        }
                    default:
                        cout.println("\nInvalid input");
                        break;
                }
            }
        }
    }

    private void welcomeMessage() {
        cout.println("\nWelcome to the main menu "+mUser.getUserName()+"!\n" +
                "[1] Browse Catalogue\n" +
                "[2] Check-out\n" +
                "[3] View Order History\n" +
                "[4] Change contact Information\n" +
                "[5] Change account settings\n" +
                (permission.equals(UserStatus.ADMIN) ?
                    "[6] Search for customers\n"+
                    "[7] Add location\n" +
                    "[8] Check inventory\n" : "")+
                "[x] Exit\n");

    }
}
