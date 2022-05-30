package com.revature.apolloracing.models;

public class Location {
    private int mID;
    private String mCity;
    private String mState;

    public int getID() {
        return mID;
    }

    public String getCity() {
        return mCity;
    }

    public String getState() {
        return mState;
    }

    public Location(Integer id) {
        if(id != null) mID = id; //Database will automatically assign id #
    }

    public Location(Integer id,
                    String city, String state) {
        this(id);
        mCity = city;
        mState = state;
    }

    @Override
    public String toString() {
        return String.format(
                "Apollo Racing Store #%d at %s, %s",
                mID, mCity, mState
        );
    }
}
