package com.example.acer.mobilustam;

/**
 * Created by acer on 18.09.2017.
 */

public class User {
    private int id;
    private String ad, soyad, email;
    private String telno;


    public User(int id, String ad, String soyad, String email, String telno) {
        this.id = id;
        this.ad = ad;
        this.soyad = soyad;
        this.email = email;
        this.telno = telno;
    }

    public int getId() {
        return id;
    }

    public String getAd() {
        return ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public String getEmail() {
        return email;
    }

    public String getTelno() {
        return telno;
    }

}



