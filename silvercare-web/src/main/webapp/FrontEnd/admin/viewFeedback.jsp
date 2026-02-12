<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>View Feedback - SilverCare Admin</title>

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
                <c:set var="activePage" value="feedback" />
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
                        <h1>Customer Feedback</h1>
                        <p class="text-muted">Review feedback from customers and public inquiries.</p>
                    </div>

                    <div class="row g-4">
                        <!-- Registered Customer Feedback -->
                        <div class="col-12">
                            <div class="card shadow-sm border-0 mb-4">
                                <div class="card-header bg-white py-3 border-bottom-0">
                                    <h5 class="card-title mb-0"><i class="bi bi-people me-2"></i>Registered Customer
                                        Reviews</h5>
                                </div>
                                <div class="card-body p-0">
                                    <div class="table-responsive">
                                        <table class="table table-hover align-middle mb-0">
                                            <thead class="bg-light">
                                                <tr>
                                                    <th class="ps-4">ID</th>
                                                    <th>Service</th>
                                                    <th>Customer</th>
                                                    <th>Rating</th>
                                                    <th>Comment</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="f" items="${feedback}">
                                                    <tr>
                                                        <td class="ps-4">#${f.id}</td>
                                                        <td>
                                                            <div class="fw-bold">${f.serviceName}</div>
                                                            <small class="text-muted">ID: ${f.serviceId}</small>
                                                        </td>
                                                        <td class="fw-bold">${f.customerName}</td>
                                                        <td>
                                                            <div class="rating-stars mb-1">
                                                                <c:forEach begin="1" end="5" var="i">
                                                                    <i
                                                                        class="bi ${i <= f.rating ? 'bi-star-fill' : 'bi-star'}"></i>
                                                                </c:forEach>
                                                            </div>
                                                            <small class="text-muted">${f.rating}/5 stars</small>
                                                        </td>
                                                        <td>${f.comment}</td>
                                                    </tr>
                                                </c:forEach>
                                                <c:if test="${empty feedback}">
                                                    <tr>
                                                        <td colspan="5" class="text-center py-5 text-muted">
                                                            <i class="bi bi-chat-left-dots fs-1 d-block mb-3"></i>
                                                            No customer reviews yet.
                                                        </td>
                                                    </tr>
                                                </c:if>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Public Contact Messages -->
                        <div class="col-12">
                            <div class="card shadow-sm border-0">
                                <div class="card-header bg-white py-3 border-bottom-0">
                                    <h5 class="card-title mb-0"><i class="bi bi-envelope me-2"></i>Public Contact
                                        Messages</h5>
                                </div>
                                <div class="card-body p-0 scroll-panel">
                                    <div class="list-group list-group-flush">
                                        <c:forEach var="msg" items="${contactMessages}">
                                            <div class="list-group-item p-4">
                                                <div
                                                    class="d-flex w-100 justify-content-between align-items-start mb-3">
                                                    <div>
                                                        <h5 class="mb-1 fw-bold text-primary">${msg.subject}</h5>
                                                        <div class="d-flex flex-wrap gap-3 mt-2">
                                                            <span><i
                                                                    class="bi bi-person me-2 text-muted"></i><strong>${msg.fullName}</strong></span>
                                                            <span><i
                                                                    class="bi bi-envelope me-2 text-muted"></i>${msg.email}</span>
                                                            <c:if test="${not empty msg.phone}">
                                                                <span><i
                                                                        class="bi bi-telephone me-2 text-muted"></i>${msg.phone}</span>
                                                            </c:if>
                                                        </div>
                                                    </div>
                                                    <small class="badge bg-light text-dark shadow-sm">
                                                        <fmt:parseDate value="${msg.createdAt}"
                                                            pattern="yyyy-MM-dd'T'HH:mm:ss.SSSXXX" var="parsedDate" />
                                                        <fmt:formatDate value="${parsedDate}"
                                                            pattern="dd MMM yyyy, HH:mm" />
                                                    </small>
                                                </div>
                                                <div class="bg-light p-3 rounded border-start border-primary border-4">
                                                    <div style="white-space: pre-wrap;">${msg.message}</div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                        <c:if test="${empty contactMessages}">
                                            <div class="p-5 text-center text-muted">
                                                <i class="bi bi-envelope-x fs-1 d-block mb-3"></i>
                                                No contact messages received.
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
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