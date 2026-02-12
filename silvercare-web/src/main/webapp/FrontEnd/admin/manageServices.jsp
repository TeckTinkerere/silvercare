<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>



            <!DOCTYPE html>
            <html lang=" en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Manage Services - SilverCare Admin</title>
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
                <c:set var="activePage" value="services" />
                <jsp:include page="adminSidebar.jsp" />

                <div class="content">
                    <!-- Mobile Header -->
                    <div class="mobile-header d-lg-none shadow-sm">
                        <button class="btn btn-primary me-3" id="toggleSidebar">
                            <i class="bi bi-list fs-4"></i>
                        </button>
                        <h4 class="mb-0">SilverCare Admin</h4>
                    </div>

                    <div
                        class="d-flex flex-column flex-sm-row justify-content-between align-items-sm-center mb-4 gap-3">
                        <h1>Manage Services</h1>
                        <a href="${pageContext.request.contextPath}/admin/add-service"
                            class="btn btn-primary shadow-sm"><i class="bi bi-plus-circle"></i> New Service</a>
                    </div>

                    <c:if test="${not empty success}">
                        <div class="alert alert-success shadow-sm border-0">${success == 'added' ? 'Service added
                            successfully!' : (success
                            == 'updated' ? 'Service updated successfully!' : 'Service deleted successfully!')}</div>
                    </c:if>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger shadow-sm border-0">${error}</div>
                    </c:if>

                    <div class="card shadow-sm border-0">
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover align-middle mb-0">
                                    <thead class="bg-light">
                                        <tr>
                                            <th class="ps-4">ID</th>
                                            <th>Name</th>
                                            <th>Category</th>
                                            <th>Price</th>
                                            <th class="text-end pe-4">Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="service" items="${services}">
                                            <tr>
                                                <td class="ps-4">${service.id}</td>
                                                <td class="fw-bold">${service.name}</td>
                                                <td><span
                                                        class="badge bg-light text-dark border">${service.categoryName}</span>
                                                </td>
                                                <td>$
                                                    <fmt:formatNumber value="${service.price}" pattern="#,##0.00" />
                                                </td>
                                                <td class="text-end pe-4">
                                                    <div class="d-flex justify-content-end gap-2">
                                                        <a href="${pageContext.request.contextPath}/admin/edit-service?id=${service.id}"
                                                            class="btn btn-sm btn-outline-primary shadow-sm"><i
                                                                class="bi bi-pencil"></i> <span
                                                                class="d-none d-md-inline">Edit</span></a>
                                                        <form
                                                            action="${pageContext.request.contextPath}/admin/delete-service"
                                                            method="POST" style="display:inline;"
                                                            onsubmit="return confirm('Are you sure you want to delete this service? This cannot be undone.')">
                                                            <input type="hidden" name="id" value="${service.id}">
                                                            <button type="submit"
                                                                class="btn btn-sm btn-outline-danger shadow-sm"><i
                                                                    class="bi bi-trash"></i> <span
                                                                    class="d-none d-md-inline">Delete</span></button>
                                                        </form>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty services}">
                                            <tr>
                                                <td colspan="4" class="text-center py-5 text-muted">
                                                    <i class="bi bi-inbox fs-1 d-block mb-3"></i>
                                                    No services found.
                                                </td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
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