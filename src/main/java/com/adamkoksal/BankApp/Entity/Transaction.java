package com.adamkoksal.BankApp.Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private Account initiator;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Account receiver;

    private String type;

    private Date date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Account getInitiator() {
        return initiator;
    }

    public void setInitiator(Account initiator) {
        this.initiator = initiator;
    }

    public Account getReceiver() {
        return receiver;
    }

    public void setReceiver(Account receiver) {
        this.receiver = receiver;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate() {
        this.date = new Date();
    }
}
