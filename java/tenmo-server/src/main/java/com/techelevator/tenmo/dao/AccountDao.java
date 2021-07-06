package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {
    BigDecimal getBalanceForUserId(int userId);

    List<User> listAllUserNames();

    Account getAccountByUserId(int userId);

}
