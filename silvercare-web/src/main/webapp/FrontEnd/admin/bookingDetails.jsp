<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Booking Details - SilverCare Admin</title>

                <!-- Google Fonts -->
                <link
                    href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&family=Inter:wght@400;500&display=swap"
                    rel="stylesheet">

                <!-- Bootstrap 5 -->
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

                <!-- Bootstrap Icons -->
                <link rel="stylesheet"
                    href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

                <!-- Global Styles -->
                <link rel="stylesheet" href="${pageContext.request.contextPath}/FrontEnd/styles.css">
                <style>
                    :root {
                        --sidebar-width: 280px;
                        --primary-color: #5b7c99;
                        --secondary-color: #2c3e50;
                        --accent-color: #f39c12;
                    }

                    body {
                        background-color: #f8f9fa;
                        overflow-x: hidden;
                    }

                    .sidebar {
                        width: var(--sidebar-width);
                        background: linear-gradient(180deg, var(--primary-color) 0%, var(--secondary-color) 100%);
                        color: white;
                        position: fixed;
                        left: calc(-1 * var(--sidebar-width));
                        top: 0;
                        height: 100vh;
                        z-index: 1040;
                        transition: all 0.3s ease;
                        box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
                    }

                    .sidebar.show {
                        left: 0;
                    }

                    .sidebar-brand {
                        padding: 1.5rem;
                        border-bottom: 1px solid rgba(255, 255, 255, 0.1);
                    }

                    .nav-link {
                        color: rgba(255, 255, 255, 0.8);
                        padding: .875rem 1.5rem;
                        margin: .25rem .75rem;
                        border-radius: 8px;
                        font-weight: 500;
                        display: flex;
                        align-items: center;
                        transition: .2s;
                    }

                    .nav-link i {
                        margin-right: .75rem;
                    }

                    .nav-link.active,
                    .nav-link:hover {
                        background-color: var(--accent-color);
                        color: white;
                    }

                    .content {
                        padding: 1rem;
                        width: 100%;
                        transition: all 0.3s ease;
                    }

                    @media (min-width: 992px) {
                        .sidebar {
                            left: 0;
                        }

                        .content {
                            margin-left: var(--sidebar-width);
                            width: calc(100% - var(--sidebar-width));
                            padding: 2rem;
                        }
                    }
                </style>
            </head>

            <body>
                <!-- Sidebar -->
                <div class="sidebar d-flex flex-column" id="sidebar">
                    <div class="sidebar-brand d-flex justify-content-between align-items-center">
                        <a href="${pageContext.request.contextPath}/admin/dashboard"
                            class="text-white text-decoration-none h4 mb-0">
                            <i class="bi bi-heart-fill"></i> SilverCare
                        </a>
                    </div>

                    <ul class="nav nav-pills flex-column mt-3">
                        <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link"><i
                                    class="bi bi-grid"></i>Dashboard</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/services" class="nav-link"><i
                                    class="bi bi-box-seam"></i>Services</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/categories" class="nav-link"><i
                                    class="bi bi-tags"></i>Categories</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/bookings" class="nav-link active"><i
                                    class="bi bi-calendar-check"></i>Bookings</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/feedback" class="nav-link"><i
                                    class="bi bi-chat-left-text"></i>Feedback</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/users" class="nav-link"><i
                                    class="bi bi-people"></i>Users</a></li>
                    </ul>
                </div>

                <div class="content">
                    <div class="mb-4">
                        <nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a
                                        href="${pageContext.request.contextPath}/admin/bookings">Bookings</a></li>
                                <li class="breadcrumb-item active" aria-current="page">Booking #${booking.id}</li>
                            </ol>
                        </nav>
                        <div class="d-flex justify-content-between align-items-center">
                            <h1><i class="bi bi-info-circle text-primary me-2"></i>Booking Details</h1>
                            <a href="${pageContext.request.contextPath}/admin/bookings"
                                class="btn btn-outline-secondary">
                                <i class="bi bi-arrow-left"></i> Back to List
                            </a>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-lg-8">
                            <div class="card shadow-sm border-0 mb-4">
                                <div
                                    class="card-header bg-white py-3 border-bottom d-flex justify-content-between align-items-center">
                                    <h5 class="mb-0">General Information</h5>
                                    <span
                                        class="badge rounded-pill 
                                        ${booking.status == 'Pending' ? 'bg-warning text-dark' : 
                                         (booking.status == 'Confirmed' ? 'bg-info text-white' :
                                         (booking.status == 'Completed' ? 'bg-success text-white' : 'bg-secondary text-white'))} fs-6">
                                        ${booking.status}
                                    </span>
                                </div>
                                <div class="card-body">
                                    <div class="row mb-3">
                                        <div class="col-sm-4 text-muted">Booking Date & Time</div>
                                        <div class="col-sm-8 fw-semibold">
                                            <fmt:parseDate value="${booking.bookingDate}"
                                                pattern="yyyy-MM-dd'T'HH:mm:ss.SSSXXX" var="parsedDate" />
                                            <fmt:formatDate value="${parsedDate}" pattern="dd MMM yyyy, HH:mm a" />
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <div class="col-sm-4 text-muted">Created At</div>
                                        <div class="col-sm-8 fw-semibold">
                                            <fmt:parseDate value="${booking.createdAt}"
                                                pattern="yyyy-MM-dd'T'HH:mm:ss.SSSXXX" var="createdDate" />
                                            <fmt:formatDate value="${createdDate}" pattern="dd MMM yyyy, HH:mm a" />
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <div class="col-sm-4 text-muted">Total Amount</div>
                                        <div class="col-sm-8 fw-bold text-primary">
                                            $
                                            <fmt:formatNumber value="${booking.totalAmount}" pattern="#,##0.00" />
                                        </div>
                                    </div>
                                    <div class="row mb-0">
                                        <div class="col-sm-4 text-muted">GST Amount</div>
                                        <div class="col-sm-8 fw-semibold">
                                            $
                                            <fmt:formatNumber value="${booking.gstAmount}" pattern="#,##0.00" />
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="card shadow-sm border-0">
                                <div class="card-header bg-white py-3 border-bottom">
                                    <h5 class="mb-0">Service Items</h5>
                                </div>
                                <div class="card-body p-0">
                                    <div class="table-responsive">
                                        <table class="table table-hover align-middle mb-0">
                                            <thead class="bg-light">
                                                <tr>
                                                    <th class="ps-4">Service</th>
                                                    <th>Unit Price</th>
                                                    <th>Quantity</th>
                                                    <th class="text-end pe-4">Subtotal</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="item" items="${booking.details}">
                                                    <tr>
                                                        <td class="ps-4">
                                                            <div class="fw-bold">${item.serviceName}</div>
                                                            <c:if test="${not empty item.notes}">
                                                                <small class="text-muted d-block mt-1">Note:
                                                                    ${item.notes}</small>
                                                            </c:if>
                                                        </td>
                                                        <td>$
                                                            <fmt:formatNumber value="${item.unitPrice}"
                                                                pattern="#,##0.00" />
                                                        </td>
                                                        <td>${item.quantity}</td>
                                                        <td class="text-end pe-4 fw-bold">
                                                            $
                                                            <fmt:formatNumber value="${item.unitPrice * item.quantity}"
                                                                pattern="#,##0.00" />
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                            <tfoot class="bg-light">
                                                <tr>
                                                    <td colspan="3" class="text-end fw-bold">Grand Total:</td>
                                                    <td class="text-end pe-4 fw-bold text-primary">$
                                                        <fmt:formatNumber value="${booking.totalAmount}"
                                                            pattern="#,##0.00" />
                                                    </td>
                                                </tr>
                                            </tfoot>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-lg-4">
                            <div class="card shadow-sm border-0 mb-4 sticky-top" style="top: 2rem; z-index: 10;">
                                <div class="card-header bg-white py-3 border-bottom">
                                    <h5 class="mb-0">Customer Information</h5>
                                </div>
                                <div class="card-body">
                                    <div class="d-flex align-items-center mb-4">
                                        <div class="avatar-circle me-3 bg-primary text-white d-flex align-items-center justify-content-center"
                                            style="width: 50px; height: 50px; border-radius: 50%;">
                                            <span class="fs-4 fw-bold">${booking.customerName.charAt(0)}</span>
                                        </div>
                                        <div>
                                            <div class="fw-bold fs-5">${booking.customerName}</div>
                                            <div class="text-muted small">Customer ID: #${booking.customerId}</div>
                                        </div>
                                    </div>

                                    <div class="mb-3">
                                        <label class="text-muted small d-block mb-1">Email Address</label>
                                        <div class="fw-semibold"><i
                                                class="bi bi-envelope me-2"></i>${booking.customerEmail}</div>
                                    </div>

                                    <hr class="my-3">

                                    <div class="d-grid">
                                        <a href="${pageContext.request.contextPath}/admin/edit-user?id=${booking.customerId}"
                                            class="btn btn-outline-primary shadow-sm">
                                            <i class="bi bi-person"></i> View Customer Profile
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
            </body>

            </html>