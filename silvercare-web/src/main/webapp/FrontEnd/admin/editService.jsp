<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Edit Service - SilverCare Admin</title>

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
                /* Additional custom styles if needed */
            </style>
        </head>

        <body>
            <!-- Sidebar -->
            <c:set var="activePage" value="services" />
            <jsp:include page="adminSidebar.jsp" />

            <div class="content">
                <!-- Mobile Header -->
                <div class="mobile-header d-lg-none shadow-sm">
                    <button class="btn btn-primary me-3" id="toggleSidebar">
                        <i class="bi bi-list fs-4"></i>
                    </button>
                    <h4 class="mb-0">SilverCare Admin</h4>
                </div>

                <div class="page-header mb-4">
                    <h1><i class="bi bi-pencil-square text-primary me-2"></i>Edit Service</h1>
                    <p class="text-muted">Modify existing service details and configuration.</p>
                </div>

                <c:choose>
                    <c:when test="${not empty service}">
                        <div class="card shadow-sm border-0">
                            <div class="card-body p-4">
                                <form action="${pageContext.request.contextPath}/admin/save-service" method="POST">
                                    <input type="hidden" name="id" value="${service.id}">

                                    <div class="row g-3">
                                        <div class="col-md-6 mb-3">
                                            <label for="service_name" class="form-label fw-bold">Service Name</label>
                                            <input type="text" class="form-control" id="service_name"
                                                name="service_name" value="${service.name}" required>
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <label for="category_id" class="form-label fw-bold">Service Category</label>
                                            <select class="form-select" id="category_id" name="category_id" required>
                                                <c:forEach var="cat" items="${categories}">
                                                    <option value="${cat.id}" ${service.categoryId==cat.id ? 'selected'
                                                        : '' }>${cat.name}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="mb-3">
                                        <label for="description" class="form-label fw-bold">Service Description</label>
                                        <textarea class="form-control" id="description" name="description" rows="4"
                                            required>${service.description}</textarea>
                                    </div>

                                    <div class="row g-3">
                                        <div class="col-md-6 mb-3">
                                            <label for="price" class="form-label fw-bold">Price ($)</label>
                                            <div class="input-group">
                                                <span class="input-group-text">$</span>
                                                <input type="number" step="0.01" class="form-control" id="price"
                                                    name="price" value="${service.price}" required>
                                            </div>
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <label for="image_url" class="form-label fw-bold">Image Path</label>
                                            <div class="input-group">
                                                <span class="input-group-text"><i class="bi bi-image"></i></span>
                                                <input type="text" class="form-control" id="image_url" name="image_url"
                                                    value="${service.imagePath}">
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Seasonal Multipliers Section -->
                                    <div class="mb-4">
                                        <h5 class="mb-3"><i class="bi bi-calendar-range text-primary me-2"></i>Seasonal Pricing Multipliers</h5>
                                        <p class="text-muted small">Set seasonal price adjustments (0.1 to 10.0). Default is 1.0 (no adjustment).</p>
                                        
                                        <c:if test="${not empty error}">
                                            <div class="alert alert-danger border-0 shadow-sm">
                                                <i class="bi bi-exclamation-triangle me-2"></i>${error}
                                            </div>
                                        </c:if>

                                        <div class="row g-3">
                                            <div class="col-md-6 col-lg-3 mb-3">
                                                <label for="spring_multiplier" class="form-label fw-bold">
                                                    <i class="bi bi-flower1 text-success me-1"></i>Spring Multiplier
                                                </label>
                                                <input type="number" step="0.01" min="0.1" max="10.0" 
                                                       class="form-control seasonal-multiplier" 
                                                       id="spring_multiplier" name="spring_multiplier"
                                                       placeholder="1.0" value="${service.springMultiplier != null ? service.springMultiplier : 1.0}">
                                                <small class="text-muted">Range: 0.1 - 10.0</small>
                                                <div class="invalid-feedback">Must be between 0.1 and 10.0</div>
                                            </div>
                                            <div class="col-md-6 col-lg-3 mb-3">
                                                <label for="summer_multiplier" class="form-label fw-bold">
                                                    <i class="bi bi-sun text-warning me-1"></i>Summer Multiplier
                                                </label>
                                                <input type="number" step="0.01" min="0.1" max="10.0" 
                                                       class="form-control seasonal-multiplier" 
                                                       id="summer_multiplier" name="summer_multiplier"
                                                       placeholder="1.0" value="${service.summerMultiplier != null ? service.summerMultiplier : 1.0}">
                                                <small class="text-muted">Range: 0.1 - 10.0</small>
                                                <div class="invalid-feedback">Must be between 0.1 and 10.0</div>
                                            </div>
                                            <div class="col-md-6 col-lg-3 mb-3">
                                                <label for="autumn_multiplier" class="form-label fw-bold">
                                                    <i class="bi bi-tree text-danger me-1"></i>Autumn Multiplier
                                                </label>
                                                <input type="number" step="0.01" min="0.1" max="10.0" 
                                                       class="form-control seasonal-multiplier" 
                                                       id="autumn_multiplier" name="autumn_multiplier"
                                                       placeholder="1.0" value="${service.autumnMultiplier != null ? service.autumnMultiplier : 1.0}">
                                                <small class="text-muted">Range: 0.1 - 10.0</small>
                                                <div class="invalid-feedback">Must be between 0.1 and 10.0</div>
                                            </div>
                                            <div class="col-md-6 col-lg-3 mb-3">
                                                <label for="winter_multiplier" class="form-label fw-bold">
                                                    <i class="bi bi-snow text-info me-1"></i>Winter Multiplier
                                                </label>
                                                <input type="number" step="0.01" min="0.1" max="10.0" 
                                                       class="form-control seasonal-multiplier" 
                                                       id="winter_multiplier" name="winter_multiplier"
                                                       placeholder="1.0" value="${service.winterMultiplier != null ? service.winterMultiplier : 1.0}">
                                                <small class="text-muted">Range: 0.1 - 10.0</small>
                                                <div class="invalid-feedback">Must be between 0.1 and 10.0</div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="d-flex gap-2">
                                        <button type="submit" class="btn btn-primary px-4 shadow-sm">
                                            <i class="bi bi-check-lg me-1"></i> Update Service
                                        </button>
                                        <a href="${pageContext.request.contextPath}/admin/services"
                                            class="btn btn-light px-4">Cancel</a>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-warning border-0 shadow-sm">
                            <i class="bi bi-exclamation-triangle me-2"></i> Service data not found or invalid ID.
                            <div class="mt-2 text-end">
                                <a href="${pageContext.request.contextPath}/admin/services"
                                    class="btn btn-sm btn-outline-warning">Back to Services</a>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
            <script>
                document.addEventListener('DOMContentLoaded', function () {
                    const toggleBtn = document.getElementById('toggleSidebar');
                    const closeBtn = document.getElementById('closeSidebar');
                    const sidebar = document.getElementById('sidebar');
                    const overlay = document.getElementById('sidebarOverlay');

                    function toggleSidebar() {
                        sidebar.classList.toggle('show');
                        overlay.classList.toggle('show');
                        document.body.style.overflow = sidebar.classList.contains('show') ? 'hidden' : '';
                    }

                    if (toggleBtn) toggleBtn.addEventListener('click', toggleSidebar);
                    if (closeBtn) closeBtn.addEventListener('click', toggleSidebar);
                    if (overlay) overlay.addEventListener('click', toggleSidebar);

                    // Client-side validation for seasonal multipliers
                    const form = document.querySelector('form');
                    const multiplierInputs = document.querySelectorAll('.seasonal-multiplier');

                    if (form && multiplierInputs.length > 0) {
                        form.addEventListener('submit', function(e) {
                            let isValid = true;
                            
                            multiplierInputs.forEach(function(input) {
                                const value = parseFloat(input.value);
                                
                                if (isNaN(value) || value < 0.1 || value > 10.0) {
                                    isValid = false;
                                    input.classList.add('is-invalid');
                                } else {
                                    input.classList.remove('is-invalid');
                                }
                            });

                            if (!isValid) {
                                e.preventDefault();
                                alert('Please ensure all seasonal multipliers are between 0.1 and 10.0');
                            }
                        });

                        // Real-time validation feedback
                        multiplierInputs.forEach(function(input) {
                            input.addEventListener('input', function() {
                                const value = parseFloat(this.value);
                                
                                if (this.value && (isNaN(value) || value < 0.1 || value > 10.0)) {
                                    this.classList.add('is-invalid');
                                } else {
                                    this.classList.remove('is-invalid');
                                }
                            });
                        });
                    }
                });
            </script>
        </body>

        </html>