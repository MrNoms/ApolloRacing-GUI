package com.revature.apolloracing.ui;

import com.revature.apolloracing.daos.LocationDAO;
import com.revature.apolloracing.models.User;
import com.revature.apolloracing.services.LocationService;
import com.revature.apolloracing.services.UserService;
import com.revature.apolloracing.util.annotations.Inject;
import com.revature.apolloracing.util.custom_exceptions.InvalidUserException;
import com.revature.apolloracing.util.database.LocationSchema;

public class StartMenu implements IMenu{
    @Inject
    private final UserService mUserService;
    @Inject
    public StartMenu(UserService us) { mUserService = us; }

    @Override
    public void start() {
        MENU: {
            while(true) {
                welcomeMessage();
                cout.print("\nEnter: ");
                String input = cin.nextLine();

                switch (input.toLowerCase()) {
                    case "1":
                        login();
                        break;
                    case "2":
                        signup();
                        break;
                    case "x":
                        goodbyeMessage();
                        break MENU;
                    default:
                        cout.println("\nInvalid input.");
                        break;
                }
            }
        }
    }

    private void welcomeMessage() {
        cout.println("\r\nWelcome to Apollo Racing Co. Your one stop shop for all\n" +
                "your karting needs.\n" +
                "[1] Login\n" +
                "[2] Signup\n" +
                "[x] Exit\n");
    }

    private void goodbyeMessage() {
        cout.println("\nCome back soon.");
    }

    private void login() {
        String uName;
        String pWord;
        User u;
        while(true) {
            cout.print("Type exit at any point to end this process and go back\n" +
                    "to the start menu." +
                    "\nUserName: ");
            uName = cin.nextLine();
            if(uName.equalsIgnoreCase("exit"))
                return;
            cout.print("Password: ");
            pWord = cin.nextLine();
            if(pWord.equalsIgnoreCase("exit"))
                return;

            try {
                u = mUserService.getValidCredentials(uName, pWord);
                break;
            } catch (InvalidUserException iu) {
                cout.println(iu.getMessage());
            }
        }
        new MainMenu(u, mUserService,
                new LocationService( new LocationDAO(new LocationSchema()))).start();

    }

    private void signup() {
        cout.println("\nBeautiful! You're one step closer to your place on the track\n" +
                "Let's get started creating your store site profile.\n" +
                "\nPlease create account credentials and provide contact information\n" +
                "Type \"exit\" at any time to return to the previous menu");

        String uName;
        String pWord;
        String eMail;
        String phone;

        credCreation:
        {
            while (true) {
                cout.print(mUserService.NAMEREQ);
                while (true) {
                    uName = cin.nextLine();
                    if(uName.equalsIgnoreCase("exit"))
                        break credCreation;

                    try {
                        if(mUserService.isValidUsername(uName) &&
                                mUserService.isNotDuplicateUsername(uName))
                            break;
                    } catch (InvalidUserException iu) {
                        cout.print(iu.getMessage());
                    }
                }
                cout.print(mUserService.PASSREQ+": ");
                while (true) {
                    pWord = cin.nextLine();
                    if(pWord.equalsIgnoreCase("exit"))
                        break credCreation;

                    try {
                        if (mUserService.isValidPassword(pWord)) {
                            cout.print("Please confirm password: ");
                            if (pWord.equals(cin.nextLine())) break;
                            else cout.print("Passwords do not match.\nEnter: ");
                        }
                    } catch (InvalidUserException iu) {
                        cout.print(iu.getMessage()+"\n: ");
                    }
                }

                cout.print("E-Mail: ");
                while (true) {
                    eMail = cin.nextLine();
                    if(eMail.equalsIgnoreCase("exit"))
                        break credCreation;
                    try {
                        if (mUserService.isValidEmail(eMail) &&
                                mUserService.isNotDuplicateEmail(eMail)) break;
                    } catch (InvalidUserException iu) {
                        cout.print(iu.getMessage()+": ");
                    }
                }
                cout.print("Please enter Phone # with your country code if you know it: ");
                while (true) {
                    phone = cin.nextLine();
                    if(phone.equalsIgnoreCase("exit"))
                        break credCreation;

                    try {
                        if (mUserService.isValidPhone(phone)) break;
                    } catch (InvalidUserException iu) {
                        cout.print(iu.getMessage()+": ");
                    }
                }

                credConfirm:
                {
                    while (true) {
                        cout.print("Please confirm your username and account information:\n" +
                                "Username: " + uName +
                                "\nEmail: " + eMail +
                                "\nPhone: " + phone +
                                "\nEnter y/n: ");
                        switch (cin.nextLine().toLowerCase()) {
                            case "y":
                                User newUser = new User(null, null, uName, pWord, eMail, phone);
                                try {
                                    mUserService.createUser(newUser);
                                    new MainMenu(newUser, mUserService,
                                            new LocationService( new LocationDAO(new LocationSchema()))).start();
                                }
                                catch(InvalidUserException iu) {
                                    cout.println("Account creation failed\n"+iu.getMessage());
                                }
                            case "exit":
                                break credCreation;
                            case "n":
                                break credConfirm;
                            default:
                                cout.println("Invalid input.");
                                break;
                        }
                    }
                }
            }
        }
    }
}
