package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    List<Transfer> listAllTransfers(int userId);

    boolean checkAgainstBalance(int userIdFrom, BigDecimal transferAmount);

    void createTransferId(int transferType, int transferStatus, int userIdFrom, int userIdTo, BigDecimal transferAmount);

    void updateAccounts(int userIdFrom, int userIdTo, BigDecimal transferAmount);

    void updateTransfer(int transferId, int transferStatus);

    Transfer retrieveTransfer(int transferId);
}
