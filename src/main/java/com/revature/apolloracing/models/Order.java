package com.revature.apolloracing.models;

import java.util.UUID;

public class Order {
    private String mID;
    private long mDateFulfilled; //Dates can be interpreted numerically
    private String mUserID;      //  as longs within MySQL tables
    private int mLocationID;
    private double mOrderTotal;

    public Order(String id, Long date) {
        mID = id == null ? UUID.randomUUID().toString() : id;
        mDateFulfilled = date == null ? 0L : date;
    }
    public Order(String id, Long date,
                 String userID, int locID, double total) {
        this(id, date);
        mUserID = userID;
        mLocationID = locID;
        mOrderTotal = total;
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

    public double getOrderTotal() {
        return mOrderTotal;
    }

    public void setOrderTotal(double total) {
        mOrderTotal = total;
    }

    @Override
    public String toString() {
        return String.format(
                "Order #: %s\n" +
                        "from store N. %d. Order Total : $%f",
                mID, mLocationID, mOrderTotal
        );
    }
}
