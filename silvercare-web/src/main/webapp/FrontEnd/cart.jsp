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
                                                <form id="cartSelectionForm"
                                                    action="${pageContext.request.contextPath}/BookingServlet?action=prepareSelection"
                                                    method="POST">
                                                    <div class="table-responsive">
                                                        <table class="table align-middle">
                                                            <thead class="table-light">
                                                                <tr>
                                                                    <th style="width: 40px;">
                                                                        <div class="form-check">
                                                                            <input class="form-check-input"
                                                                                type="checkbox" id="selectAll" checked>
                                                                        </div>
                                                                    </th>
                                                                    <th colspan="2">Service</th>
                                                                    <th>Quantity</th>
                                                                    <th class="text-end">Price</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <c:set var="totalPrice" value="0" />
                                                                <c:forEach var="item" items="${cart}">
                                                                    <c:set var="itemQty"
                                                                        value="${empty item.qty ? 1 : item.qty}" />
                                                                    <c:set var="lineTotal"
                                                                        value="${item.price * itemQty}" />
                                                                    <c:set var="totalPrice"
                                                                        value="${totalPrice + lineTotal}" />
                                                                    <tr>
                                                                        <td>
                                                                            <div class="form-check">
                                                                                <input
                                                                                    class="form-check-input item-checkbox"
                                                                                    type="checkbox" name="selectedItems"
                                                                                    value="${item.id}" checked>
                                                                            </div>
                                                                        </td>
                                                                        <td style="width: 80px;">
                                                                            <c:if test="${not empty item.image}">
                                                                                <img src="${pageContext.request.contextPath}/FrontEnd/images/${item.image.replace('images/', '')}"
                                                                                    alt="${item.name}"
                                                                                    class="img-fluid rounded shadow-sm">
                                                                            </c:if>
                                                                        </td>
                                                                        <td>
                                                                            <h6 class="mb-1 fw-bold">${item.name}</h6>
                                                                            <p class="text-muted small mb-0">
                                                                                ${item.description}</p>
                                                                        </td>
                                                                        <td>
                                                                            <div
                                                                                class="d-flex align-items-center gap-2">
                                                                                <a href="${pageContext.request.contextPath}/CartServlet?action=update&id=${item.id}&qty=${itemQty - 1}"
                                                                                    class="btn btn-outline-secondary btn-sm ${itemQty <= 1 ? 'disabled' : ''}">-</a>
                                                                                <span class="mx-2">${itemQty}</span>
                                                                                <a href="${pageContext.request.contextPath}/CartServlet?action=update&id=${item.id}&qty=${itemQty + 1}"
                                                                                    class="btn btn-outline-secondary btn-sm">+</a>
                                                                            </div>
                                                                        </td>
                                                                        <td class="text-end">
                                                                            <div class="price-summary">
                                                                                <span class="fw-bold">$
                                                                                    <fmt:formatNumber
                                                                                        value="${lineTotal}"
                                                                                        type="number"
                                                                                        minFractionDigits="2"
                                                                                        maxFractionDigits="2" />
                                                                                </span>
                                                                                <br>
                                                                                <a href="${pageContext.request.contextPath}/CartServlet?action=remove&id=${item.id}"
                                                                                    class="btn btn-link btn-sm text-danger p-0 mt-1">
                                                                                    <i class="fas fa-trash-alt"></i>
                                                                                </a>
                                                                            </div>
                                                                        </td>
                                                                    </tr>
                                                                </c:forEach>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </form>
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
                                            <button type="button" id="proceedToBookingBtn"
                                                class="btn btn-primary ${empty cart ? 'disabled' : ''}">Proceed
                                                to Booking</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </main>

                    <%-- Include Footer --%>
                        <jsp:include page="footer.jsp"></jsp:include>

                        <script>
                            document.addEventListener('DOMContentLoaded', function () {
                                const selectAll = document.getElementById('selectAll');
                                const itemCheckboxes = document.querySelectorAll('.item-checkbox');
                                const selectionForm = document.getElementById('cartSelectionForm');
                                const proceedBtn = document.getElementById('proceedToBookingBtn');

                                // Handle Select All
                                if (selectAll) {
                                    selectAll.addEventListener('change', function () {
                                        itemCheckboxes.forEach(cb => {
                                            cb.checked = selectAll.checked;
                                        });
                                        updateTotal();
                                    });
                                }

                                // Update total when selection changes
                                itemCheckboxes.forEach(cb => {
                                    cb.addEventListener('change', function () {
                                        updateTotal();
                                        // Update Select All state
                                        const allChecked = Array.from(itemCheckboxes).every(c => c.checked);
                                        selectAll.checked = allChecked;
                                    });
                                });

                                // Submit selection form
                                if (proceedBtn) {
                                    proceedBtn.addEventListener('click', function () {
                                        const selectedCount = Array.from(itemCheckboxes).filter(c => c.checked).length;
                                        if (selectedCount === 0) {
                                            alert('Please select at least one service to proceed.');
                                            return;
                                        }
                                        selectionForm.submit();
                                    });
                                }

                                function updateTotal() {
                                    let subtotal = 0;
                                    itemCheckboxes.forEach(cb => {
                                        if (cb.checked) {
                                            // Find the price in the same row
                                            const row = cb.closest('tr');
                                            const priceText = row.querySelector('.price-summary span').innerText;
                                            const price = parseFloat(priceText.replace('$', '').replace(',', ''));
                                            subtotal += price;
                                        }
                                    });

                                    const gst = subtotal * 0.09;
                                    const grandTotal = subtotal + gst;

                                    // Update display
                                    const summaryCard = document.querySelector('.col-lg-4 .card-body');
                                    if (summaryCard) {
                                        summaryCard.querySelector('div:nth-child(2) span:last-child').innerText = '$' + subtotal.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 });
                                        summaryCard.querySelector('div:nth-child(3) span:last-child').innerText = '$' + gst.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 });
                                        summaryCard.querySelector('.fw-bold.fs-5').innerText = '$' + grandTotal.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 });
                                    }

                                    // Enable/disable proceed button
                                    if (proceedBtn) {
                                        proceedBtn.classList.toggle('disabled', subtotal === 0);
                                    }
                                }
                            });
                        </script>