package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private UserDao userDao;
    private TransferDao transferDao;
    private AccountDao accountDao;

    public TransferController(UserDao userDao, TransferDao transferDao, AccountDao accountDao) {
        this.userDao = userDao;
        this.transferDao = transferDao;
        this.accountDao = accountDao;
    }

    @RequestMapping(path = "/transfer/transfer_history", method = RequestMethod.GET)
    public List<Transfer> listAllTransfers(Principal principal) {
        String loggedInUserName = principal.getName();
        int loggedInUserId = userDao.findIdByUsername(loggedInUserName);
        return transferDao.listAllTransfers(loggedInUserId);
    }


    @RequestMapping(value = "/transfer/create_transfer", method = RequestMethod.POST)
    // takes transfer info from post and makes transfer in database
    public String createTransfer(Principal principal, @RequestBody Transfer transfer) throws Exception {
        int userIdFrom = transfer.getAccountFrom();
        int userIdTo = transfer.getAccountTo();
        int transferType = transfer.getTransferTypeId();
        int transferStatus = transfer.getTransferStatusId();
        BigDecimal transferAmount = transfer.getAmount();
        Account fromAccount = accountDao.getAccountByUserId(userIdFrom);
        Account toAccount = accountDao.getAccountByUserId(userIdTo);

        // if transfer is a send, checks balance of sending to make sure they have enough $$
        if (transferType == 2) {
            if (transferDao.checkAgainstBalance(fromAccount.getUserId(), transferAmount)) {
                transferDao.createTransferId(transferType, transferStatus, fromAccount.getAccountId(), toAccount.getAccountId(), transferAmount);
                transferDao.updateAccounts(fromAccount.getUserId(), toAccount.getUserId(), transferAmount);
                return "Success! Transfer created. Money transferred.";
            } else {
                throw new Exception("Transfer not completed. Insufficient funds.");
            }
            // if request, status is pending, accounts not updated until approved
        } else if (transferType == 1) {
            transferDao.createTransferId(transferType, transferStatus, fromAccount.getAccountId(), toAccount.getAccountId(), transferAmount);
            return "Transfer created. Status is pending.";
        } else {
            throw new Exception("Transfer not created.");
        }
    }

    @RequestMapping(value = "/transfer/update_transfer", method = RequestMethod.PUT)
    public void updateTransfer(Principal principal, @RequestBody TransferStatus transferStatus) throws Exception {
        int transferId = transferStatus.getTransferId();
        int transferStatusId = transferStatus.getTransferStatus();

        Transfer retrievedTransfer = transferDao.retrieveTransfer(transferId);

        int accountFrom = retrievedTransfer.getAccountFrom();
        int accountTo = retrievedTransfer.getAccountTo();
        BigDecimal transferAmount = retrievedTransfer.getAmount();
        // if request approved, checks to see if sending has enough $$ before approving,otherwise rejects
        if (transferStatusId == 2) {
            if (transferDao.checkAgainstBalance(accountFrom, transferAmount)) {
                transferDao.updateAccounts(accountFrom, accountTo, transferAmount);
            } else {
                transferStatus.setTransferStatus(3);
            }
        }
        transferDao.updateTransfer(transferId, transferStatusId);
    }
}
