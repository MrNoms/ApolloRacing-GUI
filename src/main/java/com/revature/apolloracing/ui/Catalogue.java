package com.revature.apolloracing.ui;

import com.revature.apolloracing.models.Item;
import com.revature.apolloracing.models.User;
import com.revature.apolloracing.services.ItemService;
import com.revature.apolloracing.util.annotations.Inject;
import com.revature.apolloracing.util.custom_exceptions.ObjectDoesNotExist;

import java.sql.SQLException;
import java.util.List;

public class Catalogue implements IMenu {
    @Inject
    private final User mUser;
    @Inject
    private final ItemService mItemService;
    private String header;
    private List<Item> items;
    private int itemPerPage = 5;
    private int currPage = 0;
    public Catalogue(User u, ItemService s) {
        mUser = u;
        mItemService = s;
        header = "[\tProduct Name\t\tIn stock\tDescription\t\tStore #\t\tPrice\t]";
    }
    @Override
    public void start() {
        Integer locSearch = null;
        String descSearch = null;
        instructions();
        MENU: {
            while (true) {
                cout.println(header+'\n');
                printItems(locSearch, descSearch);
                break;
            }
        }
    }

    private void instructions() {
        cout.println("Enter < or > to navigate between pages\n" +
                "[ or ] to change which column to focus\n" +
                "space bar to alternate sorting for that column");
    }

    private void printItems(Integer loc, String desc) {
        try {
            mItemService.getStockedItems(loc, desc);
        }
        catch(ObjectDoesNotExist | SQLException e) {
            cout.println(e.getMessage());
        }
    }
}
