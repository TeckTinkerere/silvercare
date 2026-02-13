<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

    <%-- Include Header --%>
        <jsp:include page="header.jsp" />

        <main class="container my-5">
            <div class="row">
                <div class="col-lg-6 col-md-8 mx-auto">
                    <div class="card p-4 shadow-sm">
                        <div class="card-body">
                            <div class="text-center mb-4">
                                <h1 class="h3 mb-3 fw-bold">Create Your Account</h1>
                                <p>Join SilverCare to manage your senior care needs with ease.</p>
                            </div>

                            <!-- Display Error Message if registration fails -->
                            <% 
                                String regError = request.getParameter("error"); 
                                if (regError != null) { 
                                    if (regError.equals("failed")) { 
                                        out.println("<div class='alert alert-danger' role='alert'>Registration failed. Please try again.</div>");
                                    } else if (regError.equals("email_exists")) { 
                                        out.println("<div class='alert alert-danger' role='alert'>An account with this email already exists.</div>");
                                    } else if (regError.equals("password_mismatch")) {
                                        out.println("<div class='alert alert-danger' role='alert'>Passwords do not match. Please try again.</div>");
                                    }
                                }
                            %>

                        <form action="${pageContext.request.contextPath}/UserServlet?action=register" method="POST">
                            <div class="mb-3">
                                <label for="fullName" class="form-label">Full Name</label>
                                <input type="text" class="form-control" id="fullName" name="fullName" required
                                    autofocus>
                            </div>
                            <div class="mb-3">
                                <label for="email" class="form-label">Email Address</label>
                                <input type="email" class="form-control" id="email" name="email" required>
                            </div>
                            <div class="mb-3">
                                <label for="phone" class="form-label">Phone</label>
                                <input type="text" class="form-control" id="phone" name="phone" required>
                            </div>
                            <div class="mb-3">
                                <label for="address" class="form-label">Address</label>
                                <input type="text" class="form-control" id="address" name="address" required>
                            </div>
                            <div class="mb-3">
                                <label for="password" class="form-label">Password</label>
                                <input type="password" class="form-control" id="password" name="password" required>
                            </div>
                            <div class="mb-3">
                                <label for="confirmPassword" class="form-label">Confirm Password</label>
                                <input type="password" class="form-control" id="confirmPassword"
                                    name="confirmPassword" required>
                            </div>
                            <div class="d-grid">
                                <button type="submit" class="btn btn-primary btn-lg">Create Account</button>
                            </div>
                        </form>

                        <div class="text-center mt-4">
                            <p class="text-muted">Already have an account? <a href="${pageContext.request.contextPath}/login">Sign in</a></p>
                        </div>
                    </div>
                </div>
            </div>
            </div>
        </main>

        <%-- Include Footer --%>
            <jsp:include page="footer.jsp"></jsp:include>