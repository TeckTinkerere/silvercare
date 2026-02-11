<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="SilverCare - Premium Senior Care Services for your loved ones">
        <meta name="keywords" content="senior care, elderly care, home care, assisted living">
        <title>SilverCare - Senior Care Services</title>

        <!-- Favicon -->
        <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/FrontEnd/images/favicon.png">

        <!-- Google Fonts -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link
            href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&family=Inter:wght@400;500&display=swap"
            rel="stylesheet">

        <!-- Bootstrap 5.3 CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

        <!-- Custom Stylesheet -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/FrontEnd/styles.css">

        <!-- Enhanced Header Styles -->
        <style>
            /* Brand Icon Wrapper with Pulse Animation */
            .brand-icon-wrapper {
                display: inline-flex;
                align-items: center;
                justify-content: center;
                width: 40px;
                height: 40px;
                background: rgba(255, 255, 255, 0.15);
                border-radius: 50%;
                transition: all var(--transition-medium);
            }

            .navbar-brand:hover .brand-icon-wrapper {
                background: rgba(255, 255, 255, 0.25);
                transform: scale(1.1);
            }

            .brand-icon-wrapper i {
                font-size: 1.25rem;
            }

            .brand-text {
                font-size: 1.5rem;
                position: relative;
            }

            /* User Avatar Styling */
            .user-avatar {
                display: inline-flex;
                align-items: center;
                justify-content: center;
                width: 32px;
                height: 32px;
                background: rgba(255, 255, 255, 0.2);
                border-radius: 50%;
                transition: all var(--transition-fast);
                overflow: hidden;
            }

            .user-dropdown:hover .user-avatar {
                background: rgba(255, 255, 255, 0.3);
                transform: scale(1.05);
            }

            .user-avatar i {
                font-size: 1.25rem;
            }

            /* Profile Picture Image Styling */
            .profile-picture-img {
                width: 100%;
                height: 100%;
                object-fit: cover;
                border-radius: 50%;
            }

            /* Dropdown Profile Picture Styling */
            .dropdown-profile-picture {
                width: 40px;
                height: 40px;
                object-fit: cover;
                border-radius: 50%;
                border: 2px solid var(--primary-color);
            }

            /* Admin Badge Styling */
            .admin-badge {
                display: inline-flex;
                align-items: center;
                justify-content: center;
                width: 32px;
                height: 32px;
                background: rgba(255, 215, 0, 0.2);
                border-radius: 50%;
                transition: all var(--transition-fast);
            }

            .admin-dropdown:hover .admin-badge {
                background: rgba(255, 215, 0, 0.3);
                transform: scale(1.05);
            }

            .admin-badge i {
                font-size: 1.25rem;
                color: #ffd700;
            }

            /* Enhanced Dropdown Menu */
            .dropdown-menu {
                min-width: 250px;
                border-radius: var(--border-radius-lg);
                padding: 0.5rem 0;
                animation: slideDown 0.3s ease-out;
            }

            .dropdown-header {
                background: linear-gradient(135deg, rgba(91, 124, 153, 0.05) 0%, rgba(77, 182, 172, 0.05) 100%);
                border-radius: var(--border-radius);
                margin: 0.25rem 0.5rem;
            }

            .dropdown-item {
                transition: all var(--transition-fast);
                border-radius: var(--border-radius);
                margin: 0.25rem 0.5rem;
                padding: 0.6rem 1rem;
            }

            .dropdown-item:hover {
                background: linear-gradient(135deg, rgba(91, 124, 153, 0.1) 0%, rgba(77, 182, 172, 0.1) 100%);
                transform: translateX(5px);
            }

            /* Active Dropdown Item Styling */
            .dropdown-item.active-item {
                background: linear-gradient(135deg, rgba(91, 124, 153, 0.15) 0%, rgba(77, 182, 172, 0.15) 100%);
                font-weight: 600;
                border-left: 3px solid var(--accent-color);
            }

            .dropdown-item.active-item:hover {
                background: linear-gradient(135deg, rgba(91, 124, 153, 0.2) 0%, rgba(77, 182, 172, 0.2) 100%);
            }

            .dropdown-item.text-danger:hover {
                background: rgba(220, 53, 69, 0.1);
                color: #dc3545 !important;
            }

            .dropdown-item i {
                width: 20px;
                display: inline-block;
            }

            /* Cart Badge Positioning */
            .nav-link .badge {
                font-size: 0.65rem;
                padding: 0.25em 0.5em;
            }

            /* Register Button Enhancement */
            .register-btn {
                font-weight: 600;
                padding: 0.5rem 1.25rem;
                border-radius: var(--border-radius);
                transition: all var(--transition-fast);
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            }

            .register-btn:hover {
                transform: translateY(-2px);
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
            }

            /* Mobile Navigation Enhancement */
            @media (max-width: 991px) {
                .navbar-collapse {
                    background: rgba(0, 0, 0, 0.05);
                    border-radius: var(--border-radius);
                    padding: 1rem;
                    margin-top: 1rem;
                    backdrop-filter: blur(10px);
                }

                .nav-item {
                    margin-bottom: 0.25rem;
                }

                .dropdown-menu {
                    background: rgba(255, 255, 255, 0.98);
                    backdrop-filter: blur(10px);
                }

                .user-name,
                .admin-name {
                    display: inline !important;
                }

                .register-btn {
                    width: 100%;
                    margin-top: 0.5rem;
                }
            }

            /* Dropdown Toggle Arrow Animation */
            .dropdown-toggle::after {
                transition: transform var(--transition-fast);
                margin-left: 0.5rem;
            }

            .dropdown.show .dropdown-toggle::after {
                transform: rotate(180deg);
            }

            /* Nav Link Active State Enhancement */
            .nav-link.active {
                position: relative;
            }

            .nav-link.active::after {
                content: '';
                position: absolute;
                bottom: -8px;
                left: 50%;
                transform: translateX(-50%);
                width: 6px;
                height: 6px;
                background: var(--accent-color);
                border-radius: 50%;
                animation: pulse 2s ease-in-out infinite;
            }

            @keyframes pulse {

                0%,
                100% {
                    box-shadow: 0 0 0 0 rgba(77, 182, 172, 0.7);
                }

                50% {
                    box-shadow: 0 0 0 6px rgba(77, 182, 172, 0);
                }
            }

            /* Mobile Nav Link Active State */
            @media (max-width: 991px) {
                .nav-link.active::after {
                    display: none;
                }

                .nav-link.active {
                    background: rgba(255, 255, 255, 0.15);
                    border-left: 3px solid var(--accent-color);
                }
            }

            /* Header Scroll Effect */
            header.scrolled {
                box-shadow: var(--shadow-lg);
            }

            /* Active Dropdown Indicator */
            .active-dropdown>.nav-link {
                position: relative;
            }

            @media (min-width: 992px) {
                .active-dropdown>.nav-link::after {
                    content: '';
                    position: absolute;
                    bottom: -8px;
                    left: 50%;
                    transform: translateX(-50%);
                    width: 6px;
                    height: 6px;
                    background: var(--accent-color);
                    border-radius: 50%;
                    animation: pulse 2s ease-in-out infinite;
                }
            }
        </style>
    </head>

    <body>

        <!-- Enhanced Header with Gradient Background -->
        <header class="bg-primary shadow-sm">
            <nav class="navbar navbar-expand-lg navbar-dark">
                <div class="container">
                    <!-- Brand Logo with Enhanced Animation -->
                    <a class="navbar-brand d-flex align-items-center" href="${pageContext.request.contextPath}/home">
                        <div class="brand-icon-wrapper me-2">
                            <i class="fas fa-heart"></i>
                        </div>
                        <strong class="brand-text">SilverCare</strong>
                    </a>

                    <!-- Mobile Toggle Button with Enhanced Styling -->
                    <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse"
                        data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false"
                        aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon"></span>
                    </button>

                    <!-- Navigation Menu -->
                    <div class="collapse navbar-collapse" id="navbarNav">
                        <ul class="navbar-nav ms-auto align-items-lg-center">
                            <% String uri=request.getRequestURI(); boolean isHome=uri.contains("/home"); boolean
                                isAbout=uri.contains("/about"); boolean isService=uri.contains("/ServiceServlet");
                                boolean isContact=uri.contains("/contact"); boolean isCart=uri.contains("/CartServlet");
                                boolean isBookings=uri.contains("/bookings"); boolean isLogin=uri.contains("/login");
                                boolean isProfile=uri.contains("profile.jsp"); /* CSS Class variables for robust HTML
                                integration */ String homeCls=isHome ? "active" : "" ; String aboutCls=isAbout
                                ? "active" : "" ; String serviceCls=isService ? "active" : "" ; String
                                contactCls=isContact ? "active" : "" ; String cartCls=isCart ? "active" : "" ; String
                                bookingsCls=isBookings ? "active" : "" ; String loginCls=isLogin ? "active" : "" ;
                                String profileCls=isProfile ? "active" : "" ; String profileDCls=isProfile
                                ? "active-dropdown" : "" ; String profileICls=isProfile ? "active-item" : "" ; String
                                bookingsICls=isBookings ? "active-item" : "" ; String cartICls=isCart ? "active-item"
                                : "" ; /* User Session data */ Object cIdAttr=session.getAttribute("customer_id");
                                Integer customerId=(cIdAttr instanceof Number) ? ((Number)cIdAttr).intValue() : null;
                                String customerName=(String) session.getAttribute("customer_name"); String
                                profilePicture=(String) session.getAttribute("profile_picture"); Object
                                aIdAttr=session.getAttribute("admin_id"); Integer adminId=(aIdAttr instanceof Number) ?
                                ((Number)aIdAttr).intValue() : null; Object
                                cCountAttr=session.getAttribute("cart_count"); Integer cartCount=(cCountAttr instanceof
                                Number) ? ((Number)cCountAttr).intValue() : 0; if (profilePicture==null ||
                                profilePicture.trim().isEmpty()) { profilePicture="default-avatar.png" ; } %>

                                <!-- Home Link -->
                                <li class="nav-item">
                                    <a class="nav-link <%= homeCls %>" href="${pageContext.request.contextPath}/home">
                                        <i class="fas fa-home me-1"></i>
                                        <span class="nav-text">Home</span>
                                    </a>
                                </li>

                                <!-- About Us Link -->
                                <li class="nav-item">
                                    <a class="nav-link <%= aboutCls %>" href="${pageContext.request.contextPath}/about">
                                        <i class="fas fa-info-circle me-1"></i>
                                        <span class="nav-text">About</span>
                                    </a>
                                </li>

                                <!-- Services Link -->
                                <li class="nav-item">
                                    <a class="nav-link <%= serviceCls %>"
                                        href="${pageContext.request.contextPath}/ServiceServlet?action=category">
                                        <i class="fas fa-hands-helping me-1"></i>
                                        <span class="nav-text">Services</span>
                                    </a>
                                </li>

                                <%-- Contact Link - Only show when user is NOT logged in --%>
                                    <% if (customerId==null && adminId==null) { %>
                                        <li class="nav-item">
                                            <a class="nav-link <%= contactCls %>"
                                                href="${pageContext.request.contextPath}/contact">
                                                <i class="fas fa-address-book me-1"></i>
                                                <span class="nav-text">Contact</span>
                                            </a>
                                        </li>
                                        <% } %>

                                            <% if (customerId !=null) { /* Customer is logged in - Show enhanced
                                                dropdown */ %>
                                                <!-- Cart Link with Badge (Quick Access) -->
                                                <li class="nav-item">
                                                    <a class="nav-link position-relative <%= cartCls %>"
                                                        href="${pageContext.request.contextPath}/CartServlet?action=view"
                                                        title="My Cart">
                                                        <i class="fas fa-shopping-cart"></i>
                                                        <% if (cartCount !=null && cartCount> 0) { %>
                                                            <span
                                                                class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger"
                                                                style="font-size: 0.65rem; padding: 0.25em 0.5em;">
                                                                <%= cartCount %>
                                                                    <span class="visually-hidden">items in cart</span>
                                                            </span>
                                                            <% } %>
                                                    </a>
                                                </li>

                                                <!-- User Account Dropdown with Enhanced Styling -->
                                                <li class="nav-item dropdown <%= profileDCls %>">
                                                    <a class="nav-link dropdown-toggle user-dropdown <%= profileCls %>"
                                                        href="#" role="button" data-bs-toggle="dropdown"
                                                        aria-expanded="false">
                                                        <div class="d-flex align-items-center">
                                                            <div class="user-avatar me-2">
                                                                <img src="${pageContext.request.contextPath}/FrontEnd/images/<%= profilePicture %>"
                                                                    alt="Profile Picture" class="profile-picture-img"
                                                                    onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/FrontEnd/images/default-avatar.png';">
                                                            </div>
                                                            <span class="user-name d-none d-lg-inline">
                                                                <%= customerName !=null ? customerName : "Account" %>
                                                            </span>
                                                        </div>
                                                    </a>
                                                    <ul class="dropdown-menu dropdown-menu-end shadow-lg border-0 mt-2">
                                                        <li class="dropdown-header px-3 py-2">
                                                            <div class="d-flex align-items-center">
                                                                <img src="${pageContext.request.contextPath}/FrontEnd/images/<%= profilePicture %>"
                                                                    alt="Profile Picture"
                                                                    class="dropdown-profile-picture me-2"
                                                                    onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/FrontEnd/images/default-avatar.png';">
                                                                <div>
                                                                    <div class="fw-semibold">
                                                                        <%= customerName !=null ? customerName : "User"
                                                                            %>
                                                                    </div>
                                                                    <small class="text-muted">Customer Account</small>
                                                                </div>
                                                            </div>
                                                        </li>
                                                        <li>
                                                            <hr class="dropdown-divider my-2">
                                                        </li>
                                                        <li>
                                                            <a class="dropdown-item py-2 <%= bookingsICls %>"
                                                                href="${pageContext.request.contextPath}/bookings">
                                                                <i class="fas fa-calendar-check me-2 text-primary"></i>
                                                                My Bookings
                                                            </a>
                                                        </li>
                                                        <li>
                                                            <a class="dropdown-item py-2 <%= cartICls %>"
                                                                href="${pageContext.request.contextPath}/CartServlet?action=view">
                                                                <i class="fas fa-shopping-cart me-2 text-primary"></i>
                                                                My Cart
                                                                <% if (cartCount !=null && cartCount> 0) { %>
                                                                    <span class="badge bg-primary ms-1">
                                                                        <%= cartCount %>
                                                                    </span>
                                                                    <% } %>
                                                            </a>
                                                        </li>
                                                        <li>
                                                            <a class="dropdown-item py-2 <%= profileICls %>"
                                                                href="${pageContext.request.contextPath}/profile">
                                                                <i class="fas fa-user-edit me-2 text-primary"></i>
                                                                Edit Profile
                                                            </a>
                                                        </li>
                                                        <li>
                                                            <hr class="dropdown-divider my-2">
                                                        </li>
                                                        <li>
                                                            <a class="dropdown-item py-2 text-danger"
                                                                href="${pageContext.request.contextPath}/UserServlet?action=logout"
                                                                onclick="return confirm('Are you sure you want to logout?');">
                                                                <i class="fas fa-sign-out-alt me-2"></i>
                                                                Logout
                                                            </a>
                                                        </li>
                                                    </ul>
                                                </li>
                                                <% } else if (adminId !=null) { /* Admin is logged in - Show enhanced
                                                    admin dropdown */ %>
                                                    <!-- Admin Account Dropdown with Enhanced Styling -->
                                                    <li class="nav-item dropdown">
                                                        <a class="nav-link dropdown-toggle admin-dropdown" href="#"
                                                            role="button" data-bs-toggle="dropdown"
                                                            aria-expanded="false">
                                                            <div class="d-flex align-items-center">
                                                                <div class="admin-badge me-2">
                                                                    <i class="fas fa-user-shield"></i>
                                                                </div>
                                                                <span class="d-none d-lg-inline">Admin</span>
                                                            </div>
                                                        </a>
                                                        <ul
                                                            class="dropdown-menu dropdown-menu-end shadow-lg border-0 mt-2">
                                                            <li class="dropdown-header px-3 py-2">
                                                                <div class="d-flex align-items-center">
                                                                    <i class="fas fa-user-shield text-primary me-2"
                                                                        style="font-size: 1.5rem;"></i>
                                                                    <div>
                                                                        <div class="fw-semibold">Administrator</div>
                                                                        <small class="text-muted">Admin Panel
                                                                            Access</small>
                                                                    </div>
                                                                </div>
                                                            </li>
                                                            <li>
                                                                <hr class="dropdown-divider my-2">
                                                            </li>
                                                            <li>
                                                                <a class="dropdown-item py-2"
                                                                    href="${pageContext.request.contextPath}/admin/dashboard">
                                                                    <i
                                                                        class="fas fa-tachometer-alt me-2 text-primary"></i>
                                                                    Dashboard
                                                                </a>
                                                            </li>
                                                            <li>
                                                                <a class="dropdown-item py-2"
                                                                    href="${pageContext.request.contextPath}/admin/users">
                                                                    <i class="fas fa-users me-2 text-primary"></i>
                                                                    Manage Users
                                                                </a>
                                                            </li>
                                                            <li>
                                                                <a class="dropdown-item py-2"
                                                                    href="${pageContext.request.contextPath}/admin/services">
                                                                    <i class="fas fa-cog me-2 text-primary"></i>
                                                                    Manage Services
                                                                </a>
                                                            </li>
                                                            <li>
                                                                <a class="dropdown-item py-2"
                                                                    href="${pageContext.request.contextPath}/admin/categories">
                                                                    <i class="fas fa-list me-2 text-primary"></i>
                                                                    Manage Categories
                                                                </a>
                                                            </li>
                                                            <li>
                                                                <hr class="dropdown-divider my-2">
                                                            </li>
                                                            <li>
                                                                <a class="dropdown-item py-2 text-danger"
                                                                    href="${pageContext.request.contextPath}/UserServlet?action=logout"
                                                                    onclick="return confirm('Are you sure you want to logout?');">
                                                                    <i class="fas fa-sign-out-alt me-2"></i>
                                                                    Logout
                                                                </a>
                                                            </li>
                                                        </ul>
                                                    </li>
                                                    <% } else { // No user logged in (Public view) %>
                                                        <!-- Login Link with Icon -->
                                                        <li class="nav-item">
                                                            <a class="nav-link <%= loginCls %>"
                                                                href="${pageContext.request.contextPath}/login">
                                                                <i class="fas fa-sign-in-alt me-1"></i>
                                                                <span class="nav-text">Login</span>
                                                            </a>
                                                        </li>

                                                        <!-- Enhanced Register Button -->
                                                        <li class="nav-item">
                                                            <a class="btn btn-light btn-sm ms-lg-2 mt-2 mt-lg-0 register-btn"
                                                                href="${pageContext.request.contextPath}/register">
                                                                <i class="fas fa-user-plus me-1"></i>
                                                                Register
                                                            </a>
                                                        </li>
                                                        <% } %>
                        </ul>
                    </div>
                </div>
            </nav>
        </header>

        <!-- Optional: Promotional Banner (Uncomment to use) -->
        <%-- <div class="alert alert-info alert-dismissible fade show m-0 rounded-0 text-center border-0 py-2"
            role="alert"
            style="background: linear-gradient(90deg, rgba(77, 182, 172, 0.15) 0%, rgba(91, 124, 153, 0.15) 100%);">
            <div class="container">
                <i class="fas fa-info-circle me-2 text-primary"></i>
                <strong>New!</strong> Check out our specialized dementia care services.
                <a href="serviceCategory.jsp" class="alert-link ms-2 fw-semibold">Learn More →</a>
            </div>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            --%>

            <!-- Bootstrap JS Bundle (includes Popper) -->
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

            <!-- Enhanced Header JavaScript -->
            <script>
                // Smooth scrolling for anchor links
                document.querySelectorAll('a[href^="#"]').forEach(anchor => {
                    anchor.addEventListener('click', function (e) {
                        e.preventDefault();
                        const target = document.querySelector(this.getAttribute('href'));
                        if (target) {
                            target.scrollIntoView({
                                behavior: 'smooth',
                                block: 'start'
                            });
                        }
                    });
                });

                // Highlight active navigation item
                document.addEventListener('DOMContentLoaded', function () {
                    const currentLocation = window.location.pathname;
                    const navLinks = document.querySelectorAll('.nav-link');

                    navLinks.forEach(link => {
                        const href = link.getAttribute('href');
                        if (href && currentLocation.includes(href) && href !== 'home.jsp') {
                            link.classList.add('active');
                        }
                    });
                });

                // Auto-close mobile menu after clicking a link
                const navLinks = document.querySelectorAll('.navbar-nav .nav-link:not(.dropdown-toggle)');
                const navbarCollapse = document.querySelector('.navbar-collapse');

                navLinks.forEach(link => {
                    link.addEventListener('click', () => {
                        if (window.innerWidth < 992 && navbarCollapse.classList.contains('show')) {
                            const bsCollapse = bootstrap.Collapse.getInstance(navbarCollapse);
                            if (bsCollapse) {
                                bsCollapse.hide();
                            }
                        }
                    });
                });

                // Enhanced header scroll effect
                let lastScroll = 0;
                const header = document.querySelector('header');
                const navbar = document.querySelector('.navbar');

                window.addEventListener('scroll', () => {
                    const currentScroll = window.pageYOffset;

                    // Add shadow on scroll
                    if (currentScroll > 50) {
                        header.classList.add('scrolled');
                        navbar.style.padding = '0.25rem 0';
                    } else {
                        header.classList.remove('scrolled');
                        navbar.style.padding = '0.5rem 0';
                    }

                    lastScroll = currentScroll;
                });

                // Dropdown hover effect (desktop only)
                if (window.innerWidth >= 992) {
                    const dropdowns = document.querySelectorAll('.nav-item.dropdown');

                    dropdowns.forEach(dropdown => {
                        let timeout;

                        dropdown.addEventListener('mouseenter', function () {
                            clearTimeout(timeout);
                            const dropdownToggle = this.querySelector('.dropdown-toggle');
                            const dropdownMenu = this.querySelector('.dropdown-menu');

                            if (dropdownToggle && dropdownMenu) {
                                dropdownToggle.classList.add('show');
                                dropdownMenu.classList.add('show');
                                this.classList.add('show');
                            }
                        });

                        dropdown.addEventListener('mouseleave', function () {
                            const dropdownToggle = this.querySelector('.dropdown-toggle');
                            const dropdownMenu = this.querySelector('.dropdown-menu');

                            timeout = setTimeout(() => {
                                if (dropdownToggle && dropdownMenu) {
                                    dropdownToggle.classList.remove('show');
                                    dropdownMenu.classList.remove('show');
                                    this.classList.remove('show');
                                }
                            }, 200);
                        });
                    });
                }

                // Cart count update animation (if needed)
                function updateCartCount(count) {
                    const badge = document.querySelector('.nav-link .badge');
                    if (badge) {
                        badge.textContent = count;
                        badge.style.animation = 'none';
                        setTimeout(() => {
                            badge.style.animation = 'pulse 0.5s ease-in-out';
                        }, 10);
                    }
                }
            </script>