package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalanceForUserId(int loggedInUserId) {
        String sql = "SELECT SUM(balance) FROM accounts WHERE user_id = ?";
        try {
            BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, loggedInUserId);
            return balance;
        } catch (Exception e) {
            System.out.println("Error in getBalanceForUserId" + loggedInUserId + ": " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    @Override
    public List<User> listAllUserNames() {
        List<User> allUsers = new ArrayList<User>();
        String sql = "SELECT user_id, username FROM users;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            allUsers.add(mapRowToUser(results));
        }

        return allUsers;
    }

    @Override
    public Account getAccountByUserId (int userId) {
        Account account = new Account();
        String sql = "SELECT account_id, user_id, balance FROM accounts WHERE user_id = ?;";
        SqlRowSet username = jdbcTemplate.queryForRowSet(sql, userId);
        if (username.next()) {
            account = mapRowToAccount(username);
        }
        return account;
    }


    private Account mapRowToAccount(SqlRowSet s) {
        Account account = new Account();
        account.setAccountId(s.getInt("account_id"));
        account.setUserId(s.getInt("user_id"));
        account.setAmount(s.getBigDecimal("balance"));
        return account;
    }

    private User mapRowToUser(SqlRowSet s) {
        User user = new User();
        user.setId(s.getLong("user_id"));
        user.setUsername(s.getString("username"));
        return user;
    }
}
