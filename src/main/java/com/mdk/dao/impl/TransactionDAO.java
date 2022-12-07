package com.mdk.dao.impl;

import com.mdk.connection.DBConnection;
import com.mdk.dao.ITransactionDAO;
import com.mdk.models.Transaction;
import com.mdk.models.User;
import com.mdk.paging.Pageble;
import com.mdk.services.IUserService;
import com.mdk.services.impl.UserService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO extends DBConnection implements ITransactionDAO {
    public Connection conn = null;
    public PreparedStatement ps = null;
    public ResultSet rs = null;
    @Override
    public List<Transaction> findAll() {
        String sql = "SELECT * FROM transaction";
        List<Transaction> transactions = new ArrayList<Transaction>();
        try {
            conn = super.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setUserId(rs.getInt("userId"));
                transaction.setStoreId(rs.getInt("storeId"));
                transaction.setUp(rs.getBoolean("isUp"));
                transaction.setAmount(rs.getDouble("amount"));
                transactions.add(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public int count(int storeId, String dateStart, String dateEnd) {
        StringBuilder sql = new StringBuilder("select count(*) from transaction where storeId = " + storeId);
        if (dateStart != null && dateEnd != null) {
            sql.append(" and createdAt between " + "\"" + dateStart + "\"" + " and " + "\"" + dateEnd + "\"");
        }
        try {
            conn = getConnection();
            ps = conn.prepareStatement(String.valueOf(sql));
            rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Transaction> findAll(Pageble pageble, int storeId, String dateStart, String dateEnd) {
        StringBuilder sql = new StringBuilder("select * from transaction where storeId = " + storeId);
        if (dateStart != null && dateEnd != null) {
            sql.append(" and createdAt between " + "\"" + dateStart + "\"" + " and " + "\"" + dateEnd + "\"");
        }
        if (pageble.getSorter() != null) {
            sql.append(" order by "+pageble.getSorter().getSortName()+" "+pageble.getSorter().getSortBy()+"");
        }
        if (pageble.getOffset() != null && pageble.getLimit() != null) {
            sql.append(" limit "+pageble.getOffset()+", "+pageble.getLimit()+"");
        }
        List<Transaction> transactions = new ArrayList<>();
        IUserService userService = new UserService();
        try {
            conn = getConnection();
            ps = conn.prepareStatement(String.valueOf(sql));
            rs = ps.executeQuery();
            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setId(rs.getInt("id"));
                transaction.setUserId(rs.getInt("userId"));
                User user = userService.findById(rs.getInt("userId"));
                transaction.setNameUser(user.getFirstname() + " " + user.getLastname());
                transaction.setStoreId(rs.getInt("storeId"));
                transaction.setUp(rs.getBoolean("isUp"));
                transaction.setIsUpString(rs.getBoolean("isUp") ? "Rút" : "Nạp");
                transaction.setAmount(rs.getDouble("amount"));
                transaction.setCreatedAt(rs.getTimestamp("createdAt"));
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public void insert(Transaction transaction) {
        String sql = "insert into transaction(userId, storeId, isUp, amount) value(?, ?, ?, ?)";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, transaction.getUserId());
            ps.setInt(2, transaction.getStoreId());
            ps.setBoolean(3, transaction.getUp());
            ps.setDouble(4, transaction.getAmount());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
