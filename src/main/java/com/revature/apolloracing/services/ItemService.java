package com.revature.apolloracing.services;

import com.revature.apolloracing.daos.ItemDAO;
import com.revature.apolloracing.models.Item;
import com.revature.apolloracing.models.Location;
import com.revature.apolloracing.util.annotations.Inject;
import com.revature.apolloracing.util.custom_exceptions.ObjectDoesNotExist;

import java.sql.SQLException;
import java.util.List;

public class ItemService {
    @Inject
    private final ItemDAO mItemDAO;
    public ItemService(ItemDAO iDAO) { mItemDAO = iDAO; }

    public List<Item> getAllItems() throws SQLException, ObjectDoesNotExist {
        return mItemDAO.getAll();
    }

    public List<Item> getStockedItems(Integer loc, String desc)
            throws SQLException, ObjectDoesNotExist {
        return mItemDAO.getInStock(loc, desc);
    }

    public boolean addStock(Location l, Item i, int amount)
            throws SQLException {
        return mItemDAO.saveInventoryItem(l.getID(), i.getID(), amount);
    }
}
