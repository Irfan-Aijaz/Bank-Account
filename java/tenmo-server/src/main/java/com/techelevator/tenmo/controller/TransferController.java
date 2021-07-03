package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
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
    private Transfer transfer;

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


    @RequestMapping(value = "/transfer/create_transfer", method = RequestMethod.PUT)
    public String createTransfer(Principal principal, @RequestBody int transferType, int transferStatus, String username, BigDecimal transferAmount) throws Exception {
        Account fromAccount = accountDao.getAccountByUserId(userDao.findIdByUsername(principal.getName()));
        Account toAccount = accountDao.getAccountByUserId(userDao.findIdByUsername(username));

        if (transferDao.checkAgainstBalance(fromAccount.getUserId(), transferAmount)) {
            transferDao.createTransferId(transferType, transferStatus, fromAccount.getAccountId(), toAccount.getAccountId(), transferAmount);
            return "Transfer created.";
        } else {
            throw new Exception("Transfer not completed. Insufficient funds.");

        }
    }
}
