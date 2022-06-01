package com.revature.apolloracing.daos;

import com.revature.apolloracing.util.annotations.Inject;
import com.revature.apolloracing.util.custom_exceptions.ObjectDoesNotExist;
import com.revature.apolloracing.util.database.DBSchema;
import com.revature.apolloracing.util.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class CrudDAO<T> {
    //create read update delete
    @Inject
    protected DBSchema schema;
    protected Connection con;

    public CrudDAO(DBSchema s) {
        schema = s;
        con = DatabaseConnection.getCon();
    }

    protected abstract T getObject(ResultSet rs) throws SQLException;

    public abstract void save(T obj) throws SQLException;

    public T getByID(Object id) throws SQLException, ObjectDoesNotExist {
        ResultSet rs = null;
        T out;

        try (PreparedStatement stmt = con.prepareStatement(
                "SELECT * FROM "+schema.getTableName()+
                        "\nWHERE id = ?;");
        ) {

            stmt.setObject(1, id);
            rs = stmt.executeQuery();

            if(rs.next()) out = getObject(rs);
            else throw new ObjectDoesNotExist(schema.getTableName());
            return out;
        } finally {
            if(rs != null) {
                try { rs.close(); }
                catch(SQLException ignore) {}
            }
        }
    }

    public List<T> getAll() throws SQLException, ObjectDoesNotExist {
        List<T> out = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(
                "SELECT * FROM " + schema.getTableName());
             ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) out.add(getObject(rs));
            if (out.isEmpty())
                throw new ObjectDoesNotExist(schema.getTableName());
            return out;
        }
    }

    abstract void update(T obj) throws SQLException;

    abstract void delete(T obj) throws SQLException;
}
