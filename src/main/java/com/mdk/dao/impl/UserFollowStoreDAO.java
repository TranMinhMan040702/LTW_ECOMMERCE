package com.mdk.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mdk.connection.DBConnection;
import com.mdk.dao.IUserFollowStoreDAO;
import com.mdk.models.UserFollowStore;
import com.mdk.services.IStoreService;
import com.mdk.services.IUserService;
import com.mdk.services.impl.StoreService;
import com.mdk.services.impl.UserService;

public class UserFollowStoreDAO extends DBConnection implements IUserFollowStoreDAO {

	public Connection conn = null;
	public PreparedStatement ps = null;
	public ResultSet rs = null;

	@Override
	public void insert(UserFollowStore userFollowStore) {
		String sql = "insert into userfollowstore(userId, storeId) values(?, ?)";
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, userFollowStore.getUserId());
			ps.setInt(2, userFollowStore.getStoreId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(int id) {
		String sql = "delete from userfollowstore where id = ?";
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<UserFollowStore> findByUserId(int id) {
		String sql = "select * from userfollowstore where userId = ?";
		List<UserFollowStore> userFollowStores = new ArrayList<UserFollowStore>();
		IUserService userService = new UserService();
		IStoreService storeService = new StoreService();
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				UserFollowStore userFollowStore = new UserFollowStore();
				userFollowStore.setId(rs.getInt("id"));
				userFollowStore.setUserId(rs.getInt("userId"));
				userFollowStore.setStoreId(rs.getInt("storeId"));
				userFollowStore.setStore(storeService.findById(userFollowStore.getStoreId()));
				userFollowStore.setUser(userService.findById(userFollowStore.getUserId()));
				userFollowStores.add(userFollowStore);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userFollowStores;
	}

}
