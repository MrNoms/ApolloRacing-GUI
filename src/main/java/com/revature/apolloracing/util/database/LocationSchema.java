package com.revature.apolloracing.util.database;

public class LocationSchema implements DBSchema {
    public enum Cols { id, city, state }
    @Override
    public String getTableName() { return "locations"; }
}
