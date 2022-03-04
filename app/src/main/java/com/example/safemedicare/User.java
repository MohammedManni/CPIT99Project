package com.example.safemedicare;

public class User {
    String FullName, Username;
    int Id,LinkID, Phone_number, Age;

    public User() {
    }

    public User(String fullName, String username, int id, int linkID, int phone_number, int age) {
        FullName = fullName;
        Username = username;
        Id = id;
        LinkID = linkID;
        Phone_number = phone_number;
        Age = age;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getPhone_number() {
        return Phone_number;
    }

    public void setPhone_number(int phone_number) {
        Phone_number = phone_number;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }
}
