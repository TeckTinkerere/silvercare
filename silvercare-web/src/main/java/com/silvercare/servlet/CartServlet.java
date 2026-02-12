package com.silvercare.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.silvercare.util.ApiClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Servlet for Cart operations - Uses ApiClient to call REST APIs
 */
@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Gson gson = ApiClient.getGson();

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

    private int parseId(String idStr) {
        if (idStr == null || idStr.trim().isEmpty())
            return 0;
        try {
            return Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            try {
                return (int) Double.parseDouble(idStr);
            } catch (Exception e2) {
                return 0;
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "view";
        }

        switch (action) {
            case "add":
                addToCart(request, response);
                break;
            case "update":
                updateCartItem(request, response);
                break;
            default:
                doGet(request, response);
                break;
        }
    }

    private void viewCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/cart.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Add item to cart - fetches service details via REST API
     */
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
            int serviceId = parseId(serviceIdStr);
            if (serviceId <= 0) {
                response.sendRedirect(request.getContextPath() + "/FrontEnd/serviceCategory.jsp?error=invalid_id");
                return;
            }

            // Fetch service details via REST API
            ApiClient.ApiResponse<String> apiResponse = ApiClient.get("/services/" + serviceId, String.class);

            if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                JsonObject json = gson.fromJson(apiResponse.getData(), JsonObject.class);
                JsonObject serviceData = json.getAsJsonObject("data");

                if (serviceData != null) {
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
                        item.put("id", serviceData.has("id") ? serviceData.get("id").getAsInt() : serviceId);
                        item.put("name", serviceData.has("name") ? serviceData.get("name").getAsString() : "");
                        item.put("price", serviceData.has("price") ? serviceData.get("price").getAsDouble() : 0.0);
                        item.put("image",
                                serviceData.has("imagePath") ? serviceData.get("imagePath").getAsString() : "");
                        item.put("description",
                                serviceData.has("description") ? serviceData.get("description").getAsString() : "");
                        cart.add(item);
                    }

                    session.setAttribute("cart", cart);
                    session.setAttribute("cart_count", cart.size());
                    response.sendRedirect(request.getContextPath() + "/CartServlet?action=view&added=true");
                } else {
                    response.sendRedirect(request.getContextPath() + "/FrontEnd/serviceCategory.jsp?error=not_found");
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/FrontEnd/serviceCategory.jsp?error=not_found");
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
            int serviceIdToRemove = parseId(serviceIdStr);
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
            int serviceId = parseId(serviceIdStr);
            int qty = Integer.parseInt(qtyStr);

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");

            if (cart != null) {
                if (qty <= 0) {
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
