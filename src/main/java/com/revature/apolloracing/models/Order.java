package com.revature.apolloracing.models;

import java.util.Date;
import java.util.UUID;

public class Order {
    private String mID;
    private long mDateFulfilled; //Dates can be interpreted numerically
    private String mUserID;      //  as longs within MySQL tables
    private int mLocationID;

    public Order(String id, Long date) {
        mID = id == null ? UUID.randomUUID().toString() : id;
        mDateFulfilled = date == null ? 0L : date;
    }
    public Order(String id, Long date,
                 String userID, int locID) {
        this(id, date);
        mUserID = userID;
        mLocationID = locID;
    }

    public String getID() {
        return mID;
    }

    public long getDateFulfilled() {
        return mDateFulfilled;
    }

    public void setDateFulfilled(long date) {
        mDateFulfilled = date;
    }

    public String getUserID() {
        return mUserID;
    }

    public int getLocationID() {
        return mLocationID;
    }

    @Override
    public String toString() {
        return String.format(
                "Order #: %s\n" +
                        "from store N. %d.\n" +
                        "%s",
                mID, mLocationID, mDateFulfilled == 0L ?
                        "In Cart" : "Date Fulfilled: "+new Date(mDateFulfilled)
        );
    }
}
