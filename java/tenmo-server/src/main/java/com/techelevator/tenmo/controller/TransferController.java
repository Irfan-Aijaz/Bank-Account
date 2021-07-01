package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated")
public class TransferController {

    private UserDao userDao;
    private TransferDao transferDao;

    public TransferController(UserDao userDao, TransferDao transferDao) {
        this.userDao = userDao;
        this.transferDao = transferDao;
    }

    @RequestMapping(value = "/transfer/your_transfers", method = RequestMethod.GET)
    public List<Transfer> listAllTransfers(Principal principal) {
        String loggedInUserName = principal.getName();
        int loggedInUserId = userDao.findIdByUsername(loggedInUserName);
        return transferDao.listAllTransfers(loggedInUserName);
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.PUT)
    public Transfer createTransfer(Principal principal) {
        String loggedInUserName = principal.getName();
        int loggedInUserId = userDao.findIdByUsername(loggedInUserName);
        return transferDao.createTransferId(loggedInUserId);
    }
}
