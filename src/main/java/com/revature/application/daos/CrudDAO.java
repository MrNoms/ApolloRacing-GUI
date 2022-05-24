package com.revature.application.daos;

import java.sql.SQLException;
import java.util.List;

public interface CrudDAO<T> {
    //create read update delete

    void save(T obj) throws SQLException;

    T getByID(String id);

    List<T> getAll();

    void update(T obj);

    void delete(T obj);
}
