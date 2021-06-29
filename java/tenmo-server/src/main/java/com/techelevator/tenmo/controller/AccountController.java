package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.BalanceDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.security.jwt.TokenProvider;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class AccountController {

//    private final TokenProvider tokenProvider;
//    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private BalanceDao balanceDao;

    @ResponseStatus(HttpStatus.FOUND)
    @RequestMapping(value = "/account/balance", method = RequestMethod.GET)
    public BigDecimal getBalance (@RequestBody String userName) {
        return balanceDao.getBalance(userName);
    }


    @ResponseStatus(HttpStatus.FOUND)
    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public List<Integer> listAllUsers () {
        return balanceDao.listAll();
    }

}
