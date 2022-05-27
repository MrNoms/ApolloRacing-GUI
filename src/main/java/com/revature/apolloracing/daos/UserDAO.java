package com.revature.apolloracing.daos;

import com.revature.apolloracing.models.User;
import com.revature.apolloracing.util.custom_exceptions.InvalidUserException;
import com.revature.apolloracing.util.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements CrudDAO<User> {
    public enum Cols { id, role, username, password, email, phone }
    Connection con = DatabaseConnection.getCon();

    private User getUser(ResultSet rs) throws SQLException {
        return new User(rs.getString(Cols.id.name()), rs.getString(Cols.role.name()),
                rs.getString(Cols.username.name()), rs.getString(Cols.password.name()),
                rs.getString(Cols.email.name()), rs.getString(Cols.phone.name()));
    }

    @Override
    public void save(User u) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("INSERT INTO users VALUES (" +
                            "?,?,?,?,?,?);");
            stmt.setString(1, u.getID());
            stmt.setString(2, String.valueOf(u.getRole()));
            stmt.setString(3, u.getUserName());
            stmt.setString(4, u.getPassword());
            stmt.setString(5, u.getEmail());
            stmt.setString(6, u.getPhone());
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
            if (rs.next() && name.equals(rs.getString(Cols.username.name())))
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
                            Cols.username+" = ?,\n" +
                            Cols.password+" = ?,\n" +
                            Cols.email+" = ?,\n" +
                            Cols.phone+" = ?,\n" +
                                "WHERE "+Cols.id+" = ?;"
            );
            stmt.setString(1, obj.getUserName());
            stmt.setString(2, obj.getPassword());
            stmt.setString(3, obj.getEmail());
            stmt.setString(4, obj.getPhone());

            stmt.setString(5, obj.getID());

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
                    "DELETE FROM users WHERE "+Cols.id+" = ?;"
            );
            stmt.setString(1, obj.getID());

            stmt.executeUpdate();
        }
        catch(SQLException e) {
            throw e;
        }
    }
}
