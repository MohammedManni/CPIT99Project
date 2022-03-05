package com.example.safemedicare;

public class Patient extends User {
    public Patient(String fullName, String username, int id, int linkID, int phone_number, int age) {
        super(fullName, username, id, linkID, phone_number, age);
    }

    @Override
    public String getFullName() {
        return super.getFullName();
    }

    @Override
    public void setFullName(String fullName) {
        super.setFullName(fullName);
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public void setUsername(String username) {
        super.setUsername(username);
    }

    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public void setId(int id) {
        super.setId(id);
    }

    @Override
    public int getPhone_number() {
        return super.getPhone_number();
    }

    @Override
    public void setPhone_number(int phone_number) {
        super.setPhone_number(phone_number);
    }

    @Override
    public int getAge() {
        return super.getAge();
    }

    @Override
    public void setAge(int age) {
        super.setAge(age);
    }
}
