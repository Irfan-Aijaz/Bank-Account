package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {

    private int accountId;
    private int userId;
    private BigDecimal amount;

    public Account(BigDecimal amount) {
        this.amount = amount;
    }

    public Account() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // increase
    public BigDecimal amountIncrease;
    //decrease
    public BigDecimal amountDecrease;
    //transfer
    public BigDecimal amountToTransfer;

}
