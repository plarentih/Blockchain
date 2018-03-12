package com.example.plarent.blockchain.model;

import java.io.Serializable;

/**
 * Created by plarent on 12/03/2018.
 */

public class Transaction implements Serializable {
    private String key;
    private String reference;
    private long amount;
    private String currency;
    private String date;

    public Transaction(String key, String reference, long amount, String currency, String date) {
        this.key = key;
        this.reference = reference;
        this.amount = amount;
        this.currency = currency;
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
