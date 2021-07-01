package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
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
    public List<Integer> listAllUserIds() {
        List<Integer> allUsers = new ArrayList<Integer>();
        String sql = "SELECT user_id FROM accounts;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            allUsers.add(mapRowToAccount(results));
        }

        return allUsers;
    }


    private int mapRowToAccount(SqlRowSet s) {
        return s.getInt("user_id");
    }
}
