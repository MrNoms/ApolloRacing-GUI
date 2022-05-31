package com.revature.apolloracing.daos;

import com.revature.apolloracing.models.Item;
import com.revature.apolloracing.models.Location;
import com.revature.apolloracing.util.custom_exceptions.ObjectDoesNotExist;
import com.revature.apolloracing.util.database.DBSchema;
import com.revature.apolloracing.util.database.ItemSchema.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO extends CrudDAO<Item> {
    public enum invCols { location_id, amount }
    public ItemDAO(DBSchema s) { super(s); }

    @Override
    protected Item getObject(ResultSet rs) throws SQLException {
        return new Item(rs.getInt(Cols.id.name()), rs.getString(Cols.name.name()),
                rs.getString(Cols.item_description.name()), rs.getDouble(Cols.item_price.name()));
    }

    @Override
    public void save(Item obj) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(
                "INSERT INTO items (" + Cols.name + "," + Cols.item_description + "," + Cols.item_price + ") VALUES\n" +
                        "(?,?,?);")
        ) {
            int col = 1;
            for (Object arg : new Object[]{
                    obj.getName(), obj.getDescription(), obj.getPrice()
            }) {
                stmt.setObject(col++, arg);
            }
            stmt.executeUpdate();
        }
    }

    @Override
    void update(Item obj) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(
                "UPDATE items SET\n" +
                        Cols.name + " ?,\n" +
                        Cols.item_description + " ?,\n" +
                        Cols.item_price + " ?\n" +
                        "WHERE id = ?;")
        ) {
            int col = 1;
            for (Object arg : new Object[]{
                    obj.getName(), obj.getDescription(), obj.getPrice(), obj.getID()
            }) {
                stmt.setObject(col++, arg);
            }
            stmt.executeUpdate();
        }
    }

    @Override
    void delete(Item obj) throws SQLException {

        try (PreparedStatement stmt = con.prepareStatement(
                "DELETE FROM items WHERE id = ?;")
        ) {
            stmt.setInt(1, obj.getID());

            stmt.executeUpdate();
        }
    }

    public List<Item> getInStock(Integer loc, String desc) throws SQLException, ObjectDoesNotExist {
        ResultSet rs;
        List<Item> out = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(
                "SELECT (" + Cols.id + "," + Cols.name + ","+"," + Cols.item_description + "," +
                        "," + Cols.item_price + ") FROM\n" +
                        "items t JOIN inventory v ON t.id = v.item_id\n" +
                        "WHERE " + invCols.amount + " > 0" +
                        (loc != null ? ", " + invCols.location_id + "=?" : "") +
                        (desc != null ? ", " + Cols.item_description + "=?" : "") + ";")
        ) {
            int col = 1;
            if(loc != null) stmt.setInt(col++, loc);
            if(desc != null) stmt.setString(col, desc);

            rs = stmt.executeQuery();
            if (!rs.isBeforeFirst()) throw new ObjectDoesNotExist("In stock items");
            else while (rs.next()) {
                out.add(new Item((rs.getInt(Cols.id.name())),
                        rs.getString(Cols.name.name()),
                        rs.getString(Cols.item_description.name()),
                        rs.getDouble(Cols.item_price.name())));
            }
            return out;
        }
    }

    public boolean saveInventoryItem(int l, int i, int amount)
            throws SQLException {
        try ( PreparedStatement stmt = con.prepareStatement(
                "INSERT INTO inventory VALUES (?,?,?);")
        ) {
            int col = 1;
            for(int arg : new int[]{l, i, amount})
                stmt.setInt(col++, arg);
            return stmt.executeUpdate() > 0;
        }
    }
}
