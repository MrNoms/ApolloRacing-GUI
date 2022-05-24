package com.revature.application.daos;

import com.revature.application.models.User;
import com.revature.application.util.database.DatabaseConnection;

import java.io.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements CrudDAO<User> {
    // String path = "src/main/res/database/user.txt";
    Connection con = DatabaseConnection.getCon();

    @Override
    public void save(User u) throws RuntimeException {

    }

    @Override
    public User getByID(String id) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    public List<String> getAllUsernames() {
        List<String> out = new ArrayList<>();
        return out;
    }

    @Override
    public void update(User obj) {

    }

    @Override
    public void delete(User obj) {

    }
}
