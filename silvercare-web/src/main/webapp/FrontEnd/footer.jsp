<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <footer class="bg-white border-top mt-auto">
        <div class="container py-5">
            <div class="row g-4">
                <!-- Brand Column -->
                <div class="col-lg-4 col-md-12 mb-4 mb-lg-0">
                    <div class="footer-brand mb-4">
                        <h5 class="text-uppercase fw-bold mb-3 d-flex align-items-center">
                            <i class="fas fa-heart text-primary me-2"></i>
                            SilverCare
                        </h5>
                        <p class="text-muted" style="line-height: 1.7;">
                            Providing compassionate and professional in-home care for seniors.
                            Our mission is to ensure a safe, healthy, and happy life for your loved ones.
                        </p>
                    </div>
                    <!-- Social Media Links -->
                    <div class="social-links">
                        <h6 class="fw-semibold mb-3">Follow Us</h6>
                        <div class="d-flex gap-2">
                            <a href="#"
                                class="btn btn-outline-primary btn-sm rounded-circle d-flex align-items-center justify-content-center"
                                style="width: 40px; height: 40px;" title="Facebook">
                                <i class="fab fa-facebook-f"></i>
                            </a>
                            <a href="#"
                                class="btn btn-outline-primary btn-sm rounded-circle d-flex align-items-center justify-content-center"
                                style="width: 40px; height: 40px;" title="Instagram">
                                <i class="fab fa-instagram"></i>
                            </a>
                            <a href="#"
                                class="btn btn-outline-primary btn-sm rounded-circle d-flex align-items-center justify-content-center"
                                style="width: 40px; height: 40px;" title="LinkedIn">
                                <i class="fab fa-linkedin-in"></i>
                            </a>
                            <a href="#"
                                class="btn btn-outline-primary btn-sm rounded-circle d-flex align-items-center justify-content-center"
                                style="width: 40px; height: 40px;" title="Twitter">
                                <i class="fab fa-twitter"></i>
                            </a>
                        </div>
                    </div>
                </div>

                <!-- Quick Links Column -->
                <div class="col-lg-3 col-md-4 mb-4 mb-lg-0">
                    <h5 class="text-uppercase fw-bold mb-4">Quick Links</h5>
                    <ul class="list-unstyled footer-links mb-0">
                        <li class="mb-2">
                            <a href="${pageContext.request.contextPath}/home"
                                class="text-muted text-decoration-none d-flex align-items-center">
                                <i class="fas fa-chevron-right text-primary me-2" style="font-size: 0.75rem;"></i>
                                Home
                            </a>
                        </li>
                        <li class="mb-2">
                            <a href="${pageContext.request.contextPath}/about"
                                class="text-muted text-decoration-none d-flex align-items-center">
                                <i class="fas fa-chevron-right text-primary me-2" style="font-size: 0.75rem;"></i>
                                About Us
                            </a>
                        </li>
                        <li class="mb-2">
                            <a href="${pageContext.request.contextPath}/ServiceServlet?action=category"
                                class="text-muted text-decoration-none d-flex align-items-center">
                                <i class="fas fa-chevron-right text-primary me-2" style="font-size: 0.75rem;"></i>
                                Services
                            </a>
                        </li>
                        <li class="mb-2">
                            <a href="${pageContext.request.contextPath}/FrontEnd/help.jsp"
                                class="text-muted text-decoration-none d-flex align-items-center">
                                <i class="fas fa-chevron-right text-primary me-2" style="font-size: 0.75rem;"></i>
                                User Guide
                            </a>
                        </li>
                    </ul>
                </div>

                <!-- Services Column -->
                <div class="col-lg-2 col-md-4 mb-4 mb-lg-0">
                    <h5 class="text-uppercase fw-bold mb-4">Our Services</h5>
                    <ul class="list-unstyled footer-links mb-0">
                        <li class="mb-2">
                            <a href="${pageContext.request.contextPath}/ServiceServlet?action=category"
                                class="text-muted text-decoration-none d-flex align-items-center">
                                <i class="fas fa-chevron-right text-primary me-2" style="font-size: 0.75rem;"></i>
                                Personal Care
                            </a>
                        </li>
                        <li class="mb-2">
                            <a href="${pageContext.request.contextPath}/ServiceServlet?action=category"
                                class="text-muted text-decoration-none d-flex align-items-center">
                                <i class="fas fa-chevron-right text-primary me-2" style="font-size: 0.75rem;"></i>
                                Companion Care
                            </a>
                        </li>
                        <li class="mb-2">
                            <a href="${pageContext.request.contextPath}/ServiceServlet?action=category"
                                class="text-muted text-decoration-none d-flex align-items-center">
                                <i class="fas fa-chevron-right text-primary me-2" style="font-size: 0.75rem;"></i>
                                Skilled Nursing
                            </a>
                        </li>
                    </ul>
                </div>

                <!-- Contact Column -->
                <div class="col-lg-3 col-md-4">
                    <h5 class="text-uppercase fw-bold mb-4">Contact Us</h5>
                    <ul class="list-unstyled contact-info mb-0">
                        <li class="mb-3 d-flex">
                            <div class="contact-icon me-3">
                                <i class="fas fa-map-marker-alt text-primary"></i>
                            </div>
                            <div>
                                <p class="mb-0 text-muted">Singapore, SG 12345</p>
                            </div>
                        </li>
                        <li class="mb-3 d-flex">
                            <div class="contact-icon me-3">
                                <i class="fas fa-envelope text-primary"></i>
                            </div>
                            <div>
                                <a href="mailto:info@silvercare.com" class="text-muted text-decoration-none">
                                    info@silvercare.com
                                </a>
                            </div>
                        </li>
                        <li class="mb-3 d-flex">
                            <div class="contact-icon me-3">
                                <i class="fas fa-phone text-primary"></i>
                            </div>
                            <div>
                                <a href="tel:+6523456788" class="text-muted text-decoration-none">
                                    +65 234 567 88
                                </a>
                            </div>
                        </li>
                        <li class="d-flex">
                            <div class="contact-icon me-3">
                                <i class="fas fa-clock text-primary"></i>
                            </div>
                            <div>
                                <p class="mb-0 text-muted">Mon - Fri: 8AM - 6PM</p>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>

        <!-- Bottom Bar -->
        <div class="footer-bottom border-top py-3" style="background-color: rgba(0, 0, 0, 0.02);">
            <div class="container">
                <div class="row align-items-center">
                    <div class="col-md-6 text-center text-md-start mb-2 mb-md-0">
                        <p class="mb-0 text-muted small">
                            © 2025 <a href="${pageContext.request.contextPath}/home"
                                class="text-primary text-decoration-none fw-semibold">SilverCare.com</a>. All rights
                            reserved.
                        </p>
                    </div>
                    <div class="col-md-6 text-center text-md-end">
                        <div class="footer-legal-links">
                            <a href="#" class="text-muted text-decoration-none small me-3 hover-primary">Privacy
                                Policy</a>
                            <a href="#" class="text-muted text-decoration-none small me-3 hover-primary">Terms of
                                Service</a>
                            <a href="#" class="text-muted text-decoration-none small hover-primary">Sitemap</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </footer>

    <!-- Additional Footer Styles -->
    <style>
        /* Footer Link Hover Effects */
        .footer-links a {
            transition: all 0.2s ease;
        }

        .footer-links a:hover {
            color: var(--primary-color) !important;
            padding-left: 5px;
        }

        .footer-links a:hover i {
            transform: translateX(3px);
        }

        .footer-links a i {
            transition: transform 0.2s ease;
        }

        /* Social Links Hover */
        .social-links .btn {
            transition: all 0.3s ease;
        }

        .social-links .btn:hover {
            background-color: var(--primary-color);
            border-color: var(--primary-color);
            color: white;
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(91, 124, 153, 0.3);
        }

        /* Contact Info Icons */
        .contact-icon {
            width: 20px;
            display: flex;
            align-items: flex-start;
            padding-top: 2px;
        }

        .contact-info a:hover {
            color: var(--primary-color) !important;
        }

        /* Footer Bottom Links */
        .hover-primary:hover {
            color: var(--primary-color) !important;
        }

        /* Footer Animation */
        footer {
            animation: fadeIn 0.6s ease-out;
        }

        /* Responsive Adjustments */
        @media (max-width: 768px) {
            .footer-brand h5 {
                font-size: 1.25rem;
            }

            .social-links .btn {
                width: 36px;
                height: 36px;
            }
        }
    </style>

    <!-- Bootstrap 5.3 JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>

    <!-- Scroll to Top Button (Optional Enhancement) -->
    <button id="scrollToTop" class="btn btn-primary rounded-circle position-fixed bottom-0 end-0 m-4 shadow-lg"
        style="width: 50px; height: 50px; display: none; z-index: 999;">
        <i class="fas fa-arrow-up"></i>
    </button>

    <script>
        // Scroll to Top Functionality
        const scrollToTopBtn = document.getElementById('scrollToTop');

        window.addEventListener('scroll', () => {
            if (window.pageYOffset > 300) {
                scrollToTopBtn.style.display = 'flex';
                scrollToTopBtn.style.alignItems = 'center';
                scrollToTopBtn.style.justifyContent = 'center';
            } else {
                scrollToTopBtn.style.display = 'none';
            }
        });

        scrollToTopBtn.addEventListener('click', () => {
            window.scrollTo({
                top: 0,
                behavior: 'smooth'
            });
        });
    </script>
    </body>

    </html>