package com.revature.apolloracing.util.database;

public class OrderSchema implements DBSchema {
    public enum Cols { id, date_fulfilled, user_id, location_id }
    public enum Line_Cols { order_id, item_id, quantity }
    @Override
    public String getTableName() { return "orders"; }
}
