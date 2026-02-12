package com.silvercare.util;

import com.google.gson.Gson;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * JAX-RS Client utility for calling REST API
 * Follows reference project pattern but with enhanced error handling
 */
public class ApiClient {

    private static final String API_BASE_URL = System.getenv("API_BASE_URL") != null ? System.getenv("API_BASE_URL")
            : System.getProperty("api.base.url", "http://localhost:8081/s-api");
    private static final Gson gson = new Gson();

    public static Gson getGson() {
        return gson;
    }

    /**
     * API Response wrapper
     */
    public static class ApiResponse<T> {
        private int statusCode;
        private T data;
        private String error;

        public ApiResponse(int statusCode, T data, String error) {
            this.statusCode = statusCode;
            this.data = data;
            this.error = error;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public T getData() {
            return data;
        }

        public String getError() {
            return error;
        }

        public boolean isSuccess() {
            return statusCode >= 200 && statusCode < 300;
        }
    }

    /**
     * GET request with GenericType and Admin ID
     */
    public static <T> ApiResponse<T> get(String endpoint, GenericType<T> responseType, Integer adminId) {
        Client client = ClientBuilder.newClient();
        try {
            WebTarget target = client.target(API_BASE_URL + endpoint);
            Invocation.Builder invoker = target.request(MediaType.APPLICATION_JSON);
            if (adminId != null) {
                invoker.header("X-Admin-Id", adminId);
            }
            Response response = invoker.get();

            return handleResponse(response, responseType);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(500, null, "Error: " + e.getMessage());
        } finally {
            client.close();
        }
    }

    /**
     * GET request with GenericType
     */
    public static <T> ApiResponse<T> get(String endpoint, GenericType<T> responseType) {
        return get(endpoint, responseType, null);
    }

    /**
     * GET request with Admin ID
     */
    public static <T> ApiResponse<T> get(String endpoint, Class<T> responseType, Integer adminId) {
        Client client = ClientBuilder.newClient();
        try {
            WebTarget target = client.target(API_BASE_URL + endpoint);
            Invocation.Builder invoker = target.request(MediaType.APPLICATION_JSON);
            if (adminId != null) {
                invoker.header("X-Admin-Id", adminId);
            }
            Response response = invoker.get();

            return handleResponse(response, responseType);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(500, null, "Error: " + e.getMessage());
        } finally {
            client.close();
        }
    }

    /**
     * GET request
     */
    public static <T> ApiResponse<T> get(String endpoint, Class<T> responseType) {
        return get(endpoint, responseType, null);
    }

    /**
     * POST request with GenericType and Admin ID
     */
    public static <T> ApiResponse<T> post(String endpoint, Object requestBody, GenericType<T> responseType,
            Integer adminId) {
        Client client = ClientBuilder.newClient();
        try {
            WebTarget target = client.target(API_BASE_URL + endpoint);
            Invocation.Builder invoker = target.request(MediaType.APPLICATION_JSON);
            if (adminId != null) {
                invoker.header("X-Admin-Id", adminId);
            }
            Response response = invoker.post(Entity.entity(requestBody, MediaType.APPLICATION_JSON));

            return handleResponse(response, responseType);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(500, null, "Error: " + e.getMessage());
        } finally {
            client.close();
        }
    }

    /**
     * POST request with GenericType
     */
    public static <T> ApiResponse<T> post(String endpoint, Object requestBody, GenericType<T> responseType) {
        return post(endpoint, requestBody, responseType, null);
    }

    /**
     * POST request with Admin ID
     */
    public static <T> ApiResponse<T> post(String endpoint, Object requestBody, Class<T> responseType, Integer adminId) {
        Client client = ClientBuilder.newClient();
        try {
            WebTarget target = client.target(API_BASE_URL + endpoint);
            Invocation.Builder invoker = target.request(MediaType.APPLICATION_JSON);
            if (adminId != null) {
                invoker.header("X-Admin-Id", adminId);
            }
            Response response = invoker.post(Entity.entity(requestBody, MediaType.APPLICATION_JSON));

            return handleResponse(response, responseType);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(500, null, "Error: " + e.getMessage());
        } finally {
            client.close();
        }
    }

    /**
     * POST request
     */
    public static <T> ApiResponse<T> post(String endpoint, Object requestBody, Class<T> responseType) {
        return post(endpoint, requestBody, responseType, null);
    }

    /**
     * PUT request with GenericType and Admin ID
     */
    public static <T> ApiResponse<T> put(String endpoint, Object requestBody, GenericType<T> responseType,
            Integer adminId) {
        Client client = ClientBuilder.newClient();
        try {
            WebTarget target = client.target(API_BASE_URL + endpoint);
            Invocation.Builder invoker = target.request(MediaType.APPLICATION_JSON);
            if (adminId != null) {
                invoker.header("X-Admin-Id", adminId);
            }
            Response response = invoker.put(Entity.entity(requestBody, MediaType.APPLICATION_JSON));

            return handleResponse(response, responseType);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(500, null, "Error: " + e.getMessage());
        } finally {
            client.close();
        }
    }

    /**
     * PUT request with GenericType
     */
    public static <T> ApiResponse<T> put(String endpoint, Object requestBody, GenericType<T> responseType) {
        return put(endpoint, requestBody, responseType, null);
    }

    /**
     * PUT request with Admin ID
     */
    public static <T> ApiResponse<T> put(String endpoint, Object requestBody, Class<T> responseType, Integer adminId) {
        Client client = ClientBuilder.newClient();
        try {
            WebTarget target = client.target(API_BASE_URL + endpoint);
            Invocation.Builder invoker = target.request(MediaType.APPLICATION_JSON);
            if (adminId != null) {
                invoker.header("X-Admin-Id", adminId);
            }
            Response response = invoker.put(Entity.entity(requestBody, MediaType.APPLICATION_JSON));

            return handleResponse(response, responseType);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(500, null, "Error: " + e.getMessage());
        } finally {
            client.close();
        }
    }

    /**
     * PUT request
     */
    public static <T> ApiResponse<T> put(String endpoint, Object requestBody, Class<T> responseType) {
        return put(endpoint, requestBody, responseType, null);
    }

    /**
     * DELETE request with GenericType and Admin ID
     */
    public static <T> ApiResponse<T> delete(String endpoint, GenericType<T> responseType, Integer adminId) {
        Client client = ClientBuilder.newClient();
        try {
            WebTarget target = client.target(API_BASE_URL + endpoint);
            Invocation.Builder invoker = target.request(MediaType.APPLICATION_JSON);
            if (adminId != null) {
                invoker.header("X-Admin-Id", adminId);
            }
            Response response = invoker.delete();

            return handleResponse(response, responseType);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(500, null, "Error: " + e.getMessage());
        } finally {
            client.close();
        }
    }

    /**
     * DELETE request with GenericType
     */
    public static <T> ApiResponse<T> delete(String endpoint, GenericType<T> responseType) {
        return delete(endpoint, responseType, null);
    }

    /**
     * DELETE request with Admin ID
     */
    public static <T> ApiResponse<T> delete(String endpoint, Class<T> responseType, Integer adminId) {
        Client client = ClientBuilder.newClient();
        try {
            WebTarget target = client.target(API_BASE_URL + endpoint);
            Invocation.Builder invoker = target.request(MediaType.APPLICATION_JSON);
            if (adminId != null) {
                invoker.header("X-Admin-Id", adminId);
            }
            Response response = invoker.delete();

            return handleResponse(response, responseType);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(500, null, "Error: " + e.getMessage());
        } finally {
            client.close();
        }
    }

    /**
     * DELETE request
     */
    public static <T> ApiResponse<T> delete(String endpoint, Class<T> responseType) {
        return delete(endpoint, responseType, null);
    }

    /**
     * Handle response and convert to ApiResponse
     */
    private static <T> ApiResponse<T> handleResponse(Response response, Class<T> responseType) {
        int statusCode = response.getStatus();

        if (statusCode >= 200 && statusCode < 300) {
            try {
                T data = response.readEntity(responseType);
                return new ApiResponse<>(statusCode, data, null);
            } catch (Exception e) {
                return new ApiResponse<>(statusCode, null, "Error parsing response");
            }
        } else {
            return handleFailedResponse(response, statusCode);
        }
    }

    private static <T> ApiResponse<T> handleResponse(Response response, GenericType<T> responseType) {
        int statusCode = response.getStatus();

        if (statusCode >= 200 && statusCode < 300) {
            try {
                T data = response.readEntity(responseType);
                return new ApiResponse<>(statusCode, data, null);
            } catch (Exception e) {
                return new ApiResponse<>(statusCode, null, "Error parsing response");
            }
        } else {
            return handleFailedResponse(response, statusCode);
        }
    }

    private static <T> ApiResponse<T> handleFailedResponse(Response response, int statusCode) {
        String error = "Request failed with status: " + statusCode;
        try {
            error = response.readEntity(String.class);
        } catch (Exception e) {
            // Use default error message
        }
        return new ApiResponse<>(statusCode, null, error);
    }

    /**
     * Get the API base URL
     */
    public static String getApiBaseUrl() {
        return API_BASE_URL;
    }
}
