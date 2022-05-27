package com.revature.apolloracing.daos;

import com.revature.apolloracing.models.Location;
import com.revature.apolloracing.util.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class LocationDAO implements CrudDAO<Location> {
    public enum Cols { id, city, state }
    Connection con = DatabaseConnection.getCon();

    @Override
    public void save(Location l) throws SQLException {
        return;
    }

    @Override
    public Location getByID(String id) { return null;}
    public Location getByID(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Location> getAll() throws SQLException {
        return null;
    }

    @Override
    public void update(Location l) throws SQLException {

    }

    @Override
    public void delete(Location l) throws SQLException {

    }
}
