<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <% if (session.getAttribute("user")==null) { response.sendRedirect("login.jsp"); return; } %>
            <!DOCTYPE html>
            <html>

            <head>
                <title>My Profile - SilverCare</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/FrontEnd/styles.css">
                <!-- Include same CSS/JS as before or simplified -->
            </head>

            <body>
                <jsp:include page="header.jsp" />

                <div class="container my-5">
                    <h1>My Profile</h1>

                    <c:if test="${param.update eq 'success'}">
                        <div class="alert alert-success">Profile Updated Successfully</div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/UserServlet?action=update" method="POST">
                        <div class="mb-4">
                            <label>Full Name</label>
                            <input type="text" name="full_name" value="${user.fullName}" class="form-control" required>
                        </div>
                        <div class="mb-4">
                            <label>Email</label>
                            <input type="email" value="${user.email}" class="form-control" readonly>
                        </div>
                        <div class="mb-4">
                            <label>Phone</label>
                            <input type="text" name="phone" value="${user.phone}" class="form-control">
                        </div>
                        <div class="mb-4">
                            <label>Gender</label>
                            <select name="gender" class="form-control">
                                <option value="">Select Gender</option>
                                <option value="Male" ${user.gender == 'Male' ? 'selected' : ''}>Male</option>
                                <option value="Female" ${user.gender == 'Female' ? 'selected' : ''}>Female</option>
                                <option value="Other" ${user.gender == 'Other' ? 'selected' : ''}>Other</option>
                                <option value="Prefer not to say" ${user.gender == 'Prefer not to say' ? 'selected' : ''}>Prefer not to say</option>
                            </select>
                        </div>
                        <div class="mb-4">
                            <label>Address</label>
                            <textarea name="address" class="form-control">${user.address}</textarea>
                        </div>

                        <button type="submit" class="btn btn-primary">Save Changes</button>
                    </form>
                </div>

                <jsp:include page="footer.jsp" />
            </body>

            </html>