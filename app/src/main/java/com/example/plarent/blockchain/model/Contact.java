package com.example.plarent.blockchain.model;

import java.io.Serializable;

/**
 * Created by Plarent on 2/15/2018.
 */

public class Contact implements Serializable {
    private String key;
    private String contactName;
    private String contactNumber;
    private String currency;
    private long amount;

    public Contact(){

    }

    public Contact(String key, String contactName, String contactNumber, long amount, String currency) {
        this.key = key;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.amount = amount;
        this.currency = currency;
    }

    public Contact(String key, String contactName, String contactNumber) {
        this.key = key;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
