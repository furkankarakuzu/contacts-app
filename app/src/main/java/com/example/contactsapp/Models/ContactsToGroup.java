package com.example.contactsapp.Models;

public class ContactsToGroup {
    private String userid,groupid,contactsPhone;

    public ContactsToGroup(String userid, String groupid, String contactsPhone) {
        this.userid = userid;
        this.groupid = groupid;
        this.contactsPhone = contactsPhone;
    }

    public ContactsToGroup() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getContactsPhone() {
        return contactsPhone;
    }

    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }
}
