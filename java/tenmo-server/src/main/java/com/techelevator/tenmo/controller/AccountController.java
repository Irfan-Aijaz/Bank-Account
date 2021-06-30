package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated")
public class AccountController {

    //    private final TokenProvider tokenProvider;
//    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private AccountDao accountDao;
    private UserDao userDao;

    public AccountController(UserDao userDao, AccountDao accountDao) {
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    @ResponseStatus(HttpStatus.FOUND)
    @RequestMapping(value = "/account/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal) {
        String loggedInUserName = principal.getName();
        int loggedInUserId = userDao.findIdByUsername(loggedInUserName);
        return accountDao.getBalanceForUserId(loggedInUserId);
    }


    @ResponseStatus(HttpStatus.FOUND)
    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public List<Integer> listAllUsers() {
        return accountDao.listAll();
    }

}
