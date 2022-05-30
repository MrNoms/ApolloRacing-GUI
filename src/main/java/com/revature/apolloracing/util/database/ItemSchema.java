package com.revature.apolloracing.util.database;

public class ItemSchema implements DBSchema {
    private String tableName = "items";
    public enum Cols { id, item_description, item_price }
    @Override
    public String getTableName() { return tableName; }
}
