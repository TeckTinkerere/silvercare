package com.silvercare.dao;

import java.sql.SQLException;
import java.util.List;

import com.silvercare.util.UserDBUtil;
import com.silvercare.model.User;

/**
 * Data Access Object for User entity in the Main Web App.
 * Delegates actual database operations to UserDBUtil to separate SQL logic.
 */
public class UserDAO {

    private final UserDBUtil userDBUtil;

    public UserDAO() {
        this.userDBUtil = new UserDBUtil();
    }

    public User authenticate(String email, String password) throws SQLException {
        return userDBUtil.authenticate(email, password);
    }

    public User getUserById(int id) throws SQLException {
        return userDBUtil.getUserById(id);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDBUtil.getAllUsers();
    }

    public void updateTutorialStatus(int userId, boolean completed) throws SQLException {
        userDBUtil.updateTutorialStatus(userId, completed);
    }

    public void deleteUser(int id) throws SQLException {
        userDBUtil.deleteUser(id);
    }

    public User register(User user, String plainPassword) throws SQLException {
        return userDBUtil.register(user, plainPassword);
    }

    public void updateProfile(User user) throws SQLException {
        userDBUtil.updateProfile(user);
    }

    public List<User> getUsersByArea(String areaFilter) throws SQLException {
        return userDBUtil.getUsersByArea(areaFilter);
    }

    public List<User> getAllCustomers() throws SQLException {
        return userDBUtil.getAllCustomers();
    }
}
