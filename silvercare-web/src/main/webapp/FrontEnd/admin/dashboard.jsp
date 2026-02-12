<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <title>Dashboard - SilverCare Admin</title>

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
                /* Additional custom styles for dashboard if needed */
            </style>
        </head>

        <body>
            <!-- Sidebar -->
            <c:set var="activePage" value="dashboard" />
            <jsp:include page="adminSidebar.jsp" />

            <!-- Main Content -->
            <div class="content">
                <!-- Mobile Header -->
                <div class="mobile-header d-lg-none shadow-sm">
                    <button class="btn btn-primary me-3" id="toggleSidebar">
                        <i class="bi bi-list fs-4"></i>
                    </button>
                    <h4 class="mb-0">SilverCare Admin</h4>
                </div>

                <div class="page-header">
                    <h1 class="mb-3">Dashboard Overview</h1>
                    <p class="text-muted">Summary of system activity.</p>

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">${error}</div>
                    </c:if>

                    <div class="row g-4 mt-2">
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="card stat-card text-white bg-primary shadow-sm h-100">
                                <div class="card-body d-flex flex-column justify-content-center">
                                    <h5 class="card-title opacity-75">Total Services</h5>
                                    <p class="display-5 fw-bold mb-0">${empty serviceCount ? 0 : serviceCount}</p>
                                </div>
                            </div>
                        </div>

                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="card stat-card text-white bg-success shadow-sm h-100">
                                <div class="card-body d-flex flex-column justify-content-center">
                                    <h5 class="card-title opacity-75">Total Bookings</h5>
                                    <p class="display-5 fw-bold mb-0">${empty bookingCount ? 0 : bookingCount}</p>
                                </div>
                            </div>
                        </div>

                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="card stat-card text-white bg-warning shadow-sm h-100">
                                <div class="card-body d-flex flex-column justify-content-center">
                                    <h5 class="card-title opacity-75">Total Revenue</h5>
                                    <p class="display-5 fw-bold mb-0">$${empty revenue ? '0.00' : revenue}</p>
                                </div>
                            </div>
                        </div>

                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="card stat-card text-white bg-info shadow-sm h-100">
                                <div class="card-body d-flex flex-column justify-content-center">
                                    <h5 class="card-title opacity-75">Total Clients</h5>
                                    <p class="display-5 fw-bold mb-0">${empty clientCount ? 0 : clientCount}</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Export Center -->
                    <div class="row mt-4">
                        <div class="col-12">
                            <div class="card shadow-sm border-0 bg-light">
                                <div class="card-body d-flex justify-content-between align-items-center">
                                    <div>
                                        <h5 class="mb-1"><i
                                                class="bi bi-file-earmark-arrow-down text-success me-2"></i>Business
                                            Data Export Center</h5>
                                        <p class="text-muted mb-0">Generate and download comprehensive CSV reports for
                                            offline analysis.</p>
                                    </div>
                                    <div class="d-flex gap-2">
                                        <a href="${pageContext.request.contextPath}/admin/export-bookings"
                                            class="btn btn-outline-success">
                                            <i class="bi bi-calendar-check me-2"></i>Export Bookings
                                        </a>
                                        <a href="${pageContext.request.contextPath}/admin/export-users"
                                            class="btn btn-outline-primary">
                                            <i class="bi bi-people me-2"></i>Export Users
                                        </a>
                                        <a href="${pageContext.request.contextPath}/admin/export-feedback"
                                            class="btn btn-outline-info">
                                            <i class="bi bi-chat-left-text me-2"></i>Export Feedback
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Top Clients Report -->
                    <div class="row mt-4">
                        <div class="col-12 col-lg-6">
                            <div class="card shadow-sm">
                                <div class="card-header bg-primary text-white">
                                    <h5 class="mb-0"><i class="bi bi-trophy me-2"></i>Top Clients (by Value)</h5>
                                </div>
                                <div class="card-body p-0">
                                    <c:choose>
                                        <c:when test="${not empty topClients}">
                                            <div class="table-responsive">
                                                <table class="table table-hover mb-0">
                                                    <thead class="table-light">
                                                        <tr>
                                                            <th>#</th>
                                                            <th>Client</th>
                                                            <th>Area</th>
                                                            <th>Bookings</th>
                                                            <th>Total Spent</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach var="client" items="${topClients}" varStatus="loop">
                                                            <tr>
                                                                <td>${loop.index + 1}</td>
                                                                <td>
                                                                    <strong>${client.fullName}</strong><br>
                                                                    <small class="text-muted">${client.email}</small>
                                                                </td>
                                                                <td>${client.address}</td>
                                                                <td>${client.bookingCount}</td>
                                                                <td class="fw-bold text-success">$${client.totalSpent}
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <p class="text-muted p-3">No booking data available yet.</p>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>

                        <!-- Service Performance Report -->
                        <div class="col-12 col-lg-6">
                            <div class="card shadow-sm">
                                <div class="card-header bg-success text-white">
                                    <h5 class="mb-0"><i class="bi bi-star me-2"></i>Service Performance (Ratings)</h5>
                                </div>
                                <div class="card-body p-0">
                                    <c:choose>
                                        <c:when test="${not empty serviceRatings}">
                                            <div class="table-responsive">
                                                <table class="table table-hover mb-0">
                                                    <thead class="table-light">
                                                        <tr>
                                                            <th>Service</th>
                                                            <th>Avg Rating</th>
                                                            <th>Reviews</th>
                                                            <th>Status</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach var="sr" items="${serviceRatings}" varStatus="loop">
                                                            <tr>
                                                                <td><strong>${sr.serviceName}</strong></td>
                                                                <td>
                                                                    <span class="badge bg-warning text-dark fs-6">
                                                                        <i class="bi bi-star-fill"></i> ${sr.avgRating}
                                                                    </span>
                                                                </td>
                                                                <td>${sr.feedbackCount} reviews</td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${sr.avgRating >= 4}">
                                                                            <span class="badge bg-success">Top
                                                                                Rated</span>
                                                                        </c:when>
                                                                        <c:when test="${sr.avgRating >= 3}">
                                                                            <span class="badge bg-info">Average</span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="badge bg-danger">Needs
                                                                                Improvement</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <p class="text-muted p-3">No feedback data available yet.</p>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>


            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>