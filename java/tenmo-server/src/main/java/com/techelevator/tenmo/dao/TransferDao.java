package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    List<Transfer> listAllTransfers(String userName);

    Transfer createTransferId(int userIdFrom, int userIdTo, BigDecimal transferAmount);
}
