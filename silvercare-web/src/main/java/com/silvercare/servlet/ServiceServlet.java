package com.silvercare.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.silvercare.dao.ServiceDAO;
import com.silvercare.model.Service;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Traditional Servlet for Service operations - Uses JAX-RS Client to call REST
 * APIs
 */
@WebServlet("/ServiceServlet")
public class ServiceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "category"; // Default action
        }

        switch (action) {
            case "category":
                showServiceCatalog(request, response);
                break;
            case "details":
                showServiceDetails(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/home");
                break;
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Show service catalog with categories
     */
    private void showServiceCatalog(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ServiceDAO serviceDAO = new ServiceDAO();
        try {
            // Get all categories via JDBC
            List<Map<String, Object>> categories = serviceDAO.getAllCategories();

            // For each category, fetch services via JDBC
            List<Service> allServices = serviceDAO.getAllServices();

            // Map services to categories for the view
            for (Map<String, Object> category : categories) {
                int categoryId = ((Number) category.get("id")).intValue();
                List<Service> catServices = new java.util.ArrayList<>();
                for (Service s : allServices) {
                    if (s.getCategoryId() == categoryId) {
                        catServices.add(s);
                    }
                }
                category.put("services", catServices);
            }

            request.setAttribute("categories", categories);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load services: " + e.getMessage());
        }

        // Forward to JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/serviceCategory.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Show service details
     */
    private void showServiceDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/ServiceServlet?action=category");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            ServiceDAO serviceDAO = new ServiceDAO();

            // Get service by ID via JDBC
            Service service = serviceDAO.getServiceById(id);

            if (service != null) {
                String categoryName = service.getCategoryName() != null ? service.getCategoryName() : "General";

                request.setAttribute("service", service);
                request.setAttribute("categoryName", categoryName);

                // Forward to JSP
                RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/serviceDetails.jsp");
                dispatcher.forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/ServiceServlet?action=category");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/ServiceServlet?action=category&error=load_failed");
        }
    }
}
