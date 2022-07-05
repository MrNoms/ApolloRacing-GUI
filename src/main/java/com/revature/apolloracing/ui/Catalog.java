package com.revature.apolloracing.ui;

import com.revature.apolloracing.models.Item;
import com.revature.apolloracing.models.Location;
import com.revature.apolloracing.models.Order;
import com.revature.apolloracing.models.User;
import com.revature.apolloracing.services.ItemService;
import com.revature.apolloracing.services.LocationService;
import com.revature.apolloracing.services.OrderService;
import com.revature.apolloracing.util.annotations.Inject;
import com.revature.apolloracing.util.custom_exceptions.ObjectDoesNotExist;
import com.revature.apolloracing.util.database.ItemSchema;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Catalog extends IMenu {
    @Inject
    private final User mUser;
    @Inject
    private final ItemService mItemService;
    @Inject
    private final OrderService mOrderService;
    @Inject
    private final LocationService mLocationService;

    private final String[] headers = {"Product Name\t\t", "In stock\t", "Description\t\t",
            "Store #\t\t", "Price\t"};
    private final String asc = "/\\";
    private final String desc = "\\/";

    public Catalog(User u, ItemService i, OrderService o, LocationService l) {
        mUser = u;
        mItemService = i;
        mOrderService = o;
        mLocationService = l;
    }

    @Override
    public void start() {
        int maxPage;
        int itemPerPage = 6;
        int currPage = 0;
        String input;
        Order cart = null;

        Location loc;
        STORE_SELECTION: while(true){
            loc = chooseStore();
            assert loc != null : "No stores are open at the moment";
            LinkedHashMap<Integer, Map<ItemSchema.Inv_Cols, Integer>> items;
            try { items = getStoreItems(loc.getID()); }
            catch(Exception e) { cout.printf("%s\n%s has no stock\n", e.getMessage(), loc); continue; }

            currPage = 0;
            STOCK_VIEW: {
                List<Integer> itemList = new ArrayList<>(items.keySet());

                maxPage = (int) Math.ceil(items.size() / (double) itemPerPage);
                if(maxPage > 0 ) {
                    instructions();

                    try { cart = mOrderService.getCart(mUser, loc.getID()); }
                    catch(SQLException se) {
                        cout.println(se.getMessage());
                        break;
                    }
                }
                else cout.println("Store is out of stock\n");

                while (currPage < maxPage) {
                    cout.println(Arrays.stream(headers).reduce("", String::concat));

                    itemList.subList(currPage * itemPerPage, Math.min((currPage + 1) * itemPerPage, items.size()))
                            .forEach(i -> {
                                Item curr = mItemService.getItem(i);
                                cout.printf(
                                        "[%d]\t%s\t\t%d\t%s\t\t%d\t\t$ %.2f\n",
                                        itemList.indexOf(i) + 1, curr.getName(),
                                        items.get(i).get(ItemSchema.Inv_Cols.amount),
                                        curr.getDescription(), items.get(i).get(ItemSchema.Inv_Cols.location_id),
                                        curr.getPrice());
                            });
                    cout.println("[+] Change Store Catalog\n[x] Exit Shopping");
                    controls(currPage + 1, maxPage);

                    prompt();
                    input = cin.nextLine().toLowerCase();
                    switch (input) {
                        case ">":
                            currPage = ++currPage % maxPage;
                            break;
                        case "<":
                            currPage = --currPage % maxPage;
                            break;
                        case "+":
                            break STOCK_VIEW;
                        case "x":
                            break STORE_SELECTION;
                        default:
                            try {
                                int selection = Integer.parseInt(input) - 1;
                                Item selected = mItemService.getItem(itemList.get(selection));
                                prompt("How Many?");
                                int amount =  Integer.parseInt(cin.nextLine());
                                if(mOrderService.addToCart(cart, selected, amount))
                                    cout.printf("Successfully added %d %s to your cart\n",
                                            amount, selected.getName());
                                else cout.printf("Not enough %s in stock\n", selected.getName());
                                break;
                            } catch (NumberFormatException ignore) {}
                            cout.println("\nInvalid menu option.");
                    }
                }
            }
        }
    }

    private void instructions() {
        cout.println("Enter < or > to navigate between pages");//\n" +
                //"[ or ] to change which column to focus\n" +
                //"enter with no selection to alternate sorting for that column");
    }

    private void controls(int page, int max) {
        cout.println("\t<\tPrev Page\t "+page+"/"+max+" \tNext Page\t>");
    }

    private Location chooseStore() {
        List<Location> locations;

        cout.println("Choose a store catalogue.");
        try {
            locations = mLocationService.getAllLocs();

            AtomicInteger i = new AtomicInteger();
            locations.forEach(loc -> cout.printf(
                    "[%d] %s\n", i.incrementAndGet(), loc));
        } catch (SQLException | ObjectDoesNotExist ignore) {
            return null;
        }

        prompt(); String input; int l;
        while(true) {
            input = cin.nextLine();
            try {
                l = Integer.parseInt(input);
                if(l > 0 && l < locations.size()+1) break;
                throw new NumberFormatException("Invalid input");
            } catch(NumberFormatException ignore) {
                prompt("Not a menu option\n");
            }
        }
        return locations.get(l-1);
    }

    private LinkedHashMap<Integer, Map<ItemSchema.Inv_Cols, Integer>> getStoreItems(Integer l)
            throws ObjectDoesNotExist, SQLException {
        return  mItemService.getStockedItems(l, null);
    }
}
