package com.revature.application.services;

import com.revature.application.daos.UserDAO;
import com.revature.application.models.User;
import com.revature.application.util.annotations.Inject;
import com.revature.application.util.custom_exceptions.InvalidUserException;

public class UserService {
    public final String NAMEREQ =
            "Username must be alphanumeric and 8-20 characters long: ";
    private final String REPREQ = "\tNo repetitive characters\n";
    private final String ALNREQ = "\tUse letters and numbers\n";
    private final String SPECREQ = "\tUse least one of the following ~ ` ! @ # $ % ^ & * ? ; :\n";
    private final String LENREQ = "\tBe at least 8 characters long\n";
    public final String PASSREQ = "Password requirements:\n"+
            REPREQ+ALNREQ+SPECREQ+LENREQ+": ";

    @Inject
    private final UserDAO mUserDAO;
    @Inject
    public UserService(UserDAO uDAO) {
        mUserDAO = uDAO;
    }

    public void createUser(User u) {
        try { mUserDAO.save(u); }
        catch(Exception e) {
            throw e;
        }
    }

    public void isValidUsername(String uName) throws InvalidUserException {
        if(uName.matches("^(?!.*_{2})(?=\\w{8,20}$)[^_].*[^_]$"))
            return;
        throw new InvalidUserException("Invalid Username. "+NAMEREQ);
    }

    public boolean isValidPassword(String pWord) throws InvalidUserException {
        if(pWord.matches("^(?=.*[~`!@#$%^&*?;:]+)(?=.*\\w+)(?!.*(.)\\1\\1).{8,}"))
            return true;

        StringBuilder error = new StringBuilder("Invalid Password.\n");
        if(pWord.matches("^(?!.*[~`!@#$%^&*?;:]+).*")) error.append(SPECREQ);
        if(pWord.matches("^(?!.*\\w+).*")) error.append(ALNREQ);
        if(pWord.matches("(?=.*(.)\\1\\1).*")) error.append(REPREQ);
        if(pWord.matches("^.{0,7}$")) error.append(LENREQ);
        throw new InvalidUserException(new String(error)+"\n: ");
    }

    public boolean isValidEmail(String mail) throws InvalidUserException {
        if(mail.toLowerCase().matches("^(?!.*[-.]{2})[\\w.-]+(?!@\\.)@[a-z-.]+\\.[a-z]{2,3}"))
            return true;
        throw new InvalidUserException("Please enter a valid email: ");
    }

    public boolean isValidPhone(String pNum) throws InvalidUserException {
        if(pNum.matches("^(|(\\d{1,3}|(1|44)-\\d{3}) ?)(?!\\(\\d{3}\\)[.-])(\\(\\d{3}\\) ?|\\d{3})([ .-]?)\\d{3}\\5\\d{4}"))
            return true;
        throw new InvalidUserException("Please enter a valid phone number");
    }
}
