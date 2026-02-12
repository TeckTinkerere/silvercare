<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
            <script src="https://js.stripe.com/v3/"></script>

            <%-- Include Header --%>
                <jsp:include page="header.jsp" />

                <main class="container my-5">

                    <%-- Display Error Messages --%>
                        <% String errorParam=request.getParameter("error"); String errorMsg=request.getParameter("msg");
                            if (errorParam !=null) { %>
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="fas fa-exclamation-circle me-2"></i>
                                <strong>
                                    <% if ("payment_failed".equals(errorParam)) { %>
                                        Payment Failed!
                                        <% } else { %>
                                            Booking Failed!
                                            <% } %>
                                </strong>
                                <% if ("payment_failed".equals(errorParam)) { %>
                                    Your payment could not be processed. Please check your card details and try again.
                                    <br><small class="text-muted">
                                        💳 For testing, use card: 4242 4242 4242 4242, any future expiry, any CVV
                                    </small>
                                    <% } else if ("sql_error".equals(errorParam) && errorMsg !=null) { %>
                                        Database error: <%= errorMsg %>
                                            <% } else if ("invalid_data".equals(errorParam)) { %>
                                                Please fill in all required fields correctly.
                                                <% } else { %>
                                                    An error occurred while processing your booking: <%= errorParam %>
                                                        <% } %>
                                                            <button type="button" class="btn-close"
                                                                data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                            <% } %>

                                <!-- Page Header -->
                                <section class="page-header text-center mb-5 fade-in">
                                    <div class="hero-icon mb-3">
                                        <i class="fas fa-calendar-check text-primary"
                                            style="font-size: 3.5rem; opacity: 0.15;"></i>
                                    </div>
                                    <h1 class="display-4 fw-bold mb-3" style="letter-spacing: -0.02em;">
                                        Book Your Service
                                    </h1>
                                    <p class="lead text-muted"
                                        style="max-width: 600px; margin: 0 auto; font-size: 1.15rem;">
                                        Schedule a convenient time for our professional care services
                                    </p>
                                </section>

                                <div class="row g-4">
                                    <!-- Booking Form Column -->
                                    <div class="col-lg-8">
                                        <div class="card border-0 shadow-sm slide-up">
                                            <div class="card-header bg-white border-0 pt-4 pb-3">
                                                <h3 class="h4 mb-0 fw-bold d-flex align-items-center">
                                                    <span
                                                        class="icon-wrapper bg-primary bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center me-2"
                                                        style="width: 40px; height: 40px;">
                                                        <i class="fas fa-edit text-primary"></i>
                                                    </span>
                                                    Booking Details
                                                </h3>
                                            </div>
                                            <div class="card-body p-4">
                                                <form
                                                    action="${pageContext.request.contextPath}/BookingServlet?action=save"
                                                    method="POST" id="bookingForm">
                                                    <c:if test="${not empty service}">
                                                        <input type="hidden" name="service_id" value="${service.id}">
                                                        <input type="hidden" name="price" value="${service.price}">
                                                    </c:if>

                                                    <!-- Date and Time Section -->
                                                    <div class="form-section mb-4">
                                                        <h5 class="fw-semibold mb-3">
                                                            <i class="fas fa-calendar-alt text-primary me-2"></i>
                                                            Select Date &amp; Time
                                                        </h5>

                                                        <div class="row g-3">
                                                            <div class="col-md-6">
                                                                <label for="bookingDate"
                                                                    class="form-label fw-semibold text-muted small">
                                                                    <i class="fas fa-calendar me-2"></i>SERVICE DATE
                                                                </label>
                                                                <input type="date" class="form-control form-control-lg"
                                                                    id="bookingDate" name="bookingDate" required>
                                                                <small class="form-text text-muted mt-1 d-block">
                                                                    <i class="fas fa-info-circle me-1"></i>Select your
                                                                    preferred service date
                                                                </small>
                                                            </div>

                                                            <div class="col-md-6">
                                                                <label for="bookingTime"
                                                                    class="form-label fw-semibold text-muted small">
                                                                    <i class="fas fa-clock me-2"></i>SERVICE TIME
                                                                </label>
                                                                <input type="time" class="form-control form-control-lg"
                                                                    id="bookingTime" name="bookingTime" required>
                                                                <small class="form-text text-muted mt-1 d-block">
                                                                    <i class="fas fa-info-circle me-1"></i>Available:
                                                                    8:00 AM - 6:00 PM
                                                                </small>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <!-- Service Duration Section -->
                                                    <div class="form-section mb-4">
                                                        <h5 class="fw-semibold mb-3">
                                                            <i class="fas fa-hourglass-half text-primary me-2"></i>
                                                            Service Duration
                                                        </h5>

                                                        <div class="row g-3">
                                                            <div class="col-md-6">
                                                                <label for="duration"
                                                                    class="form-label fw-semibold text-muted small">
                                                                    <i class="fas fa-stopwatch me-2"></i>DURATION
                                                                    (HOURS)
                                                                </label>
                                                                <select class="form-select form-control-lg"
                                                                    id="duration" name="duration" required>
                                                                    <option value="">Select duration...</option>
                                                                    <option value="1">1 Hour</option>
                                                                    <option value="2" selected>2 Hours</option>
                                                                    <option value="3">3 Hours</option>
                                                                    <option value="4">4 Hours</option>
                                                                    <option value="6">6 Hours</option>
                                                                    <option value="8">8 Hours (Full Day)</option>
                                                                </select>
                                                            </div>

                                                            <div class="col-md-6">
                                                                <label for="frequency"
                                                                    class="form-label fw-semibold text-muted small">
                                                                    <i class="fas fa-redo me-2"></i>FREQUENCY
                                                                </label>
                                                                <select class="form-select form-control-lg"
                                                                    id="frequency" name="frequency" required>
                                                                    <option value="once" selected>One-time Service
                                                                    </option>
                                                                    <option value="daily">Daily</option>
                                                                    <option value="weekly">Weekly</option>
                                                                    <option value="biweekly">Bi-weekly</option>
                                                                    <option value="monthly">Monthly</option>
                                                                </select>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <!-- Contact Information Section -->
                                                    <div class="form-section mb-4">
                                                        <h5 class="fw-semibold mb-3">
                                                            <i class="fas fa-user-circle text-primary me-2"></i>
                                                            Contact Information
                                                        </h5>

                                                        <div class="row g-3">
                                                            <div class="col-md-6">
                                                                <label for="contactName"
                                                                    class="form-label fw-semibold text-muted small">
                                                                    <i class="fas fa-user me-2"></i>CONTACT NAME
                                                                </label>
                                                                <input type="text" class="form-control form-control-lg"
                                                                    id="contactName" name="contactName" required
                                                                    placeholder="Enter contact name">
                                                            </div>

                                                            <div class="col-md-6">
                                                                <label for="contactPhone"
                                                                    class="form-label fw-semibold text-muted small">
                                                                    <i class="fas fa-phone me-2"></i>PHONE NUMBER
                                                                </label>
                                                                <input type="tel" class="form-control form-control-lg"
                                                                    id="contactPhone" name="contactPhone" required
                                                                    placeholder="Enter phone number">
                                                            </div>
                                                        </div>

                                                        <div class="mt-3">
                                                            <label for="serviceAddress"
                                                                class="form-label fw-semibold text-muted small">
                                                                <i class="fas fa-map-marker-alt me-2"></i>SERVICE
                                                                ADDRESS
                                                            </label>
                                                            <textarea class="form-control form-control-lg"
                                                                id="serviceAddress" name="serviceAddress" rows="2"
                                                                required
                                                                placeholder="Enter the address where service will be provided"></textarea>
                                                        </div>
                                                    </div>

                                                    <!-- Additional Information Section -->
                                                    <div class="form-section mb-4">
                                                        <h5 class="fw-semibold mb-3">
                                                            <i class="fas fa-clipboard-list text-primary me-2"></i>
                                                            Additional Information
                                                        </h5>

                                                        <label for="notes"
                                                            class="form-label fw-semibold text-muted small">
                                                            <i class="fas fa-comment-dots me-2"></i>SPECIAL INSTRUCTIONS
                                                            (OPTIONAL)
                                                        </label>
                                                        <textarea class="form-control form-control-lg" id="notes"
                                                            name="notes" rows="4"
                                                            placeholder="Any special requirements, preferences, or medical conditions we should know about..."></textarea>
                                                        <small class="form-text text-muted mt-1 d-block">
                                                            <i class="fas fa-info-circle me-1"></i>Include any relevant
                                                            information to help us serve you better
                                                        </small>
                                                    </div>

                                                    <!-- Terms and Conditions -->
                                                    <div class="form-check mb-4 p-3"
                                                        style="background: linear-gradient(135deg, rgba(91, 124, 153, 0.05) 0%, rgba(77, 182, 172, 0.05) 100%); border-radius: var(--border-radius);">
                                                        <input class="form-check-input" type="checkbox" id="agreeTerms"
                                                            required>
                                                        <label class="form-check-label" for="agreeTerms">
                                                            I agree to the <a href="#"
                                                                class="text-primary fw-semibold">Terms and
                                                                Conditions</a>
                                                            and <a href="#" class="text-primary fw-semibold">Privacy
                                                                Policy</a>
                                                        </label>
                                                    </div>

                                                    <!-- Stripe Payment Section -->
                                                    <div id="payment-section" class="form-section mb-4">
                                                        <h5 class="fw-semibold mb-3">
                                                            <i class="fas fa-credit-card text-primary me-2"></i>
                                                            Payment Details
                                                        </h5>
                                                        <div id="card-element"
                                                            class="form-control form-control-lg py-3 mb-3">
                                                            <!-- Stripe Element will be inserted here -->
                                                        </div>

                                                        <!-- Separate Postal Code Field for Singapore (6 digits) -->
                                                        <div class="mb-3">
                                                            <label for="postal-code" class="form-label">Postal
                                                                Code</label>
                                                            <input type="text" class="form-control form-control-lg"
                                                                id="postal-code" name="postalCode"
                                                                placeholder="Enter 6-digit postal code" maxlength="6"
                                                                pattern="[0-9]{6}" required>
                                                            <small class="text-muted">Singapore postal codes are 6
                                                                digits</small>
                                                        </div>

                                                        <div id="card-errors" role="alert"
                                                            class="text-danger small mt-2"></div>
                                                        <input type="hidden" name="paymentIntentId"
                                                            id="paymentIntentId">
                                                    </div>

                                                    <!-- Submit Buttons -->
                                                    <div class="d-flex gap-3 mt-4">
                                                        <button type="submit" class="btn btn-primary btn-lg flex-fill"
                                                            id="submit-button">
                                                            <i class="fas fa-check-circle me-2"></i>
                                                            <span id="button-text">Pay & Confirm Booking</span>
                                                            <span id="spinner"
                                                                class="spinner-border spinner-border-sm d-none"
                                                                role="status" aria-hidden="true"></span>
                                                        </button>
                                                        <a href="${pageContext.request.contextPath}/ServiceServlet?action=category"
                                                            class="btn btn-outline-secondary btn-lg"
                                                            style="width: auto; padding: 0.75rem 2rem;">
                                                            <i class="fas fa-times me-2"></i>Cancel
                                                        </a>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Order Summary Column -->
                                    <div class="col-lg-4">
                                        <!-- Service Summary Card (if service selected or cart items present) -->
                                        <c:if test="${not empty service or not empty cartItems}">
                                            <div class="card border-0 shadow-sm mb-4 slide-up">
                                                <div class="card-header bg-primary text-white pt-3 pb-3">
                                                    <h5 class="mb-0 fw-bold">
                                                        <i class="fas fa-file-invoice me-2"></i>Service Summary
                                                    </h5>
                                                </div>
                                                <div class="card-body p-0">
                                                    <c:choose>
                                                        <c:when test="${not empty service}">
                                                            <c:if test="${not empty service.imagePath}">
                                                                <c:set var="imageName"
                                                                    value="${service.imagePath.replace('images/', '')}" />
                                                                <img src="${pageContext.request.contextPath}/FrontEnd/images/${imageName}"
                                                                    class="card-img-top" alt="${service.name}"
                                                                    style="height: 180px; object-fit: cover;">
                                                            </c:if>

                                                            <div class="p-4">
                                                                <h6 class="fw-bold mb-2">${service.name}</h6>
                                                                <p class="text-muted small mb-3">${service.description}
                                                                </p>

                                                                <div
                                                                    class="d-flex justify-content-between align-items-center mb-2">
                                                                    <span class="text-muted">Base Price:</span>
                                                                    <span class="fw-bold text-primary fs-5">
                                                                        $
                                                                        <fmt:formatNumber value="${service.price}"
                                                                            type="currency" currencySymbol="" />
                                                                    </span>
                                                                </div>
                                                            </div>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <div class="p-4">
                                                                <h6 class="fw-bold mb-3">Cart Summary</h6>
                                                                <c:forEach var="item" items="${cartItems}">
                                                                    <div
                                                                        class="d-flex justify-content-between align-items-center mb-2">
                                                                        <div class="d-flex align-items-center">
                                                                            <c:if test="${not empty item.image}">
                                                                                <img src="${pageContext.request.contextPath}/FrontEnd/images/${item.image.replace('images/', '')}"
                                                                                    alt="${item.name}"
                                                                                    class="rounded me-2"
                                                                                    style="width: 40px; height: 40px; object-fit: cover;">
                                                                            </c:if>
                                                                            <span class="small">${item.name}</span>
                                                                        </div>
                                                                        <span class="fw-semibold small">$
                                                                            <fmt:formatNumber value="${item.price}"
                                                                                type="currency" currencySymbol="" />
                                                                        </span>
                                                                    </div>
                                                                </c:forEach>
                                                            </div>
                                                        </c:otherwise>
                                                    </c:choose>

                                                    <div class="px-4 pb-4">
                                                        <hr class="mt-0">
                                                        <div class="pricing-info">
                                                            <div class="d-flex justify-content-between mb-2">
                                                                <span class="text-muted small">Subtotal</span>
                                                                <span id="summary-subtotal"
                                                                    class="fw-semibold small">$0.00</span>
                                                            </div>
                                                            <div class="d-flex justify-content-between mb-2">
                                                                <span class="text-muted small">GST (9%)</span>
                                                                <span id="summary-gst"
                                                                    class="fw-semibold small">$0.00</span>
                                                            </div>
                                                            <div
                                                                class="d-flex justify-content-between mt-2 pt-2 border-top">
                                                                <span class="fw-bold">Total Amount</span>
                                                                <span id="summary-total"
                                                                    class="fw-bold text-primary fs-5">$0.00</span>
                                                            </div>
                                                            <p class="small text-muted mb-2 mt-3">
                                                                <i class="fas fa-info-circle me-2"></i>
                                                                Final price calculated based on duration
                                                            </p>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:if>

                                        <!-- Booking Information Card -->
                                        <div class="card border-0 shadow-sm slide-up">
                                            <div class="card-body p-4">
                                                <h5 class="fw-bold mb-3">
                                                    <i class="fas fa-info-circle text-primary me-2"></i>
                                                    Booking Information
                                                </h5>

                                                <div class="info-item mb-3">
                                                    <div class="d-flex align-items-start">
                                                        <div class="icon-wrapper bg-primary bg-opacity-10 rounded-circle d-flex align-items-center justify-content-center me-3"
                                                            style="width: 40px; height: 40px; min-width: 40px;">
                                                            <i class="fas fa-clock text-primary"></i>
                                                        </div>
                                                        <div>
                                                            <h6 class="fw-semibold mb-1">Service Hours</h6>
                                                            <p class="text-muted small mb-0">Monday - Sunday,
                                                                8:00 AM - 6:00 PM</p>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="info-item mb-3">
                                                    <div class="d-flex align-items-start">
                                                        <div class="icon-wrapper bg-primary bg-opacity-10 rounded-circle d-flex align-items-center justify-content-center me-3"
                                                            style="width: 40px; height: 40px; min-width: 40px;">
                                                            <i class="fas fa-calendar-alt text-primary"></i>
                                                        </div>
                                                        <div>
                                                            <h6 class="fw-semibold mb-1">Advance Booking</h6>
                                                            <p class="text-muted small mb-0">Book at least 24
                                                                hours in advance</p>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="info-item mb-3">
                                                    <div class="d-flex align-items-start">
                                                        <div class="icon-wrapper bg-primary bg-opacity-10 rounded-circle d-flex align-items-center justify-content-center me-3"
                                                            style="width: 40px; height: 40px; min-width: 40px;">
                                                            <i class="fas fa-undo text-primary"></i>
                                                        </div>
                                                        <div>
                                                            <h6 class="fw-semibold mb-1">Cancellation Policy
                                                            </h6>
                                                            <p class="text-muted small mb-0">Free cancellation
                                                                up to 12 hours before service</p>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="info-item">
                                                    <div class="d-flex align-items-start">
                                                        <div class="icon-wrapper bg-primary bg-opacity-10 rounded-circle d-flex align-items-center justify-content-center me-3"
                                                            style="width: 40px; height: 40px; min-width: 40px;">
                                                            <i class="fas fa-shield-alt text-primary"></i>
                                                        </div>
                                                        <div>
                                                            <h6 class="fw-semibold mb-1">Satisfaction Guarantee
                                                            </h6>
                                                            <p class="text-muted small mb-0">100% satisfaction
                                                                or your money back</p>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <!-- Need Help Card -->
                                        <div class="card border-0 shadow-sm mt-4 slide-up"
                                            style="background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-dark) 100%);">
                                            <div class="card-body p-4 text-white text-center">
                                                <i class="fas fa-headset mb-3"
                                                    style="font-size: 2.5rem; opacity: 0.9;"></i>
                                                <h5 class="fw-bold mb-2">Need Help?</h5>
                                                <p class="mb-3 opacity-90">Our customer support team is here to
                                                    assist you</p>
                                                <a href="${pageContext.request.contextPath}/contact"
                                                    class="btn btn-light btn-sm">
                                                    <i class="fas fa-phone-alt me-2"></i>Contact Support
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                </main>

                <!-- Custom Styles -->
                <style>
                    /* Page Animations */
                    .fade-in {
                        animation: fadeIn 0.6s ease-in;
                    }

                    @keyframes fadeIn {
                        from {
                            opacity: 0;
                            transform: translateY(-20px);
                        }

                        to {
                            opacity: 1;
                            transform: translateY(0);
                        }
                    }

                    .slide-up {
                        animation: slideUp 0.6s ease-out;
                    }

                    @keyframes slideUp {
                        from {
                            opacity: 0;
                            transform: translateY(30px);
                        }

                        to {
                            opacity: 1;
                            transform: translateY(0);
                        }
                    }

                    /* Card Enhancements */
                    .card {
                        border-radius: var(--border-radius-lg);
                        transition: all 0.3s ease;
                    }

                    .card:hover {
                        transform: translateY(-5px);
                        box-shadow: var(--shadow-lg) !important;
                    }

                    /* Form Controls */
                    .form-control-lg,
                    .form-select {
                        padding: 0.75rem 1rem;
                        font-size: 1rem;
                        border-radius: var(--border-radius);
                    }

                    .form-control:focus,
                    .form-select:focus {
                        border-color: var(--primary-color);
                        box-shadow: 0 0 0 0.2rem rgba(91, 124, 153, 0.15);
                    }

                    /* Form Sections */
                    .form-section {
                        padding: 1.5rem;
                        background: linear-gradient(135deg, rgba(91, 124, 153, 0.03) 0%, rgba(77, 182, 172, 0.03) 100%);
                        border-radius: var(--border-radius);
                        border-left: 4px solid var(--accent-color);
                    }

                    /* Icon Wrapper */
                    .icon-wrapper {
                        transition: all 0.3s ease;
                    }

                    .card:hover .icon-wrapper {
                        transform: scale(1.1);
                    }

                    /* Label Styling */
                    .form-label.small {
                        font-size: 0.75rem;
                        letter-spacing: 0.05em;
                        margin-bottom: 0.5rem;
                    }

                    /* Button Enhancements */
                    .btn-lg {
                        padding: 0.75rem 1.5rem;
                        font-size: 1rem;
                        border-radius: var(--border-radius);
                        transition: all 0.3s ease;
                    }

                    .btn-primary:hover {
                        transform: translateY(-2px);
                        box-shadow: 0 4px 12px rgba(91, 124, 153, 0.3);
                    }

                    /* Info Items */
                    .info-item {
                        transition: all 0.3s ease;
                    }

                    .info-item:hover {
                        transform: translateX(5px);
                    }

                    /* Responsive Adjustments */
                    @media (max-width: 768px) {
                        .display-4 {
                            font-size: 2rem;
                        }

                        .lead {
                            font-size: 1rem;
                        }

                        .form-section {
                            padding: 1rem;
                        }
                    }
                </style>

                <!-- Custom JavaScript -->
                <script>
                    // Set minimum date to tomorrow and update price
                    document.addEventListener('DOMContentLoaded', function () {
                        const dateInput = document.getElementById('bookingDate');
                        const today = new Date();
                        today.setDate(today.getDate() + 1); // Tomorrow
                        const minDate = today.toISOString().split('T')[0];
                        dateInput.min = minDate;

                        // Set default date to tomorrow
                        dateInput.value = minDate;

                        // Initial price update
                        updateSummaryPrice();
                    });

                    // Listen for duration changes to update price
                    document.getElementById('duration').addEventListener('change', updateSummaryPrice);

                    function updateSummaryPrice() {
                        const duration = parseInt(document.getElementById('duration').value) || 1;
                        let subtotal = 0;

                        <c:choose>
                            <c:when test="${not empty service}">
                                subtotal = ${service.price} * duration;
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="item" items="${cartItems}">
                                    subtotal += ${item.price} * duration;
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>

                        const gst = subtotal * 0.09;
                        const total = subtotal + gst;

                        document.getElementById('summary-subtotal').innerText = '$' + subtotal.toFixed(2);
                        document.getElementById('summary-gst').innerText = '$' + gst.toFixed(2);
                        document.getElementById('summary-total').innerText = '$' + total.toFixed(2);
                    }

                    // Form validation
                    document.getElementById('bookingForm').addEventListener('submit', function (e) {
                        const dateInput = document.getElementById('bookingDate');
                        const timeInput = document.getElementById('bookingTime');
                        const agreeTerms = document.getElementById('agreeTerms');

                        if (!agreeTerms.checked) {
                            e.preventDefault();
                            alert('Please agree to the Terms and Conditions to continue.');
                            return false;
                        }

                        // Validate time (8 AM to 6 PM)
                        const timeValue = timeInput.value;
                        if (timeValue) {
                            const [hours, minutes] = timeValue.split(':').map(Number);
                            const totalMinutes = hours * 60 + minutes;

                            if (totalMinutes < 480 || totalMinutes >= 1080) { // 8:00 AM to 6:00 PM
                                e.preventDefault();
                                alert('Please select a time between 8:00 AM and 6:00 PM.');
                                timeInput.focus();
                                return false;
                            }
                        }
                    });

                    // Stripe Integration
                    const stripe = Stripe('pk_test_51SuOybRvxNai4ttVuCCZyPBdeQdQde5oUQgBUXw4kf98sgaWOaP9fmmKxF7qew9grmNVfH2B1SvXSqoleOyxX5zX006vPcKnQh');
                    const elements = stripe.elements();
                    const card = elements.create('card', {
                        style: {
                            base: {
                                fontSize: '16px',
                                color: '#32325d',
                            }
                        },
                        // Hide postal code from card element - we'll use a separate field for 6-digit Singapore postal codes
                        hidePostalCode: true
                    });
                    card.mount('#card-element');

                    const form = document.getElementById('bookingForm');
                    const submitBtn = document.getElementById('submit-button');
                    const spinner = document.getElementById('spinner');
                    const buttonText = document.getElementById('button-text');

                    let paymentProcessed = false; // Flag to track if payment is done

                    form.addEventListener('submit', async function (e) {
                        // If payment already processed, allow normal form submission
                        if (paymentProcessed) {
                            return true;
                        }

                        e.preventDefault();

                        if (!document.getElementById('agreeTerms').checked) {
                            alert('Please agree to the Terms and Conditions.');
                            return;
                        }

                        setLoading(true);

                        try {
                            // 1. Calculate Amount using current UI values
                            const duration = parseInt(document.getElementById('duration').value) || 1;
                            let subtotal = 0;
                            <c:choose>
                                <c:when test="${not empty service}">
                                    subtotal = ${service.price} * duration;
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="item" items="${cartItems}">
                                        subtotal += ${item.price} * duration;
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>

                            const amount = subtotal * 1.09;

                            console.log('Calculated amount before GST:', subtotal);
                            console.log('Final amount with GST:', amount);

                            // Stripe requires minimum $0.50 for SGD
                            if (amount < 0.50) {
                                alert('The calculated amount ($' + amount.toFixed(2) + ') is below Stripe\'s minimum of $0.50 for SGD.\n\nFor testing purposes, the system will use $0.50 as the minimum amount.');
                                console.log('Amount too small, will use minimum $0.50');
                            }

                            // 2. Create PaymentIntent on Backend
                            const formData = new URLSearchParams();
                            formData.append('amount', amount.toFixed(2));
                            formData.append('currency', 'sgd');
                            formData.append('duration', duration);
                            <c:if test="${not empty service}">
                                formData.append('serviceId', '${service.id}');
                            </c:if>

                            console.log('Sending payment request with amount:', amount.toFixed(2));

                            const response = await fetch('${pageContext.request.contextPath}/BookingServlet?action=createPaymentIntent', {
                                method: 'POST',
                                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                                body: formData
                            });

                            const data = await response.json();
                            if (data.error) throw new Error(data.error);

                            // 3. Confirm Payment with Stripe
                            const result = await stripe.confirmCardPayment(data.clientSecret, {
                                payment_method: {
                                    card: card,
                                    billing_details: {
                                        name: document.getElementById('contactName').value
                                    }
                                }
                            });

                            if (result.error) {
                                throw new Error(result.error.message);
                            } else if (result.paymentIntent.status === 'succeeded') {
                                // 4. Finalize Booking - Submit form to create booking
                                document.getElementById('paymentIntentId').value = result.paymentIntent.id;

                                // Set flag to allow form submission
                                paymentProcessed = true;

                                // Submit the form - this will POST to BookingServlet?action=save
                                // which will redirect to bookingDetails.jsp
                                form.submit();
                            }
                        } catch (err) {
                            alert(err.message);
                            setLoading(false);
                        }
                    });

                    function setLoading(isLoading) {
                        if (isLoading) {
                            submitBtn.disabled = true;
                            spinner.classList.remove('d-none');
                            buttonText.classList.add('d-none');
                        } else {
                            submitBtn.disabled = false;
                            spinner.classList.add('d-none');
                            buttonText.classList.remove('d-none');
                        }
                    }

                    // Real-time form feedback
                    const formInputs = document.querySelectorAll('.form-control, .form-select');
                    formInputs.forEach(input => {
                        input.addEventListener('blur', function () {
                            if (this.value && this.checkValidity()) {
                                this.classList.add('is-valid');
                                this.classList.remove('is-invalid');
                            } else if (this.value) {
                                this.classList.add('is-invalid');
                                this.classList.remove('is-valid');
                            }
                        });
                    });
                </script>

                <%-- Include Footer --%>
                    <jsp:include page="footer.jsp"></jsp:include>