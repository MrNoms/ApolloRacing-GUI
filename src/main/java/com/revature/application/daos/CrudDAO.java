package com.revature.application.daos;

import java.util.List;

public interface CrudDAO<T> {
    //create read update delete
    void save(T obj);

    T getByID(String id);

    List<T> getAll();

    void update(T obj);

    void delete(T obj);
}
