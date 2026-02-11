<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Activity Logs - SilverCare Admin</title>

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
                    }

                    .sidebar {
                        width: var(--sidebar-width);
                        background: linear-gradient(180deg, var(--primary-color) 0%, var(--secondary-color) 100%);
                        color: white;
                        position: fixed;
                        top: 0;
                        height: 100vh;
                        z-index: 1040;
                        box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
                    }

                    .nav-link {
                        color: rgba(255, 255, 255, 0.8);
                        padding: .875rem 1.5rem;
                        margin: .25rem .75rem;
                        border-radius: 8px;
                        font-weight: 500;
                        display: flex;
                        align-items: center;
                    }

                    .nav-link.active,
                    .nav-link:hover {
                        background-color: var(--accent-color);
                        color: white;
                    }

                    .content {
                        margin-left: var(--sidebar-width);
                        padding: 2rem;
                    }

                    .log-details {
                        max-width: 250px;
                        overflow: hidden;
                        text-overflow: ellipsis;
                        white-space: nowrap;
                    }

                    .badge-action {
                        min-width: 120px;
                    }
                </style>
            </head>

            <body>
                <!-- Sidebar -->
                <div class="sidebar d-flex flex-column">
                    <div class="p-4 border-bottom border-light border-opacity-10">
                        <a href="${pageContext.request.contextPath}/admin/dashboard"
                            class="text-white text-decoration-none h4 mb-0">
                            <i class="bi bi-heart-fill"></i> SilverCare
                        </a>
                    </div>
                    <ul class="nav nav-pills flex-column mt-3">
                        <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link"><i
                                    class="bi bi-grid me-2"></i>Dashboard</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/services" class="nav-link"><i
                                    class="bi bi-box-seam me-2"></i>Services</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/bookings" class="nav-link"><i
                                    class="bi bi-calendar-check me-2"></i>Bookings</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/logs" class="nav-link active"><i
                                    class="bi bi-journal-text me-2"></i>Activity Logs</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/users" class="nav-link"><i
                                    class="bi bi-people me-2"></i>Users</a></li>
                    </ul>
                </div>

                <div class="content">
                    <div class="page-header mb-4">
                        <h1><i class="bi bi-journal-text text-primary me-2"></i>Admin Activity Logs</h1>
                        <p class="text-muted">Audit trail of system interactions performed by administrators.</p>
                    </div>

                    <div class="card shadow-sm border-0">
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover align-middle mb-0">
                                    <thead class="bg-light">
                                        <tr>
                                            <th class="ps-4">Timestamp</th>
                                            <th>Administrator</th>
                                            <th>Action</th>
                                            <th>Details</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="log" items="${logs}">
                                            <tr>
                                                <td class="ps-4">
                                                    <fmt:formatDate value="${log.createdAt}"
                                                        pattern="dd MMM yyyy, HH:mm:ss" />
                                                </td>
                                                <td>
                                                    <div class="fw-bold">${log.adminName}</div>
                                                    <small class="text-muted">ID: #${log.adminId}</small>
                                                </td>
                                                <td>
                                                    <span class="badge rounded-pill badge-action 
                                            ${log.action.startsWith('Delete') ? 'bg-danger' : 
                                             (log.action.startsWith('Create') ? 'bg-success' : 'bg-primary')}">
                                                        ${log.action}
                                                    </span>
                                                </td>
                                                <td class="log-details" title="${log.details}">
                                                    ${log.details}
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty logs}">
                                            <tr>
                                                <td colspan="4" class="text-center py-5 text-muted">
                                                    <i class="bi bi-inbox fs-1 d-block mb-3"></i>
                                                    No activity logs found.
                                                </td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </body>

            </html>