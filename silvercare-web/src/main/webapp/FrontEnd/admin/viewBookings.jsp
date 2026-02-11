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
                    :root {
                        --sidebar-width: 280px;
                        --primary-color: #5b7c99;
                        --secondary-color: #2c3e50;
                        --accent-color: #f39c12;
                    }

                    body {
                        background-color: #f8f9fa;
                        overflow-x: hidden;
                    }

                    .sidebar {
                        width: var(--sidebar-width);
                        background: linear-gradient(180deg, var(--primary-color) 0%, var(--secondary-color) 100%);
                        color: white;
                        position: fixed;
                        left: calc(-1 * var(--sidebar-width));
                        top: 0;
                        height: 100vh;
                        z-index: 1040;
                        transition: all 0.3s ease;
                        box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
                    }

                    .sidebar.show {
                        left: 0;
                    }

                    .sidebar-brand {
                        padding: 1.5rem;
                        border-bottom: 1px solid rgba(255, 255, 255, 0.1);
                    }

                    .nav-link {
                        color: rgba(255, 255, 255, 0.8);
                        padding: .875rem 1.5rem;
                        margin: .25rem .75rem;
                        border-radius: 8px;
                        font-weight: 500;
                        display: flex;
                        align-items: center;
                        transition: .2s;
                    }

                    .nav-link i {
                        margin-right: .75rem;
                    }

                    .nav-link.active,
                    .nav-link:hover {
                        background-color: var(--accent-color);
                        color: white;
                    }

                    .sidebar-footer {
                        margin-top: auto;
                        padding: 1.5rem;
                        border-top: 1px solid rgba(255, 255, 255, 0.1);
                    }

                    .logout-btn {
                        display: flex;
                        padding: .75rem 1rem;
                        border-radius: 8px;
                        color: white;
                        text-decoration: none;
                        background-color: rgba(255, 255, 255, 0.1);
                        transition: .2s;
                    }

                    .logout-btn:hover {
                        background-color: rgba(220, 53, 69, .8);
                    }

                    .content {
                        padding: 1rem;
                        width: 100%;
                        transition: all 0.3s ease;
                    }

                    .mobile-header {
                        display: flex;
                        align-items: center;
                        padding: 1rem;
                        background-color: white;
                        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
                        margin-bottom: 1rem;
                        border-radius: 8px;
                    }

                    .sidebar-overlay {
                        display: none;
                        position: fixed;
                        top: 0;
                        left: 0;
                        width: 100vw;
                        height: 100vh;
                        background: rgba(0, 0, 0, 0.5);
                        z-index: 1030;
                    }

                    .sidebar-overlay.show {
                        display: block;
                    }

                    /* Desktop layout */
                    @media (min-width: 992px) {
                        .sidebar {
                            left: 0;
                        }

                        .content {
                            margin-left: var(--sidebar-width);
                            width: calc(100% - var(--sidebar-width));
                            padding: 2rem;
                        }

                        .mobile-header {
                            display: none;
                        }

                        .sidebar-overlay {
                            display: none !important;
                        }
                    }
                </style>
            </head>

            <body>

                <!-- Sidebar Overlay -->
                <div class="sidebar-overlay" id="sidebarOverlay"></div>

                <!-- Sidebar -->
                <div class="sidebar d-flex flex-column" id="sidebar">
                    <div class="sidebar-brand d-flex justify-content-between align-items-center">
                        <a href="${pageContext.request.contextPath}/admin/dashboard"
                            class="text-white text-decoration-none h4 mb-0">
                            <i class="bi bi-heart-fill"></i> SilverCare
                        </a>
                        <button class="btn btn-link text-white d-lg-none p-0" id="closeSidebar">
                            <i class="bi bi-x-lg fs-4"></i>
                        </button>
                    </div>

                    <ul class="nav nav-pills flex-column mt-3">
                        <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link"><i
                                    class="bi bi-grid"></i>Dashboard</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/services" class="nav-link"><i
                                    class="bi bi-box-seam"></i>Services</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/categories" class="nav-link"><i
                                    class="bi bi-tags"></i>Categories</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/bookings" class="nav-link active"><i
                                    class="bi bi-calendar-check"></i>Bookings</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/feedback" class="nav-link"><i
                                    class="bi bi-chat-left-text"></i>Feedback</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/logs" class="nav-link"><i
                                    class="bi bi-journal-text"></i>Activity Logs</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/users" class="nav-link"><i
                                    class="bi bi-people"></i>Users</a></li>
                    </ul>

                    <div class="sidebar-footer">
                        <a href="${pageContext.request.contextPath}/logout" class="logout-btn"><i
                                class="bi bi-box-arrow-right"></i> Sign Out</a>
                    </div>
                </div>

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
                                                    <fmt:parseDate value="${booking.bookingDate}"
                                                        pattern="yyyy-MM-dd'T'HH:mm:ss.SSSXXX" var="parsedDate" />
                                                    <div><i class="bi bi-calendar-event me-1"></i>
                                                        <fmt:formatDate value="${parsedDate}" pattern="dd MMM yyyy" />
                                                    </div>
                                                    <small class="text-muted"><i class="bi bi-clock me-1"></i>
                                                        <fmt:formatDate value="${parsedDate}" pattern="HH:mm a" />
                                                    </small>
                                                </td>
                                                <td class="fw-bold">$
                                                    <fmt:formatNumber value="${booking.totalAmount}"
                                                        pattern="#,##0.00" />
                                                </td>
                                                <td>
                                                    <span
                                                        class="badge rounded-pill 
                                        ${booking.status == 'Pending' ? 'bg-warning text-dark' : 
                                         (booking.status == 'Confirmed' ? 'bg-info text-white' :
                                         (booking.status == 'Completed' ? 'bg-success text-white' : 'bg-secondary text-white'))}">
                                                        ${booking.status}
                                                    </span>
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