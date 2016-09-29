package com.development.unobtainium.cimedic2.models;

import java.io.File;

/**
 * Created by unobtainium on 24/09/16.
 */
public class Patient {
    private Integer id = 0;
    private String names;
    private String last_name;
    private String mothers_last_name;
    private String birthday;
    private int district_id;
    private String document_number;
    private int document_type;
    private String address;
    private String email;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;
    private String image;
    private File picture;

    public void setPicture(File picture) {
        this.picture = picture;
    }

    public Patient(String names, String last_name, String mothers_last_name, String birthday, int district_id, String document_number, int document_type, String address, String email, String password) {
        this.names = names;
        this.last_name = last_name;
        this.mothers_last_name = mothers_last_name;
        this.birthday = birthday;
        this.district_id = district_id;
        this.document_number = document_number;
        this.document_type = document_type;
        this.address = address;
        this.email = email;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public boolean valid(){
        return id > 0;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMothers_last_name() {
        return mothers_last_name;
    }

    public void setMothers_last_name(String mothers_last_name) {
        this.mothers_last_name = mothers_last_name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    public String getDocument_number() {
        return document_number;
    }

    public void setDocument_number(String document_number) {
        this.document_number = document_number;
    }

    public int getDocument_type() {
        return document_type;
    }

    public void setDocument_type(int document_type) {
        this.document_type = document_type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}