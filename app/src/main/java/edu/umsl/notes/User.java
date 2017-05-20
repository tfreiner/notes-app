package edu.umsl.notes;

public class User {

    private String first;
    private String last;
    private String email;
    private String username;
    private String password;
    private String activeUser;

    public User(String userFirst, String userLast, String userEmail, String username,  String userPassword, String activeUser) {
        this.first = userFirst;
        this.last = userLast;
        this.username = username;
        this.email = userEmail;
        this.password = userPassword;
        this.activeUser = activeUser;
    }

    public String getUsername(){
        return username;
    }
    public String getEmail(){
        return email;
    }
    public String getFirst(){
        return first;
    }
    public String getLast() {
        return last;
    }
    public String getPassword(){
        return password;
    }
    public String getActiveUser(){
        return activeUser;
    }

}
