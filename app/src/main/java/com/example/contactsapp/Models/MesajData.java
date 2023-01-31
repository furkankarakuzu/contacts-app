package com.example.contactsapp.Models;

public class MesajData {
    private String userid,messagename,messagedesc;

    public MesajData() {
    }

    public MesajData(String userid, String messagename, String messagedesc) {
        this.userid = userid;
        this.messagename = messagename;
        this.messagedesc = messagedesc;
    }

    public String getUserid() {
        return userid;
    }
    public String getMessagename() {
        return messagename;
    }
    public String getMessagedesc() {
        return messagedesc;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public void setMessagename(String messagename) {
        this.messagename = messagename;
    }
    public void setMessagedesc(String messagedesc) {
        this.messagedesc = messagedesc;
    }
}
