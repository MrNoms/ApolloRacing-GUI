package com.revature.application.daos;

import com.revature.application.models.User;
import com.revature.application.util.database.DatabaseConnection;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements CrudDAO<User> {
    // String path = "src/main/res/database/user.txt";
    Connection con = DatabaseConnection.getCon();

    @Override
    public void save(User u) throws SQLException {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.execute(String.format(
                    "INSERT INTO users VALUES ('%s', '%s', '%s', '%s'," +
                            "'%s', '%s', %b, %b);",
                    u.getID(), u.getRole(), u.getUserName(), u.getPassword(),
                    u.getEmail(), u.getPhone(), u.getEmailAuth(), u.getPhoneAuth())
            );
        }
        catch (SQLException e) {
            throw new SQLException("SQLException: "+e.getMessage()+
                    "\nSQLState: "+e.getSQLState());
        }
        finally {
            if (stmt != null) {
                try {stmt.close();}
                catch(SQLException ignore) {}
                stmt = null;
            }
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
