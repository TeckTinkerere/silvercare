package com.silvercare.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.silvercare.model.Service;
import com.silvercare.util.ServiceDBUtil;

/**
 * Data Access Object for Service entity
 * Contains field mappings and delegates SQL operations to utility beans
 * This follows the assignment requirement of separating concerns
 */
@Repository
public class ServiceDAO {

    private final ServiceDBUtil serviceDBUtil;

    @Autowired
    public ServiceDAO(ServiceDBUtil serviceDBUtil) {
        this.serviceDBUtil = serviceDBUtil;
    }

    /**
     * Default constructor for legacy support
     */
    public ServiceDAO() {
        this.serviceDBUtil = new ServiceDBUtil();
    }

    /**
     * Get all services - delegates to utility bean
     */
    public List<Service> getAllServices() throws SQLException {
        return serviceDBUtil.getAllServices();
    }

    /**
     * Get service by ID - delegates to utility bean
     */
    public Service getServiceById(int id) throws SQLException {
        return serviceDBUtil.getServiceById(id);
    }

    /**
     * Get all categories - delegates to utility bean
     */
    public List<Map<String, Object>> getCategories() throws SQLException {
        return serviceDBUtil.getAllCategories();
    }

    /**
     * Add service - delegates to utility bean
     */
    public boolean addService(Service service) throws SQLException {
        return serviceDBUtil.addService(service);
    }

    /**
     * Update service - delegates to utility bean
     */
    public boolean updateService(Service service) throws SQLException {
        return serviceDBUtil.updateService(service);
    }

    /**
     * Delete service - delegates to utility bean
     */
    public boolean deleteService(int serviceId) throws SQLException {
        return serviceDBUtil.deleteService(serviceId);
    }

    /**
     * Get services by category - delegates to utility bean
     */
    public List<Service> getServicesByCategory(int categoryId) throws SQLException {
        return serviceDBUtil.getServicesByCategory(categoryId);
    }

    /**
     * Search services - delegates to utility bean
     */
    public List<Service> searchServices(String searchTerm) throws SQLException {
        return serviceDBUtil.searchServices(searchTerm);
    }

    /**
     * Get all services with category name - delegates to utility bean
     */
    public List<Service> getAllServicesWithCategory() throws SQLException {
        return serviceDBUtil.getAllServicesWithCategory();
    }

    /**
     * Add category
     */
    public boolean addCategory(String name, String description, String icon) throws SQLException {
        return serviceDBUtil.addCategory(name, description, icon);
    }

    /**
     * Update category
     */
    public boolean updateCategory(int id, String name, String description, String icon) throws SQLException {
        return serviceDBUtil.updateCategory(id, name, description, icon);
    }

    /**
     * Delete category
     */
    public boolean deleteCategory(int id) throws SQLException {
        return serviceDBUtil.deleteCategory(id);
    }
}
