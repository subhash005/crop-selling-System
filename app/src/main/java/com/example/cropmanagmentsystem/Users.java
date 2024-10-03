package com.example.cropmanagmentsystem;

public class Users {
    String profilepic, mail, userName, password, userId , address,number;


    public Users(){

    }
    // Constructor to initialize all fields
    public Users(String userId, String profilepic, String mail, String userName, String password, String address, String number) {
        this.userId = userId;
        this.profilepic = profilepic;
        this.mail = mail;
        this.userName = userName;
        this.password = password;
        this.address = address;
        this.number = number;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
