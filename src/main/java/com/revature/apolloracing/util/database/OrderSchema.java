package com.revature.apolloracing.util.database;

public class OrderSchema implements DBSchema {
    private String tableName = "orders";
    public enum Cols { id, date_fulfilled, user_id, location_id }
    @Override
    public String getTableName() { return tableName; }
}
