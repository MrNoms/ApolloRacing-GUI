package com.revature.apolloracing.daos;

import com.revature.apolloracing.models.Order;
import com.revature.apolloracing.util.database.DBSchema;
import com.revature.apolloracing.util.database.OrderSchema.Cols;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDAO extends CrudDAO<Order> {
    public OrderDAO(DBSchema s) { super(s); }

    @Override
    protected Order getObject(ResultSet rs) throws SQLException {
        return new Order(rs.getString(Cols.id.name()), rs.getLong(Cols.date_fulfilled.name()),
                rs.getString(Cols.user_id.name()), rs.getInt(Cols.location_id.name()));
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
