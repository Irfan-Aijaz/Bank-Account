package com.techelevator.tenmo.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcBalanceDao implements BalanceDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcBalanceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalance(String username){
        BigDecimal balance = new BigDecimal("0");
        String sql = "SELECT balance FROM accounts INNER JOIN users ON user.user_id = accounts.user_id WHERE username = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
        if (results.next()) {
            balance = balance.add(mapRowToBalance(results));
        }
        return balance;
    }

    @Override
    public List<Integer> listAll() {
        List<Integer> allUsers = new ArrayList<>();
        String sql = "SELECT user_id FROM accounts;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            allUsers.add(mapRowToAccount(results));
        }

        return allUsers;
    }

    private BigDecimal mapRowToBalance(SqlRowSet b){
        BigDecimal balance = new BigDecimal("0");
        return balance.add(b.getBigDecimal("balance"));
    }

    private int mapRowToAccount(SqlRowSet s) {
        return s.getInt("user_id");
    }
}
