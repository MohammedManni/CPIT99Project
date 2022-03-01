package com.example.safemedicare;

public class Patient extends User {
    public Patient(String firstName, String lastName, String username, String email, String password, String hospital, int id, int phone_number, int age) {
        super(firstName, lastName, username, email, password, hospital, id, phone_number, age);
    }
}
