package com.revature.apolloracing.daos;

import com.revature.apolloracing.models.User;
import com.revature.apolloracing.util.custom_exceptions.InvalidUserException;
import com.revature.apolloracing.util.custom_exceptions.ObjectDoesNotExist;
import com.revature.apolloracing.util.database.DBSchema;
import com.revature.apolloracing.util.database.UserSchema.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends CrudDAO<User> {
    public UserDAO(DBSchema s) {
        super(s);
    }

    @Override
    protected User getObject(ResultSet rs) throws SQLException {
        return new User(rs.getString(Cols.id.name()), rs.getString(Cols.role.name()),
                rs.getString(Cols.username.name()), rs.getString(Cols.password.name()),
                rs.getString(Cols.email.name()), rs.getString(Cols.phone.name()));
    }

    @Override
    public void save(User u) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(
                "INSERT INTO users VALUES" +
                        "\n(?,?,?,?,?,?);")
        ) {

            int col = 1;
            for (Object arg : new Object[]{
                    u.getID(), String.valueOf(u.getRole()), u.getUserName(),
                    u.getPassword(), u.getEmail(), u.getPhone()
            }) {
                stmt.setObject(col++, arg);
            }

            stmt.executeUpdate();
        }
    }

    @Override
    public void update(User obj) throws SQLException {

        try (PreparedStatement stmt = con.prepareStatement(
                "UPDATE users SET\n" +
                        Cols.role + " = ?,\n" +
                        Cols.username + " = ?,\n" +
                        Cols.password + " = ?,\n" +
                        Cols.email + " = ?,\n" +
                        Cols.phone + " = ?\n" +
                        "WHERE id = ?;")
        ) {
            int col = 1;
            for (Object arg : new Object[]{
                    obj.getRole().name(), obj.getUserName(), obj.getPassword(),
                    obj.getEmail(), obj.getPhone(), obj.getID()
            }) {
                stmt.setObject(col++, arg);
            }
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(User obj) throws SQLException {

        try (
                PreparedStatement stmt = con.prepareStatement(
                        "DELETE FROM users WHERE id = ?;")
        ) {
            stmt.setString(1, obj.getID());

            stmt.executeUpdate();
        }
    }

    public User getByCredentials(String name, String pass) throws SQLException, InvalidUserException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User out;

        try {
            stmt = con.prepareStatement(
                    "SELECT * FROM users\n" +
                            "WHERE " + Cols.username + " = ? AND " + Cols.password + " = ?;");
            stmt.setString(1, name);
            stmt.setString(2, pass);
            rs = stmt.executeQuery();
            //rs should return only a single record
            if (rs.next()) out = getObject(rs);
            else throw new InvalidUserException("Password and Username combination is incorrect.");

            return out;
        } finally {
            if(stmt!=null) {
                try {stmt.close();}
                catch(SQLException ignore) {}
            }
            if(rs!=null) {
                try{rs.close();}
                catch(SQLException ignore) {}
            }
        }
    }

    public boolean findUsername(String name) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement(
                    "SELECT * FROM users WHERE username = ?;");
            stmt.setString(1, name);
            rs = stmt.executeQuery();
            if (rs.next() && name.equals(rs.getString(Cols.username.name())))
                return true;
        } catch (SQLException ignore) {}
        finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ignore) {}
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ignore) {}
            }
        }
        return false;
    }

    public boolean findEmail(String email) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement(
                    "SELECT * FROM users WHERE email = ?;");
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            if (rs.next() && email.equals(rs.getString(Cols.email.name())))
                return true;
        }
        finally {
            if (stmt != null) {
                try { stmt.close(); }
                catch (SQLException ignore) {}
            }
            if (rs != null) {
                try { rs.close(); }
                catch (SQLException ignore) {}
            }
        }
        return false;
    }

    public List<User> getAllLike(String s) throws SQLException, ObjectDoesNotExist {
        List<User> out = new ArrayList<>();
        ResultSet rs = null;
        try (
                PreparedStatement stmt =
                        con.prepareStatement(
                                "SELECT * FROM users WHERE \n" +
                                        "INSTR("+Cols.username+",?) " +
                                        "OR INSTR("+Cols.email+",?) IS TRUE;"
                )
        ) {
            stmt.setString(1, s);
            stmt.setString(2, s);
            rs = stmt.executeQuery();
            while (rs.next()) out.add(getObject(rs));
            if (out.isEmpty())
                throw new ObjectDoesNotExist(schema.getTableName());
            return out;
        } finally {
            if(rs!=null) {
                try{rs.close();}
                catch(SQLException ignore){}
            }
        }
    }
}