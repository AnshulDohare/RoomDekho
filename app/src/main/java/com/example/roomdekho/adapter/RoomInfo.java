package com.example.roomdekho.adapter;

public class RoomInfo {
    String rCity;
    String rArea;
    String rAddress;
    String rRent;
    String rDescription;
    String rImageUri;

    public RoomInfo(String rCity, String rArea, String rAddress, String rRent, String rDescription,String rImageUri) {
        this.rCity = rCity;
        this.rArea = rArea;
        this.rAddress = rAddress;
        this.rRent = rRent;
        this.rDescription = rDescription;
        this.rImageUri = rImageUri;
    }

    public String getrCity() {
        return rCity;
    }

    public String getrUserId() {
        return rImageUri;
    }

    public void setrUserId(String rImageUri) {
        this.rImageUri = rImageUri;
    }

    public void setrCity(String rCity) {
        this.rCity = rCity;
    }

    public String getrArea() {
        return rArea;
    }

    public void setrArea(String rArea) {
        this.rArea = rArea;
    }

    public String getrAddress() {
        return rAddress;
    }

    public void setrAddress(String rAddress) {
        this.rAddress = rAddress;
    }

    public String getrRent() {
        return rRent;
    }

    public void setrRent(String rRent) {
        this.rRent = rRent;
    }

    public String getrDescription() {
        return rDescription;
    }

    public void setrDescription(String rDescription) {
        this.rDescription = rDescription;
    }
}
