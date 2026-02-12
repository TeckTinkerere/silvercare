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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servlet for Home page - Uses ApiClient to call REST APIs
 */
@WebServlet({ "/home", "" })
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Gson gson = ApiClient.getGson();

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
                shouldShowTutorial = true;
            } else if (tutorialCompleted == null || !tutorialCompleted) {
                shouldShowTutorial = true;
            }
        }
        request.setAttribute("shouldShowTutorial", shouldShowTutorial);

        // --- 2. Account Deleted Message ---
        String accountDeleted = request.getParameter("accountDeleted");
        if ("true".equals(accountDeleted)) {
            request.setAttribute("accountDeleted", true);
        }

        // --- 3. Fetch Service Categories via REST API ---
        try {
            ApiClient.ApiResponse<String> apiResponse = ApiClient.get("/services/categories", String.class);

            if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                JsonObject json = gson.fromJson(apiResponse.getData(), JsonObject.class);
                JsonArray dataArray = json.getAsJsonArray("data");

                Type listType = new TypeToken<List<Map<String, Object>>>() {
                }.getType();
                List<Map<String, Object>> allCategories = gson.fromJson(dataArray, listType);

                // Take top 3 categories
                List<Map<String, Object>> topCategories = allCategories.stream()
                        .limit(3)
                        .collect(Collectors.toList());

                request.setAttribute("categories", topCategories);
            } else {
                request.setAttribute("categories", new ArrayList<>());
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("categories", new ArrayList<>());
        }

        // Forward to JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/home.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
