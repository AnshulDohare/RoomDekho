package com.example.roomdekho.my_classes;

public class ProfileInfo {
    String pEmail;
    String pMobile;
    String pName;
    String pProfession;
    String pAddress;

    public ProfileInfo(String pEmail, String pMobile, String pProfession, String pAddress,String pName) {
        this.pEmail = pEmail;
        this.pMobile = pMobile;
        this.pProfession = pProfession;
        this.pAddress = pAddress;
        this.pName = pName;
    }
    ProfileInfo(){}
    public String getpEmail() {
        return pEmail;
    }

    public void setpEmail(String pEmail) {
        this.pEmail = pEmail;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpMobile() {
        return pMobile;
    }

    public void setpMobile(String pMobile) {
        this.pMobile = pMobile;
    }

    public String getpProfession() {
        return pProfession;
    }

    public void setpProfession(String pProfession) {
        this.pProfession = pProfession;
    }

    public String getpAddress() {
        return pAddress;
    }

    public void setpAddress(String pAddress) {
        this.pAddress = pAddress;
    }

}
