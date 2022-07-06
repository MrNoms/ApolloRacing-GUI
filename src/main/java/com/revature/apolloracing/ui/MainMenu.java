package com.revature.apolloracing.ui;

import com.revature.apolloracing.daos.*;
import com.revature.apolloracing.models.*;
import com.revature.apolloracing.models.User.UserStatus;
import com.revature.apolloracing.services.*;
import com.revature.apolloracing.util.annotations.Inject;
import com.revature.apolloracing.util.custom_exceptions.InvalidUserException;
import com.revature.apolloracing.util.custom_exceptions.ObjectDoesNotExist;
import com.revature.apolloracing.util.database.*;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class MainMenu extends IMenu {
    @Inject
    private final User mUser;
    private final UserStatus permission;
    @Inject
    private final UserService mUserService;
    @Inject
    private final LocationService mLocService;
    @Inject
    private final OrderService mOrderService;
    @Inject
    private final ItemService mItemService;

    @Inject
    public MainMenu(User user, UserService uServ, LocationService lServ, OrderService oServ, ItemService iServ) {
        mUser = user;
        permission = user.getRole();

        mUserService = uServ;
        mLocService = lServ;
        mOrderService = oServ;
        mItemService = iServ;
    }

    @Override
    public void start() {
        MENU:
        {
            while(true) {
                welcomeMessage();
                prompt();
                String input = cin.nextLine();

                switch (input.toLowerCase()) {
                    case "1":
                        new Catalog(mUser, new ItemService(new ItemDAO(new ItemSchema())),
                                new OrderService(new OrderDAO(new OrderSchema())),
                                new LocationService(new LocationDAO(new LocationSchema()))).start();
                        break;
                    case "2":
                        viewCarts(mUser);
                        break;
                    case "3":
                        viewOrderHistory(mUser);
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
                prompt();
                String input = cin.nextLine();

                switch (input.toLowerCase()) {
                    case "1":
                        viewCarts(u);
                        break;
                    case "2":
                        viewOrderHistory(u);
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
                "[1] Browse Catalog\n" +
                "[2] View Carts\n" +
                "[3] View Order History\n" +
                "[4] Change contact Information\n" +
                "[5] Change account settings\n" +
                (permission.equals(UserStatus.ADMIN) ?
                    "[6] Search for customers\n"+
                    "[7] Manage locations\n" : "")+
                "[x] Sign out\n");

    }

    private void adminMessage(User u) {
        cout.println("\nProxy menu for User "+u.getUserName()+"\n" +
                "[1] Check-out\n" +
                "[2] View Order History\n" +
                "[3] Change contact Information\n" +
                "[4] Change account settings\n" +
                "[5] Change user status\n" +
                "[6] Delete user\n" +
                "[x] Return\n");

    }

    private void changeContactInfo() {
        changeInfo:
        {
            String input;
            cout.println("Please confirm your password.");
            while(true) {
                try {
                    prompt("\n");
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

                prompt("\n");
                input = cin.nextLine();
                switch (input.toLowerCase()) {
                    case "1":
                        prompt("New E-Mail");
                        while (true) {
                            input = cin.nextLine();
                            try {
                                if (mUserService.isValidEmail(input) &&
                                        mUserService.isNotDuplicateEmail(input)) {
                                    mUser.setEmail(input);
                                    break;
                                }
                            } catch (InvalidUserException | SQLException e) {
                                prompt(e.getMessage() + "\n");
                            }
                        }
                        break;
                    case "2":
                        prompt("New Phone Number ");
                        while (true) {
                            input = cin.nextLine();
                            try {
                                if (mUserService.isValidPhone(input)) {
                                    mUser.setPhone(input);
                                    break;
                                }
                            } catch (InvalidUserException iu) {
                                prompt(iu.getMessage() + "\n");
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
                    cout.print("\n");
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

                 prompt("\n");
                 input = cin.nextLine();
                 switch (input.toLowerCase()) {
                     case "1":
                         prompt("New Username");
                         while (true) {
                             input = cin.nextLine();
                             try {
                                 if (mUserService.isValidUsername(input) &&
                                         mUserService.isNotDuplicateUsername(input)) {
                                     mUser.setUserName(input);
                                     break;
                                 }
                             } catch (InvalidUserException iu) {
                                 cout.print(iu.getMessage() + "\n");
                             }
                         }
                         break;
                     case "2":
                         prompt("New Password");
                         while (true) {
                             input = cin.nextLine();
                             try {
                                 if (mUserService.isValidPassword(input)) {
                                     mUser.setPassword(input);
                                     break;
                                 }
                             } catch (InvalidUserException iu) {
                                 cout.print(iu.getMessage() + "\n");
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
        cout.println("Enter a string to query users by");
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
            cout.print("\nChoose user");
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
            prompt("\n");
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
        prompt("Type CONFIRM to delete user\n"+u+"\n");
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

                prompt("[+] Add new location\n" +
                        "[x] Cancel\n");
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
                prompt("\n");
                input = cin.nextLine();
                switch (input.toLowerCase()) {
                    case "*":
                        receiving(iServ, locs.get(n-1));
                        break;
                    case ".":
                        inventoryCheck(iServ, locs.get(n-1), null);
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
                prompt("\nItem ID#");
                id = Math.max(cin.nextInt(), 0);
                cin.nextLine();

                prompt("Total    ");
                amt = Math.max(cin.nextInt(), 0);
                cin.nextLine();

                received.add(id); amounts.add(amt);
            }
        }
        catch(InputMismatchException ignore) {cin.nextLine();}

        LinkedHashMap<Integer, Map<ItemSchema.Inv_Cols, Integer>> inStock;
        try {
            iServ.prepareInventory();

            inStock = iServ.getStockedItems(l.getID(), null);
            List<Integer> inStockIDs = new ArrayList<>(inStock.keySet());

            Savepoint preReceive = iServ.editInventory(iServ.toString());
            if(!inStock.isEmpty()) {
                iServ.addStock(l,
                        inStockIDs.stream()
                                .filter(received::contains)
                                .map(iServ::getItem)
                                .collect(Collectors.toList()),
                        amounts.stream()
                                .filter(a -> inStockIDs.contains(received.get(amounts.indexOf(a))))
                                .collect(Collectors.toList()));
            }

            iServ.createStock(l,
                    received.stream()
                            .filter(i->!inStockIDs.contains(i))
                            .collect(Collectors.toList()),
                    amounts.stream()
                            .filter(a->!inStockIDs.contains(received.get(amounts.indexOf(a))))
                            .collect(Collectors.toList()));

            inventoryCheck(iServ, l, preReceive);
        }
        catch(SQLException e) { cout.println(e.getMessage()+"\nState: "+e.getSQLState()); }
    }

    private void inventoryCheck(ItemService iServ, Location l, Savepoint sp) {
        try {
            iServ.getStockedItems(l.getID(), null)
                    .forEach((i, a) ->
                            cout.println("\n= Item: "+a.get(ItemSchema.Inv_Cols.amount) +
                                    " at Store #"+a.get(ItemSchema.Inv_Cols.location_id)+"\t"+i));
            if(sp != null) {
                prompt("Enter CONFIRM to save changes" +
                        "\tAny other input will rollback inventory changes\n");
                iServ.saveInventory(sp, cin.nextLine().equals("CONFIRM"));
            }
            prompt("Press [ENTER] to continue "); cin.nextLine();
        }
        catch(SQLException | ObjectDoesNotExist e) {
            cout.println(e.getMessage());
        }
    }

    private void addLocation() {
        prompt("City");
        String c = cin.nextLine();
        prompt("State");
        String s = cin.nextLine();
        try {
            mLocService.createLocation(new Location(null, c, s));
        }
        catch(SQLException e) {
            cout.println("Store set failure\n"+e.getMessage());
        }
    }
    private void closeStore(Location l) {
        prompt("Type CONFIRM for store closure at\n"+l+"\n");
        try {
            if (cin.nextLine().equals("CONFIRM") && mLocService.closeStore(l)) {
                cout.println("Deletion successful.");
            } else {
                cout.println("Deletion failed.");
            }
        }
        catch(ObjectDoesNotExist | SQLException ne) { cout.println(ne.getMessage()); }
    }

    private void viewCarts(User u) {
        while(true) {
            String input;
            List<Order> orders = mOrderService.getAllCarts(u);
            cout.println("\nSelect a cart to view.");

            AtomicInteger i = new AtomicInteger(0);
            orders.forEach(o -> cout.printf("[%d] Order #: %s\n\t%s\n",
                    i.incrementAndGet(), o.getID(), mLocService.getLocation(o.getLocationID()).toString()));
            prompt("[x] Back\n");
            try {
                input = cin.nextLine();
                if(input.equalsIgnoreCase("x")) break;
                checkout(orders.get(Integer.parseInt(input)-1).getID());
            } catch (NumberFormatException nf) {
                cout.println("Invalid Input.");
            } catch (IndexOutOfBoundsException iob) {
                cout.println("Invalid order choice.");
            }
        }

    }

    private void checkout(String orderID) {
        Map<Integer, Integer[]> inventoryToOrder;

        try { inventoryToOrder = mOrderService.checkInventory(orderID); }
        catch(SQLException ignore) {
            cout.println("Error getting order information");
            return;
        }

        inventoryToOrder.forEach((id, iArr) -> {
            if(iArr[0] > iArr[1]) {
                prompt(String.format("You tried to order %d %s, but there are only %d left in stock. Enter new amount to order or 0 to remove.\n",
                        iArr[0], mItemService.getItem(id).getName(), iArr[1]));
            }

            int newQuantity = iArr[0];
            while(newQuantity > iArr[1]) {
                try { newQuantity = Integer.parseInt(cin.nextLine()); }
                catch (NumberFormatException nfe) {
                    prompt("Not a valid amount");
                    continue;
                }

                try { mOrderService.changeOrder(orderID, id, newQuantity); }
                catch(SQLException se) { cout.printf("Could not update order %s\n", orderID); }

            }
        });

        printReceipt(mOrderService.getOrder(orderID));
        cout.println("[+] Checkout\t[-] Cancel Checkout");

        cout.println("If you want to change your order, enter the item number for the item you want to reduce or remove.");

        ORDER_VIEW: while(true) {
            String input = "";
            prompt("Item/Checkout");
            try {
                int selection;
                if (inventoryToOrder.containsKey(selection = Integer.parseInt(input = cin.nextLine()))) {
                    prompt("Enter the reduced amount to order or 0 to remove ");
                    mOrderService.changeOrder(orderID,
                            selection,
                            Math.max(Integer.parseInt(cin.nextLine()), 0));
                }
                else cout.println("That item is not present in your order");
            } catch (NumberFormatException nfe) {
                switch (input) {
                    case "-": break ORDER_VIEW;
                    case "+":
                        Order toFulfill = mOrderService.getOrder(orderID);
                        if(toFulfill != null) {
                            if(mItemService.sellItems(inventoryToOrder, toFulfill.getLocationID())) {
                                try {
                                    mOrderService.fulfillOrder(toFulfill);
                                    break ORDER_VIEW;
                                }
                                catch(SQLException se) { cout.println("Order checkout failed\n"+se.getMessage()); }
                            }
                            else cout.println("Order checkout failed.");
                        }
                    default: cout.println("Invalid input.");
                }

            } catch (SQLException se) {
                cout.println("Error making changes to your order.\n"+se.getMessage());
            }
        }
    }

    private void viewOrderHistory(User u) {
        mOrderService.getOrderHistory(u).forEach(this::printReceipt);

        prompt("Continue?");
        cin.nextLine();
    }

    private void printReceipt(Order o) {
        Map<Integer, Double> itemizedReceipt;

        try { itemizedReceipt = mOrderService.getReceipt(o.getID()); }
        catch(SQLException ignore) {
            cout.println("Error getting order information");
            return;
        }

        cout.printf("\nOrder Details\n===\n#%s\nID\tItem\t\t\t\tAmount\t\tDescription\tTotal\n", o.getID());
        AtomicReference<Double> grandTotal = new AtomicReference<>(0.0);
        itemizedReceipt.forEach((iID, total) -> {
            Item curr = mItemService.getItem(iID);
            cout.printf("=%d= %s\t%d\t%s\t\t\t$ %.2f\n", iID,
                    curr.getName(), (int)(total/curr.getPrice()), curr.getDescription(), total);
            grandTotal.set(grandTotal.get()+total);
        });
        cout.printf("\t\t\tGrand Total\t\t\t$ %.2f\n", grandTotal.get());
    }
}
