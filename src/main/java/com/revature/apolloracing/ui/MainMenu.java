package com.revature.apolloracing.ui;

import com.revature.apolloracing.daos.ItemDAO;
import com.revature.apolloracing.models.Location;
import com.revature.apolloracing.models.User;
import com.revature.apolloracing.models.User.UserStatus;
import com.revature.apolloracing.services.ItemService;
import com.revature.apolloracing.services.LocationService;
import com.revature.apolloracing.services.UserService;
import com.revature.apolloracing.util.annotations.Inject;
import com.revature.apolloracing.util.custom_exceptions.InvalidUserException;
import com.revature.apolloracing.util.custom_exceptions.ObjectDoesNotExist;
import com.revature.apolloracing.util.database.ItemSchema;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainMenu implements IMenu {
    @Inject
    private final User mUser;
    @Inject
    private final UserService mUserService;
    @Inject
    private final LocationService mLocService;
    @Inject
    private final UserStatus permission;
    @Inject
    public MainMenu(User user, UserService uServ, LocationService lServ) {
        mUser = user;
        mUserService = uServ;
        mLocService = lServ;
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

                switch (input.toLowerCase()) {
                    case "1":
                        new Catalogue(mUser, new ItemService(new ItemDAO(new ItemSchema()))).start();
                        break;
                    case "2":
                        cout.println("Needs implement. Show the users cart and prompt order creation.");
                        break;
                    case "3":
                        cout.println("Needs implement. Query the database to allow user to see\n" +
                                "list of orders and view their contents.");
                        break;
                    case "4":
                        changeContactInfo();
                        break;
                    case "5":
                        changeAccountInfo();
                        break;
                    case "x":
                        break MENU;
                    case "6":
                        if(permission.equals(UserStatus.ADMIN)) {
                            searchUser();
                            break;
                        }
                    case "7":
                        if(permission.equals(UserStatus.ADMIN)) {
                            manageStores();
                            break;
                        }
                    default:
                        cout.println("\nInvalid input");
                        break;
                }
            }
        }
    }

    private void adminStart(User u) {
        MENU:
        {
            while(true) {
                adminMessage(u);
                cout.print("\nEnter: ");
                String input = cin.nextLine();

                switch (input.toLowerCase()) {
                    case "1":
                        cout.println("Needs implement. Show the users cart and prompt order creation.");
                        break;
                    case "2":
                        cout.println("Needs implement. Query the database to allow user to see\n" +
                                "list of orders and view their contents.");
                        break;
                    case "3":
                        changeContactInfo();
                        break;
                    case "4":
                        changeAccountInfo();
                        break;
                    case "5":
                        changeUserStatus(u);
                        break;
                    case "6":
                        if(removeUser(u)) break MENU;
                        else break;
                    case "x":
                        break MENU;
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
                    "[7] Manage locations\n" : "")+
                "[x] Exit\n");

    }

    private void adminMessage(User u) {
        cout.println("\nProxy menu for User "+u.getUserName()+"\n" +
                "[1] Check-out\n" +
                "[2] View Order History\n" +
                "[3] Change contact Information\n" +
                "[4] Change account settings\n" +
                "[5] Change user status\n" +
                "[6] Delete user\n" +
                "[x] Exit\n");

    }

    private void changeContactInfo() {
        changeInfo:
        {
            cout.println("Please confirm your password: ");
            String input = cin.nextLine();
            if (input.equalsIgnoreCase("exit")) break changeInfo;
            if (mUserService.getValidCredentials(
                    mUser.getUserName(), input) != null) {
                while (true) {

                    cout.printf("[1] Email: %s\n" +
                                    "[2] Phone: %s\n" +
                                    "[x] Set Changes\n",
                            mUser.getEmail(), mUser.getPhone()
                    );

                    cout.print("\nEnter: ");
                    input = cin.nextLine();
                    switch (input.toLowerCase()) {
                        case "1":
                            cout.print("New E-Mail: ");
                            while (true) {
                                input = cin.nextLine();
                                try {
                                    if (mUserService.isValidEmail(input) &&
                                            mUserService.isNotDuplicateEmail(input)) {
                                        mUser.setEmail(input);
                                        break;
                                    }
                                } catch (InvalidUserException iu) {
                                    cout.print(iu.getMessage() + ": ");
                                }
                            }
                            break;
                        case "2":
                            cout.print("New Phone Number: ");
                            while (true) {
                                input = cin.nextLine();
                                try {
                                    if (mUserService.isValidPhone(input)) {
                                        mUser.setPhone(input);
                                        break;
                                    }
                                } catch (InvalidUserException iu) {
                                    cout.print(iu.getMessage() + ": ");
                                }
                            }
                            break;
                        case "x":
                            break changeInfo;
                        default:
                            cout.println("Invalid input.");
                            break;
                    }
                }
            }
        }
        mUserService.updateUser(mUser);
    }

    private void changeAccountInfo() {
        changeInfo:
        {
            cout.println("Please confirm your password: ");
            String input = cin.nextLine();
            if (input.equalsIgnoreCase("exit")) break changeInfo;
            if (mUserService.getValidCredentials(
                    mUser.getUserName(), input) != null) {
                while (true) {

                    cout.printf("[1] Username: %s\n" +
                                    "[2] Password: %s\n" +
                                    "[x] Set Changes\n",
                            mUser.getUserName(), mUser.getPassword()
                    );

                    cout.print("\nEnter: ");
                    input = cin.nextLine();
                    switch (input.toLowerCase()) {
                        case "1":
                            cout.print("New Username: ");
                            while (true) {
                                input = cin.nextLine();
                                try {
                                    if (mUserService.isValidUsername(input) &&
                                            mUserService.isNotDuplicateUsername(input)) {
                                        mUser.setUserName(input);
                                        break;
                                    }
                                } catch (InvalidUserException iu) {
                                    cout.print(iu.getMessage() + ": ");
                                }
                            }
                            break;
                        case "2":
                            cout.print("New Password: ");
                            while (true) {
                                input = cin.nextLine();
                                try {
                                    if (mUserService.isValidPassword(input)) {
                                        mUser.setPassword(input);
                                        break;
                                    }
                                } catch (InvalidUserException iu) {
                                    cout.print(iu.getMessage() + ": ");
                                }
                            }
                            break;
                        case "x":
                            break changeInfo;
                        default:
                            cout.println("Invalid input.");
                            break;
                    }
                }
            }
        }
        mUserService.updateUser(mUser);
    }

    private void searchUser() {
        List<User> result;
        cout.println("Enter a string to query users by: ");
        String query = cin.nextLine();
        try {
            result = mUserService.searchUser(query);
            for(int i = 1; i <= result.size(); i++) {
                cout.printf("\n[%d] %s\n", i, result.get(i-1));
            }
            cout.println("[x]: Cancel");
        } catch (SQLException | ObjectDoesNotExist e) {
            cout.println(e.getMessage());
            return;
        }


        while(true) {
            cout.print("\nChoose user: ");
            String in = cin.nextLine();
            try {
                if (in.equalsIgnoreCase("x")) break;
                else if (in.matches("^(?=\\d+$)\\d+")) {
                    adminStart(result.get(Integer.parseInt(in) - 1));
                    break;
                }
                else cout.println("Invalid input.");
            }
            catch(IndexOutOfBoundsException ignore) {
                cout.println("Invalid input.");
            }
        }
    }

    private void changeUserStatus(User u) {
        while(true) {
            cout.println("Current user status: "+u.getRole()+
                    "\nChange to:");

            int i = 1;
            for(UserStatus s : UserStatus.values()) {
                cout.printf("\t[%d] "+s, i++);
            }
            cout.print("\n> ");
            String in = cin.nextLine();

            try {
                if (in.matches("^(?=\\d+$)\\d+")) {
                    u.setRole(UserStatus.values()[Integer.parseInt(in)-1]);
                    break;
                }
                else cout.println("Invalid input.");
            }
            catch(IndexOutOfBoundsException ignore) {
                cout.println("Invalid input.");
            }
        }
        mUserService.updateUser(u);
    }

    private boolean removeUser(User u) {
        cout.println("Type CONFIRM to delete user\n"+u);
        if(cin.nextLine().equals("CONFIRM") && mUserService.removeUser(u)) {
            cout.println("Deletion successful.");
            return true;
        }
        else {
            cout.println("Deletion failed.");
            return false;
        }
    }

    private void manageStores() {
        List<Location> all = new ArrayList<>();
        int n;

        cout.println("Select a location to view or modify.");
        locMenu:
        {
            while (true) {
                try {
                    all = mLocService.getAllLocs();
                    for (int i = 1; i <= all.size(); i++) {
                        cout.printf("[%d] " + all.get(i - 1) + "\n", i);
                    }
                } catch (SQLException e) {
                    cout.println(e.getMessage());
                } catch (ObjectDoesNotExist ignore) {}

                cout.println("[+] Add new location\n" +
                        "[x] Cancel\n> ");
                String in = cin.nextLine().toLowerCase();
                switch (in) {
                    case "+":
                        addLocation();
                        break;
                    case "x":
                        return;
                    default:
                        try {
                            n = Integer.parseInt(in);
                            break locMenu;
                        }
                        catch(NumberFormatException nfe) {
                            cout.println("Invalid Input.");
                            break;
                        }
                }
            }
        }

        cout.println(all.get(n-1)+
                "\n[*] Receive Inventory\n" +
                "[.] Confirm Stock\n" +
                "\n[-] Close store");
        String input;
        storeMenu: {
            ItemService iServ = new ItemService(new ItemDAO(new ItemSchema()));
            while(true) {
                input = cin.nextLine();
                switch (input) {
                    case "*":
                    case ".":
                    case "-":
                    default:
                        cout.println("Invalid Input.");
                        break;
                }
            }
        }
    }

    private void addLocation() {
        cout.print("City: ");
        String c = cin.nextLine();
        cout.print("State: ");
        String s = cin.nextLine();
        try {
            mLocService.createLocation(new Location(null, c, s));
        }
        catch(SQLException e) {
            cout.println("Store set failure\n"+e.getMessage());
        }
    }
}
