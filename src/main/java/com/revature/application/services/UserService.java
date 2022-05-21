package com.revature.application.services;

import com.revature.application.util.custom_exceptions.InvalidUserException;

import java.util.spi.CalendarNameProvider;

public class UserService {
    public final String NAMEREQ =
            "Username must be alphanumeric and 8-20 characters long: ";

    public final String REPREQ = "\tNo repetitive characters\n";
    public final String ALNREQ = "\tUse letters and numbers\n";
    public final String SPECREQ = "\tUse least one of the following ~ ` ! @ # $ % ^ & * ? ; :\n";
    public final String LENREQ = "\tBe at least 8 characters long\n";
    public final String PASSREQ = "Password requirements:\n"+
            REPREQ+ALNREQ+SPECREQ+LENREQ+": ";


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
        if(mail.toLowerCase().matches("^(?!.*[-.]{2})[\\w.-]+(?!.*@\\.)@[a-z-.]+\\.[a-z]{2,3}"))
            return true;
        throw new InvalidUserException("Please enter a valid email: ");
    }
}
