<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

        <style>
            /* Sidebar Styles embedded here for portability */
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

            /* Mobile Header Styles */
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
                <li><a href="${pageContext.request.contextPath}/admin/dashboard"
                        class="nav-link ${activePage == 'dashboard' ? 'active' : ''}"><i
                            class="bi bi-grid"></i>Dashboard</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/services"
                        class="nav-link ${activePage == 'services' ? 'active' : ''}"><i
                            class="bi bi-box-seam"></i>Services</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/categories"
                        class="nav-link ${activePage == 'categories' ? 'active' : ''}"><i
                            class="bi bi-tags"></i>Categories</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/bookings"
                        class="nav-link ${activePage == 'bookings' ? 'active' : ''}"><i
                            class="bi bi-calendar-check"></i>Bookings</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/feedback"
                        class="nav-link ${activePage == 'feedback' ? 'active' : ''}"><i
                            class="bi bi-chat-left-text"></i>Feedback</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/logs"
                        class="nav-link ${activePage == 'logs' ? 'active' : ''}"><i
                            class="bi bi-journal-text"></i>Activity Logs</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/users"
                        class="nav-link ${activePage == 'users' ? 'active' : ''}"><i class="bi bi-people"></i>Users</a>
                </li>
            </ul>

            <div class="sidebar-footer">
                <a href="${pageContext.request.contextPath}/FrontEnd/help.jsp" class="logout-btn mb-2">
                    <i class="bi bi-book"></i> User Guide
                </a>
                <a href="${pageContext.request.contextPath}/UserServlet?action=logout" class="logout-btn"><i
                        class="bi bi-box-arrow-right"></i> Sign Out</a>
            </div>
        </div>

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