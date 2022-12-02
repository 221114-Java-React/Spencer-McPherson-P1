package com.revature.dtos.responses;

public class Principal {
    private String userID;
    private String username;
    private String email;
    private String given_name;
    private boolean is_Active;
    private String role;


    public Principal() {
        super();
    }

    public Principal(String userID, String username, String email, String given_name, boolean is_Active, String role) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.given_name = given_name;
        this.is_Active = is_Active;
        this.role = role;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGiven_name() {
        return given_name;
    }

    public void setGiven_name(String given_name) {
        this.given_name = given_name;
    }

    public boolean isIs_Active() {
        return is_Active;
    }

    public void setIs_Active(boolean is_Active) {
        this.is_Active = is_Active;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Principal{" +
                "userID='" + userID + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", given_name='" + given_name + '\'' +
                ", is_Active=" + is_Active +
                ", role='" + role + '\'' +
                '}';
    }
}
