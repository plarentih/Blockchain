package com.example.plarent.blockchain.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by plarent on 14/03/2018.
 */

@Table(name = "Person")
public class Person extends Model {
    @Column(name = "Number")
    public String phoneNumber;

    @Column(name = "Wallet")
    public String wallet;

    public Person(){
        super();
    }

    public Person(String name, String wallet) {
        super();
        this.phoneNumber = name;
        this.wallet = wallet;
    }

    public String getName() {
        return phoneNumber;
    }

    public void setName(String name) {
        this.phoneNumber = name;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }
}
