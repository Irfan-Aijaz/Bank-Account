package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

public interface BalanceDao {
    BigDecimal getBalance(String username);

    List<Integer> listAll();
}
