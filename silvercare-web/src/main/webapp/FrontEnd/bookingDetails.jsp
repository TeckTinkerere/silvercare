<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

            <%-- Redirect if not logged in --%>
                <c:if test="${empty sessionScope.customer_id}">
                    <c:redirect url="login.jsp" />
                </c:if>

                <%-- Include Header --%>
                    <jsp:include page="header.jsp" />

                    <main class="container my-5">
                        <div class="mb-3">
                            <a href="${pageContext.request.contextPath}/BookingServlet?action=list"
                                class="text-decoration-none">
                                <i class="fas fa-arrow-left me-2"></i>Back to Booking List
                            </a>
                        </div>

                        <div class="card shadow-sm mb-4">
                            <div class="card-header bg-light py-3">
                                <div class="d-flex justify-content-between align-items-center">
                                    <h1 class="h4 mb-0">Booking #${booking.id}</h1>
                                    <span
                                        class="badge ${booking.status == 'Pending' ? 'bg-warning' : (booking.status == 'Confirmed' ? 'bg-success' : 'bg-secondary')} fs-6">
                                        ${booking.status}
                                    </span>
                                </div>
                            </div>
                            <div class="card-body">
                                <div class="row mb-4">
                                    <div class="col-md-6">
                                        <h5 class="card-title text-muted">Booking Information</h5>
                                        <p class="card-text">
                                            <strong>Date:</strong>
                                            <fmt:formatDate value="${booking.bookingDate}"
                                                pattern="MMM dd, yyyy HH:mm" /><br>
                                            <strong>Total Amount:</strong>
                                            <fmt:setLocale value="en_US" />
                                            <fmt:formatNumber value="${booking.totalAmount}" type="currency"
                                                currencySymbol="$" /><br>
                                            <strong>GST:</strong>
                                            <fmt:formatNumber value="${booking.gstAmount}" type="currency"
                                                currencySymbol="$" />
                                        </p>
                                    </div>
                                </div>

                                <h5 class="card-title text-muted border-bottom pb-2 mb-3">Services</h5>
                                <div class="table-responsive">
                                    <table class="table align-middle">
                                        <thead class="table-light">
                                            <tr>
                                                <th>Service Name</th>
                                                <th>Note</th>
                                                <th>Quantity</th>
                                                <th>Unit Price</th>
                                                <th>Action</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="detail" items="${booking.details}">
                                                <tr>
                                                    <td>${detail.serviceName}</td>
                                                    <td>${detail.notes}</td>
                                                    <td>${detail.quantity}</td>
                                                    <td>
                                                        <fmt:formatNumber value="${detail.unitPrice}" type="currency"
                                                            currencySymbol="$" />
                                                    </td>
                                                    <td>
                                                        <!-- Leave Feedback button only works for existing bookings. 
                                         In a real app, maybe only if status is Completed. 
                                         For assignment, we allow it. -->
                                                        <a href="${pageContext.request.contextPath}/FeedbackServlet?action=form&service_id=${detail.serviceId}&booking_id=${booking.id}"
                                                            class="btn btn-sm btn-outline-primary">
                                                            <i class="fas fa-star me-1"></i> Feedback
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </main>

                    <%-- Include Footer --%>
                        <jsp:include page="footer.jsp"></jsp:include>