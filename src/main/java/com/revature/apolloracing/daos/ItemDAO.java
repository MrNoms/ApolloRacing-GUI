package com.revature.apolloracing.daos;

import com.revature.apolloracing.models.Item;
import com.revature.apolloracing.util.custom_exceptions.ObjectDoesNotExist;
import com.revature.apolloracing.util.database.DBSchema;
import com.revature.apolloracing.util.database.ItemSchema.*;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.List;

public class ItemDAO extends CrudDAO<Item> {
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
                        Cols.name + "= ?,\n" +
                        Cols.item_description + "= ?,\n" +
                        Cols.item_price + "= ?\n" +
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

    public LinkedHashMap<Item, Integer[]> getInStock(Integer loc, String desc) throws SQLException, ObjectDoesNotExist {
        ResultSet rs = null;
        LinkedHashMap<Item, Integer[]> out = new LinkedHashMap<>();

        try (PreparedStatement stmt = con.prepareStatement(
                "SELECT " + Cols.id + "," + Inv_Cols.location_id + "," + Inv_Cols.amount + "," + Cols.name + "," +
                        Cols.item_description + "," + Cols.item_price + " FROM\n" +
                        "items t JOIN inventory v ON t.id = v.item_id\n" +
                        "WHERE " + Inv_Cols.amount + " > 0" +
                        (loc != null ? " AND " + Inv_Cols.location_id + "=?" : "") +
                        (desc != null ? " AND " + Cols.item_description + "=?" : "") + ";")
        ) {
            int col = 1;
            if(loc != null) stmt.setInt(col++, loc);
            if(desc != null) stmt.setString(col, desc);

            rs = stmt.executeQuery();
            while (rs.next()) {
                out.put(getObject(rs),
                        new Integer[] {rs.getInt(Inv_Cols.location_id.name()),
                                rs.getInt(Inv_Cols.amount.name())}
                );
            }
        } finally {
            if(rs!=null) {
                try{rs.close();}
                catch(SQLException ignore) {}
            }
        }
        return out;
    }

    public void setIsolation_DirtyRead() throws SQLException {
        if(con.getAutoCommit()) con.setAutoCommit(false);
        con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
    }

    public Savepoint startTransactionWithSavepoint(String savePoint) throws SQLException{
        return con.setSavepoint(savePoint);
    }

    public void endTransaction(Savepoint sp, boolean commitOrRollback) throws SQLException {
        if(commitOrRollback) con.commit();
        else con.rollback(sp);

        con.releaseSavepoint(sp);
        if(!con.getAutoCommit()) con.setAutoCommit(true);
    }

    public void saveInventoryItems(int l, List<Integer> i, List<Integer> amount)
            throws SQLException {
        StringBuilder update = new StringBuilder("INSERT INTO inventory VALUES");
        for(int x = 0; x < i.size(); x++) { update.append("\n(?,?,?),"); }
        update.replace(update.lastIndexOf(","), update.length(), ";");

        try (PreparedStatement stmt = con.prepareStatement(new String(update))) {
            for (int x = 0; x < i.size(); x++) {
                stmt.setInt(3*x + 1, l);
                stmt.setInt(3*x + 2, i.get(x));
                stmt.setInt(3*x + 3, amount.get(x));
            }
            stmt.executeUpdate();
        }
    }

    public void updateInventoryItems(int l, Item i, int amount)
            throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement("" +
                "UPDATE inventory SET "+Inv_Cols.amount+"="+Inv_Cols.amount+"+?\n" +
                "WHERE "+Inv_Cols.location_id+"= ? AND "+Inv_Cols.item_id+"=?;")
        ) {
            stmt.setInt(1, amount);
            stmt.setInt(2, l);
            stmt.setInt(3, i.getID());

            stmt.executeUpdate();
        }
    }
}
