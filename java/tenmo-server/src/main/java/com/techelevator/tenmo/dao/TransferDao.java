package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    List<Transfer> listAllTransfers(int userId);

    boolean checkTransferAmountAgainstBalance (int userIdFrom, int userIdTo, BigDecimal transferAmount);

    Transfer createTransferId(int userIdFrom, int userIdTo, BigDecimal transferAmount);

}
