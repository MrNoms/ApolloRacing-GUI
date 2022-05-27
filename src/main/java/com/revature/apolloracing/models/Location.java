package com.revature.apolloracing.models;

public class Location {
    private final int mID;
    private String mCity;
    private String mState;

    public int getID() {
        return mID;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public Location(int id, String city, String state) {
        mID = id;
        mCity = city;
        mState = state;
    }

    @Override
    public String toString() {
        return String.format(
                "Apollo Racing Store %d at %s, %s",
                mID, mCity, mState
        );
    }
}
