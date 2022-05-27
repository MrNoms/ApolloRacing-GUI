package com.revature.application.daos;

import java.sql.SQLException;
import java.util.List;

public interface CrudDAO<T> {
    //create read update delete

    void save(T obj) throws SQLException;

    T getByID(String id) throws SQLException;

    List<T> getAll() throws SQLException;

    void update(T obj) throws SQLException;

    void delete(T obj) throws SQLException;
}
