<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <jsp:include page="header.jsp" />

        <main class="container my-5">
            <div class="row justify-content-center">
                <div class="col-lg-10">
                    <div class="text-center mb-5">
                        <span
                            class="badge bg-primary-subtle text-primary border border-primary-subtle px-3 py-2 rounded-pill mb-3">User
                            Guide & Documentation</span>
                        <h1 class="display-4 fw-bold">Getting Started with SilverCare</h1>
                        <p class="lead text-muted">A comprehensive guide to navigating and using the platform's
                            features.</p>
                    </div>

                    <!-- Role Selector / Status -->
                    <div class="card border-0 shadow-sm mb-5 bg-light">
                        <div class="card-body p-4 d-flex align-items-center justify-content-between">
                            <div>
                                <h5 class="mb-1">Current Access Level</h5>
                                <p class="text-muted mb-0 small">Links marked with <i
                                        class="bi bi-lock-fill text-muted"></i> require specific permissions.</p>
                            </div>
                            <div class="text-end">
                                <c:choose>
                                    <c:when test="${not empty sessionScope.admin_id}">
                                        <span class="badge bg-danger fs-6 px-3 py-2"><i
                                                class="bi bi-shield-lock-fill me-1"></i> Administrator Access</span>
                                    </c:when>
                                    <c:when test="${not empty sessionScope.customer_id}">
                                        <span class="badge bg-success fs-6 px-3 py-2"><i
                                                class="bi bi-person-check-fill me-1"></i> Client Access</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary fs-6 px-3 py-2"><i
                                                class="bi bi-eye-fill me-1"></i> Guest Access</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>

                    <!-- Feature Sections -->
                    <div class="row g-4">
                        <!-- Guest Features -->
                        <div class="col-md-6">
                            <div class="card h-100 border-0 shadow-sm hover-up">
                                <div class="card-body p-4">
                                    <div class="d-flex align-items-center mb-3">
                                        <div class="bg-primary text-white p-3 rounded-3 me-3">
                                            <i class="bi bi-search fs-4"></i>
                                        </div>
                                        <h4 class="mb-0">Guest Features</h4>
                                    </div>
                                    <p class="text-muted">Explore our curated selection of care services without an
                                        account.</p>
                                    <ul class="list-group list-group-flush mt-3">
                                        <li class="list-group-item px-0 py-2 border-0">
                                            <a href="${pageContext.request.contextPath}/ServiceServlet?action=category"
                                                class="text-decoration-none d-flex justify-content-between align-items-center">
                                                <span>Browse All Services</span>
                                                <i class="bi bi-arrow-right-short fs-5 text-primary"></i>
                                            </a>
                                        </li>
                                        <li class="list-group-item px-0 py-2 border-0">
                                            <span class="d-flex justify-content-between align-items-center text-muted">
                                                <span>Real-time Service Search</span>
                                                <span class="badge bg-light text-muted border">Live</span>
                                            </span>
                                        </li>
                                        <li class="list-group-item px-0 py-2 border-0">
                                            <a href="${pageContext.request.contextPath}/FrontEnd/login.jsp"
                                                class="text-decoration-none d-flex justify-content-between align-items-center">
                                                <span>Create an Account</span>
                                                <i class="bi bi-arrow-right-short fs-5 text-primary"></i>
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>

                        <!-- Client Features -->
                        <div class="col-md-6">
                            <div class="card h-100 border-0 shadow-sm hover-up">
                                <div class="card-body p-4">
                                    <div class="d-flex align-items-center mb-3">
                                        <div class="bg-success text-white p-3 rounded-3 me-3">
                                            <i class="bi bi-person-fill fs-4"></i>
                                        </div>
                                        <h4 class="mb-0">Client Portal</h4>
                                    </div>
                                    <p class="text-muted">Manage your elderly care bookings, payments, and feedback.</p>
                                    <ul class="list-group list-group-flush mt-3">
                                        <li class="list-group-item px-0 py-2 border-0">
                                            <c:choose>
                                                <c:when test="${not empty sessionScope.customer_id}">
                                                    <a href="${pageContext.request.contextPath}/CartServlet?action=view"
                                                        class="text-decoration-none d-flex justify-content-between align-items-center">
                                                        <span>Manage Shopping Cart (CRUD)</span>
                                                        <i class="bi bi-cart-check fs-5 text-success"></i>
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <span
                                                        class="d-flex justify-content-between align-items-center text-muted opacity-75">
                                                        <span>Manage Shopping Cart</span>
                                                        <i class="bi bi-lock-fill small"></i>
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </li>
                                        <li class="list-group-item px-0 py-2 border-0">
                                            <c:choose>
                                                <c:when test="${not empty sessionScope.customer_id}">
                                                    <a href="${pageContext.request.contextPath}/BookingServlet?action=list"
                                                        class="text-decoration-none d-flex justify-content-between align-items-center">
                                                        <span>View My Bookings & History</span>
                                                        <i class="bi bi-calendar-range fs-5 text-success"></i>
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <span
                                                        class="d-flex justify-content-between align-items-center text-muted opacity-75">
                                                        <span>Booking History</span>
                                                        <i class="bi bi-lock-fill small"></i>
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </li>
                                        <li class="list-group-item px-0 py-2 border-0">
                                            <span class="d-flex justify-content-between align-items-center text-muted">
                                                <span>Submit Feedback & Ratings</span>
                                                <span class="badge bg-light text-success border">New</span>
                                            </span>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>

                        <!-- Admin Features -->
                        <div class="col-12 mt-4">
                            <div class="card border-0 shadow-sm bg-dark text-white p-2">
                                <div class="card-body p-4">
                                    <div class="row align-items-center">
                                        <div class="col-lg-7">
                                            <div class="d-flex align-items-center mb-3">
                                                <div class="bg-danger text-white p-3 rounded-3 me-3">
                                                    <i class="bi bi-speedometer2 fs-4"></i>
                                                </div>
                                                <h4 class="mb-0 text-white">System Administration</h4>
                                            </div>
                                            <p class="text-light opacity-75 mb-4">Enterprise-grade management tools for
                                                total system control and business intelligence.</p>

                                            <div class="row g-3">
                                                <div class="col-sm-6">
                                                    <div class="d-flex align-items-start mb-2">
                                                        <i class="bi bi-check-circle-fill text-danger me-2 mt-1"></i>
                                                        <span>BI Reports (Top Clients, Service Performance)</span>
                                                    </div>
                                                    <div class="d-flex align-items-start mb-2">
                                                        <i class="bi bi-check-circle-fill text-danger me-2 mt-1"></i>
                                                        <span>User Management & Area Filtering</span>
                                                    </div>
                                                </div>
                                                <div class="col-sm-6">
                                                    <div class="d-flex align-items-start mb-2">
                                                        <i class="bi bi-check-circle-fill text-danger me-2 mt-1"></i>
                                                        <span>Service Catalog & Category Management</span>
                                                    </div>
                                                    <div class="d-flex align-items-start mb-2">
                                                        <i class="bi bi-check-circle-fill text-danger me-2 mt-1"></i>
                                                        <span>Monthly Revenue & Booking Analytics</span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-lg-5 text-center text-lg-end mt-4 mt-lg-0">
                                            <c:choose>
                                                <c:when test="${not empty sessionScope.admin_id}">
                                                    <a href="${pageContext.request.contextPath}/admin/dashboard"
                                                        class="btn btn-danger btn-lg px-5 py-3 rounded-pill shadow">
                                                        <i class="bi bi-layout-text-sidebar-reverse me-2"></i> Enter
                                                        Admin Terminal
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <button
                                                        class="btn btn-outline-light btn-lg px-5 py-3 rounded-pill disabled opacity-50">
                                                        <i class="bi bi-lock-fill me-2"></i> Admin Terminal Locked
                                                    </button>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Additional Help / FAQ -->
                    <div class="mt-5 text-center">
                        <hr class="mb-5 opacity-10">
                        <h3 class="fw-bold mb-4">Frequently Asked Questions</h3>
                        <div class="row text-start g-4">
                            <div class="col-md-4">
                                <h6 class="fw-bold"><i class="bi bi-question-circle text-primary me-2"></i> How do I
                                    book a service?</h6>
                                <p class="text-muted small">Search for a service, add it to your cart, and proceed to
                                    checkout where you can select a date and time.</p>
                            </div>
                            <div class="col-md-4">
                                <h6 class="fw-bold"><i class="bi bi-question-circle text-primary me-2"></i> Is my data
                                    secure?</h6>
                                <p class="text-muted small">We use Stripe for payment processing and BCrypt for secure
                                    password hashing to protect your information.</p>
                            </div>
                            <div class="col-md-4">
                                <h6 class="fw-bold"><i class="bi bi-question-circle text-primary me-2"></i> Can I cancel
                                    a booking?</h6>
                                <p class="text-muted small">Yes, bookings can be cancelled through the 'My Bookings'
                                    section in your client dashboard.</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>

        <style>
            .hover-up {
                transition: transform 0.3s ease, box-shadow 0.3s ease;
            }

            .hover-up:hover {
                transform: translateY(-8px);
                box-shadow: 0 1rem 3rem rgba(0, 0, 0, .1) !important;
            }

            .bg-primary-subtle {
                background-color: #e7f0f7;
            }

            .list-group-item a {
                transition: padding-left 0.2s;
            }

            .list-group-item a:hover {
                padding-left: 5px;
            }
        </style>

        <jsp:include page="footer.jsp" />