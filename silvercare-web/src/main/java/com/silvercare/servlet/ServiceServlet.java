package com.silvercare.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.silvercare.util.ApiClient;
import com.silvercare.util.PriceCalculator;
import com.silvercare.model.Service;
import com.silvercare.model.Season;
import com.silvercare.service.SeasonService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet for Service Catalog - Uses ApiClient to call REST APIs.
 */
@WebServlet(urlPatterns = { "/services", "/ServiceServlet" })
public class ServiceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Gson gson = ApiClient.getGson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("details".equals(action)) {
            showServiceDetails(request, response);
        } else {
            showServiceCatalog(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Show service catalog with categories - via REST API
     */
    private void showServiceCatalog(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String categoryId = request.getParameter("category");
        String search = request.getParameter("search");

        try {
            // Get current season
            Season currentSeason = SeasonService.getInstance().getCurrentSeason();
            request.setAttribute("currentSeason", currentSeason);
            System.out.println("ServiceServlet: Current season is " + currentSeason);

            // Fetch categories via REST API
            List<Map<String, Object>> categories = new ArrayList<>();
            ApiClient.ApiResponse<String> catResponse = ApiClient.get("/services/categories", String.class);
            if (catResponse.isSuccess() && catResponse.getData() != null) {
                JsonObject catJson = gson.fromJson(catResponse.getData(), JsonObject.class);
                JsonArray catArray = catJson.getAsJsonArray("data");
                Type listType = new TypeToken<List<Map<String, Object>>>() {
                }.getType();
                categories = gson.fromJson(catArray, listType);

                // Convert numeric IDs from Double to Integer
                for (Map<String, Object> category : categories) {
                    if (category.get("id") instanceof Number) {
                        category.put("id", ((Number) category.get("id")).intValue());
                    }
                }

                request.setAttribute("categories", categories);
            } else {
                request.setAttribute("categories", categories);
            }

            // Fetch services via REST API
            String serviceEndpoint;
            if (search != null && !search.trim().isEmpty()) {
                serviceEndpoint = "/services/search?query=" + java.net.URLEncoder.encode(search.trim(), "UTF-8");
            } else if (categoryId != null && !categoryId.trim().isEmpty()) {
                serviceEndpoint = "/services/category/" + categoryId;
            } else {
                serviceEndpoint = "/services";
            }

            ApiClient.ApiResponse<String> svcResponse = ApiClient.get(serviceEndpoint, String.class);
            if (svcResponse.isSuccess() && svcResponse.getData() != null) {
                JsonObject svcJson = gson.fromJson(svcResponse.getData(), JsonObject.class);
                JsonArray dataArray = svcJson.getAsJsonArray("data");
                Type serviceListType = new TypeToken<List<Service>>() {
                }.getType();
                List<Service> services = gson.fromJson(dataArray, serviceListType);

                // Apply seasonal pricing to all services
                PriceCalculator.applySeasonalPricing(services, currentSeason);
                System.out.println("ServiceServlet: Applied seasonal pricing to " + services.size() + " services");

                request.setAttribute("services", services);

                // Group services by category and attach to category objects
                for (Map<String, Object> category : categories) {
                    List<Service> categoryServices = new ArrayList<>();
                    Object catIdObj = category.get("id");
                    int catId = -1;

                    if (catIdObj instanceof Number) {
                        catId = ((Number) catIdObj).intValue();
                    } else if (catIdObj instanceof String) {
                        try {
                            catId = (int) Double.parseDouble((String) catIdObj);
                        } catch (NumberFormatException e) {
                            continue;
                        }
                    }

                    for (Service service : services) {
                        if (catId == service.getCategoryId()) {
                            categoryServices.add(service);
                        }
                    }
                    category.put("services", categoryServices);
                }
            } else {
                request.setAttribute("services", new ArrayList<>());
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("categories", new ArrayList<>());
            request.setAttribute("services", new ArrayList<>());
            request.setAttribute("servicesByCategory", new HashMap<>());
            request.setAttribute("currentSeason", Season.NEUTRAL);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/serviceCategory.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Show service details - via REST API
     */
    private void showServiceDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");

        if (idStr != null) {
            try {
                // Get current season
                Season currentSeason = SeasonService.getInstance().getCurrentSeason();
                request.setAttribute("currentSeason", currentSeason);

                // Fetch service details from API (includes category name)
                ApiClient.ApiResponse<String> apiResponse = ApiClient.get("/services/" + idStr, String.class);

                if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                    JsonObject json = gson.fromJson(apiResponse.getData(), JsonObject.class);
                    JsonObject dataObj = json.getAsJsonObject("data");
                    Service service = gson.fromJson(dataObj, Service.class);

                    // Calculate seasonal price
                    BigDecimal multiplier = service.getMultiplierForSeason(currentSeason);
                    BigDecimal seasonalPrice = PriceCalculator.calculateSeasonalPrice(service.getPrice(), multiplier);
                    service.setSeasonalPrice(seasonalPrice);
                    service.setCurrentSeason(currentSeason);

                    System.out.println("ServiceServlet: Service " + service.getName() +
                            " - Base: " + service.getPrice() +
                            ", Multiplier: " + multiplier +
                            ", Seasonal: " + seasonalPrice);

                    request.setAttribute("service", service);

                    // Set category name from the service object (API now includes it)
                    if (service.getCategoryName() != null && !service.getCategoryName().isEmpty()) {
                        request.setAttribute("categoryName", service.getCategoryName());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("currentSeason", Season.NEUTRAL);
            }
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/serviceDetails.jsp");
        dispatcher.forward(request, response);
    }
}
