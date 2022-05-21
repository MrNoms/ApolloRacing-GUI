package com.revature.application.models;

import java.util.UUID;

public class User {

    private final String mID;
    private UserStatus mRole;
    private String mUsername;
    private String mPassword;
    private String mEmail;
    private String mPhone;
    private boolean m2FAEmail;
    private boolean m2FAPhone;

    public User() {
        mID = UUID.randomUUID().toString();
        mRole = UserStatus.DEFAULT;
        m2FAEmail = false;
        m2FAPhone = false;
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

    public boolean getEmailAuth() { return m2FAEmail; }
    public void toggleEmailAuth() { m2FAEmail = !m2FAEmail; }

    public boolean getPhoneAuth() { return m2FAPhone; }
    public void togglePhoneAuth() { m2FAPhone = !m2FAPhone; }
}
