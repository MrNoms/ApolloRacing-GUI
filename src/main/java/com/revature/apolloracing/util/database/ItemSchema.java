package com.revature.apolloracing.util.database;

public class ItemSchema implements DBSchema {
    public enum Cols { id, name, item_description, item_price }
    public enum Inv_Cols { location_id, item_id, amount }
    @Override
    public String getTableName() { return "items"; }
}
