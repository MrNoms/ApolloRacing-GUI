package com.revature.apolloracing.services;

import com.revature.apolloracing.daos.UserDAO;
import com.revature.apolloracing.models.User;
import com.revature.apolloracing.util.annotations.Inject;
import com.revature.apolloracing.util.custom_exceptions.InvalidUserException;

import java.sql.SQLException;

public class UserService {
    public final String NAMEREQ =
            "Username must be alphanumeric and 8-20 characters long: ";
    private final String REPREQ = "\tNo repetitive characters\n";
    private final String ALNREQ = "\tUse letters and numbers\n";
    private final String SPECREQ = "\tUse at least one of the following ~ ` ! @ # $ % ^ & * ? ; :\n";
    private final String LENREQ = "\tBe 8-256 characters long\n";
    public final String PASSREQ = "Password requirements:\n"+
            REPREQ+ALNREQ+SPECREQ+LENREQ;

    @Inject
    private final UserDAO mUserDAO;
    @Inject
    public UserService(UserDAO uDAO) {
        mUserDAO = uDAO;
    }

    public void createUser(User u) {
        try { mUserDAO.save(u); }
        catch(SQLException e) {
            throw new InvalidUserException(e.getMessage()+
                    "\nSQLState: "+e.getSQLState());
        }
    }

    public void removeUser(User u) {
        try { mUserDAO.delete(u); }
        catch(SQLException e) {
            throw new InvalidUserException(e.getMessage()+
                    "\nSQLState: "+e.getSQLState());
        }
    }

    public void updateUser(User u) {
        try { mUserDAO.update(u); }
        catch(SQLException e) {
            throw new InvalidUserException(e.getMessage()+
                    "\nSQLState: "+e.getSQLState());
        }
    }

    public boolean isValidUsername(String uName) throws InvalidUserException {
        if(uName.matches("^(?!.*_{2})(?=\\w{8,20}$)[^_].*[^_]$"))
            return true;
        throw new InvalidUserException("Invalid Username. "+NAMEREQ);
    }

    public boolean isNotDuplicateUsername(String uName) throws InvalidUserException {
        if (mUserDAO.findUsername(uName))
            throw new InvalidUserException("Username Unavailable. "+NAMEREQ);
        return true;
    }

    public boolean isValidPassword(String pWord) throws InvalidUserException {
        if(pWord.matches("^(?=.*[~`!@#$%^&*?;:\\w]+)(?!.*(.)\\1\\1).{8,256}"))
            return true;

        StringBuilder error = new StringBuilder("Invalid Password.\n");
        if(pWord.matches("^(?!.*[~`!@#$%^&*?;:]+).*")) error.append(SPECREQ);
        if(pWord.matches("^(?!.*\\w+).*")) error.append(ALNREQ);
        if(pWord.matches("(?=.*(.)\\1\\1).*")) error.append(REPREQ);
        if(pWord.matches("^.{0,7}$|^.{257,}$")) error.append(LENREQ);
        throw new InvalidUserException(new String(error));
    }

    public User getValidCredentials(String uName, String pWord) throws InvalidUserException {
        User out;
        try { out = mUserDAO.getByCredentials(uName, pWord); }
        catch (SQLException e) {
            throw new InvalidUserException(e.getMessage());
        }
        return out;
    }

    public boolean isValidEmail(String mail) throws InvalidUserException {
        if(mail.toLowerCase().matches("^(?!.*[-.]{2})[\\w.-]+(?!@\\.)@[a-z-.]+\\.[a-z]{2,3}"))
            return true;
        throw new InvalidUserException("Please enter a valid email");
    }

    public boolean isValidPhone(String pNum) throws InvalidUserException {
        if(pNum.matches("^(|(\\d{1,3}|(1|44)-\\d{3}) ?)(?!\\(\\d{3}\\)[.-])(\\(\\d{3}\\) ?|\\d{3})[ .-]?\\d{3}[ .-]?\\d{4}"))
            return true;
        throw new InvalidUserException("Please enter a valid phone number");
    }

}
