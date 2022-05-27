package com.revature.apolloracing.models;

import java.util.UUID;

public class User {
    public enum UserStatus { DEFAULT, ADMIN }

    private final String mID;
    private UserStatus mRole;
    private String mUsername;
    private String mPassword;
    private String mEmail;
    private String mPhone;

    public User() {
        mID = UUID.randomUUID().toString();
        mRole = UserStatus.DEFAULT;
    }
    public User(String name, String pass, String mail, String num) {
        this();
        mUsername = name;
        mPassword = pass;
        mEmail = mail;
        mPhone = num;
    }
    public User(String id, String role, String name, String pass, String mail,
                String num) {
        mID = id;
        mRole = UserStatus.valueOf(role);
        mUsername = name;
        mPassword = pass;
        mEmail = mail;
        mPhone = num;
    }

    public String getID() { return mID; }

    public UserStatus getRole() { return mRole; }
    public void setRole(UserStatus role) { mRole = role; }

    public String getUserName() { return mUsername; }
    public void setUserName(String uName) { mUsername = uName; }

    public String getPassword() { return mPassword; }
    public void setPassword(String pWord) { mPassword = pWord; }

    public String getEmail() { return mEmail; }
    public void setEmail(String eMail) { mEmail = eMail; }

    public String getPhone() { return mPhone; }
    public void setPhone(String pNum) { mPhone = pNum; }

    @Override
    public String toString() {
        return String.format(
                "Username: %s\n" +
                        "Password: *****\n" +
                        "Email: %s" +
                        "Phone: %s",
                mUsername, mEmail, mPhone
        );
    }
}
