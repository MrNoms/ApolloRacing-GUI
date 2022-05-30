package com.revature.apolloracing.models;

public class Item {
    private int mID;
    private String mDescription;
    private double mPrice;

    public Item(Integer id) {
        if(id != null) mID = id;
    }
    public Item(Integer id,
                String desc, double price) {
        this(id);
        mDescription = desc;
        mPrice = price;
    }

    public int getID() {
        return mID;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String desc) {
        mDescription = desc;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        mPrice = price;
    }

    @Override
    public String toString() {
        return String.format(
                "Part #: %d\t\t$%f\n" +
                "Product Description: %s",
                mID, mPrice, mDescription
        );
    }
}
