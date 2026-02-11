package com.silvercare.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.silvercare.model.Service;
import com.silvercare.util.ServiceDBUtil;

/**
 * DAO for Service entity in silvercare-web.
 * Delegates actual database operations to ServiceDBUtil to separate SQL logic.
 */
public class ServiceDAO {

    private final ServiceDBUtil serviceDBUtil;

    public ServiceDAO() {
        this.serviceDBUtil = new ServiceDBUtil();
    }

    public List<Service> getAllServices() throws SQLException {
        return serviceDBUtil.getAllServices();
    }

    public Service getServiceById(int id) throws SQLException {
        return serviceDBUtil.getServiceById(id);
    }

    public List<Map<String, Object>> getAllCategories() throws SQLException {
        return serviceDBUtil.getAllCategories();
    }

    public List<Service> searchServices(String term) throws SQLException {
        return serviceDBUtil.searchServices(term);
    }

    public void addService(Service s) throws SQLException {
        serviceDBUtil.addService(s);
    }

    public void updateService(Service s) throws SQLException {
        serviceDBUtil.updateService(s);
    }

    public void deleteService(int id) throws SQLException {
        serviceDBUtil.deleteService(id);
    }

    public void addCategory(String name, String description, String icon) throws SQLException {
        serviceDBUtil.addCategory(name, description, icon);
    }

    public void deleteCategory(int id) throws SQLException {
        serviceDBUtil.deleteCategory(id);
    }
}
