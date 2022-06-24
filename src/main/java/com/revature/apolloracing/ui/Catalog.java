package com.revature.apolloracing.ui;

import com.revature.apolloracing.models.Item;
import com.revature.apolloracing.models.Location;
import com.revature.apolloracing.models.User;
import com.revature.apolloracing.services.ItemService;
import com.revature.apolloracing.services.LocationService;
import com.revature.apolloracing.services.OrderService;
import com.revature.apolloracing.util.annotations.Inject;
import com.revature.apolloracing.util.custom_exceptions.ObjectDoesNotExist;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Catalogue extends IMenu {
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

    public Catalogue(User u, ItemService i, OrderService o, LocationService l) {
        mUser = u;
        mItemService = i;
        mOrderService = o;
        mLocationService = l;
    }

    @Override
    public void start() {
        LinkedHashMap<Item, Integer[]> items = new LinkedHashMap<>();
        int maxPage;
        int itemPerPage = 10;
        int currPage = 0;
        //Integer locSearch = null;
        String descSearch = null;
        String input;

        Location loc;
        STORE_SELECTION: while(true){
            cout.println("Choose a store catalogue.");
            loc = chooseStore();
            getStoreItems(locSearch, items);

            STOCK_VIEW: {
                instructions();
                List<Item> itemList = new ArrayList<>(items.keySet());
                maxPage = (int) Math.ceil(items.size() / (double) itemPerPage);

                if (maxPage > 0) {
                    cout.println(Arrays.stream(headers).reduce("", String::concat));

                    itemList.subList(currPage * itemPerPage, Math.min((currPage + 1) * itemPerPage, items.size()))
                            .forEach(i -> cout.printf(
                                    "[%d]\t%s\t\t%d\t%s\t\t%d\t\t$ %.2f\n",
                                    itemList.indexOf(i) + 1, i.getName(), items.get(i)[1], i.getDescription(), items.get(i)[0], i.getPrice()
                            ));
                    cout.println("[*] Checkout]\n[x] Exit");
                    controls(currPage + 1, maxPage);

                    input = cin.nextLine().toLowerCase();
                    switch (input) {
                        case ">":
                            currPage = ++currPage % maxPage;
                            break;
                        case "<":
                            currPage = --currPage % maxPage;
                            break;
                        case "x":
                            break MENU;
                        default:
                            int selection;
                            try {
                                selection = Integer.parseInt(input);

                                break;
                            } catch (NumberFormatException ignore) {
                            }
                            cout.println("\nInvalid input");

                    }
                }
                else cout.println("");
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
        String input;
        List<Location> locations = new ArrayList<>();
        try {
            locations = mLocationService.getAllLocs();
            AtomicInteger i = new AtomicInteger();
            locations.forEach(loc -> cout.printf(
                    "[%d] %s", i.incrementAndGet(), loc));
        } catch (SQLException | ObjectDoesNotExist ignore) {
            cout.println("No stores are stocked at the moment.");
            return null;
        }

        prompt();
        int l;
        while(true) {
            input = cin.nextLine();
            try {
                l = Integer.parseInt(input);
                assert(l > 0);
                break;
            }
            catch(NumberFormatException | AssertionError e) {
                prompt("Invalid input.\n");
            }
        }
        return locations.get(l-1);
    }

    private void getStoreItems(Integer l, LinkedHashMap<Item, Integer[]> itemList) {
        try { itemList = mItemService.getStockedItems(l, null); }
        catch(ObjectDoesNotExist | SQLException e) {
            cout.println(e.getMessage());
        }
    }
}
