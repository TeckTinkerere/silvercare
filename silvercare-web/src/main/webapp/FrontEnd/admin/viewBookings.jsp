<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>View Bookings - SilverCare Admin</title>

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
                <c:set var="activePage" value="bookings" />
                <jsp:include page="adminSidebar.jsp" />

                <div class="content">
                    <!-- Mobile Header -->
                    <div class="mobile-header d-lg-none shadow-sm">
                        <button class="btn btn-primary me-3" id="toggleSidebar">
                            <i class="bi bi-list fs-4"></i>
                        </button>
                        <h4 class="mb-0">SilverCare Admin</h4>
                    </div>

                    <div class="page-header mb-4 d-flex justify-content-between align-items-center">
                        <div>
                            <h1><i class="bi bi-calendar-check text-primary me-2"></i>Bookings</h1>
                            <p class="text-muted">Manage care service appointments.</p>
                        </div>
                        <a href="${pageContext.request.contextPath}/admin/export-bookings"
                            class="btn btn-success shadow-sm">
                            <i class="bi bi-download me-2"></i>Download CSV
                        </a>
                    </div>

                    <div class="card shadow-sm border-0">
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover align-middle mb-0">
                                    <thead class="bg-light">
                                        <tr>
                                            <th class="ps-4">ID</th>
                                            <th>Customer</th>
                                            <th>Date & Time</th>
                                            <th>Total</th>
                                            <th>Status</th>
                                            <th class="text-end pe-4">Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="booking" items="${bookings}">
                                            <tr>
                                                <td class="ps-4">#${booking.id}</td>
                                                <td>
                                                    <div class="fw-bold">${booking.customerName}</div>
                                                    <small
                                                        class="text-muted d-none d-sm-block">${booking.customerEmail}</small>
                                                </td>
                                                <td>
                                                    <c:catch var="dateError">
                                                        <fmt:parseDate value="${booking.bookingDate}"
                                                            pattern="yyyy-MM-dd'T'HH:mm:ss.SSSXXX" var="parsedDate" />
                                                    </c:catch>
                                                    <c:if test="${empty dateError && not empty parsedDate}">
                                                        <div><i class="bi bi-calendar-event me-1"></i>
                                                            <fmt:formatDate value="${parsedDate}"
                                                                pattern="dd MMM yyyy" />
                                                        </div>
                                                        <small class="text-muted"><i class="bi bi-clock me-1"></i>
                                                            <fmt:formatDate value="${parsedDate}" pattern="HH:mm a" />
                                                        </small>
                                                    </c:if>
                                                    <c:if test="${not empty dateError || empty parsedDate}">
                                                        <span class="text-muted small">${booking.bookingDate != null ?
                                                            booking.bookingDate.split(' ')[0] : 'N/A'}</span>
                                                    </c:if>
                                                </td>
                                                <td class="fw-bold">$
                                                    <fmt:formatNumber value="${booking.totalAmount}"
                                                        pattern="#,##0.00" />
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${booking.status == 'Confirmed'}">
                                                            <span class="badge rounded-pill bg-primary">Confirmed</span>
                                                        </c:when>
                                                        <c:when test="${booking.status == 'Pending'}">
                                                            <span
                                                                class="badge rounded-pill bg-warning text-dark">Pending</span>
                                                        </c:when>
                                                        <c:when test="${booking.status == 'Cancelled'}">
                                                            <span class="badge rounded-pill bg-danger">Cancelled</span>
                                                        </c:when>
                                                        <c:when test="${booking.status == 'Completed'}">
                                                            <span class="badge rounded-pill bg-success">Completed</span>
                                                        </c:when>
                                                        <c:when test="${booking.status == 'Refunded'}">
                                                            <span
                                                                class="badge rounded-pill bg-info text-dark">Refunded</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span
                                                                class="badge rounded-pill bg-secondary">${booking.status}</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="text-end pe-4">
                                                    <a href="${pageContext.request.contextPath}/admin/booking-details?id=${booking.id}"
                                                        class="btn btn-sm btn-outline-primary shadow-sm">
                                                        <i class="bi bi-eye"></i> <span
                                                            class="d-none d-sm-inline">Details</span>
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty bookings}">
                                            <tr>
                                                <td colspan="6" class="text-center py-5 text-muted">
                                                    <i class="bi bi-inbox fs-1 d-block mb-3"></i>
                                                    No bookings found.
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
            </body>

            </html>