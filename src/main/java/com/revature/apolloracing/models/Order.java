package com.revature.apolloracing.models;

import java.sql.Date;
import java.util.UUID;

public class Order {
    private String mID;
    private Date mDateFulfilled;
    private String mUserID;
    private int mLocationID;

    private Order() {
        mID = UUID.randomUUID().toString();
        mDateFulfilled = null;
    }
    public Order(String userID, int locID) {
        this();
        mUserID = userID;
        mLocationID = locID;
    }

    public Order(String mID, Date mDateFulfilled, String mUserID, int mLocationID) {
        this.mID = mID;
        this.mDateFulfilled = mDateFulfilled;
        this.mUserID = mUserID;
        this.mLocationID = mLocationID;
    }

    public String getID() {
        return mID;
    }

    public Date getDateFulfilled() {
        return mDateFulfilled;
    }

    public void setDateFulfilled(Date date) {
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
                mID, mLocationID, mDateFulfilled == null ?
                        "In Cart" : "Date Fulfilled: "+mDateFulfilled
        );
    }
}
