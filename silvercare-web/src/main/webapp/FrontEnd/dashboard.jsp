<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<% 
    try {
        // Simple authentication check
        if (session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
%>

<!DOCTYPE html>
<html>

            <head>
                <title>My Dashboard - SilverCare</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/FrontEnd/styles.css">
                <script>
                    function pollStatus(bookingId) {
                        setInterval(function () {
                            fetch('${pageContext.request.contextPath}/poll/status?bookingId=' + bookingId)
                                .then(response => response.json())
                                .then(data => {
                                    var el = document.getElementById('status-' + bookingId);
                                    if (el && data.status !== el.innerText) {
                                        el.innerText = data.status;
                                        el.classList.add('updated'); // Can add CSS flash effect
                                    }
                                })
                                .catch(err => console.error(err));
                        }, 5000); // Poll every 5 seconds
                    }
                </script>
                <style>
                    .updated {
                        background-color: #e0ffe0;
                        transition: background-color 1s;
                    }
                </style>
            </head>

            <body>
                <jsp:include page="header.jsp" />

                <div class="container">
                    <h1>Welcome, ${user.fullName}</h1>

                    <div class="profile-section">
                        <img src="${pageContext.request.contextPath}/FrontEnd/images/${user.profilePicture != null ? user.profilePicture : 'default-avatar.png'}"
                            width="100">
                        <p>Email: ${user.email}</p>
                        <p>Phone: ${user.phone}</p>
                    </div>

                    <h2>Your Bookings</h2>
                    <table border="1">
                        <thead>
                            <tr>
                                <th>Booking ID</th>
                                <th>Date</th>
                                <th>Total</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="b" items="${bookings}">
                                <tr>
                                    <td>${b.id}</td>
                                    <td>${b.bookingDate}</td>
                                    <td>$${b.totalAmount}</td>
                                    <td id="status-${b.id}">
                                        ${b.status}
                                        <script>pollStatus('${b.id}');</script>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty bookings}">
                                <tr>
                                    <td colspan="4">No bookings found. <a
                                            href="${pageContext.request.contextPath}/ServiceServlet?action=category">Book
                                            a Service</a></td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>

                <jsp:include page="footer.jsp" />
            </body>

            </html>