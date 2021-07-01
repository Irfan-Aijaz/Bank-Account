package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {


    private BigDecimal amount;

    public Account(BigDecimal amount) {
        this.amount = amount;
    }

    public Account() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    // increase
    public BigDecimal amountIncrease;
    //decrease
    public BigDecimal amountDecrease;
    //transfer
    public BigDecimal amountToTransfer;

}
