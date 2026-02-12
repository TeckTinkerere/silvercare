<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

        <%-- Include Header --%>
            <jsp:include page="header.jsp" />

            <main class="container my-5">
                <div class="row justify-content-center">
                    <div class="col-md-8 text-center">
                        <div class="card shadow-lg border-0 rounded-4">
                            <div class="card-body p-5">
                                <div class="mb-4">
                                    <i class="fas fa-check-circle text-success" style="font-size: 5rem;"></i>
                                </div>

                                <h1 class="display-4 fw-bold text-success mb-3">Payment Successful!</h1>
                                <p class="lead mb-4">Your booking has been confirmed.</p>

                                <c:if test="${not empty param.bookingId}">
                                    <div class="alert alert-light border d-inline-block px-4 py-2 mb-4">
                                        <strong>Booking ID:</strong> #${param.bookingId}
                                    </div>
                                </c:if>

                                <p class="text-muted mb-5">
                                    We have sent a confirmation email to your registered email address.<br>
                                    You can view the details of your booking in your account.
                                </p>

                                <div class="d-grid gap-3 d-sm-flex justify-content-sm-center">
                                    <a href="${pageContext.request.contextPath}/BookingServlet?action=list"
                                        class="btn btn-primary btn-lg px-5">
                                        <i class="fas fa-calendar-check me-2"></i>My Bookings
                                    </a>
                                    <a href="${pageContext.request.contextPath}/home"
                                        class="btn btn-outline-secondary btn-lg px-5">
                                        <i class="fas fa-home me-2"></i>Home Page
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </main>

            <%-- Include Footer --%>
                <jsp:include page="footer.jsp"></jsp:include>