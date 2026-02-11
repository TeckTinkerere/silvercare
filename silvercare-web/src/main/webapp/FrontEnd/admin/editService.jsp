<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Edit Service - SilverCare Admin</title>

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
                    <li><a href="${pageContext.request.contextPath}/admin/services" class="nav-link active"><i
                                class="bi bi-box-seam"></i>Services</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/categories" class="nav-link"><i
                                class="bi bi-tags"></i>Categories</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/bookings" class="nav-link"><i
                                class="bi bi-calendar-check"></i>Bookings</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/feedback" class="nav-link"><i
                                class="bi bi-chat-left-text"></i>Feedback</a></li>
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

                <div class="page-header mb-4">
                    <h1><i class="bi bi-pencil-square text-primary me-2"></i>Edit Service</h1>
                    <p class="text-muted">Modify existing service details and configuration.</p>
                </div>

                <c:choose>
                    <c:when test="${not empty service}">
                        <div class="card shadow-sm border-0">
                            <div class="card-body p-4">
                                <form action="${pageContext.request.contextPath}/admin/save-service" method="POST">
                                    <input type="hidden" name="id" value="${service.id}">

                                    <div class="row g-3">
                                        <div class="col-md-6 mb-3">
                                            <label for="service_name" class="form-label fw-bold">Service Name</label>
                                            <input type="text" class="form-control" id="service_name"
                                                name="service_name" value="${service.name}" required>
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <label for="category_id" class="form-label fw-bold">Service Category</label>
                                            <select class="form-select" id="category_id" name="category_id" required>
                                                <c:forEach var="cat" items="${categories}">
                                                    <option value="${cat.id}" ${service.categoryId==cat.id ? 'selected'
                                                        : '' }>${cat.name}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="mb-3">
                                        <label for="description" class="form-label fw-bold">Service Description</label>
                                        <textarea class="form-control" id="description" name="description" rows="4"
                                            required>${service.description}</textarea>
                                    </div>

                                    <div class="row g-3">
                                        <div class="col-md-6 mb-3">
                                            <label for="price" class="form-label fw-bold">Price ($)</label>
                                            <div class="input-group">
                                                <span class="input-group-text">$</span>
                                                <input type="number" step="0.01" class="form-control" id="price"
                                                    name="price" value="${service.price}" required>
                                            </div>
                                        </div>
                                        <div class="col-md-6 mb-4">
                                            <label for="image_url" class="form-label fw-bold">Image Path</label>
                                            <div class="input-group">
                                                <span class="input-group-text"><i class="bi bi-image"></i></span>
                                                <input type="text" class="form-control" id="image_url" name="image_url"
                                                    value="${service.imagePath}">
                                            </div>
                                        </div>
                                    </div>

                                    <div class="d-flex gap-2">
                                        <button type="submit" class="btn btn-primary px-4 shadow-sm">
                                            <i class="bi bi-check-lg me-1"></i> Update Service
                                        </button>
                                        <a href="${pageContext.request.contextPath}/admin/services"
                                            class="btn btn-light px-4">Cancel</a>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-warning border-0 shadow-sm">
                            <i class="bi bi-exclamation-triangle me-2"></i> Service data not found or invalid ID.
                            <div class="mt-2 text-end">
                                <a href="${pageContext.request.contextPath}/admin/services"
                                    class="btn btn-sm btn-outline-warning">Back to Services</a>
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