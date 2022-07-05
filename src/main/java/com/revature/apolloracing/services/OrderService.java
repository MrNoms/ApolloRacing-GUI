package com.revature.apolloracing.services;

import com.revature.apolloracing.daos.OrderDAO;
import com.revature.apolloracing.models.Item;
import com.revature.apolloracing.models.Order;
import com.revature.apolloracing.models.User;
import com.revature.apolloracing.util.annotations.Inject;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class OrderService {
    @Inject
    private final OrderDAO mOrderDAO;
    public OrderService(OrderDAO oDAO) { mOrderDAO = oDAO; }

    public Order getOrder(String orderID) {
        try { return mOrderDAO.getByID(orderID); }
        catch(SQLException ignore) { return null; }
    }
    public void fulfillOrder(Order o) throws SQLException {
        mOrderDAO.update(o);
    }
    public Order getCart(User u, int locID) throws SQLException {
        return mOrderDAO.getOpenOrder(u.getID(), locID);
    }
    public List<Order> getAllCarts(User u) {
        return mOrderDAO.openOrdersByUser(u.getID());
    }

    public List<Order> getOrderHistory(User u) {
        return mOrderDAO.getClosedOrders(u.getID());
    }

    public boolean addToCart(Order c, Item i, int amount) {
        Map<Integer, Integer[]> inventoryForOrder;
        try { inventoryForOrder = mOrderDAO.availableInventoryForOrder(c.getID());
            Integer[] inventoryCheck = inventoryForOrder.get(i.getID());
            if(inventoryCheck == null) return mOrderDAO.addToOpenOrder(c, i, amount);
            else if((inventoryCheck[0]+amount) > inventoryCheck[1]) return false;
            else {
                mOrderDAO.updateLineItem(c.getID(), i.getID(), inventoryCheck[0]+amount);
                return true;
            }
        }
        catch(SQLException se) { return false; }

    }

    public Map<Integer, Double> getReceipt(String orderID) throws SQLException {
        return mOrderDAO.itemize(orderID);
    }

    public void changeOrder(String orderID, Integer itemID, Integer newQuantity)
            throws SQLException {
        mOrderDAO.updateLineItem(orderID, itemID, newQuantity);
    }

    public Map<Integer, Integer[]> checkInventory(String orderID) throws SQLException {
        return mOrderDAO.availableInventoryForOrder(orderID);
    }
}
