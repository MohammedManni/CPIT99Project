package com.example.safemedicare;

public class User {
    String FirstName, LastName, Username, Email, Password, Hospital;
    int Id, Phone_number, Age;

    public User() {
    }

    public User(String firstName, String lastName, String username, String email, String password, String hospital, int id, int phone_number, int age) {
        FirstName = firstName;
        LastName = lastName;
        Username = username;
        Email = email;
        Password = password;
        Hospital = hospital;
        Id = id;
        Phone_number = phone_number;
        Age = age;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getHospital() {
        return Hospital;
    }

    public void setHospital(String hospital) {
        Hospital = hospital;
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
