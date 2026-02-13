<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%-- Include Header --%>
<jsp:include page="header.jsp" />

<main class="container my-5">
    <div class="row">
        <div class="col-lg-5 col-md-8 mx-auto">
            <div class="card p-4 shadow-sm">
                <div class="card-body">
                    <div class="text-center mb-4">
                        <h1 class="h3 mb-3 fw-bold">Welcome Back</h1>
                        <p>Sign in to continue to your account.</p>
                    </div>

                    <!-- Display Error/Success Messages -->
                    <%
                        String error = request.getParameter("error");
                        
                        if ("invalid".equals(error)) {
                            out.println("<div class='alert alert-danger' role='alert'>Invalid email or password. Please try again.</div>");
                        } else if ("system_error".equals(error)) {
                            out.println("<div class='alert alert-danger' role='alert'>A system error occurred. Please try again later.</div>");
                        } else if ("unauthorized".equals(error)) {
                            out.println("<div class='alert alert-warning' role='alert'>You must be logged in to access that page.</div>");
                        }
                        
                        String logout = request.getParameter("logout");
                        if ("success".equals(logout)) {
                            out.println("<div class='alert alert-success' role='alert'>You have been logged out successfully.</div>");
                        }
                        
                        String success = request.getParameter("success");
                        if ("registered".equals(success)) {
                            out.println("<div class='alert alert-success' role='alert'>Registration successful! Please login.</div>");
                        }
                    %>

                    <form action="${pageContext.request.contextPath}/UserServlet?action=login" method="POST">
                        <div class="mb-3">
                            <label for="email" class="form-label">Email</label>
                            <input type="text" class="form-control" id="email" name="email" required autofocus>
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">Password</label>
                            <input type="password" class="form-control" id="password" name="password" required>
                        </div>
                        <div class="d-grid">
                            <button type="submit" class="btn btn-primary btn-lg">Sign In</button>
                        </div>
                    </form>

                    <div class="text-center mt-4">
                        <p class="text-muted">Don't have an account? <a href="${pageContext.request.contextPath}/register">Sign up now</a></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<%-- Include Footer --%>
<jsp:include page="footer.jsp" />