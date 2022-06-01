package com.revature.apolloracing.services;

import com.revature.apolloracing.daos.UserDAO;
import com.revature.apolloracing.util.custom_exceptions.InvalidUserException;
import com.revature.apolloracing.util.database.UserSchema;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserServiceTest {
    UserService uServ = new UserService(new UserDAO(new UserSchema()));

    @Test
    public void isValidUsername() {
        assertTrue(uServ.isValidUsername("1234567a"));
    }
    @Test
    public void isValidUsername1() {
        assertTrue(uServ.isValidUsername("12_4567a"));
    }
    @Test
    public void isValidUsername2() {
        assertTrue(uServ.isValidUsername("Lots_of_lettersN1num"));
    }

    private void usernameTooShort() {
        uServ.isValidUsername("7_chars");
    }
    @Test
    public void notValidUsername() {
        assertThrows(InvalidUserException.class,this::usernameTooShort);
    }

    private void usernameTooLong() {
        uServ.isValidUsername("Way2Many_CharsInThisUsername");
    }
    @Test
    public void notValidUsername1() {
        assertThrows(InvalidUserException.class, this::usernameTooLong);
    }

    private void consecutive__() {
        uServ.isValidUsername("ThereAre2__sHere");
    }
    @Test
    public void notValidUsername2() {
        assertThrows(InvalidUserException.class, this::consecutive__);
    }

    private void ending_() {
        uServ.isValidUsername("NameEndswithA_");
    }
    @Test
    public void notValidUsername3() {
        assertThrows(InvalidUserException.class, this::ending_);
    }

    private void badCharacter() {
        uServ.isValidUsername("RightNumberWrongCH@R");
    }
    @Test
    public void notValidUsername4() {
        assertThrows(InvalidUserException.class, this::badCharacter);
    }

    @Test
    public void isValidPassword() {
        assertTrue(uServ.isValidPassword("123456t&"));
    }
    @Test
    public void isValidPassword1() {
        assertTrue(uServ.isValidPassword("R3gu74R0!dP@ssw046"));
    }

    private void tooShort() {
        uServ.isValidPassword("short1");
    }
    @Test
    public void notValidPassword() {
        assertThrows(InvalidUserException.class, this::tooShort);
    }

    private void noSpecialChar() {
        uServ.isValidPassword("NotGoodenou6h");
    }
    @Test
    public void notValidPassword1() {
        assertThrows(InvalidUserException.class, this::noSpecialChar);
    }

    private void repeats() {
        uServ.isValidPassword("WouldB3G~~~d");
    }
    @Test
    public void notValidPassword2() {
        assertThrows(InvalidUserException.class, this::repeats);
    }

    @Test
    public void isValidEmail() {
        assertTrue(uServ.isValidEmail("justANormal_address@domain.dir"));
    }

    private void twoAts() {
        uServ.isValidEmail("really@weird@messed.up");
    }
    @Test
    public void notValidEmail() {
        assertThrows(InvalidUserException.class, this::twoAts);
    }

    @Test
    public void isValidPhone() {
        assertTrue(uServ.isValidPhone("1 510-123-4567"));
    }
    @Test
    public void isValidPhone1() {
        assertTrue(uServ.isValidPhone("12 647-123-4567"));
    }

    private void noAreaCode() {
        uServ.isValidPhone("345-3445");
    }
    @Test
    public void notValidPhone() {
        assertThrows(InvalidUserException.class, this::noAreaCode);
    }

}