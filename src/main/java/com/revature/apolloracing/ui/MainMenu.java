package com.revature.apolloracing.ui;

import com.revature.apolloracing.daos.ItemDAO;
import com.revature.apolloracing.models.Item;
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
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

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
                cout.print("\n> ");
                String input = cin.nextLine();

                switch (input.toLowerCase()) {
                    case "1":
                        cout.println("Needs implement. Show stocked items via query. Selection add item to\n" +
                                "unfulfilled order.");
                        break;
                    case "2":
                        cout.println("Needs implement. Show the users cart via query and prompt order creation.");
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
                cout.print("\n~ ");
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
            String input;
            cout.println("Please confirm your password.");
            while(true) {
                try {
                    cout.print("\n> ");
                    input = cin.nextLine();
                    if (input.equalsIgnoreCase("exit")) break changeInfo;

                    if (mUserService.getValidCredentials(mUser.getUserName(), input)
                            != null)
                        break;
                } catch(SQLException | InvalidUserException e) {cout.println(e.getMessage());}
            }

            while (true) {
                cout.printf("[1] Email: %s\n" +
                                "[2] Phone: %s\n" +
                                "[x] Set Changes\n",
                        mUser.getEmail(), mUser.getPhone()
                );

                cout.print("\n> ");
                input = cin.nextLine();
                switch (input.toLowerCase()) {
                    case "1":
                        cout.print("New E-Mail> ");
                        while (true) {
                            input = cin.nextLine();
                            try {
                                if (mUserService.isValidEmail(input) &&
                                        mUserService.isNotDuplicateEmail(input)) {
                                    mUser.setEmail(input);
                                    break;
                                }
                            } catch (InvalidUserException | SQLException e) {
                                cout.print(e.getMessage() + "\n> ");
                            }
                        }
                        break;
                    case "2":
                        cout.print("New Phone Number> ");
                        while (true) {
                            input = cin.nextLine();
                            try {
                                if (mUserService.isValidPhone(input)) {
                                    mUser.setPhone(input);
                                    break;
                                }
                            } catch (InvalidUserException iu) {
                                cout.print(iu.getMessage() + "\n> ");
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
        try {mUserService.updateUser(mUser); }
        catch(InvalidUserException iu) {
            cout.println(iu.getMessage()+"\nPlease try again.");
        }
    }

    private void changeAccountInfo() {
        changeInfo:
        {
            String input;
            cout.println("Please confirm your password.");
            while(true) {
                try {
                    cout.print("\n> ");
                    input = cin.nextLine();
                    if (input.equalsIgnoreCase("exit")) break changeInfo;

                    if (mUserService.getValidCredentials(mUser.getUserName(), input)
                            != null)
                        break;
                } catch(SQLException | InvalidUserException e) {cout.println(e.getMessage());}
            }

             while (true) {
                 cout.printf("[1] Username: %s\n" +
                                 "[2] Password: %s\n" +
                                 "[x] Set Changes\n",
                         mUser.getUserName(), mUser.getPassword()
                 );

                 cout.print("\n> ");
                 input = cin.nextLine();
                 switch (input.toLowerCase()) {
                     case "1":
                         cout.print("New Username> ");
                         while (true) {
                             input = cin.nextLine();
                             try {
                                 if (mUserService.isValidUsername(input) &&
                                         mUserService.isNotDuplicateUsername(input)) {
                                     mUser.setUserName(input);
                                     break;
                                 }
                             } catch (InvalidUserException iu) {
                                 cout.print(iu.getMessage() + "\n> ");
                             }
                         }
                         break;
                     case "2":
                         cout.print("New Password> ");
                         while (true) {
                             input = cin.nextLine();
                             try {
                                 if (mUserService.isValidPassword(input)) {
                                     mUser.setPassword(input);
                                     break;
                                 }
                             } catch (InvalidUserException iu) {
                                 cout.print(iu.getMessage() + "\n> ");
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
        try {mUserService.updateUser(mUser); }
        catch(InvalidUserException iu) {
            cout.println(iu.getMessage()+"\nPlease try again.");
        }
    }

    private void searchUser() {
        List<User> result;
        cout.println("Enter a string to query users by> ");
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
            cout.print("\nChoose user> ");
            String in = cin.nextLine();
            try {
                if (in.equalsIgnoreCase("x")) break;
                else if (in.matches("^(?=\\d+$)\\d+")) {
                    adminStart(result.get(Integer.parseInt(in)-1));
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
            cout.print("Current user status: "+u.getRole()+
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

        try {mUserService.updateUser(u); }
        catch(InvalidUserException iu) {
            cout.println(iu.getMessage()+"\nPlease try again.");
        }
    }

    private boolean removeUser(User u) {
        cout.println("Type CONFIRM to delete user\n"+u+"\n>");
        try {
            if (cin.nextLine().equals("CONFIRM") && mUserService.removeUser(u)) {
                cout.println("Deletion successful.");
                return true;
            } else {
                cout.println("Deletion failed.");
                return false;
            }
        }
        catch(ObjectDoesNotExist ne) { cout.println(ne.getMessage()); }
        return false;
    }

    private void manageStores() {
        List<Location> locs = new ArrayList<>();
        int n;

        cout.println("Select a location to view or modify.");
        locMenu:
        {
            while (true) {
                try {
                    locs = mLocService.getAllLocs();
                    for (int i = 0; i < locs.size(); i++) {
                        cout.printf("[%d] " + locs.get(i) + "\n", i+1);
                    }
                } catch (SQLException e) {
                    cout.println(e.getMessage());
                } catch (ObjectDoesNotExist ignore) {}

                cout.print("[+] Add new location\n" +
                        "[x] Cancel\n> ");
                String input = cin.nextLine().toLowerCase();
                switch (input) {
                    case "+":
                        addLocation();
                        break;
                    case "x":
                        return;
                    default:
                        try {
                            n = Integer.parseInt(input);
                            break locMenu;
                        }
                        catch(NumberFormatException nfe) {
                            cout.println("Invalid Input.");
                            break;
                        }
                }
            }
        }

        String input;
        storeMenu: {
            ItemService iServ = new ItemService(new ItemDAO(new ItemSchema()));
            while(true) {
                cout.println(locs.get(n-1)+
                        "\n[*] Receive Inventory\n" +
                        "[.] Confirm Stock\n" +
                        "[x] Exit\n" +
                        "\n[-] Close store");
                cout.print("\n>");
                input = cin.nextLine();
                switch (input.toLowerCase()) {
                    case "*":
                        receiving(iServ, locs.get(n-1));
                        break;
                    case ".":
                        inventoryCheck(iServ, locs.get(n-1), false);
                        break;
                    case "x":
                        break storeMenu;
                    case "-":
                        closeStore(locs.get(n-1));
                        break storeMenu;
                    default:
                        cout.println("Invalid Input.");
                        break;
                }
            }
        }
    }

    private void receiving(ItemService iServ, Location l) {
        List<Integer> received = new ArrayList<>();
        List<Integer> amounts = new ArrayList<>();
        cout.println("Record received inventory for "+l+
                "\nEnter [x] to begin confirmation");

        int id, amt;
        try {
            while (true) {
                cout.print("\nItem ID# > ");
                id = cin.nextInt();
                cin.nextLine();

                cout.print("Total    > ");
                amt = cin.nextInt();
                cin.nextLine();

                received.add(id); amounts.add(amt);
            }
        }
        catch(InputMismatchException ignore) {cin.nextLine();}

        LinkedHashMap<Item, Integer> inStock;
        try {
            iServ.prepareInventory("prepInv");

            inStock = iServ.getStockedItems(l.getID(), null);
            List<Item> inStockItems = new ArrayList<>(inStock.keySet());
            List<Integer> inStockIDs = inStockItems.stream().map(Item::getID).collect(Collectors.toList());

            iServ.addStock(l,
                    inStockItems.stream().filter(i->received.contains(i.getID())).collect(Collectors.toList()),
                    amounts.stream().filter(a->received.contains(inStockIDs.get(amounts.indexOf(a)))).collect(Collectors.toList()));

            iServ.createStock(l,
                    received.stream().filter(i->!inStockIDs.contains(i)).collect(Collectors.toList()),
                    amounts.stream().filter(a->!inStockIDs.contains(received.get(amounts.indexOf(a)))).collect(Collectors.toList())
            );
            inventoryCheck(iServ, l, true);
        }
        catch(SQLException e) { cout.println(e.getMessage()+"\nState: "+e.getSQLState()); }
    }

    private void inventoryCheck(ItemService iServ, Location l, boolean newItems) {
        try {
            iServ.getStockedItems(l.getID(), null)
                    .forEach((i, a)->cout.println("= "+a+"\t"+i));
            if(newItems) {
                cout.println("[] Enter CONFIRM to commit changes" +
                        "\t[Any key] Rollback inventory changes");
                switch (cin.nextLine()) {
                    case "CONFIRM":
                        iServ.solidifyInventory();
                    default:
                        iServ.revertInventory("prepInv");
                }
            }
        }
        catch(SQLException | ObjectDoesNotExist e) {
            cout.println(e.getMessage());
        }
    }

    private void addLocation() {
        cout.print("City> ");
        String c = cin.nextLine();
        cout.print("State> ");
        String s = cin.nextLine();
        try {
            mLocService.createLocation(new Location(null, c, s));
        }
        catch(SQLException e) {
            cout.println("Store set failure\n"+e.getMessage());
        }
    }
    private void closeStore(Location l) {
        cout.println("Type CONFIRM for store closure at\n"+l+"\n>");
        try {
            if (cin.nextLine().equals("CONFIRM") && mLocService.closeStore(l)) {
                cout.println("Deletion successful.");
            } else {
                cout.println("Deletion failed.");
            }
        }
        catch(ObjectDoesNotExist | SQLException ne) { cout.println(ne.getMessage()); }
    }
}
