package com.techelevator.tenmo.dao;

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
    public List<Integer> listAll() {
        List<Integer> allUsers = new ArrayList<>();
        String sql = "SELECT user_id FROM accounts;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            allUsers.add(mapRowToAccount(results));
        }

        return allUsers;
    }
//    //@Override
//    /public int createTransferId (int userIdFrom, int userIdTo, BigDecimal transferAmount){
//        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount)" +
//                "VALUES (2, 2, ?, ?, ?)" +
//                "WHERE (SELECT balance FROM accounts WHERE user_id = ? AND balance > ?)";
//    }



    private int mapRowToAccount(SqlRowSet s) {
        return s.getInt("user_id");
    }
}
