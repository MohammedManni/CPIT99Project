package com.example.safemedicare;

public class Patient {
    String userName, name;
    int id,phone_number,age;

    public Patient(int id,String userName, String name, int phone_number, int age) {
        this.userName = userName;
        this.name = name;
        this.id = id;
        this.phone_number = phone_number;
        this.age = age;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(int phone_number) {
        this.phone_number = phone_number;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}