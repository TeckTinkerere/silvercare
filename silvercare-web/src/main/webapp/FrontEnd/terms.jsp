<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Terms of Service - SilverCare</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/FrontEnd/styles.css">
    </head>

    <body>
        <jsp:include page="/FrontEnd/header.jsp" />

        <div class="container my-5">
            <h1 class="mb-4">Terms of Service</h1>
            <p>Last updated: February 2026</p>

            <h3>1. Acceptance of Terms</h3>
            <p>By accessing or using SilverCare services, you agree to be bound by these Terms.</p>

            <h3>2. Use of Services</h3>
            <p>You agree to use our services only for lawful purposes and in accordance with these Terms.</p>

            <h3>3. Booking and Payments</h3>
            <p>All bookings are subject to availability and confirmation. Payments must be made in accordance with our
                pricing policy.</p>

            <h3>4. Limitation of Liability</h3>
            <p>SilverCare shall not be liable for any indirect, incidental, special, consequential, or punitive damages.
            </p>
        </div>

        <jsp:include page="/FrontEnd/footer.jsp" />
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>

    </html>