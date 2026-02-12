<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Manage Categories - SilverCare Admin</title>

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
            <c:set var="activePage" value="categories" />
            <jsp:include page="adminSidebar.jsp" />

            <!-- Main Content -->
            <div class="content">
                <!-- Mobile Header -->
                <div class="mobile-header d-lg-none shadow-sm">
                    <button class="btn btn-primary me-3" id="toggleSidebar">
                        <i class="bi bi-list fs-4"></i>
                    </button>
                    <h4 class="mb-0">SilverCare Admin</h4>
                </div>

                <div class="page-header mb-4">
                    <h1>Manage Service Categories</h1>
                    <p class="text-muted">Organize your services by categories.</p>
                </div>

                <c:if test="${param.status == 'success'}">
                    <div class="alert alert-success shadow-sm border-0">Operation completed successfully!</div>
                </c:if>
                <c:if test="${param.status == 'deleted'}">
                    <div class="alert alert-info shadow-sm border-0">Category deleted successfully!</div>
                </c:if>
                <c:if test="${param.status == 'error'}">
                    <div class="alert alert-danger shadow-sm border-0">An error occurred. Please try again.</div>
                </c:if>

                <div class="row g-4">
                    <div class="col-12 col-lg-7">
                        <div class="card shadow-sm border-0 h-100">
                            <div class="card-header bg-white border-bottom-0 py-3">
                                <h5 class="mb-0">Existing Categories</h5>
                            </div>
                            <div class="card-body p-0">
                                <div class="table-responsive">
                                    <table class="table table-hover align-middle mb-0">
                                        <thead class="bg-light">
                                            <tr>
                                                <th class="ps-4">ID</th>
                                                <th>Category Name</th>
                                                <th class="text-end pe-4">Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="category" items="${categories}">
                                                <tr>
                                                    <td class="ps-4">${category.id}</td>
                                                    <td>
                                                        <div class="d-flex align-items-center">
                                                            <div class="bg-light p-2 rounded me-3">
                                                                <i
                                                                    class="${category.icon != null ? category.icon : 'bi bi-tag'} text-primary fs-5"></i>
                                                            </div>
                                                            <div>
                                                                <div class="fw-bold">${category.name}</div>
                                                                <small
                                                                    class="text-muted d-none d-sm-block">${category.description}</small>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td class="text-end pe-4">
                                                        <div class="d-flex justify-content-end gap-2">
                                                            <button class="btn btn-sm btn-outline-primary shadow-sm"
                                                                onclick="editCategory('${category.id}', '${category.name}', '${category.description}', '${category.icon}')">
                                                                <i class="bi bi-pencil"></i> <span
                                                                    class="d-none d-md-inline">Edit</span>
                                                            </button>
                                                            <form
                                                                action="${pageContext.request.contextPath}/admin/delete-category"
                                                                method="POST" style="display:inline;"
                                                                onsubmit="return confirm('Are you sure you want to delete this category?')">
                                                                <input type="hidden" name="id" value="${category.id}">
                                                                <button type="submit"
                                                                    class="btn btn-sm btn-outline-danger shadow-sm"><i
                                                                        class="bi bi-trash"></i> <span
                                                                        class="d-none d-md-inline">Delete</span></button>
                                                            </form>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            <c:if test="${empty categories}">
                                                <tr>
                                                    <td colspan="3" class="text-center py-5 text-muted">
                                                        <i class="bi bi-tags fs-1 d-block mb-3"></i>
                                                        No categories found.
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-12 col-lg-5">
                        <div class="card shadow-sm border-0 h-100">
                            <div
                                class="card-header bg-white border-bottom-0 py-3 d-flex justify-content-between align-items-center">
                                <h5 class="mb-0" id="formTitle">Add New Category</h5>
                                <button type="button" class="btn btn-sm btn-outline-secondary d-none" id="resetBtn"
                                    onclick="resetForm()">
                                    <i class="bi bi-arrow-counterclockwise"></i> Reset
                                </button>
                            </div>
                            <div class="card-body">
                                <form action="${pageContext.request.contextPath}/admin/save-category" method="POST">
                                    <input type="hidden" name="category_id" id="categoryId">
                                    <div class="mb-3">
                                        <label class="form-label fw-bold">Category Name</label>
                                        <input type="text" name="category_name" id="categoryName" class="form-control"
                                            placeholder="e.g. Nursing Care" required>
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label fw-bold">Description</label>
                                        <textarea name="description" id="categoryDesc" class="form-control" rows="4"
                                            placeholder="Brief description of this category"></textarea>
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label fw-bold">Icon Class (Bootstrap/FontAwesome)</label>
                                        <div class="input-group">
                                            <span class="input-group-text"><i id="iconPreview"
                                                    class="bi bi-tag"></i></span>
                                            <input type="text" name="icon" id="categoryIcon" class="form-control"
                                                placeholder="bi bi-heart-fill" oninput="updateIconPreview(this.value)">
                                        </div>
                                        <small class="text-muted">Use <a href="https://icons.getbootstrap.com/"
                                                target="_blank">Bootstrap Icons</a> e.g. bi bi-heart</small>
                                    </div>
                                    <button type="submit" class="btn btn-primary w-100 py-2 shadow-sm" id="submitBtn">
                                        <i class="bi bi-check-circle me-1"></i> Add Category
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
            <script>
                function editCategory(id, name, desc, icon) {
                    document.getElementById('categoryId').value = id;
                    document.getElementById('categoryName').value = name;
                    document.getElementById('categoryDesc').value = (desc === 'null' || desc === 'undefined') ? '' : desc;
                    document.getElementById('categoryIcon').value = (icon === 'null' || icon === 'undefined') ? '' : icon;
                    document.getElementById('formTitle').innerText = 'Edit Category #' + id;
                    document.getElementById('submitBtn').innerHTML = '<i class="bi bi-save me-1"></i> Save Changes';
                    document.getElementById('resetBtn').classList.remove('d-none');
                    updateIconPreview(icon);

                    // Scroll to form on mobile
                    if (window.innerWidth < 992) {
                        document.getElementById('formTitle').scrollIntoView({ behavior: 'smooth' });
                    }
                }

                function resetForm() {
                    document.getElementById('categoryId').value = '';
                    document.getElementById('categoryName').value = '';
                    document.getElementById('categoryDesc').value = '';
                    document.getElementById('categoryIcon').value = '';
                    document.getElementById('formTitle').innerText = 'Add New Category';
                    document.getElementById('submitBtn').innerHTML = '<i class="bi bi-check-circle me-1"></i> Add Category';
                    document.getElementById('resetBtn').classList.add('d-none');
                    updateIconPreview('');
                }

                function updateIconPreview(val) {
                    const preview = document.getElementById('iconPreview');
                    preview.className = val ? val : 'bi bi-tag';
                }

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
                });
            </script>
        </body>

        </body>

        </html>