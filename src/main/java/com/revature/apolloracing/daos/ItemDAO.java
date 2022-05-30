package com.revature.apolloracing.daos;

import com.revature.apolloracing.models.Item;
import com.revature.apolloracing.util.database.DBSchema;
import com.revature.apolloracing.util.database.ItemSchema.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemDAO extends CrudDAO<Item> {
    public ItemDAO(DBSchema s) { super(s); }

    @Override
    protected Item getObject(ResultSet rs) throws SQLException {
        return new Item(rs.getInt(Cols.id.name()), rs.getString(Cols.item_description.name()),
                rs.getDouble(Cols.item_price.name()));
    }

    @Override
    public void save(Item obj) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(
                    "INSERT INTO items ("+Cols.item_description+","+Cols.item_price+") VALUES\n" +
                            "(?,?);"
            );
            stmt.setString(1, obj.getDescription());
            stmt.setDouble(2, obj.getPrice());
            stmt.executeUpdate();
        }
        catch(SQLException e) { throw e; }
        finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ignore) {}
            }
        }
    }

    @Override
    void update(Item obj) throws SQLException {
            PreparedStatement stmt = null;

            try {
                stmt = con.prepareStatement(
                        "UPDATE items SET\n" +
                                Cols.item_description+" ?,\n" +
                                Cols.item_price+" ?\n" +
                                "WHERE id = ?;"
                );
                stmt.setString(1, obj.getDescription());
                stmt.setDouble(2, obj.getPrice());
                stmt.setInt(3, obj.getID());

                stmt.executeUpdate();
            }
            catch(SQLException e) { throw e; }
            finally {
                if(stmt != null) {
                    try { stmt.close(); }
                    catch(SQLException ignore) {}
                }
            }
    }

    @Override
    void delete(Item obj) throws SQLException {
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement(
                    "DELETE FROM users WHERE id = ?;"
            );
            stmt.setInt(1, obj.getID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ignore) {}
            }
        }
    }
}
