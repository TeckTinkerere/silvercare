<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

        <%-- Include Header --%>
            <jsp:include page="header.jsp" />

            <%-- Tutorial Configuration --%>
                <div id="tutorialConfig" data-show-tutorial="${shouldShowTutorial}" style="display: none;"></div>

                <main class="container my-5">

                    <%-- Account Deleted Success Message --%>
                        <c:if test="${accountDeleted}">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                <i class="fas fa-check-circle me-2"></i>
                                <strong>Account Deleted Successfully!</strong> Your account and all associated data have
                                been permanently removed. Thank you for using SilverCare.
                                <button type="button" class="btn-close" data-bs-dismiss="alert"
                                    aria-label="Close"></button>
                            </div>
                        </c:if>

                        <!-- Hero Section with Enhanced Visual Design -->
                        <section class="hero-section text-center bg-white p-5 rounded shadow-sm mb-5 fade-in">
                            <div class="hero-content">
                                <div class="hero-icon mb-4">
                                    <i class="fas fa-heart text-primary" style="font-size: 3.5rem; opacity: 0.15;"></i>
                                </div>
                                <h1 class="display-4 fw-bold mb-3" style="letter-spacing: -0.02em;">
                                    Compassionate Care for Your Loved Ones
                                </h1>
                                <p class="lead text-muted mt-3 mb-4"
                                    style="max-width: 700px; margin-left: auto; margin-right: auto; font-size: 1.15rem; line-height: 1.7;">
                                    SilverCare offers professional, reliable, and <strong
                                        class="text-primary">heartfelt</strong> in-home senior care services.
                                    We are dedicated to enhancing the quality of life for our seniors.
                                </p>
                                <div
                                    class="hero-actions mt-4 d-flex flex-column flex-sm-row gap-3 justify-content-center">
                                    <a href="${pageContext.request.contextPath}/ServiceServlet?action=category"
                                        class="btn btn-primary btn-lg px-4 py-3">
                                        <i class="fas fa-hands-helping me-2"></i>
                                        Explore Our Services
                                    </a>
                                    <a href="${pageContext.request.contextPath}/contact"
                                        class="btn btn-outline-primary btn-lg px-4 py-3">
                                        <i class="fas fa-envelope me-2"></i>
                                        Contact Us Today
                                    </a>
                                </div>
                            </div>
                        </section>

                        <!-- Features/Services Overview Section with Enhanced Cards -->
                        <section class="services-overview mb-5 slide-up">
                            <div class="text-center mb-5">
                                <h2 class="h1 fw-bold mb-3">How We Can Help</h2>
                                <p class="text-muted" style="max-width: 600px; margin: 0 auto;">
                                    Our comprehensive care services are designed to meet the unique needs of each senior
                                </p>
                            </div>

                            <div class="row g-4 text-center">
                                <c:forEach var="category" items="${categories}">
                                    <c:set var="iconClass"
                                        value="${empty category.icon ? 'fas fa-heart' : category.icon}" />
                                    <div class="col-md-4">
                                        <div class="service-card card h-100 p-4 border-0 shadow-sm">
                                            <div class="service-icon mb-3">
                                                <div class="icon-wrapper bg-primary bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center"
                                                    style="width: 80px; height: 80px;">
                                                    <i class="${iconClass} text-primary" style="font-size: 2rem;"></i>
                                                </div>
                                            </div>
                                            <h3 class="h4 mb-3 fw-semibold">${category.name}</h3>
                                            <p class="text-muted mb-0" style="line-height: 1.7;">
                                                ${category.description}
                                            </p>
                                            <div class="mt-auto pt-4">
                                                <a href="${pageContext.request.contextPath}/ServiceServlet?action=category#category-${category.id}"
                                                    class="text-primary fw-semibold text-decoration-none">
                                                    Learn More <i class="fas fa-arrow-right ms-1"></i>
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                                <c:if test="${empty categories and empty error}">
                                    <div class='col-12'>
                                        <div class='alert alert-info'>Service categories will be displayed here.</div>
                                    </div>
                                </c:if>
                                <c:if test="${not empty error}">
                                    <div class='col-12'>
                                        <div class='alert alert-danger'>Error loading categories: ${error}</div>
                                    </div>
                                </c:if>
                            </div>
                        </section>

                        <!-- Stats Section (New Addition) -->
                        <section class="stats-section mb-5 slide-up">
                            <div class="bg-primary bg-gradient text-white p-5 rounded shadow-sm">
                                <div class="row text-center g-4">
                                    <div class="col-md-3 col-6">
                                        <div class="stat-item">
                                            <h3 class="display-5 fw-bold mb-2">500+</h3>
                                            <p class="mb-0 opacity-75">Families Served</p>
                                        </div>
                                    </div>
                                    <div class="col-md-3 col-6">
                                        <div class="stat-item">
                                            <h3 class="display-5 fw-bold mb-2">50+</h3>
                                            <p class="mb-0 opacity-75">Certified Caregivers</p>
                                        </div>
                                    </div>
                                    <div class="col-md-3 col-6">
                                        <div class="stat-item">
                                            <h3 class="display-5 fw-bold mb-2">10+</h3>
                                            <p class="mb-0 opacity-75">Years Experience</p>
                                        </div>
                                    </div>
                                    <div class="col-md-3 col-6">
                                        <div class="stat-item">
                                            <h3 class="display-5 fw-bold mb-2">98%</h3>
                                            <p class="mb-0 opacity-75">Satisfaction Rate</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </section>

                        <!-- Testimonial Section with Enhanced Design -->
                        <section class="testimonial-section bg-white p-5 rounded shadow-sm slide-up">
                            <div class="text-center mb-5">
                                <h2 class="h1 fw-bold mb-3">What Our Clients Say</h2>
                                <div class="rating mb-3">
                                    <i class="fas fa-star text-warning"></i>
                                    <i class="fas fa-star text-warning"></i>
                                    <i class="fas fa-star text-warning"></i>
                                    <i class="fas fa-star text-warning"></i>
                                    <i class="fas fa-star text-warning"></i>
                                    <span class="text-muted ms-2">(4.9 out of 5)</span>
                                </div>
                            </div>

                            <div class="row justify-content-center">
                                <div class="col-lg-8">
                                    <div class="testimonial-card p-4 position-relative">
                                        <div class="quote-icon text-primary opacity-25 mb-3">
                                            <i class="fas fa-quote-left" style="font-size: 3rem;"></i>
                                        </div>
                                        <blockquote class="blockquote mb-4">
                                            <p class="fs-5 mb-0" style="line-height: 1.8;">
                                                "The <strong class="text-primary">caregivers</strong> from SilverCare
                                                have been a true blessing for our family.
                                                Their professionalism and genuine kindness are unmatched. I highly
                                                recommend their services."
                                            </p>
                                        </blockquote>
                                        <div class="d-flex align-items-center">
                                            <div class="avatar bg-primary bg-opacity-10 rounded-circle d-flex align-items-center justify-content-center me-3"
                                                style="width: 50px; height: 50px;">
                                                <i class="fas fa-user text-primary"></i>
                                            </div>
                                            <div>
                                                <footer class="blockquote-footer mb-0 text-dark fw-semibold">
                                                    The Lee Family
                                                </footer>
                                                <small class="text-muted">Verified Client</small>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </section>

                        <!-- Call-to-Action Section (New Addition) -->
                        <section class="cta-section mt-5 slide-up">
                            <div class="bg-gradient p-5 rounded shadow-sm text-center"
                                style="background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-dark) 100%);">
                                <div>
                                    <h2 class="h1 fw-bold mb-3" style="color: #000000 !important;">Ready to Get Started?
                                    </h2>
                                    <p class="lead mb-4"
                                        style="max-width: 600px; margin: 0 auto; color: #000000 !important; opacity: 0.8;">
                                        Contact us today for a free consultation and discover how we can help your loved
                                        ones
                                    </p>
                                    <a href="${pageContext.request.contextPath}/contact"
                                        class="btn btn-light btn-lg px-5 py-3">
                                        <i class="fas fa-phone-alt me-2"></i>
                                        Schedule a Consultation
                                    </a>
                                </div>
                            </div>
                        </section>

                </main>

                <!-- Tutorial Modal -->
                <div class="modal fade" id="tutorialModal" data-bs-backdrop="static" data-bs-keyboard="false"
                    tabindex="-1" aria-labelledby="tutorialModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered modal-lg">
                        <div class="modal-content border-0 shadow-lg">
                            <div class="modal-header border-0 pb-0">
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"
                                    id="tutorialCloseBtn"></button>
                            </div>
                            <div class="modal-body px-5 py-4">
                                <!-- Slide 1: Welcome -->
                                <div class="tutorial-slide active" data-slide="1">
                                    <div class="text-center mb-4">
                                        <div class="tutorial-icon mb-4">
                                            <i class="fas fa-hand-holding-heart text-primary"
                                                style="font-size: 4rem;"></i>
                                        </div>
                                        <h2 class="fw-bold mb-3">Welcome to SilverCare</h2>
                                        <p class="lead text-muted mb-3">
                                            Your trusted platform for senior care services. We're here to help you find
                                            and book quality care services for your loved ones.
                                        </p>
                                        <a href="${pageContext.request.contextPath}/about"
                                            class="btn btn-outline-primary mt-3 tutorial-action-btn">
                                            <i class="fas fa-info-circle me-2"></i>Learn More About Us
                                        </a>
                                    </div>
                                </div>

                                <!-- Slide 2: Browse Services -->
                                <div class="tutorial-slide" data-slide="2">
                                    <div class="text-center mb-4">
                                        <div class="tutorial-icon mb-4">
                                            <i class="fas fa-search text-primary" style="font-size: 4rem;"></i>
                                        </div>
                                        <h2 class="fw-bold mb-3">Browse Services</h2>
                                        <p class="lead text-muted mb-3">
                                            Explore our wide range of care services including medical care, personal
                                            care, and companionship. Filter by category to find exactly what you need.
                                        </p>
                                        <a href="${pageContext.request.contextPath}/ServiceServlet?action=category"
                                            class="btn btn-outline-primary mt-3 tutorial-action-btn">
                                            <i class="fas fa-th-large me-2"></i>View All Services
                                        </a>
                                    </div>
                                </div>

                                <!-- Slide 3: Book Appointments -->
                                <div class="tutorial-slide" data-slide="3">
                                    <div class="text-center mb-4">
                                        <div class="tutorial-icon mb-4">
                                            <i class="fas fa-calendar-check text-primary" style="font-size: 4rem;"></i>
                                        </div>
                                        <h2 class="fw-bold mb-3">Book Appointments</h2>
                                        <p class="lead text-muted mb-3">
                                            Add services to your cart and complete the booking process. Choose your
                                            preferred date and time, and we'll take care of the rest.
                                        </p>
                                        <a href="${pageContext.request.contextPath}/CartServlet?action=view"
                                            class="btn btn-outline-primary mt-3 tutorial-action-btn">
                                            <i class="fas fa-shopping-cart me-2"></i>View My Cart
                                        </a>
                                    </div>
                                </div>

                                <!-- Slide 4: Manage Your Profile -->
                                <div class="tutorial-slide" data-slide="4">
                                    <div class="text-center mb-4">
                                        <div class="tutorial-icon mb-4">
                                            <i class="fas fa-user-cog text-primary" style="font-size: 4rem;"></i>
                                        </div>
                                        <h2 class="fw-bold mb-3">Manage Your Profile</h2>
                                        <p class="lead text-muted mb-3">
                                            Keep your personal information up to date. Update your contact details,
                                            change your password, and manage your bookings all in one place.
                                        </p>
                                        <a href="${pageContext.request.contextPath}/profile"
                                            class="btn btn-outline-primary mt-3 tutorial-action-btn">
                                            <i class="fas fa-user-circle me-2"></i>Go to My Profile
                                        </a>
                                    </div>
                                </div>

                                <!-- Slide 5: Get Started -->
                                <div class="tutorial-slide" data-slide="5">
                                    <div class="text-center mb-4">
                                        <div class="tutorial-icon mb-4">
                                            <i class="fas fa-rocket text-primary" style="font-size: 4rem;"></i>
                                        </div>
                                        <h2 class="fw-bold mb-3">Get Started</h2>
                                        <p class="lead text-muted mb-3">
                                            You're all set! Start exploring our services and book your first
                                            appointment. If you need help, visit our contact page or check the FAQ
                                            section.
                                        </p>
                                        <a href="${pageContext.request.contextPath}/contact"
                                            class="btn btn-outline-primary mt-3 tutorial-action-btn">
                                            <i class="fas fa-envelope me-2"></i>Contact Support
                                        </a>
                                    </div>
                                </div>

                                <!-- Progress Indicators -->
                                <div class="tutorial-progress text-center my-4">
                                    <span class="progress-dot active" data-slide="1"></span>
                                    <span class="progress-dot" data-slide="2"></span>
                                    <span class="progress-dot" data-slide="3"></span>
                                    <span class="progress-dot" data-slide="4"></span>
                                    <span class="progress-dot" data-slide="5"></span>
                                </div>
                            </div>
                            <div class="modal-footer border-0 pt-0 px-5 pb-4">
                                <div class="d-flex justify-content-between w-100">
                                    <button type="button" class="btn btn-outline-secondary" id="skipTutorialBtn">
                                        <i class="fas fa-forward me-2"></i>Skip Tutorial
                                    </button>
                                    <div class="tutorial-nav-buttons">
                                        <button type="button" class="btn btn-outline-primary me-2" id="prevSlideBtn"
                                            style="display: none;">
                                            <i class="fas fa-chevron-left me-2"></i>Previous
                                        </button>
                                        <button type="button" class="btn btn-primary" id="nextSlideBtn">
                                            Next<i class="fas fa-chevron-right ms-2"></i>
                                        </button>
                                        <button type="button" class="btn btn-success" id="finishTutorialBtn"
                                            style="display: none;">
                                            <i class="fas fa-check me-2"></i>Get Started
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Additional Inline Styles for Enhanced Visuals -->
                <style>
                    /* Hero Section Enhancements */
                    .hero-section {
                        position: relative;
                        overflow: hidden;
                    }

                    .hero-section::before {
                        content: '';
                        position: absolute;
                        top: -50%;
                        right: -10%;
                        width: 500px;
                        height: 500px;
                        background: radial-gradient(circle, rgba(91, 124, 153, 0.05) 0%, transparent 70%);
                        border-radius: 50%;
                    }

                    .hero-content {
                        position: relative;
                        z-index: 1;
                    }

                    /* Service Cards Hover Effects */
                    .service-card {
                        transition: all 0.3s ease;
                        border-radius: var(--border-radius-lg);
                    }

                    .service-card:hover {
                        transform: translateY(-10px);
                        box-shadow: var(--shadow-lg) !important;
                    }

                    .service-card .icon-wrapper {
                        transition: all 0.3s ease;
                    }

                    .service-card:hover .icon-wrapper {
                        transform: scale(1.1);
                        background-color: var(--primary-color) !important;
                    }

                    .service-card:hover .icon-wrapper i {
                        color: white !important;
                    }

                    .service-card a {
                        transition: all 0.2s ease;
                    }

                    .service-card:hover a {
                        transform: translateX(5px);
                        display: inline-block;
                    }

                    /* Stats Section Animation */
                    .stat-item h3 {
                        animation: countUp 1.5s ease-out;
                    }

                    @keyframes countUp {
                        from {
                            opacity: 0;
                            transform: translateY(20px);
                        }

                        to {
                            opacity: 1;
                            transform: translateY(0);
                        }
                    }

                    /* Testimonial Card Enhancement */
                    .testimonial-card {
                        background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%);
                        border-radius: var(--border-radius-lg);
                        border-left: 4px solid var(--accent-color);
                    }

                    /* CTA Section */
                    .cta-section .btn-light:hover {
                        transform: translateY(-3px);
                        box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
                    }

                    /* Responsive Typography */
                    @media (max-width: 768px) {
                        .hero-section h1 {
                            font-size: 2rem;
                        }

                        .hero-section .lead {
                            font-size: 1rem;
                        }

                        .stat-item h3 {
                            font-size: 2rem;
                        }
                    }

                    /* Tutorial Modal Styles */
                    #tutorialModal .modal-content {
                        border-radius: 20px;
                        overflow: hidden;
                    }

                    #tutorialModal .modal-body {
                        min-height: 400px;
                        display: flex;
                        flex-direction: column;
                        justify-content: center;
                    }

                    .tutorial-slide {
                        display: none;
                        animation: slideIn 0.5s ease-in-out;
                    }

                    .tutorial-slide.active {
                        display: block;
                    }

                    @keyframes slideIn {
                        from {
                            opacity: 0;
                            transform: translateX(30px);
                        }

                        to {
                            opacity: 1;
                            transform: translateX(0);
                        }
                    }

                    .tutorial-icon {
                        animation: iconBounce 1s ease-in-out;
                    }

                    @keyframes iconBounce {

                        0%,
                        100% {
                            transform: translateY(0);
                        }

                        50% {
                            transform: translateY(-10px);
                        }
                    }

                    .tutorial-progress {
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        gap: 12px;
                    }

                    .progress-dot {
                        width: 12px;
                        height: 12px;
                        border-radius: 50%;
                        background-color: #dee2e6;
                        cursor: pointer;
                        transition: all 0.3s ease;
                        display: inline-block;
                    }

                    .progress-dot:hover {
                        background-color: #adb5bd;
                        transform: scale(1.2);
                    }

                    .progress-dot.active {
                        background-color: var(--primary-color);
                        width: 14px;
                        height: 14px;
                        box-shadow: 0 0 0 3px rgba(91, 124, 153, 0.2);
                    }

                    .tutorial-nav-buttons {
                        display: flex;
                        align-items: center;
                    }

                    /* Tutorial Action Buttons */
                    .tutorial-action-btn {
                        transition: all 0.3s ease;
                        padding: 0.75rem 1.5rem;
                        font-weight: 500;
                    }

                    .tutorial-action-btn:hover {
                        transform: translateY(-2px);
                        box-shadow: 0 4px 12px rgba(91, 124, 153, 0.3);
                    }

                    /* Responsive Tutorial Modal */
                    @media (max-width: 768px) {
                        #tutorialModal .modal-body {
                            min-height: 350px;
                            padding: 2rem 1.5rem !important;
                        }

                        #tutorialModal .modal-footer {
                            padding: 1rem 1.5rem !important;
                        }

                        .tutorial-icon i {
                            font-size: 3rem !important;
                        }

                        .tutorial-slide h2 {
                            font-size: 1.5rem;
                        }

                        .tutorial-slide .lead {
                            font-size: 1rem;
                        }

                        .modal-footer .d-flex {
                            flex-direction: column;
                            gap: 1rem;
                        }

                        .tutorial-nav-buttons {
                            width: 100%;
                            justify-content: space-between;
                        }

                        #skipTutorialBtn {
                            width: 100%;
                        }
                    }
                </style>

                <!-- Tutorial JavaScript -->
                <script>
                    document.addEventListener('DOMContentLoaded', function () {
                        // Get tutorial configuration
                        var tutorialConfigElement = document.getElementById('tutorialConfig');
                        var shouldShowTutorial = tutorialConfigElement ? tutorialConfigElement.getAttribute('data-show-tutorial') === 'true' : false;

                        // Initialize Tutorial Modal
                        let currentSlide = 1;
                        const totalSlides = 5;

                        const tutorialSlides = document.querySelectorAll('.tutorial-slide');
                        const progressDots = document.querySelectorAll('.progress-dot');
                        const prevBtn = document.getElementById('prevSlideBtn');
                        const nextBtn = document.getElementById('nextSlideBtn');
                        const finishBtn = document.getElementById('finishTutorialBtn');
                        const skipBtn = document.getElementById('skipTutorialBtn');
                        const tutorialModalElement = document.getElementById('tutorialModal');
                        const tutorialCloseBtn = document.getElementById('tutorialCloseBtn');

                        // Function to mark tutorial as completed using basic form submission
                        function completeTutorial() {
                            // Create a hidden form to submit to completeTutorial.jsp
                            var form = document.createElement('form');
                            form.method = 'POST';
                            form.action = '${pageContext.request.contextPath}/UserServlet?action=completeTutorial';
                            form.style.display = 'none';

                            // Add a hidden input to indicate we want to return to home.jsp
                            var returnInput = document.createElement('input');
                            returnInput.type = 'hidden';
                            returnInput.name = 'returnTo';
                            returnInput.value = 'home';
                            form.appendChild(returnInput);

                            var actionInput = document.createElement('input');
                            actionInput.type = 'hidden';
                            actionInput.name = 'action';
                            actionInput.value = 'completeTutorial';
                            form.appendChild(actionInput);

                            document.body.appendChild(form);
                            form.submit();
                        }

                        function showSlide(slideNumber) {
                            // Hide all slides
                            tutorialSlides.forEach(slide => {
                                slide.classList.remove('active');
                            });

                            // Show current slide
                            const currentSlideElement = document.querySelector('.tutorial-slide[data-slide="' + slideNumber + '"]');
                            if (currentSlideElement) {
                                currentSlideElement.classList.add('active');
                            }

                            // Update progress dots
                            progressDots.forEach(dot => {
                                dot.classList.remove('active');
                            });
                            const currentDot = document.querySelector('.progress-dot[data-slide="' + slideNumber + '"]');
                            if (currentDot) {
                                currentDot.classList.add('active');
                            }

                            // Update navigation buttons
                            if (prevBtn) {
                                prevBtn.style.display = slideNumber === 1 ? 'none' : 'inline-block';
                            }

                            if (slideNumber === totalSlides) {
                                if (nextBtn) nextBtn.style.display = 'none';
                                if (finishBtn) finishBtn.style.display = 'inline-block';
                            } else {
                                if (nextBtn) nextBtn.style.display = 'inline-block';
                                if (finishBtn) finishBtn.style.display = 'none';
                            }

                            currentSlide = slideNumber;
                        }

                        // Next button
                        if (nextBtn) {
                            nextBtn.addEventListener('click', function () {
                                if (currentSlide < totalSlides) {
                                    showSlide(currentSlide + 1);
                                }
                            });
                        }

                        // Previous button
                        if (prevBtn) {
                            prevBtn.addEventListener('click', function () {
                                if (currentSlide > 1) {
                                    showSlide(currentSlide - 1);
                                }
                            });
                        }

                        // Progress dots
                        progressDots.forEach(dot => {
                            dot.addEventListener('click', function () {
                                const slideNumber = parseInt(this.getAttribute('data-slide'));
                                showSlide(slideNumber);
                            });
                        });

                        // Skip button
                        if (skipBtn) {
                            skipBtn.addEventListener('click', function () {
                                if (confirm('Are you sure you want to skip the tutorial? You can always access help from the menu.')) {
                                    completeTutorial();
                                }
                            });
                        }

                        // Finish button
                        if (finishBtn) {
                            finishBtn.addEventListener('click', function () {
                                completeTutorial();
                            });
                        }

                        // Close button
                        if (tutorialCloseBtn) {
                            tutorialCloseBtn.addEventListener('click', function (e) {
                                e.preventDefault();
                                if (confirm('Are you sure you want to skip the tutorial? You can always access help from the menu.')) {
                                    completeTutorial();
                                }
                            });
                        }

                        // Handle action button clicks in slides
                        // When user clicks action button, mark tutorial as complete before navigating
                        const actionButtons = document.querySelectorAll('.tutorial-action-btn');
                        actionButtons.forEach(function (btn) {
                            btn.addEventListener('click', function (e) {
                                e.preventDefault();
                                var targetUrl = this.getAttribute('href');

                                // Create form to mark tutorial complete with redirect to target page
                                var form = document.createElement('form');
                                form.method = 'POST';
                                form.action = '${pageContext.request.contextPath}/UserServlet?action=completeTutorial';
                                form.style.display = 'none';

                                var redirectInput = document.createElement('input');
                                redirectInput.type = 'hidden';
                                redirectInput.name = 'redirectTo';
                                redirectInput.value = targetUrl;
                                form.appendChild(redirectInput);

                                document.body.appendChild(form);
                                form.submit();
                            });
                        });

                        // Initialize on first slide
                        showSlide(1);

                        // Auto-display modal if needed
                        if (shouldShowTutorial && tutorialModalElement) {
                            const tutorialModal = new bootstrap.Modal(tutorialModalElement, {
                                backdrop: 'static',
                                keyboard: false
                            });
                            tutorialModal.show();
                        }
                    });
                </script>

                <%-- Include Footer --%>
                    <jsp:include page="footer.jsp"></jsp:include>