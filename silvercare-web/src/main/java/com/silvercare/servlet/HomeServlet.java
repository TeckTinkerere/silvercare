package com.silvercare.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.silvercare.dao.ServiceDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Traditional Servlet for Home page - Uses JAX-RS Client to call REST APIs
 */
@WebServlet({ "/home", "" })
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        // --- 1. Tutorial Logic ---
        Integer customerId = (Integer) session.getAttribute("customer_id");
        Boolean tutorialCompleted = (Boolean) session.getAttribute("tutorial_completed");
        boolean shouldShowTutorial = false;

        String showTutorial = request.getParameter("showTutorial");
        if (customerId != null) {
            if ("true".equals(showTutorial)) {
                // Manual trigger from profile page
                shouldShowTutorial = true;
            } else if (tutorialCompleted == null || !tutorialCompleted) {
                // First-time user
                shouldShowTutorial = true;
            }
        }
        request.setAttribute("shouldShowTutorial", shouldShowTutorial);

        // --- 2. Account Deleted Message ---
        String accountDeleted = request.getParameter("accountDeleted");
        if ("true".equals(accountDeleted)) {
            request.setAttribute("accountDeleted", true);
        }

        // Fetch Service Categories via local DAO
        try {
            ServiceDAO serviceDAO = new ServiceDAO();
            List<Map<String, Object>> allCategories = serviceDAO.getAllCategories();

            // Take top 3 categories
            List<Map<String, Object>> topCategories = allCategories.stream()
                    .limit(3)
                    .collect(Collectors.toList());

            request.setAttribute("categories", topCategories);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("categories", new ArrayList<>());
        }

        // Forward to JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/home.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
