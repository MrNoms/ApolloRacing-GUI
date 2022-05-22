package com.revature.application.daos;

import com.revature.application.models.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class UserDAO implements CrudDAO<User> {
    String path = "src/main/res/database/user.txt";

    @Override
    public void save(User u) {
        try {
            File base = new File(path);
            FileWriter fin = new FileWriter(base, true);
            fin.write(u.toFileString());
            fin.close();
        }
        catch(IOException ioe) {
            throw new RuntimeException(ioe.getMessage());
        }
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
