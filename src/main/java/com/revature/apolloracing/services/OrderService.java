package com.revature.apolloracing.services;

import com.revature.apolloracing.daos.OrderDAO;
import com.revature.apolloracing.models.Item;
import com.revature.apolloracing.models.Order;
import com.revature.apolloracing.models.User;
import com.revature.apolloracing.util.annotations.Inject;

import java.sql.SQLException;

public class OrderService {
    @Inject
    private final OrderDAO mOrderDAO;
    public OrderService(OrderDAO oDAO) { mOrderDAO = oDAO; }

    public Order getCart(User u, int locID) throws SQLException {
        return mOrderDAO.getOpenOrder(u.getID(), locID);
    }

    public boolean addToCart(Order c, Item i, int amount) {
        return mOrderDAO.addToOpenOrder(c, i, amount);
    }
}
