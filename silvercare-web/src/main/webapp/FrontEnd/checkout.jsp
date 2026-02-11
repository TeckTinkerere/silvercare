<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!-- WARNING: This is a PLACEHOLDER IMPLEMENTATION -->
    <!--
    ═══════════════════════════════════════════════════════════════════════════════
    CRITICAL SECURITY ISSUE: This page collects raw card details which is NOT PCI compliant.
    
    PROPER IMPLEMENTATION REQUIRED:
    1. Add Stripe.js to your page: <script src="https://js.stripe.com/v3/"></script>
    2. Replace card inputs with Stripe Elements (secure hosted fields)
    3. Get publishable key from server: <%= application.getAttribute("stripePublishableKey") %>
    4. On form submit:
       a) Create PaymentIntent on server (call /api/payment/create-intent)
       b) Use Stripe.js to confirm payment with client_secret
       c) On success, submit booking with paymentIntentId
    
    Example flow:
       Frontend -> /api/payment/create-intent -> Get client_secret
       Frontend -> Stripe.confirmPayment (with Stripe Elements)
       Frontend -> /api/bookings (with paymentIntentId) -> Create booking
    
    DO NOT use this implementation in production!
    ═══════════════════════════════════════════════════════════════════════════════
    -->
    <jsp:include page="header.jsp" />
    <div class="container my-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="alert alert-warning" role="alert">
                    <strong>⚠️ Development Mode:</strong> This checkout uses simulated payment.
                    For production, integrate <a href="https://stripe.com/docs/payments/payment-intents"
                        target="_blank">Stripe Elements</a>.
                </div>

                <h2>Checkout</h2>
                <form action="${pageContext.request.contextPath}/BookingServlet?action=save" method="POST"
                    id="checkout-form">
                    <!-- Order Summary -->
                    <div class="card mb-4">
                        <div class="card-header bg-primary text-white">
                            <h5 class="mb-0">Order Summary</h5>
                        </div>
                        <div class="card-body">
                            <div class="d-flex justify-content-between mb-2">
                                <span>Service Price (Subtotal)</span>
                                <span id="displaySubtotal" class="fw-semibold">$0.00</span>
                            </div>
                            <div class="d-flex justify-content-between mb-2 text-muted">
                                <span>GST (9%)</span>
                                <span id="displayGST">$0.00</span>
                            </div>
                            <hr>
                            <div class="d-flex justify-content-between mb-2">
                                <span class="fw-bold fs-5">Grand Total</span>
                                <span id="displayTotal" class="fw-bold fs-5 text-primary">$0.00</span>
                            </div>

                            <input type="hidden" id="price" name="price" value="${param.price}">
                            <input type="hidden" name="service_id" value="${param.service_id}">
                            <input type="hidden" name="bookingDate" value="${param.bookingDate}">
                            <input type="hidden" name="bookingTime" value="${param.bookingTime}">
                            <input type="hidden" name="notes" value="${param.notes}">
                        </div>
                    </div>

                    <!-- Simulated Payment Section -->
                    <div class="card mb-4">
                        <div class="card-header bg-secondary text-white">
                            <h5 class="mb-0">Payment Details (Simulated)</h5>
                        </div>
                        <div class="card-body">
                            <div class="alert alert-info mb-3">
                                <strong>Test Mode:</strong> Use card number <code>4242424242424242</code> for testing.
                                Any future date and CVV will work.
                            </div>

                            <!-- TODO: Replace with Stripe Elements -->
                            <div class="mb-3">
                                <label for="cardNumber" class="form-label">Card Number</label>
                                <input type="text" class="form-control" id="cardNumber" name="cardNumber"
                                    placeholder="4242 4242 4242 4242" pattern="[0-9]{16}" value="4242424242424242"
                                    required>
                                <small class="text-muted">16 digits, no spaces</small>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="cardExpiry" class="form-label">Expiry Date</label>
                                    <input type="text" class="form-control" id="cardExpiry" name="cardExpiry"
                                        placeholder="MM/YY" value="12/25" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="cardCvv" class="form-label">CVV</label>
                                    <input type="text" class="form-control" id="cardCvv" name="cardCvv"
                                        placeholder="123" pattern="[0-9]{3}" value="123" required>
                                </div>
                            </div>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-primary btn-lg w-100">
                        <i class="bi bi-lock-fill me-2"></i>Complete Booking
                    </button>
                </form>
            </div>
        </div>
    </div>

    <script>
        // Calculate totals
        function updateTotals() {
            var price = parseFloat(document.getElementById('price').value) || 0;
            var gstRate = 0.09;
            var subtotal = price;
            var gst = subtotal * gstRate;
            var total = subtotal + gst;

            document.getElementById('displaySubtotal').innerText = "$" + subtotal.toFixed(2);
            document.getElementById('displayGST').innerText = "$" + gst.toFixed(2);
            document.getElementById('displayTotal').innerText = "$" + total.toFixed(2);
        }

        // Initialize on page load
        window.addEventListener('load', updateTotals);
    </script>

    <jsp:include page="footer.jsp" />