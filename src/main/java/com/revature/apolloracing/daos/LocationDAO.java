package com.revature.apolloracing.daos;

import com.revature.apolloracing.models.Location;
import com.revature.apolloracing.util.database.DBSchema;
import com.revature.apolloracing.util.database.LocationSchema.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationDAO extends CrudDAO<Location> {
    public LocationDAO(DBSchema s) {
        super(s);
    }

    @Override
    protected Location getObject(ResultSet rs) throws SQLException {
        return new Location(rs.getInt(Cols.id.name()), rs.getString(Cols.city.name()),
                rs.getString(Cols.state.name()));
    }

    @Override
    public void save(Location l) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(
                "INSERT INTO locations ("+Cols.city+","+Cols.state+") VALUES" +
                        "\n(?, ?);")
        ){
            stmt.setString(1, l.getCity());
            stmt.setString(2, l.getState());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(Location l) throws SQLException {
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement(
                    "UPDATE locations SET\n" +
                            Cols.city+" ?,\n" +
                            Cols.state+" ?\n" +
                            "WHERE id = ?;"
            );
            stmt.setString(1, l.getCity());
            stmt.setString(2, l.getState());
            stmt.setInt(3, l.getID());

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
    public void delete(Location l) throws SQLException {
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement(
                    "DELETE FROM locations WHERE id = ?;"
            );
            stmt.setInt(1, l.getID());

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
