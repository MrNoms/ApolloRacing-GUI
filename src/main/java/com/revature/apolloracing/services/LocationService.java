package com.revature.apolloracing.services;

import com.revature.apolloracing.daos.LocationDAO;
import com.revature.apolloracing.models.Location;
import com.revature.apolloracing.util.annotations.Inject;
import com.revature.apolloracing.util.custom_exceptions.ObjectDoesNotExist;

import java.sql.SQLException;
import java.util.List;

public class LocationService {
    @Inject
    private final LocationDAO mLocDAO;
    @Inject
    public LocationService(LocationDAO lDAO) { mLocDAO = lDAO; }

    public void createLocation(Location l) throws SQLException {
        mLocDAO.save(l);
    }

    public List<Location> getAllLocs() throws SQLException, ObjectDoesNotExist {
        return mLocDAO.getAll();
    }

    public boolean closeStore(Location l) throws SQLException, ObjectDoesNotExist {
        mLocDAO.delete(l);
        return true;
    }
}
