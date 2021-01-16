package com.piyush025.lifeshare;

public class ReceiverInformation {

    public String name;
    public String bloodgroup;
    public String mob;
    public String dob;
    public String emailID;
    public String gender;
    public String bankID;
    public String userID;


    public ReceiverInformation(String name, String bg, String email, String mob, String bankID, String gender, String dob,String receiver) {
        this.name = name;
        this.bloodgroup = bg;
        this.mob = mob;
        this.dob = dob;
        this.emailID = email;
        this.gender = gender;
        this.bankID = bankID;
        this.userID = receiver;
    }
}
