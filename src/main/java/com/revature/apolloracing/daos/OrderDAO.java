package com.revature.apolloracing.daos;

import com.revature.apolloracing.models.Item;
import com.revature.apolloracing.models.Order;
import com.revature.apolloracing.util.annotations.Inject;
import com.revature.apolloracing.util.database.DBSchema;
import com.revature.apolloracing.util.database.OrderSchema.*;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDAO extends CrudDAO<Order> {
    @Inject
    public OrderDAO(DBSchema s) { super(s); }

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

    @Override
    public void update(Order o) throws SQLException {

        try (PreparedStatement stmt = con.prepareStatement(
                "UPDATE orders SET\n" +
                        Cols.date_fulfilled + " = ?,\n" +
                        Cols.location_id + " = ?\n" +
                        "WHERE id = ?;"
        )) {
            int col = 1;
            for (Object arg : new Object[]{
                    o.getDateFulfilled(), o.getLocationID()
            }) {
                stmt.setObject(col++, arg);
            }
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
}
