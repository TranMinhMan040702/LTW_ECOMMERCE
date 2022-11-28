
package com.mdk.dao.impl;

import com.mdk.connection.DBConnection;
import com.mdk.dao.IUserDAO;
import com.mdk.models.User;
import com.mdk.paging.Pageble;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends DBConnection implements IUserDAO {
	public Connection conn = null;
	public PreparedStatement ps = null;
	public ResultSet rs = null;

	@Override
	public List<User> findAll() {
		String sql = "SELECT * FROM user WHERE role = 'user'";
		List<User> users = new ArrayList<User>();
		try {
			conn = super.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setFirstname(rs.getString("firstname"));
				user.setLastname(rs.getString("lastname"));
				user.setId_card(rs.getString("id_card"));
				user.setEmail(rs.getString("email"));
				user.setPhone(rs.getString("phone"));
				users.add(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;

	}

	@Override
	public List<User> top10Users_Orders() {
		String sql = "select user.id, user.firstname, user.lastname, user.id_card, user.email, user.phone, total from (select userId, count(userId) as total from orders group by userId order by total desc limit 10) as tb join user on tb.userId = user.id";
		List<User> users = new ArrayList<User>();
		try {
			conn = super.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setFirstname(rs.getString("firstname"));
				user.setLastname(rs.getString("lastname"));
				user.setId_card(rs.getString("id_card"));
				user.setEmail(rs.getString("email"));
				user.setPhone(rs.getString("phone"));
				user.setTotalOrders(rs.getInt("total"));
				users.add(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}

	@Override
	public int count() {
		StringBuilder sql = new StringBuilder("select count(*) from user where role = 'user'");
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
	public List<User> findAll(Pageble pageble) {
		StringBuilder sql = new StringBuilder("select * from user");
		if (pageble.getSorter() != null) {
			sql.append(" order by " + pageble.getSorter().getSortName() + " " + pageble.getSorter().getSortBy() + "");
		}
		if (pageble.getOffset() != null && pageble.getLimit() != null) {
			sql.append(" limit " + pageble.getOffset() + ", " + pageble.getLimit() + "");
		}
		List<User> users = new ArrayList<User>();
		try {
			conn = getConnection();
			ps = conn.prepareStatement(String.valueOf(sql));
			rs = ps.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setFirstname(rs.getString("firstname"));
				user.setLastname(rs.getString("lastname"));
				user.setId_card(rs.getString("id_card"));
				user.setEmail(rs.getString("email"));
				user.setPhone(rs.getString("phone"));
				users.add(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}

	@Override
	public User findOneByUsernameAndPassword(String username, String password) {
		String sql = "select * from user where email = ? and password = ?";
		User user = new User();
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			rs = ps.executeQuery();
			while (rs.next()) {
				user.setId(rs.getInt("id"));
				user.setFirstname(rs.getString("firstname"));
				user.setLastname(rs.getString("lastname"));
				user.setId_card(rs.getString("id_card"));
				user.setEmail(rs.getString("email"));
				user.setPhone(rs.getString("phone"));
				user.setPassword(rs.getString("password"));
				user.setRole(rs.getString("role"));
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public User findById(int id) {
		String sql = "select * from user where id = ?";
		User user = new User();
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				user.setId(rs.getInt("id"));
				user.setFirstname(rs.getString("firstname"));
				user.setLastname(rs.getString("lastname"));
				user.setId_card(rs.getString("id_card"));
				user.setEmail(rs.getString("email"));
				user.setPhone(rs.getString("phone"));
				user.setIsEmailActive(rs.getBoolean("isEmailActive"));
				user.setPhoneActive(rs.getBoolean("isPhoneActive"));
				user.setPassword(rs.getString("password"));
				user.setRole(rs.getString("role"));
				user.setAvatar(rs.getString("avatar"));
				user.seteWallet(rs.getDouble("eWallet"));
				user.setCreatedAt(rs.getTimestamp("createdAt"));
				user.setUpdatedAt(rs.getTimestamp("updatedAt"));
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void insert(User user) {
		String sql = "INSERT INTO user(firstname, lastname, id_card, email, phone, isEmailActive, isPhoneActive, password, role, avatar,  eWallet) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			conn = super.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, user.getFirstname());
			ps.setString(2, user.getLastname());
			ps.setString(3, user.getId_card());
			ps.setString(4, user.getEmail());
			ps.setString(5, user.getPhone());
			ps.setBoolean(6, user.isIsEmailActive());
			ps.setBoolean(7, user.isPhoneActive());
			ps.setString(8, user.getPassword());
			ps.setString(9, user.getRole());
			ps.setString(10, user.getAvatar());
			ps.setDouble(11, user.geteWallet());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(User user) {
		String sql = "UPDATE user"
				+ "SET firstname = ?, lastname = ?, id_card = ?, email = ?, phone = ?, isEmailActive = ?, isPhoneActive = ?, password = ?, role = ?, avatar = ?,  eWallet = ?) "
				+ "WHERE id = ?";
		try {
			conn = super.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, user.getFirstname());
			ps.setString(2, user.getLastname());
			ps.setString(3, user.getId_card());
			ps.setString(4, user.getEmail());
			ps.setString(5, user.getPhone());
			ps.setBoolean(6, user.isIsEmailActive());
			ps.setBoolean(7, user.isPhoneActive());
			ps.setString(8, user.getPassword());
			ps.setString(9, user.getRole());
			ps.setString(10, user.getAvatar());
			ps.setDouble(11, user.geteWallet());
			ps.setInt(12, user.getId());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(int id) {
		String sql = "DELETE FROME user WHERE id = ?";
		try {
			conn = super.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<User> findBySearching(String keyword) {
		String sql = "SELECT * FROM user WHERE role = 'user' and CONCAT(firstname, ' ', lastname) LIKE CONCAT('%', ?, '%')";
		List<User> users = new ArrayList<User>();
		try {
			conn = super.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, keyword);
			rs = ps.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setFirstname(rs.getString("firstname"));
				user.setLastname(rs.getString("lastname"));
				user.setId_card(rs.getString("id_card"));
				user.setEmail(rs.getString("email"));
				user.setPhone(rs.getString("phone"));
				// user.setTotalOrders(rs.getInt("total"));
				users.add(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}
}
