package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Balance {

    private BigDecimal amount;

    public Balance(BigDecimal amount) {
        this.amount = amount;
    }

    public Balance() {
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    // increase
    public BigDecimal amountIncrease;
    //decrease
    public BigDecimal amountDecrease;
    //transfer
    public BigDecimal amountToTransfer;

}
