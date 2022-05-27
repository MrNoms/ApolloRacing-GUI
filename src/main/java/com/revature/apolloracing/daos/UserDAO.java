package com.revature.application.daos;

import com.revature.application.models.User;
import com.revature.application.util.custom_exceptions.InvalidUserException;
import com.revature.application.util.database.DatabaseConnection;
import com.revature.application.util.database.UserDBSchema.Cols;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements CrudDAO<User> {
    Connection con = DatabaseConnection.getCon();

    private User getUser(ResultSet rs) throws SQLException {
        return new User(rs.getString(Cols.ID), rs.getString(Cols.ROLE),
                rs.getString(Cols.USERNAME), rs.getString(Cols.PASSWORD),
                rs.getString(Cols.EMAIL), rs.getString(Cols.PHONE),
                rs.getBoolean(Cols.EMAIL2FA), rs.getBoolean(Cols.PHONE2FA));
    }

    @Override
    public void save(User u) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("INSERT INTO users VALUES (" +
                            "?,?,?,?,?,?,?,?);");
            stmt.setString(1, u.getID());
            stmt.setString(2, String.valueOf(u.getRole()));
            stmt.setString(3, u.getUserName());
            stmt.setString(4, u.getPassword());
            stmt.setString(5, u.getEmail());
            stmt.setString(6, u.getPhone());
            stmt.setBoolean(7, u.getEmailAuth());
            stmt.setBoolean(8, u.getPhoneAuth());
            stmt.executeUpdate();
        }
        catch (SQLException e) { throw e; }
        finally {
            if (stmt != null) {
                try {stmt.close();}
                catch(SQLException ignore) {}
                stmt = null;
            }
        }
    }

    @Override
    public User getByID(String id) throws SQLException {
        PreparedStatement stmt;
        ResultSet rs;
        User out = null;

        try {
            stmt = con.prepareStatement(
                    "SELECT * FROM users\n" +
                            "WHERE id = ?;"
            );
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            if(rs.next()) out = getUser(rs);
            else throw new InvalidUserException("User does not exist.");
            return out;
        }
        catch(SQLException | InvalidUserException e) {
            throw e;
        }
    }

    public User getByCredentials(String name, String pass) throws SQLException {
        PreparedStatement stmt;
        ResultSet rs;
        User out = null;

        try {
            stmt = con.prepareStatement(
                    "SELECT * FROM users\n" +
                            "WHERE username = ? AND password = ?;");
            stmt.setString(1, name);
            stmt.setString(2, pass);
            rs = stmt.executeQuery();
            //rs should return only a single record
            if(rs.next()) out = getUser(rs);
            else throw new InvalidUserException("Password and Username combination is incorrect.");
            return out;
        }
        catch(SQLException | InvalidUserException e) {
            throw e;
        }
    }

    @Override
    public List<User> getAll() throws SQLException {
        PreparedStatement stmt;
        ResultSet rs;
        List<User> out = new ArrayList<>();

        try {
            stmt = con.prepareStatement(
                    "SELECT * FROM users;"
            );
            rs = stmt.executeQuery();
            while(rs.next()) out.add(getUser(rs));
            if(out.isEmpty())
                throw new InvalidUserException("No users exist.");
            return out;
        }
        catch(SQLException | InvalidUserException e) {
            throw e;
        }
    }

    public Boolean findUsername(String name) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement(
                    "SELECT * FROM users WHERE username = ?;");
            stmt.setString(1, name);
            rs = stmt.executeQuery();
            if (rs.next() && name.equals(rs.getString(Cols.USERNAME)))
                return true;
        }
        catch (SQLException ignore) {}
        finally {
            if (stmt != null) {
                try {stmt.close();}
                catch(SQLException ignore) {}
                stmt = null;
            }
            if(rs != null) {
                try { rs.close(); }
                catch(SQLException ignore) {}
                rs = null;
            }
        }
        return false;
    }

    @Override
    public void update(User obj) throws SQLException {
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement(
                    "UPDATE users SET\n" +
                            Cols.USERNAME+" = ?,\n" +
                            Cols.PASSWORD+" = ?,\n" +
                            Cols.EMAIL+" = ?,\n" +
                            Cols.PHONE+" = ?,\n" +
                            Cols.EMAIL2FA+" = ?,\n" +
                            Cols.PHONE2FA+" = ?, \n" +
                                "WHERE "+Cols.ID+" = ?;"
            );
            stmt.setString(1, obj.getUserName());
            stmt.setString(2, obj.getPassword());
            stmt.setString(3, obj.getEmail());
            stmt.setString(4, obj.getPhone());
            stmt.setBoolean(5, obj.getEmailAuth());
            stmt.setBoolean(6, obj.getPhoneAuth());

            stmt.setString(7, obj.getID());

            stmt.executeUpdate();
        }
        catch(SQLException e) {
            throw e;
        }
    }

    @Override
    public void delete(User obj) throws SQLException {
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement(
                    "DELETE FROM users WHERE "+Cols.ID+" = ?;"
            );
            stmt.setString(1, obj.getID());

            stmt.executeUpdate();
        }
        catch(SQLException e) {
            throw e;
        }
    }
}
