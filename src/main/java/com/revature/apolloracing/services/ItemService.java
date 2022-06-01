package com.revature.apolloracing.services;

import com.revature.apolloracing.daos.ItemDAO;
import com.revature.apolloracing.models.Item;
import com.revature.apolloracing.models.Location;
import com.revature.apolloracing.util.annotations.Inject;
import com.revature.apolloracing.util.custom_exceptions.ObjectDoesNotExist;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

public class ItemService {
    @Inject
    private final ItemDAO mItemDAO;
    public ItemService(ItemDAO iDAO) { mItemDAO = iDAO; }

    public List<Item> getAllItems() throws SQLException, ObjectDoesNotExist {
        return mItemDAO.getAll();
    }

    public LinkedHashMap<Item, Integer> getStockedItems(Integer loc, String desc)
            throws SQLException, ObjectDoesNotExist {
        return mItemDAO.getInStock(loc, desc);
    }

    public void prepareInventory(String savePoint) throws SQLException {
        mItemDAO.startTransaction(savePoint);
    }
    public void revertInventory(String savePoint) throws SQLException {
        mItemDAO.rollbackTransaction(savePoint);
    }
    public void solidifyInventory() throws SQLException {
        mItemDAO.commitTransaction();
    }

    public void createStock(Location l, List<Integer> i, List<Integer> amount)
            throws SQLException {
        mItemDAO.saveInventoryItems(
                l.getID(), i, amount);
    }

    public void addStock(Location l, List<Item> i, List<Integer> amount)
            throws SQLException {
        for(int x = 0; x < i.size(); x++) {
            mItemDAO.updateInventoryItems(l.getID(), i.get(x), amount.get(x));
        }
    }
}
