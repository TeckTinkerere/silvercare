package com.silvercare.dao;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.silvercare.model.User;
import com.silvercare.util.UserDBUtil;

/**
 * Data Access Object for User entity
 * Contains field mappings and delegates SQL operations to utility beans
 * This follows the assignment requirement of separating concerns
 */
@Repository
public class UserDAO {

    private final UserDBUtil userDBUtil;

    @Autowired
    public UserDAO(UserDBUtil userDBUtil) {
        this.userDBUtil = userDBUtil;
    }

    // Field mappings for User entity (optional if using external models, but kept
    // for legacy support)
    private int userId;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private String gender;
    private String medicalInfo;
    private String profilePicture;
    private String role;
    private boolean tutorialCompleted;

    /**
     * Default constructor for legacy/testing
     */
    public UserDAO() {
        this.userDBUtil = new UserDBUtil();
    }

    /**
     * Constructor with User object for field mapping
     */
    public UserDAO(User user) {
        this();
        if (user != null) {
            this.userId = user.getId();
            this.email = user.getEmail();
            this.fullName = user.getFullName();
            this.phone = user.getPhone();
            this.address = user.getAddress();
            this.gender = user.getGender();
            this.medicalInfo = user.getMedicalInfo();
            this.profilePicture = user.getProfilePicture();
            this.role = user.getRole();
            this.tutorialCompleted = user.isTutorialCompleted();
        }
    }

    /**
     * Authenticate user login - delegates to utility bean
     */
    public User login(String email, String password) throws SQLException {
        return userDBUtil.authenticateUser(email, password);
    }

    /**
     * Register new user - hashes password before delegating to utility bean
     */
    public boolean register(User user, String password) throws SQLException {
        String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt());
        return userDBUtil.registerCustomer(user, hashedPassword);
    }

    /**
     * Reset user password - hashes new password and delegates to utility bean
     */
    public boolean resetPassword(int userId, String newPassword, String role) throws SQLException {
        String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(newPassword, org.mindrot.jbcrypt.BCrypt.gensalt());
        return userDBUtil.updateUserPassword(userId, hashedPassword, role);
    }

    /**
     * Update user profile - delegates to utility bean
     */
    public boolean updateProfile(User user) throws SQLException {
        return userDBUtil.updateCustomerProfile(user);
    }

    /**
     * Update tutorial status - delegates to utility bean
     */
    public boolean updateTutorialStatus(int userId, boolean completed) throws SQLException {
        return userDBUtil.updateTutorialStatus(userId, completed);
    }

    public java.util.List<User> getAllCustomers() throws SQLException {
        return userDBUtil.getAllCustomers();
    }

    // Getter and Setter methods for field mappings
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMedicalInfo() {
        return medicalInfo;
    }

    public void setMedicalInfo(String medicalInfo) {
        this.medicalInfo = medicalInfo;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isTutorialCompleted() {
        return tutorialCompleted;
    }

    public void setTutorialCompleted(boolean tutorialCompleted) {
        this.tutorialCompleted = tutorialCompleted;
    }
}
