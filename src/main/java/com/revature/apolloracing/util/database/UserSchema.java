package com.revature.apolloracing.util.database;

public class UserSchema implements DBSchema {
    private String tableName = "users";
    public enum Cols { id, role, username, password, email, phone };

    @Override
    public String getTableName() {
        return tableName;
    }
}
