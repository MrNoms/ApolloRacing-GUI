package com.revature.application.daos;

import com.revature.application.models.User;

import java.util.List;

public class UserDAO implements CrudDAO<User> {
    String path = "src/main/res/database/user.txt";

    @Override
    public void save(User obj) {

    }

    @Override
    public User getByID(String id) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public void update(User obj) {

    }

    @Override
    public void delete(User obj) {

    }
}
