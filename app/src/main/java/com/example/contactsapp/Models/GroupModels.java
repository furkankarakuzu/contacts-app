package com.example.contactsapp.Models;

public class GroupModels {

    private String userid,groupname,groupdesc,link,groupid;

    public GroupModels(String userid, String groupname, String groupdesc, String link, String groupid) {
        this.userid = userid;
        this.groupname = groupname;
        this.groupdesc = groupdesc;
        this.link = link;
        this.groupid = groupid;
    }

    public GroupModels(){}

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getGroupdesc() {
        return groupdesc;
    }

    public void setGroupdesc(String groupdesc) {
        this.groupdesc = groupdesc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }
}
