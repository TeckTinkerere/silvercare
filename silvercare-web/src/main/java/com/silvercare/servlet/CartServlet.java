package com.silvercare.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.silvercare.dao.ServiceDAO;
import com.silvercare.model.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Traditional Servlet for Cart operations
 */
@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "view";
        }

        switch (action) {
            case "view":
                viewCart(request, response);
                break;
            case "add":
                addToCart(request, response);
                break;
            case "remove":
                removeFromCart(request, response);
                break;
            case "update":
                updateCartItem(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/CartServlet?action=view");
                break;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("update".equals(action)) {
            updateCartItem(request, response);
        } else {
            doGet(request, response);
        }
    }

    private void viewCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/cart.jsp");
        dispatcher.forward(request, response);
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        if (session.getAttribute("customer_id") == null && session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/FrontEnd/login.jsp?redirect=cart");
            return;
        }

        String idParam = request.getParameter("id");
        String serviceIdParam = request.getParameter("service_id");

        String serviceIdStr = idParam;
        if (serviceIdStr == null || serviceIdStr.trim().isEmpty()) {
            serviceIdStr = serviceIdParam;
        }

        if (serviceIdStr == null || serviceIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/FrontEnd/serviceCategory.jsp?error=invalid_id");
            return;
        }

        try {
            int serviceId = Integer.parseInt(serviceIdStr);

            try {
                // Use local DAO to get service by ID
                ServiceDAO serviceDAO = new ServiceDAO();
                Service service = serviceDAO.getServiceById(serviceId);

                if (service != null) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");
                    if (cart == null) {
                        cart = new ArrayList<>();
                    }

                    // Check if already in cart
                    boolean exists = false;
                    for (Map<String, Object> item : cart) {
                        int id = ((Number) item.get("id")).intValue();
                        if (id == serviceId) {
                            exists = true;
                            break;
                        }
                    }

                    if (!exists) {
                        Map<String, Object> item = new HashMap<>();
                        item.put("id", service.getId());
                        item.put("name", service.getName());
                        item.put("price", service.getPrice().doubleValue());
                        item.put("image", service.getImagePath());
                        item.put("description", service.getDescription());
                        cart.add(item);
                    }

                    session.setAttribute("cart", cart);
                    session.setAttribute("cart_count", cart.size());
                    response.sendRedirect(request.getContextPath() + "/CartServlet?action=view&added=true");
                } else {
                    response.sendRedirect(request.getContextPath() + "/FrontEnd/serviceCategory.jsp?error=not_found");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/FrontEnd/serviceCategory.jsp?error=database_error");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/FrontEnd/serviceCategory.jsp?error=invalid_id");
        }
    }

    private void removeFromCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String serviceIdStr = request.getParameter("id");

        if (serviceIdStr == null || serviceIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=view");
            return;
        }

        try {
            int serviceIdToRemove = Integer.parseInt(serviceIdStr);
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");

            if (cart != null) {
                Iterator<Map<String, Object>> iterator = cart.iterator();
                while (iterator.hasNext()) {
                    Map<String, Object> item = iterator.next();
                    int itemId = ((Number) item.get("id")).intValue();
                    if (itemId == serviceIdToRemove) {
                        iterator.remove();
                        break;
                    }
                }
                session.setAttribute("cart", cart);
                session.setAttribute("cart_count", cart.size());
            }
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=view&removed=true");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=view&error=invalid_id");
        }
    }

    /**
     * Update cart item quantity (CRUD - Update operation)
     */
    private void updateCartItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String serviceIdStr = request.getParameter("id");
        String qtyStr = request.getParameter("qty");

        if (serviceIdStr == null || qtyStr == null) {
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=view");
            return;
        }

        try {
            int serviceId = Integer.parseInt(serviceIdStr);
            int qty = Integer.parseInt(qtyStr);

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");

            if (cart != null) {
                if (qty <= 0) {
                    // If quantity is 0 or negative, remove the item
                    cart.removeIf(item -> ((Number) item.get("id")).intValue() == serviceId);
                } else {
                    for (Map<String, Object> item : cart) {
                        if (((Number) item.get("id")).intValue() == serviceId) {
                            item.put("qty", qty);
                            break;
                        }
                    }
                }
                session.setAttribute("cart", cart);
                session.setAttribute("cart_count", cart.size());
            }
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=view&updated=true");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=view&error=invalid_input");
        }
    }
}
