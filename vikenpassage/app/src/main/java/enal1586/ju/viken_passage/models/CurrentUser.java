package enal1586.ju.viken_passage.models;

import com.google.firebase.auth.FirebaseUser;

public class CurrentUser {
    
    private static final CurrentUser ourInstance = new CurrentUser();
    
    private String userName = "";
    private String phoneNumber = "";
    private boolean isLoggedIn = false;
    
    public static CurrentUser getInstance() { return ourInstance; }
    
    public static void logOut() {
        ourInstance.userName = "";
        ourInstance.phoneNumber = "";
        ourInstance.isLoggedIn = false;
    }
    public static void logIn(FirebaseUser currentUser) {
        ourInstance.setLoggedIn(true);
        ourInstance.setPhoneNumber(currentUser.getPhoneNumber());
        ourInstance.setUserName(currentUser.getDisplayName());
    }
    
    private CurrentUser() { /* Dont make this public! */ }
    
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public boolean isLoggedIn() {
        return isLoggedIn;
    }
    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
    
}
