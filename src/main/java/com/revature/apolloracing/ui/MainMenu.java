package com.revature.application.ui;

import com.revature.application.models.User;
import com.revature.application.models.UserStatus;
import com.revature.application.util.annotations.Inject;

public class MainMenu implements IMenu {
    @Inject
    private final User mUser;
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
                            cout.println("Needs implement. Admin will use customer id to place their\n" +
                                    "current cart as an order");
                            break;
                        }
                    case "8":
                        if(permission.equals(UserStatus.ADMIN)) {
                            cout.println("Needs implement. Admin will add received supply to the appropriate\n" +
                                    "location");
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
                    "[7] Check-out order for customer\n" +
                    "[8] Replenish inventory\n": "")+
                "[x] Exit\n");

    }
}
