package com.silvercare.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.silvercare.model.ContactMessage;
import com.silvercare.util.ContactMessageDBUtil;

/**
 * Data Access Object for ContactMessage entity
 */
@Repository
public class ContactMessageDAO {

    private final ContactMessageDBUtil contactMessageDBUtil;

    @Autowired
    public ContactMessageDAO(ContactMessageDBUtil contactMessageDBUtil) {
        this.contactMessageDBUtil = contactMessageDBUtil;
    }

    /**
     * Default constructor for legacy support
     */
    public ContactMessageDAO() {
        this.contactMessageDBUtil = new ContactMessageDBUtil();
    }

    /**
     * Save contact message - delegates to utility bean
     */
    public boolean saveContactMessage(ContactMessage message) throws SQLException {
        return contactMessageDBUtil.saveContactMessage(message);
    }

    /**
     * Get all contact messages - delegates to utility bean
     */
    public List<ContactMessage> getAllContactMessages() throws SQLException {
        return contactMessageDBUtil.getAllContactMessages();
    }
}
