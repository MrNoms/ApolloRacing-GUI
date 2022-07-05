package com.revature.apolloracing.daos;

import com.revature.apolloracing.models.Item;
import com.revature.apolloracing.models.Order;
import com.revature.apolloracing.util.annotations.Inject;
import com.revature.apolloracing.util.database.DBSchema;
import com.revature.apolloracing.util.database.ItemSchema;
import com.revature.apolloracing.util.database.OrderSchema.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDAO extends CrudDAO<Order> {
    @Inject
    public OrderDAO(DBSchema s) {
        super(s);
    }


    @Override
    protected Order getObject(ResultSet rs) throws SQLException {
        return new Order(rs.getString(Cols.id.name()),
                rs.getDate(Cols.date_fulfilled.name()),
                rs.getString(Cols.user_id.name()),
                rs.getInt(Cols.location_id.name()));
    }

    @Override
    public void save(Order o) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(
                "INSERT INTO orders VALUES" +
                        "\n(?, ?, ?, ?);"
        )) {
            int col = 1;
            for (Object arg : new Object[]{
                    o.getID(), o.getDateFulfilled(), o.getUserID(),
                    o.getLocationID()
            }) {
                stmt.setObject(col++, arg);
            }
            stmt.executeUpdate();
        }
    }

    public List<Order> getClosedOrders(String userID) {
        try (PreparedStatement stmt = con.prepareStatement(
                "SELECT * FROM orders " +
                        "WHERE "+Cols.user_id+" = ? AND "+Cols.date_fulfilled+" IS NOT NULL;"
        )) {
            stmt.setString(1, userID);
            ResultSet rs = stmt.executeQuery();

            List<Order> orders = new ArrayList<>(0);

            while(rs.next()) orders.add(getObject(rs));

            return orders;
        } catch (SQLException e) {
            return null;
        }
    }

    public Order getOpenOrder(String userID, int locID) throws SQLException {
        try(PreparedStatement stmt = con.prepareStatement(
                "SELECT * FROM orders"+
                        "\nWHERE "+Cols.date_fulfilled+" IS NULL\n" +
                        "AND "+Cols.user_id+" = ? AND "+Cols.location_id+" = ?;")
        ) {
            int col = 1;
            stmt.setString(col++, userID);
            stmt.setInt(col, locID);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                return getObject(rs);
            }
            else {
                Order newOrder = new Order(userID, locID);
                save(newOrder);
                return newOrder;
            }
        }
    }

    public boolean addToOpenOrder(Order cart, Item i, int amount) {
        try(PreparedStatement stmt = con.prepareStatement(
                "INSERT INTO order_line_items VALUES (?,?,?);"
        )) {
            stmt.setString(1, cart.getID());
            stmt.setInt(2, i.getID());
            stmt.setInt(3, amount);
            stmt.executeUpdate();
        } catch (SQLException ignore) {
             return false;
        }

        return true;
    }

    public List<Order> openOrdersByUser(String userID) {
        List<Order> out = new ArrayList<>();

        try(PreparedStatement stmt = con.prepareStatement(
                "SELECT * FROM orders"+
                        "\nWHERE "+Cols.date_fulfilled+" IS NULL\n" +
                        "AND "+Cols.user_id+" = ?;")
        ) {
            stmt.setString(1, userID);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                out.add(getObject(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return out;
    }

    @Override
    public void update(Order o) throws SQLException {

        try (PreparedStatement stmt = con.prepareStatement(
                "UPDATE orders SET\n" +
                        Cols.date_fulfilled + " = NOW(),\n" +
                        Cols.location_id + " = ?\n" +
                        "WHERE id = ?;"
        )) {
            stmt.setInt(1, o.getLocationID());
            stmt.setString(2, o.getID());
            stmt.executeUpdate();
        }
    }

    public void updateLineItem(String orderID, Integer itemID, Integer newQuantity)
            throws SQLException {
        try(PreparedStatement stmt = con.prepareStatement(
                "UPDATE order_line_items\n" +
                        "SET "+Line_Cols.quantity+"= ?\n" +
                        "WHERE "+Line_Cols.order_id+"= ? AND\n" +
                        Line_Cols.item_id+"= ?;"
        )) {
            if(newQuantity == 0) deleteLineItem(orderID, itemID);

            stmt.setString(2, orderID);
            stmt.setInt(3, itemID);
            stmt.setInt(1, newQuantity);

            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Order obj) throws SQLException {

        try (PreparedStatement stmt = con.prepareStatement(
                "DELETE FROM orders WHERE id = ?;"
        )) {
            stmt.setString(1, obj.getID());

            stmt.executeUpdate();
        }
    }

    public void deleteLineItem(String orderID, Integer itemID) throws SQLException{
        try (PreparedStatement stmt  = con.prepareStatement(
                "DELETE FROM order_line_items\n" +
                        "WHERE "+Line_Cols.order_id+" = ? AND " +
                        Line_Cols.item_id+" = ?;"
        )) {
            stmt.setString(1, orderID);
            stmt.setInt(2, itemID);

            stmt.executeUpdate();
        }
    }

    public Map<Integer, Double> itemize(String orderID)
            throws SQLException {
        try(PreparedStatement stmt = con.prepareStatement(
                "SELECT\n" +
                        "i."+ItemSchema.Cols.id+","+"SUM("+ItemSchema.Cols.item_price+"*"+Line_Cols.quantity+") AS 'Total'\n" +
                        "FROM orders o JOIN order_line_items ON o."+Cols.id+" = "+Line_Cols.order_id+"\n" +
                        "JOIN items i ON "+Line_Cols.item_id+" = i."+ItemSchema.Cols.id+"\n" +
                        "WHERE o."+Cols.id+" = ?\n" +
                        "GROUP BY i."+ItemSchema.Cols.id+";"
        )) {
            stmt.setString(1, orderID);
            ResultSet rs = stmt.executeQuery();

            Map<Integer, Double> receipt = new HashMap<>();
            while(rs.next()) {
                receipt.put(rs.getInt(ItemSchema.Cols.id.name()), rs.getDouble("Total"));
            }
            return receipt;
        }
    }

    public Map<Integer, Integer[]> availableInventoryForOrder(String orderID)
            throws SQLException {

        try(PreparedStatement stmt = con.prepareStatement(
                "SELECT "+Line_Cols.item_id+","+Line_Cols.quantity+","+ItemSchema.Inv_Cols.amount+
                        "\nFROM order_line_items JOIN inventory USING ("+ Line_Cols.item_id+")\n" +
                        "WHERE "+Line_Cols.order_id+"=?;"
        )) {
            stmt.setString(1, orderID);
            ResultSet rs = stmt.executeQuery();

            Map<Integer, Integer[]> inventoryCheck = new HashMap<>();
            while(rs.next()) {
                inventoryCheck.put(rs.getInt(Line_Cols.item_id.name()),
                        new Integer[] {rs.getInt(Line_Cols.quantity.name()),
                                rs.getInt(ItemSchema.Inv_Cols.amount.name())
                });
            }

            return inventoryCheck;
        }
    }
}
