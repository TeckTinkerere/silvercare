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
 * Filter to protect admin resources.
 * Complies with Topic 4 (Session Management) and Risk 3 (Access Control).
 */
@WebFilter("/admin/*")
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        boolean loggedIn = (session != null && session.getAttribute("user") != null);
        String role = (session != null) ? (String) session.getAttribute("role") : null;
        boolean isAdmin = "admin".equalsIgnoreCase(role);

        if (loggedIn && isAdmin) {
            // User is admin, allow request to proceed
            chain.doFilter(request, response);
        } else {
            // Not admin or not logged in, redirect to login
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/FrontEnd/login.jsp?error=unauthorized");
        }
    }

    @Override
    public void destroy() {
    }
}
