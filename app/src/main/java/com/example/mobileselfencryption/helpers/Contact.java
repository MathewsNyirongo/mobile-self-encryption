package com.example.mobileselfencryption.helpers;

public class Contact {
    private String id;
    private String contactName;
    private String contactNumber1;
    private String contactNumber2;
    private String contactNumber3;
    private String contactNumber4;
    private String contactNumber5;

    public Contact(String id, String contactName, String contactNumber1, String contactNumber2, String contactNumber3, String contactNumber4, String contactNumber5){
        setId(id);
        setContactName(contactName);
        setContactNumber1(contactNumber1);
        setContactNumber2(contactNumber2);
        setContactNumber3(contactNumber3);
        setContactNumber4(contactNumber4);
        setContactNumber5(contactNumber5);
    }
    public void setId(String id){ this.id = id; }

    public void setContactName(String contactName){
        this.contactName = contactName;
    }

    public void setContactNumber1(String contactNumber1){
        this.contactNumber1 = contactNumber1;
    }

    public void setContactNumber2(String contactNumber2){
        this.contactNumber2 = contactNumber2;
    }

    public void setContactNumber3(String contactNumber3){
        this.contactNumber3 = contactNumber3;
    }

    public void setContactNumber4(String contactNumber4){
        this.contactNumber4 = contactNumber4;
    }

    public void setContactNumber5(String contactNumber5){
        this.contactNumber5 = contactNumber5;
    }

    public String getId() {
        return id;
    }

    public String getContactName(){
        return contactName;
    }

    public String getContactNumber1(){
        return contactNumber1;
    }

    public String getContactNumber2(){
        return contactNumber2;
    }

    public String getContactNumber3(){
        return contactNumber3;
    }

    public String getContactNumber4(){
        return contactNumber4;
    }

    public String getContactNumber5(){
        return contactNumber5;
    }
}
