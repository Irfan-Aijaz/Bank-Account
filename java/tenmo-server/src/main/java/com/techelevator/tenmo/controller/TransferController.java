package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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



//    @RequestMapping(value = "/transfer", method = RequestMethod.PUT)
//    public Transfer createTransfer(Principal principal) {
//        Account fromAccount = accountDao.getAccountByUserId(userDao.findIdByUsername(principal.getName()));
//
//        Account toAccount = accountDao.getAccountByUserId(transfer.transferToUser);
//
//        return transferDao.createTransferId(loggedInUserId);
//   //Account transfer method will check if the from account has enough $ and if yes, update the balance of both and return true
//  //returns false if the from account did not have enough money
//  if (fromAccount.transfer(toAccount, transfer.amount)) { //the balances on the account were updated
//          //create a row for teh transfer in the transfer table
//        transferDao.createTransfer(fromAccount.getAccountId(),toAccount.getAccountId),transfer.amount,Transfer.APPROVED_STATU
//        accountDao.save (fromAccount); //save teh from account with new balance
//        accountDao.save (toAccount);//save to the to account with the new blanace
//
//          else {
//            throw new Exception("Transfer not completed. Insufficent funds.");
//    }

}
