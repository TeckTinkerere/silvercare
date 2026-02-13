<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

        <%-- Include Header --%>
            <jsp:include page="header.jsp" />

            <main class="container my-5">

                <!-- Contact Section -->
                <section class="contact-section bg-white p-5 rounded shadow-sm fade-in">
                    <div class="row">
                        <div class="col-lg-8 mx-auto">
                            <div class="text-center mb-5">
                                <div class="contact-icon mb-4">
                                    <div class="icon-wrapper bg-primary bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center"
                                        style="width: 80px; height: 80px;">
                                        <i class="fas fa-envelope text-primary" style="font-size: 2rem;"></i>
                                    </div>
                                </div>
                                <h1 class="display-5 fw-bold mb-3">Get in Touch</h1>
                                <p class="lead text-muted">We're here to help. Send us a message and we'll get back to
                                    you
                                    as soon as possible.</p>
                            </div>


                            <!-- Contact Form -->
                            <div class="contact-form-wrapper">
                                <c:if test="${not empty error}">
                                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                        <i class="fas fa-exclamation-circle me-2"></i>${error}
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"
                                            aria-label="Close"></button>
                                    </div>
                                </c:if>
                                <c:if test="${not empty success}">
                                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                                        <i class="fas fa-check-circle me-2"></i>${success}
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"
                                            aria-label="Close"></button>
                                    </div>
                                </c:if>
                                <c:set var="loggedIn" value="${not empty sessionScope.customer_id}" />
                                <c:set var="userName" value="${loggedIn ? sessionScope.customer_name : ''}" />
                                <c:set var="userEmail" value="${loggedIn ? (not empty sessionScope.customer_email ? sessionScope.customer_email : sessionScope.user.email) : ''}" />

                                <form action="${pageContext.request.contextPath}/contact" method="POST">

                                    <div class="row g-3">
                                        <div class="col-md-6">
                                            <label for="name" class="form-label fw-semibold">
                                                <i class="fas fa-user text-primary me-2"></i>Full Name *
                                            </label>
                                            <input type="text" class="form-control form-control-lg" id="name"
                                                name="name" value="${userName}" placeholder="John Doe" required>
                                        </div>
                                        <div class="col-md-6">
                                            <label for="email" class="form-label fw-semibold">
                                                <i class="fas fa-envelope text-primary me-2"></i>Email Address *
                                            </label>
                                            <input type="email" class="form-control form-control-lg" id="email"
                                                name="email" value="${userEmail}" placeholder="john@example.com" required>
                                        </div>
                                        <div class="col-12">
                                            <label for="phone" class="form-label fw-semibold">
                                                <i class="fas fa-phone text-primary me-2"></i>Phone Number
                                                (Optional)
                                            </label>
                                            <input type="tel" class="form-control form-control-lg" id="phone"
                                                name="phone" placeholder="+65 1234 5678">
                                        </div>
                                        <div class="col-12">
                                            <label for="subject" class="form-label fw-semibold">
                                                <i class="fas fa-tag text-primary me-2"></i>Subject *
                                            </label>
                                            <input type="text" class="form-control form-control-lg" id="subject"
                                                name="subject" placeholder="How can we help you?" required>
                                        </div>
                                        <div class="col-12">
                                            <label for="message" class="form-label fw-semibold">
                                                <i class="fas fa-comment-dots text-primary me-2"></i>Message *
                                            </label>
                                            <textarea class="form-control form-control-lg" id="message" name="message"
                                                rows="6" placeholder="Tell us more about your inquiry..."
                                                required></textarea>
                                        </div>
                                        <div class="col-12 text-center mt-4">
                                            <button type="submit" class="btn btn-primary btn-lg px-5">
                                                <i class="fas fa-paper-plane me-2"></i>Send Message
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </section>

                <!-- Contact Information Cards -->
                <section class="contact-info mt-5 slide-up">
                    <div class="row g-4">
                        <div class="col-md-4">
                            <div class="info-card card h-100 border-0 shadow-sm p-4 text-center">
                                <div class="card-icon mb-3">
                                    <div class="icon-wrapper bg-primary bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center"
                                        style="width: 60px; height: 60px;">
                                        <i class="fas fa-phone-alt text-primary" style="font-size: 1.5rem;"></i>
                                    </div>
                                </div>
                                <h3 class="h5 fw-semibold mb-2">Call Us</h3>
                                <p class="text-muted mb-2">Mon - Fri: 8AM - 6PM</p>
                                <a href="tel:+6523456788" class="text-primary fw-semibold text-decoration-none">
                                    +65 234 567 88
                                </a>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="info-card card h-100 border-0 shadow-sm p-4 text-center">
                                <div class="card-icon mb-3">
                                    <div class="icon-wrapper bg-primary bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center"
                                        style="width: 60px; height: 60px;">
                                        <i class="fas fa-envelope text-primary" style="font-size: 1.5rem;"></i>
                                    </div>
                                </div>
                                <h3 class="h5 fw-semibold mb-2">Email Us</h3>
                                <p class="text-muted mb-2">We'll respond within 24 hours</p>
                                <a href="mailto:info@silvercare.com"
                                    class="text-primary fw-semibold text-decoration-none">
                                    info@silvercare.com
                                </a>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="info-card card h-100 border-0 shadow-sm p-4 text-center">
                                <div class="card-icon mb-3">
                                    <div class="icon-wrapper bg-primary bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center"
                                        style="width: 60px; height: 60px;">
                                        <i class="fas fa-map-marker-alt text-primary" style="font-size: 1.5rem;"></i>
                                    </div>
                                </div>
                                <h3 class="h5 fw-semibold mb-2">Visit Us</h3>
                                <p class="text-muted mb-2">Singapore, SG 12345</p>
                                <a href="#" class="text-primary fw-semibold text-decoration-none">
                                    Get Directions
                                </a>
                            </div>
                        </div>
                    </div>
                </section>

            </main>

            <!-- Additional Styles -->
            <style>
                /* Contact Icon Enhancement */
                .contact-icon .icon-wrapper {
                    transition: all var(--transition-medium);
                }

                .contact-icon .icon-wrapper:hover {
                    transform: scale(1.1);
                    background-color: var(--primary-color) !important;
                }

                .contact-icon .icon-wrapper:hover i {
                    color: white !important;
                }

                /* Form Styling Enhancement */
                .form-control,
                .form-select {
                    border: 2px solid #e9ecef;
                    border-radius: var(--border-radius);
                    transition: all var(--transition-fast);
                }

                .form-control:focus,
                .form-select:focus {
                    border-color: var(--primary-color);
                    box-shadow: 0 0 0 0.25rem rgba(91, 124, 153, 0.15);
                }

                .form-control-lg,
                .form-select-lg {
                    padding: 0.75rem 1rem;
                }

                /* Form Labels */
                .form-label {
                    color: var(--heading-color);
                    margin-bottom: 0.5rem;
                }

                .form-label i {
                    width: 20px;
                }

                /* Submit Button Enhancement */
                .contact-section .btn-primary {
                    font-weight: 600;
                    letter-spacing: 0.5px;
                    padding: 0.75rem 2.5rem;
                    transition: all var(--transition-fast);
                }

                .contact-section .btn-primary:hover {
                    transform: translateY(-3px);
                    box-shadow: 0 8px 20px rgba(91, 124, 153, 0.4);
                }

                /* Info Cards Hover Effect */
                .info-card {
                    transition: all var(--transition-medium);
                    border-radius: var(--border-radius-lg);
                }

                .info-card:hover {
                    transform: translateY(-8px);
                    box-shadow: var(--shadow-lg) !important;
                }

                .info-card .icon-wrapper {
                    transition: all var(--transition-fast);
                }

                .info-card:hover .icon-wrapper {
                    transform: scale(1.1);
                    background-color: var(--primary-color) !important;
                }

                .info-card:hover .icon-wrapper i {
                    color: white !important;
                }

                /* Readonly Input Styling */
                input[readonly] {
                    background-color: #f8f9fa;
                    cursor: not-allowed;
                }

                /* Form Wrapper */
                .contact-form-wrapper {
                    background: linear-gradient(135deg, rgba(248, 249, 250, 0.5) 0%, rgba(255, 255, 255, 0.5) 100%);
                    padding: 2rem;
                    border-radius: var(--border-radius-lg);
                    border: 1px solid rgba(0, 0, 0, 0.05);
                }

                /* Responsive Adjustments */
                @media (max-width: 768px) {
                    .contact-section h1 {
                        font-size: 2rem;
                    }

                    .contact-icon .icon-wrapper {
                        width: 60px;
                        height: 60px;
                    }

                    .contact-icon i {
                        font-size: 1.5rem !important;
                    }

                    .contact-form-wrapper,
                    .feedback-form-wrapper {
                        padding: 1.5rem;
                    }
                }
            </style>

            <!-- Additional JavaScript -->
            <script>
                // Form Validation Enhancement
                document.addEventListener('DOMContentLoaded', function () {
                    const forms = document.querySelectorAll('form');

                    forms.forEach(form => {
                        form.addEventListener('submit', function (e) {
                            const requiredFields = form.querySelectorAll('[required]');
                            let allValid = true;

                            requiredFields.forEach(field => {
                                if (!field.value.trim()) {
                                    allValid = false;
                                    field.classList.add('is-invalid');
                                } else {
                                    field.classList.remove('is-invalid');
                                }
                            });

                            if (!allValid) {
                                e.preventDefault();
                                alert('Please fill in all required fields');
                            }
                        });
                    });

                    // Real-time validation
                    const inputs = document.querySelectorAll('.form-control, .form-select');
                    inputs.forEach(input => {
                        input.addEventListener('blur', function () {
                            if (this.required && !this.value.trim()) {
                                this.classList.add('is-invalid');
                            } else {
                                this.classList.remove('is-invalid');
                            }
                        });
                    });
                });

                // Character count for textarea 
                const textarea = document.querySelector('textarea');
                if (textarea) {
                    textarea.addEventListener('input', function () {
                        const maxLength = 1000;
                        const remaining = maxLength - this.value.length;

                        if (remaining < 0) {
                            this.value = this.value.substring(0, maxLength);
                        }
                    });
                }
            </script>

            <%-- Include Footer --%>
                <jsp:include page="footer.jsp"></jsp:include>