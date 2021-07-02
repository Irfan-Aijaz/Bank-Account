package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean checkTransferAmountAgainstBalance (int userIdFrom, int userIdTo, BigDecimal transferAmount) {
        String sql1 = "SELECT balance FROM accounts WHERE user_id;";
        SqlRowSet results1 = jdbcTemplate.queryForRowSet(sql1, userIdFrom);

        return transferAmount.compareTo(results1.getBigDecimal("balance"))>=0;
    }


    @Override
    public Transfer createTransferId (int userIdFrom, int userIdTo, BigDecimal transferAmount) {
        Transfer transfer = new Transfer();
//        String sql1 = "SELECT balance FROM accounts WHERE user_id = ? AND balance > ?;";
//        SqlRowSet results1 = jdbcTemplate.queryForRowSet(sql1);
        //compare to

            String sql2 = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                    "VALUES (2, 1, ?, ?, ?);";
            SqlRowSet results2 = jdbcTemplate.queryForRowSet(sql2, userIdFrom, userIdTo, transferAmount);

            if (results2.next()) {
                transfer = mapRowToTransfer(results2);
            }

//            String sqlDecrease = "UPDATE accounts SET balance = balance - ? WHERE user_id = ?; ";
//            SqlRowSet resultDecrease = jdbcTemplate.queryForRowSet(sqlDecrease, transferAmount, userIdFrom);
//
//            String sqlIncrease = "UPDATE accounts SET balance = balance + ? WHERE user_id = ?; ";
//            SqlRowSet resultIncrease = jdbcTemplate.queryForRowSet(sqlIncrease, transferAmount, userIdTo);

        return transfer;
    }

    @Override
    public List<Transfer> listAllTransfers (int userID) {
        List<Transfer> listOfTransfers = new ArrayList<>();
        String sql = "SELECT transfer_type_id, transfer_status_id, account_from, account_to, amount\n" +
                "FROM transfers\n" +
                "INNER JOIN accounts ON ((account_from = account_id) OR (account_to = account_id))  \n" +
                "WHERE user_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userID);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            listOfTransfers.add(transfer);
        }

        return listOfTransfers;
    }


    private Transfer mapRowToTransfer(SqlRowSet s) {
        Transfer transfer = new Transfer();
        transfer.setTransferTypeId(s.getInt("transfer_type_id"));
        transfer.setTransferStatusId(s.getInt("transfer_status_id"));
        transfer.setAccountFrom(s.getInt("account_from"));
        transfer.setAccountTo(s.getInt("account_to"));
        transfer.setAmount(s.getBigDecimal("amount"));
        return transfer;
    }

}
