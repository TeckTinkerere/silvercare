<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
            <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

                <%-- Include Header --%>
                    <jsp:include page="header.jsp" />

                    <main class="container my-5">
                        <div class="bg-white p-5 rounded shadow-sm">
                            <div class="row">
                                <div class="col-lg-6">
                                    <!-- Fixed size image container to maintain consistent display -->
                                    <div style="width: 100%; height: 400px; overflow: hidden; border-radius: 0.375rem;">
                                        <c:if test="${not empty service.imagePath}">
                                            <img src="${pageContext.request.contextPath}/FrontEnd/images/${fn:replace(service.imagePath, 'images/', '')}"
                                                alt="${service.name}"
                                                style="width: 100%; height: 100%; object-fit: cover;">
                                        </c:if>
                                    </div>
                                </div>
                                <div class="col-lg-6">
                                    <h1 class="display-5 fw-bold">${service.name}</h1>
                                    <p class="lead"><span class="badge bg-secondary">${categoryName}</span></p>
                                    <p>${service.description}</p>
                                    
                                    <c:choose>
                                        <c:when test="${service.seasonalPrice != null && service.seasonalPrice != service.price}">
                                            <%-- Show seasonal price with base price strikethrough --%>
                                            <div class="my-4">
                                                <div class="d-flex align-items-center gap-3 mb-2">
                                                    <p class="display-6 fw-bold text-primary mb-0">
                                                        $<fmt:formatNumber value="${service.seasonalPrice}" minFractionDigits="2" maxFractionDigits="2" />
                                                    </p>
                                                    <c:if test="${service.seasonalPrice > service.price}">
                                                        <span class="badge bg-warning text-dark fs-6">
                                                            <i class="fas fa-arrow-up"></i> Peak Season Pricing
                                                        </span>
                                                    </c:if>
                                                    <c:if test="${service.seasonalPrice < service.price}">
                                                        <span class="badge bg-success fs-6">
                                                            <i class="fas fa-tag"></i> Seasonal Discount
                                                        </span>
                                                    </c:if>
                                                </div>
                                                <p class="text-muted text-decoration-line-through mb-0">
                                                    Regular Price: $<fmt:formatNumber value="${service.price}" minFractionDigits="2" maxFractionDigits="2" />
                                                </p>
                                                <c:if test="${service.seasonalPrice < service.price}">
                                                    <p class="text-success fw-bold mb-0">
                                                        You save: $<fmt:formatNumber value="${service.price - service.seasonalPrice}" minFractionDigits="2" maxFractionDigits="2" />
                                                    </p>
                                                </c:if>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <%-- Show regular price only --%>
                                            <p class="display-6 fw-bold my-4">
                                                $<fmt:formatNumber value="${service.price}" minFractionDigits="2" maxFractionDigits="2" />
                                            </p>
                                        </c:otherwise>
                                    </c:choose>

                                    <!-- Add to Cart Form -->
                                    <form action="${pageContext.request.contextPath}/CartServlet?action=add"
                                        method="POST">
                                        <input type="hidden" name="id" value="${service.id}">
                                        <div class="d-grid gap-2">
                                            <button type="submit" class="btn btn-primary btn-lg">Add to Cart</button>
                                        </div>
                                    </form>

                                    <a href="${pageContext.request.contextPath}/ServiceServlet?action=category"
                                        class="btn btn-link mt-3">&larr; Back to Services</a>
                                </div>
                            </div>
                        </div>
                    </main>

                    <%-- Include Footer --%>
                        <jsp:include page="footer.jsp"></jsp:include>