package com.example.s6proyect;

public class User {
    private String username;
    private String password;
    private String ID;

    public User (String username, String password, String ID) {
        this.username = username;
        this.password = password;
        this.ID = ID;
    }

    public User () {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }
}
