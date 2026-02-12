<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Edit User - SilverCare Admin</title>

            <!-- Google Fonts -->
            <link
                href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&family=Inter:wght@400;500&display=swap"
                rel="stylesheet">

            <!-- Bootstrap 5 -->
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

            <!-- Bootstrap Icons -->
            <link rel="stylesheet"
                href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

            <!-- Global Styles -->
            <link rel="stylesheet" href="${pageContext.request.contextPath}/FrontEnd/styles.css">
            <style>
                /* Additional custom styles if needed */
            </style>
        </head>

        <body>
            <!-- Sidebar -->
            <c:set var="activePage" value="users" />
            <jsp:include page="adminSidebar.jsp" />

            <div class="content">
                <!-- Mobile Header -->
                <div class="mobile-header d-lg-none shadow-sm">
                    <button class="btn btn-primary me-3" id="toggleSidebar">
                        <i class="bi bi-list fs-4"></i>
                    </button>
                    <h4 class="mb-0">SilverCare Admin</h4>
                </div>

                <div class="page-header mb-4">
                    <h1><i class="bi bi-pencil-square text-primary me-2"></i>Edit User</h1>
                    <p class="text-muted">Modify existing user information and permissions.</p>
                </div>

                <c:choose>
                    <c:when test="${not empty userData}">
                        <div class="card shadow-sm border-0">
                            <div class="card-body p-4">
                                <form action="${pageContext.request.contextPath}/admin/save-user" method="POST">
                                    <input type="hidden" name="id" value="${userData.id}">

                                    <div class="row g-3">
                                        <div class="col-md-6 mb-3">
                                            <label for="fullName" class="form-label fw-bold">Full Name</label>
                                            <input type="text" class="form-control" id="fullName" name="fullName"
                                                value="${userData.fullName}" required>
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <label for="email" class="form-label fw-bold">Email Address</label>
                                            <input type="email" class="form-control" id="email" name="email"
                                                value="${userData.email}" required>
                                        </div>
                                    </div>

                                    <div class="row g-3">
                                        <div class="col-md-6 mb-3">
                                            <label for="phone" class="form-label fw-bold">Phone Number</label>
                                            <input type="tel" class="form-control" id="phone" name="phone"
                                                value="${userData.phone}">
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <label for="role" class="form-label fw-bold">Role</label>
                                            <select class="form-select" id="role" name="role" required>
                                                <option value="Customer" ${userData.role=='Customer' ? 'selected' : ''
                                                    }>Customer</option>
                                                <option value="Admin" ${userData.role=='Admin' ? 'selected' : '' }>
                                                    Administrator</option>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="mb-3">
                                        <label for="address" class="form-label fw-bold">Address</label>
                                        <textarea class="form-control" id="address" name="address"
                                            rows="2">${userData.address}</textarea>
                                    </div>

                                    <div class="mb-4">
                                        <label for="medicalInfo" class="form-label fw-bold">Medical Information</label>
                                        <textarea class="form-control" id="medicalInfo" name="medicalInfo"
                                            rows="3">${userData.medicalInfo}</textarea>
                                    </div>

                                    <div class="d-flex gap-2">
                                        <button type="submit" class="btn btn-primary px-4 shadow-sm">
                                            <i class="bi bi-check-lg me-1"></i> Update User
                                        </button>
                                        <a href="${pageContext.request.contextPath}/admin/users"
                                            class="btn btn-light px-4">Cancel</a>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-warning border-0 shadow-sm">
                            <i class="bi bi-exclamation-triangle me-2"></i> User data not found or invalid ID.
                            <div class="mt-2 text-end">
                                <a href="${pageContext.request.contextPath}/admin/users"
                                    class="btn btn-sm btn-outline-warning">Back to Users</a>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
            <script>
                document.addEventListener('DOMContentLoaded', function () {
                    const toggleBtn = document.getElementById('toggleSidebar');
                    const closeBtn = document.getElementById('closeSidebar');
                    const sidebar = document.getElementById('sidebar');
                    const overlay = document.getElementById('sidebarOverlay');

                    function toggleSidebar() {
                        sidebar.classList.toggle('show');
                        overlay.classList.toggle('show');
                        document.body.style.overflow = sidebar.classList.contains('show') ? 'hidden' : '';
                    }

                    if (toggleBtn) toggleBtn.addEventListener('click', toggleSidebar);
                    if (closeBtn) closeBtn.addEventListener('click', toggleSidebar);
                    if (overlay) overlay.addEventListener('click', toggleSidebar);
                });
            </script>
        </body>

        </html>