package com.example.moyezasus.blooddonor;

public class Info {
    String name;
    String email;
    String phone;
    String password;
    String address;
    String age;
    String bloodGenres;
    String varsityGenres;
    String imageUrl;

    public Info(){

    }


    public Info(String imageUrl, String name, String phone, String email, String address, String age, String bloodGenres, String varsityGenres) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.age = age;
        this.bloodGenres = bloodGenres;
        this.varsityGenres = varsityGenres;
    }
}
