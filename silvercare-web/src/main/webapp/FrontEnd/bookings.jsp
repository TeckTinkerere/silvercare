<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

            <%-- Redirect if not logged in --%>
                <%-- Redirect if not logged in --%>
                    <% Object cIdLogCheck=session.getAttribute("customer_id"); if (cIdLogCheck==null) {
                        response.sendRedirect(request.getContextPath() + "/FrontEnd/login.jsp" ); return; } %>

                        <%-- Include Header --%>
                            <jsp:include page="header.jsp" />

                            <main class="container my-5">
                                <div class="d-flex justify-content-between align-items-center mb-4">
                                    <h1 class="h2 mb-0">My Bookings</h1>
                                </div>

                                <%-- Error and Success Messages --%>
                                    <c:if test="${not empty param.error}">
                                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                            <i class="fas fa-exclamation-circle me-2"></i>
                                            <strong>Error:</strong>
                                            <c:choose>
                                                <c:when test="${param.error == 'database_error'}">A database error
                                                    occurred.</c:when>
                                                <c:when test="${param.error == 'unauthorized_booking'}">You are not
                                                    authorized to view this booking.</c:when>
                                                <c:otherwise>${param.error}</c:otherwise>
                                            </c:choose>
                                            <c:if test="${not empty param.msg}">
                                                <div class="mt-1 small">${param.msg}</div>
                                            </c:if>
                                            <button type="button" class="btn-close" data-bs-dismiss="alert"
                                                aria-label="Close"></button>
                                        </div>
                                    </c:if>

                                    <c:if test="${param.success == 'true'}">
                                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                                            <i class="fas fa-check-circle me-2"></i>
                                            Operation completed successfully!
                                            <button type="button" class="btn-close" data-bs-dismiss="alert"
                                                aria-label="Close"></button>
                                        </div>
                                    </c:if>

                                    <div class="card shadow-sm">
                                        <div class="card-body">
                                            <div class="table-responsive">
                                                <c:choose>
                                                    <c:when test="${not empty bookings}">
                                                        <table class="table table-hover align-middle">
                                                            <thead class="table-light">
                                                                <tr>
                                                                    <th>Booking ID</th>
                                                                    <th>Date</th>
                                                                    <th>Total Amount</th>
                                                                    <th>Status</th>
                                                                    <th>Actions</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <c:forEach var="booking" items="${bookings}">
                                                                    <tr>
                                                                        <td>#${booking.id}</td>
                                                                        <td>
                                                                            <c:if
                                                                                test="${not empty booking.bookingDate}">
                                                                                <fmt:parseDate
                                                                                    value="${booking.bookingDate}"
                                                                                    pattern="yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
                                                                                    var="parsedDate" />
                                                                                <fmt:formatDate value="${parsedDate}"
                                                                                    pattern="MMM dd, yyyy HH:mm" />
                                                                            </c:if>
                                                                            <c:if test="${empty booking.bookingDate}">
                                                                                <span class="text-muted">N/A</span>
                                                                            </c:if>
                                                                        </td>
                                                                        <td>
                                                                            <c:if
                                                                                test="${not empty booking.totalAmount}">
                                                                                <fmt:setLocale value="en_US" />
                                                                                <fmt:formatNumber
                                                                                    value="${booking.totalAmount}"
                                                                                    type="currency"
                                                                                    currencySymbol="$" />
                                                                            </c:if>
                                                                            <c:if test="${empty booking.totalAmount}">
                                                                                $0.00
                                                                            </c:if>
                                                                        </td>
                                                                        <td>
                                                                            <c:choose>
                                                                                <c:when
                                                                                    test="${booking.status == 'Confirmed'}">
                                                                                    <span
                                                                                        class="badge bg-primary">Confirmed</span>
                                                                                </c:when>
                                                                                <c:when
                                                                                    test="${booking.status == 'Pending'}">
                                                                                    <span
                                                                                        class="badge bg-warning text-dark">Pending</span>
                                                                                </c:when>
                                                                                <c:when
                                                                                    test="${booking.status == 'Cancelled'}">
                                                                                    <span
                                                                                        class="badge bg-danger">Cancelled</span>
                                                                                </c:when>
                                                                                <c:when
                                                                                    test="${booking.status == 'Completed'}">
                                                                                    <span
                                                                                        class="badge bg-success">Completed</span>
                                                                                </c:when>
                                                                                <c:when
                                                                                    test="${booking.status == 'Refunded'}">
                                                                                    <span
                                                                                        class="badge bg-info text-dark">Refunded</span>
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    <span
                                                                                        class="badge bg-secondary">${booking.status}</span>
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                        </td>
                                                                        <td>
                                                                            <a href="${pageContext.request.contextPath}/BookingServlet?action=details&id=${booking.id}"
                                                                                class="btn btn-sm btn-outline-primary">
                                                                                View Details
                                                                            </a>
                                                                        </td>
                                                                    </tr>
                                                                </c:forEach>
                                                            </tbody>
                                                        </table>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="text-center py-5">
                                                            <i class="fas fa-calendar-times fa-3x text-muted mb-3"></i>
                                                            <p class="lead text-muted">No bookings found.</p>
                                                            <a href="${pageContext.request.contextPath}/ServiceServlet?action=category"
                                                                class="btn btn-primary">Browse Services</a>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </div>
                            </main>

                            <%-- Include Footer --%>
                                <jsp:include page="footer.jsp"></jsp:include>