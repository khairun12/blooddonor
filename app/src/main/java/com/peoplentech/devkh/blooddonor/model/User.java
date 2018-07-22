package com.peoplentech.devkh.blooddonor.model;

/**
 * Created by User on 7/9/2018.
 */

public class User {

    private int id;
    private String name;
    private String email;
    private String password;
    private String gender;
    private String birth;
    private String area;
    private String phone;
    private String status;
    private String bloodgroup;

    public User(String name, String email, String password, String gender, String birth, String area, String phone, String status, String bloodgroup) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.birth = birth;
        this.area = area;
        this.phone = phone;
        this.status = status;
        this.bloodgroup = bloodgroup;
    }

    public User(int id, String name, String email, String gender){
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
    }

    public User(int id, String name, String email, String password, String gender, String birth, String area, String phone, String status, String bloodgroup) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.birth = birth;
        this.area = area;
        this.phone = phone;
        this.status = status;
        this.bloodgroup = bloodgroup;
    }

    public User( String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(int id, String name, String phone, String bloodgroup, String area){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.bloodgroup = bloodgroup;
        this.area = area;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword(){
        return password;
    }

    public String getGender() {
        return gender;
    }

    public String getBirth() {
        return birth;
    }

    public String getArea() {
        return area;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }

    public String getBlood() { return bloodgroup; }
}
