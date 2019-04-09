package enal1586.ju.viken_passage.models;

class User {
    
    private static final User ourInstance = new User();
    
    static User getInstance() { return ourInstance; }
    
    private User() { /* Dont make this public! */ }
    
}
