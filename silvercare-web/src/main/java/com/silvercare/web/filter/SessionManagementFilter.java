package com.silvercare.web.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Enhanced Session Management Filter for strict state separation.
 * Ensures proper isolation between admin, authenticated users, and public
 * access.
 * Complies with Topic 4 (Session Management) and Risk 3 (Access Control).
 */
@WebFilter("/*")
public class SessionManagementFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();

        // Extract the path without context
        String path = requestURI.substring(contextPath.length());

        // 1. Public Access - No authentication required
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        // 2. Admin Routes - Strict /admin/* protection (handled by LoginFilter)
        if (path.startsWith("/admin/")) {
            // LoginFilter will handle admin-specific checks
            chain.doFilter(request, response);
            return;
        }

        // 3. Protected User Routes - Require authentication but not admin
        if (isProtectedUserPath(path)) {
            boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

            if (!isLoggedIn) {
                httpResponse.sendRedirect(contextPath + "/FrontEnd/login.jsp?error=login_required");
                return;
            }
        }

        // 4. Add session security headers
        if (session != null) {
            // Ensure session attributes are properly scoped
            String userRole = (String) session.getAttribute("role");

            // Verify role consistency with user object
            if (session.getAttribute("user") != null && userRole == null) {
                // If user exists but role is missing, fix it
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> user = (java.util.Map<String, Object>) session.getAttribute("user");
                userRole = (String) user.get("role");
                session.setAttribute("role", userRole);
            }

            // Set role-specific attributes for view logic
            if ("admin".equalsIgnoreCase(userRole)) {
                session.setAttribute("isAdmin", true);
                session.setAttribute("isCustomer", false);
            } else if ("customer".equalsIgnoreCase(userRole)) {
                session.setAttribute("isAdmin", false);
                session.setAttribute("isCustomer", true);
            }
        }

        // Continue the filter chain
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup logic if needed
    }

    /**
     * Check if path is publicly accessible without authentication
     */
    private boolean isPublicPath(String path) {
        return path.startsWith("/FrontEnd/") && !path.contains("/profile") && !path.contains("/booking")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.startsWith("/uploads/")
                || path.equals("/")
                || path.equals("/home")
                || path.equals("/HomeServlet")
                || path.equals("/ContactServlet")
                || path.equals("/ServiceServlet")
                || path.equals("/UserServlet") // Login/Register endpoints
                || path.equals("/LoginServlet")
                || path.equals("/logout");
    }

    /**
     * Check if path requires user authentication (but not admin)
     */
    private boolean isProtectedUserPath(String path) {
        return path.startsWith("/BookingServlet")
                || path.startsWith("/CartServlet")
                || path.contains("/profile")
                || path.contains("/booking")
                || path.contains("/checkout")
                || path.equals("/user/tutorial/complete");
    }
}
