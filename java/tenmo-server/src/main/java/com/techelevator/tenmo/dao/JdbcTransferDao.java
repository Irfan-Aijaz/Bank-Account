package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean checkAgainstBalance(int userIdFrom, BigDecimal transferAmount) {
        String sql1 = "SELECT balance FROM accounts WHERE user_id = ?;";
        SqlRowSet results1 = jdbcTemplate.queryForRowSet(sql1, userIdFrom);
        if (results1.next()) {
            return results1.getBigDecimal("balance").compareTo(transferAmount) >= 0;
        }
        return false;
    }


    @Override
    public void createTransferId(int transferType, int transferStatus, int userIdFrom, int userIdTo, BigDecimal transferAmount) {

        String sql1 = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?);";
        jdbcTemplate.update(sql1, transferType, transferStatus, userIdFrom, userIdTo, transferAmount);

    }

    @Override
    public void updateAccounts(int userIdFrom, int userIdTo, BigDecimal transferAmount) {
        String sqlDecrease = "UPDATE accounts SET balance = balance - ? WHERE user_id = ?; ";
        jdbcTemplate.update(sqlDecrease, transferAmount, userIdFrom);

        String sqlIncrease = "UPDATE accounts SET balance = balance + ? WHERE user_id = ?; ";
        jdbcTemplate.update(sqlIncrease, transferAmount, userIdTo);
    }

    @Override
    public void updateTransfer(int transferId, int transferStatus) {
        String sqlUpdateTransfer = "UPDATE transfers SET transfer_status_id = ? WHERE transfer_id = ?; ";
        jdbcTemplate.update(sqlUpdateTransfer, transferStatus, transferId);
    }

    public Transfer retrieveTransfer(int transferId) {
        Transfer retrievedTransfer = new Transfer();
        String sql = "Select transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfers " +
                "WHERE transfer_id = ?; ";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId);

        if (result.next()) {
            retrievedTransfer = mapRowToTransferTransferId(result);
        }

        return retrievedTransfer;
    }

    @Override
    public List<Transfer> listAllTransfers(int userID) {
        List<Transfer> listOfTransfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, uf.username AS from_user, ut.username AS to_user, amount \n" +
                "FROM transfers \n" +
                "INNER JOIN accounts AS a_f ON (account_from = a_f.account_id) \n" +
                "INNER JOIN accounts AS a_t ON (account_to = a_t.account_id) \n" +
                "INNER JOIN users AS uf ON a_f.user_id = uf.user_id \n" +
                "INNER JOIN users AS ut ON a_t.user_id = ut.user_id \n" +
                "WHERE a_f.user_id = ? OR a_t.user_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userID, userID);
        while (results.next()) {
            Transfer transfer = mapRowToTransferUserName(results);
            listOfTransfers.add(transfer);
        }

        return listOfTransfers;
    }


    private Transfer mapRowToTransferUserName(SqlRowSet s) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(s.getInt("transfer_id"));
        transfer.setTransferTypeId(s.getInt("transfer_type_id"));
        transfer.setTransferStatusId(s.getInt("transfer_status_id"));
        transfer.setUsernameFrom(s.getString("from_user"));
        transfer.setUsernameTo(s.getString("to_user"));
        transfer.setAmount(s.getBigDecimal("amount"));
        return transfer;
    }

    private Transfer mapRowToTransferTransferId(SqlRowSet s) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(s.getInt("transfer_id"));
        transfer.setTransferTypeId(s.getInt("transfer_type_id"));
        transfer.setTransferStatusId(s.getInt("transfer_status_id"));
        transfer.setAccountFrom(s.getInt("account_from"));
        transfer.setAccountTo(s.getInt("account_to"));
        transfer.setAmount(s.getBigDecimal("amount"));
        return transfer;
    }


}
