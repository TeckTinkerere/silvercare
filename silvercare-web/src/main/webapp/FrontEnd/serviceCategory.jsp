<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

            <%-- Include Header --%>
                <jsp:include page="header.jsp" />

                <main class="container my-5">
                    <div class="text-center mb-5">
                        <h1 class="display-5 fw-bold">Our Services</h1>
                        <p class="lead text-muted">Browse our comprehensive range of senior care services.</p>
                    </div>

                    <%-- This is where the getServices.jsp output will be included --%>
                        <c:forEach var="category" items="${categories}">
                            <section id="category-${category.id}" class="mb-5">
                                <div class="category-header d-flex align-items-center mb-4 pb-2 border-bottom">
                                    <div class="icon-wrapper bg-primary bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center me-3"
                                        style="width: 50px; height: 50px;">
                                        <i
                                            class="${empty category.icon ? 'fas fa-heart' : category.icon} text-primary fs-4"></i>
                                    </div>
                                    <div>
                                        <h2 class="h3 fw-bold mb-0">${category.name}</h2>
                                        <p class="text-muted mb-0 small">${category.description}</p>
                                    </div>
                                </div>

                                <div class="row g-4">
                                    <c:forEach var="service" items="${category.services}">
                                        <div class="col-md-6 col-lg-4">
                                            <div class="card h-100 border-0 shadow-sm service-card">
                                                <c:if test="${not empty service.imagePath}">
                                                    <img src="${pageContext.request.contextPath}/FrontEnd/images/${service.imagePath.replace('images/', '')}"
                                                        class="card-img-top" alt="${service.name}"
                                                        style="height: 200px; object-fit: cover;">
                                                </c:if>
                                                <div class="card-body">
                                                    <h5 class="card-title fw-bold">${service.name}</h5>
                                                    <p class="card-text text-muted small mb-3">${service.description}
                                                    </p>
                                                    <div class="d-flex justify-content-between align-items-center mt-3">
                                                        <span class="fs-5 fw-bold text-primary">
                                                            $
                                                            <fmt:formatNumber value="${service.price}"
                                                                minFractionDigits="2" maxFractionDigits="2" />
                                                        </span>
                                                        <div class="d-flex gap-2">
                                                            <a href="${pageContext.request.contextPath}/ServiceServlet?action=details&id=${service.id}"
                                                                class="btn btn-outline-primary btn-sm">View Details</a>
                                                            <c:if test="${not empty sessionScope.customer_id}">
                                                                <form
                                                                    action="${pageContext.request.contextPath}/CartServlet?action=add"
                                                                    method="POST" class="d-inline">
                                                                    <input type="hidden" name="id"
                                                                        value="${service.id}">
                                                                    <button type="submit"
                                                                        class="btn btn-primary btn-sm">
                                                                        <i class="fas fa-cart-plus"></i>
                                                                    </button>
                                                                </form>
                                                            </c:if>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                    <c:if test="${empty category.services}">
                                        <div class="col-12">
                                            <p class="text-muted fst-italic">No services available in this category.</p>
                                        </div>
                                    </c:if>
                                </div>
                            </section>
                        </c:forEach>

                        <c:if test="${empty categories}">
                            <div class="alert alert-info">No service categories found.</div>
                        </c:if>

                </main>

                <%-- Include Footer --%>
                    <jsp:include page="footer.jsp"></jsp:include>