<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>SilverCare - Services</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/FrontEnd/styles.css">
    <style>
        .service-card { border: 1px solid #ddd; padding: 15px; margin: 10px; display: inline-block; width: 300px; vertical-align: top; }
        .service-img { width: 100%; height: 200px; object-fit: cover; }
    </style>
</head>
<body>
    <jsp:include page="header.jsp" />

    <div class="container">
        <h1>Our Care Services</h1>
        
        <div class="categories">
            <c:forEach var="cat" items="${categories}">
                <button onclick="filter('${cat.id}')">${cat.name}</button>
            </c:forEach>
            <button onclick="filter('all')">All</button>
        </div>

        <div class="services-grid">
            <c:forEach var="service" items="${services}">
                <div class="service-card" data-cat="${service.categoryId}">
                    <img src="${pageContext.request.contextPath}/FrontEnd/${service.imagePath != null ? service.imagePath : 'images/default.jpg'}" class="service-img" alt="${service.name}">
                    <h3>${service.name}</h3>
                    <p>${service.description}</p>
                    <p class="price">$${service.price} / unit</p>
                    <form action="${pageContext.request.contextPath}/FrontEnd/bookingForm.jsp" method="get">
                        <input type="hidden" name="service_id" value="${service.id}">
                        <input type="hidden" name="price" value="${service.price}">
                        <button type="submit">Book Now</button>
                    </form>
                </div>
            </c:forEach>
        </div>
    </div>
    
    <script>
        function filter(catId) {
            var cards = document.getElementsByClassName('service-card');
            for (var i = 0; i < cards.length; i++) {
                if (catId === 'all' || cards[i].getAttribute('data-cat') === catId) {
                    cards[i].style.display = 'inline-block';
                } else {
                    cards[i].style.display = 'none';
                }
            }
        }
    </script>
    <jsp:include page="footer.jsp" />
</body>
</html>
