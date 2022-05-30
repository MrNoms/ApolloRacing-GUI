package com.revature.apolloracing.util.database;

public class LocationSchema implements DBSchema {
    private String tableName = "locations";
    public enum Cols { id, city, state }
    @Override
    public String getTableName() { return tableName; }
}
