package com.silvercare.dao;

import java.sql.SQLException;
import java.util.Map;

import com.silvercare.util.ContactDBUtil;

/**
 * DAO for Contact messages.
 * Delegates database operations to ContactDBUtil.
 */
public class ContactDAO {

    private ContactDBUtil contactDBUtil;

    public ContactDAO() {
        this.contactDBUtil = new ContactDBUtil();
    }

    public void saveContact(Map<String, Object> contactData) throws SQLException {
        contactDBUtil.saveContact(contactData);
    }
}
