<%@ page import="java.sql.*, java.util.*, java.io.*" %>
    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
            <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

                <%-- Include Header --%>
                    <jsp:include page="header.jsp" />

                    <main class="container my-5">
                        <div class="text-center mb-5">
                            <h1 class="display-5 fw-bold">Your Cart</h1>
                            <p class="lead text-muted">Review the services you've selected for booking.</p>
                        </div>

                        <c:if test="${param.removed eq 'true'}">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                <i class="fas fa-check-circle me-2"></i>
                                <strong>Success!</strong> Item removed from cart.
                                <button type="button" class="btn-close" data-bs-dismiss="alert"
                                    aria-label="Close"></button>
                            </div>
                        </c:if>
                        <c:if test="${param.added eq 'true'}">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                <i class="fas fa-check-circle me-2"></i>
                                <strong>Success!</strong> Item added to cart.
                                <button type="button" class="btn-close" data-bs-dismiss="alert"
                                    aria-label="Close"></button>
                            </div>
                        </c:if>
                        <c:if test="${param.updated eq 'true'}">
                            <div class="alert alert-info alert-dismissible fade show" role="alert">
                                <i class="fas fa-sync-alt me-2"></i>
                                <strong>Updated!</strong> Cart quantity has been updated.
                                <button type="button" class="btn-close" data-bs-dismiss="alert"
                                    aria-label="Close"></button>
                            </div>
                        </c:if>
                        <c:if test="${param.error eq 'invalid_id'}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="fas fa-exclamation-circle me-2"></i>
                                <strong>Error!</strong> Invalid service ID.
                                <button type="button" class="btn-close" data-bs-dismiss="alert"
                                    aria-label="Close"></button>
                            </div>
                        </c:if>

                        <div class="row">
                            <div class="col-lg-8">
                                <div class="card shadow-sm">
                                    <div class="card-body">
                                        <c:choose>
                                            <c:when test="${empty cart}">
                                                <p>Your cart is empty.</p>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="totalPrice" value="0" />
                                                <c:forEach var="item" items="${cart}">
                                                    <c:set var="itemQty" value="${empty item.qty ? 1 : item.qty}" />
                                                    <c:set var="lineTotal" value="${item.price * itemQty}" />
                                                    <c:set var="totalPrice" value="${totalPrice + lineTotal}" />
                                                    <div class="border-bottom py-4">
                                                        <div class="row align-items-center">
                                                            <div class="col-md-2 d-none d-md-block">
                                                                <c:if test="${not empty item.image}">
                                                                    <img src="${pageContext.request.contextPath}/FrontEnd/images/${item.image.replace('images/', '')}"
                                                                        alt="${item.name}"
                                                                        class="img-fluid rounded shadow-sm">
                                                                </c:if>
                                                            </div>
                                                            <div class="col-md-4">
                                                                <h5 class="mb-1 fw-bold">${item.name}</h5>
                                                                <p class="text-muted small mb-0">${item.description}</p>
                                                            </div>
                                                            <div class="col-md-3">
                                                                <form method="POST"
                                                                    action="${pageContext.request.contextPath}/CartServlet"
                                                                    class="d-flex align-items-center gap-2">
                                                                    <input type="hidden" name="action" value="update">
                                                                    <input type="hidden" name="id" value="${item.id}">
                                                                    <label
                                                                        class="form-label mb-0 small text-muted">Qty:</label>
                                                                    <input type="number" name="qty" value="${itemQty}"
                                                                        min="1" max="10"
                                                                        class="form-control form-control-sm"
                                                                        style="width: 65px;">
                                                                    <button type="submit"
                                                                        class="btn btn-outline-primary btn-sm">
                                                                        <i class="fas fa-sync-alt"></i>
                                                                    </button>
                                                                </form>
                                                            </div>
                                                            <div class="col-md-3 text-end">
                                                                <div class="price-summary mb-2">
                                                                    <span class="text-muted small">Unit: $
                                                                        <fmt:formatNumber value="${item.price}"
                                                                            type="currency" currencySymbol="" />
                                                                    </span><br>
                                                                    <span class="fw-bold fs-5">$
                                                                        <fmt:formatNumber value="${lineTotal}"
                                                                            type="currency" currencySymbol="" />
                                                                    </span>
                                                                </div>
                                                                <a href="${pageContext.request.contextPath}/CartServlet?action=remove&id=${item.id}"
                                                                    class="btn btn-outline-danger btn-sm rounded-pill">
                                                                    <i class="fas fa-trash-alt me-1"></i> Remove
                                                                </a>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-4">
                                <div class="card shadow-sm">
                                    <div class="card-body">
                                        <h5 class="card-title">Summary</h5>
                                        <div class="d-flex justify-content-between my-2">
                                            <span>Subtotal</span>
                                            <span>$
                                                <fmt:formatNumber value="${totalPrice}" type="currency"
                                                    currencySymbol="" />
                                            </span>
                                        </div>
                                        <div class="d-flex justify-content-between my-2 text-muted">
                                            <span>GST (9%)</span>
                                            <span>$
                                                <fmt:formatNumber value="${totalPrice * 0.09}" type="currency"
                                                    currencySymbol="" />
                                            </span>
                                        </div>
                                        <hr>
                                        <div class="d-flex justify-content-between my-3">
                                            <span class="fw-bold">Grand Total</span>
                                            <span class="fw-bold fs-5">
                                                $
                                                <fmt:formatNumber value="${totalPrice * 1.09}" type="currency"
                                                    currencySymbol="" />
                                            </span>
                                        </div>
                                        <div class="d-grid">
                                            <a href="${pageContext.request.contextPath}/BookingServlet?action=form"
                                                class="btn btn-primary ${empty cart ? 'disabled' : ''}">Proceed
                                                to Booking</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </main>

                    <%-- Include Footer --%>
                        <jsp:include page="footer.jsp"></jsp:include>