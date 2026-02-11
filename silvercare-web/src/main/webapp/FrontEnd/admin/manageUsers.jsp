<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Manage Users - SilverCare Admin</title>

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
                        --success-color: #28a745;
                        --info-color: #17a2b8;
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

                    .sidebar-footer {
                        margin-top: auto;
                        padding: 1.5rem;
                        border-top: 1px solid rgba(255, 255, 255, 0.1);
                    }

                    .logout-btn {
                        display: flex;
                        padding: .75rem 1rem;
                        border-radius: 8px;
                        color: white;
                        text-decoration: none;
                        background-color: rgba(255, 255, 255, 0.1);
                        transition: .2s;
                    }

                    .logout-btn:hover {
                        background-color: rgba(220, 53, 69, .8);
                    }

                    .content {
                        padding: 1rem;
                        width: 100%;
                        transition: all 0.3s ease;
                    }

                    .mobile-header {
                        display: flex;
                        align-items: center;
                        padding: 1rem;
                        background-color: white;
                        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
                        margin-bottom: 1rem;
                        border-radius: 8px;
                    }

                    .sidebar-overlay {
                        display: none;
                        position: fixed;
                        top: 0;
                        left: 0;
                        width: 100vw;
                        height: 100vh;
                        background: rgba(0, 0, 0, 0.5);
                        z-index: 1030;
                    }

                    .sidebar-overlay.show {
                        display: block;
                    }

                    .user-avatar {
                        width: 40px;
                        height: 40px;
                        border-radius: 50%;
                        background-color: #e9ecef;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        font-weight: bold;
                        color: var(--primary-color);
                        object-fit: cover;
                    }

                    .badge-role-admin {
                        background-color: #f8d7da;
                        color: #842029;
                    }

                    .badge-role-customer {
                        background-color: #d1e7dd;
                        color: #0f5132;
                    }

                    /* Desktop layout */
                    @media (min-width: 992px) {
                        .sidebar {
                            left: 0;
                        }

                        .content {
                            margin-left: var(--sidebar-width);
                            width: calc(100% - var(--sidebar-width));
                            padding: 2rem;
                        }

                        .mobile-header {
                            display: none;
                        }

                        .sidebar-overlay {
                            display: none !important;
                        }
                    }
                </style>
            </head>

            <body>

                <!-- Sidebar Overlay -->
                <div class="sidebar-overlay" id="sidebarOverlay"></div>

                <!-- Sidebar -->
                <div class="sidebar d-flex flex-column" id="sidebar">
                    <div class="sidebar-brand d-flex justify-content-between align-items-center">
                        <a href="${pageContext.request.contextPath}/admin/dashboard"
                            class="text-white text-decoration-none h4 mb-0">
                            <i class="bi bi-heart-fill"></i> SilverCare
                        </a>
                        <button class="btn btn-link text-white d-lg-none p-0" id="closeSidebar">
                            <i class="bi bi-x-lg fs-4"></i>
                        </button>
                    </div>

                    <ul class="nav nav-pills flex-column mt-3">
                        <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link"><i
                                    class="bi bi-grid"></i>Dashboard</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/services" class="nav-link"><i
                                    class="bi bi-box-seam"></i>Services</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/categories" class="nav-link"><i
                                    class="bi bi-tags"></i>Categories</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/bookings" class="nav-link"><i
                                    class="bi bi-calendar-check"></i>Bookings</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/feedback" class="nav-link"><i
                                    class="bi bi-chat-left-text"></i>Feedback</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/logs" class="nav-link"><i
                                    class="bi bi-journal-text"></i>Activity Logs</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/users" class="nav-link active"><i
                                    class="bi bi-people"></i>Users</a></li>
                    </ul>

                    <div class="sidebar-footer">
                        <a href="${pageContext.request.contextPath}/logout" class="logout-btn"><i
                                class="bi bi-box-arrow-right"></i> Sign Out</a>
                    </div>
                </div>

                <div class="content">
                    <!-- Mobile Header -->
                    <div class="mobile-header d-lg-none shadow-sm">
                        <button class="btn btn-primary me-3" id="toggleSidebar">
                            <i class="bi bi-list fs-4"></i>
                        </button>
                        <h4 class="mb-0">SilverCare Admin</h4>
                    </div>

                    <div
                        class="page-header d-flex flex-column flex-md-row justify-content-between align-items-md-center mb-4 gap-3">
                        <div>
                            <h1>Users Management</h1>
                            <p class="text-muted mb-0">Manage registered customers and administrative access.</p>
                        </div>
                        <div>
                            <a href="${pageContext.request.contextPath}/admin/add-user"
                                class="btn btn-primary shadow-sm">
                                <i class="bi bi-person-plus me-2"></i>Add New User
                            </a>
                        </div>
                    </div>

                    <c:if test="${not empty param.status}">
                        <div class="alert alert-${param.status == 'success' || param.status == 'deleted' ? 'success' : 'danger'} alert-dismissible fade show mb-4 border-0 shadow-sm"
                            role="alert">
                            <i
                                class="bi ${param.status == 'success' || param.status == 'deleted' ? 'bi-check-circle' : 'bi-exclamation-triangle'} me-2"></i>
                            <c:choose>
                                <c:when test="${param.status == 'success'}">User updated successfully!</c:when>
                                <c:when test="${param.status == 'deleted'}">User deleted successfully!</c:when>
                                <c:when test="${param.status == 'reset_success'}">Password reset successfully!</c:when>
                                <c:otherwise>An error occurred.
                                    <c:out value="${param.msg}" />
                                </c:otherwise>
                            </c:choose>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                    <div class="card shadow-sm border-0">
                        <div class="card-header bg-white py-3 border-bottom-0">
                            <h5 class="card-title mb-0"><i class="bi bi-table me-2"></i>User Directory</h5>
                        </div>
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover align-middle mb-0 text-nowrap">
                                    <thead class="bg-light">
                                        <tr>
                                            <th class="ps-4">User</th>
                                            <th>Contact Info</th>
                                            <th>Role</th>
                                            <th>Joined Date</th>
                                            <th class="text-end pe-4">Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="user" items="${users}">
                                            <tr>
                                                <td class="ps-4">
                                                    <div class="d-flex align-items-center">
                                                        <div class="user-avatar me-3">
                                                            <c:choose>
                                                                <c:when test="${not empty user.profilePicture}">
                                                                    <img src="${pageContext.request.contextPath}/${user.profilePicture}"
                                                                        alt="Avatar" class="user-avatar"
                                                                        onerror="this.src='https://ui-avatars.com/api/?name=${user.fullName}&background=random'">
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <c:out
                                                                        value="${not empty user.fullName ? user.fullName.substring(0, 1) : 'U'}" />
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </div>
                                                        <div>
                                                            <div class="fw-bold">${user.fullName}</div>
                                                            <small class="text-muted">ID: #${user.id}</small>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div><i class="bi bi-envelope me-2 text-muted"></i>${user.email}
                                                    </div>
                                                    <c:if test="${not empty user.phone}">
                                                        <small class="text-muted"><i
                                                                class="bi bi-telephone me-2"></i>${user.phone}</small>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <span
                                                        class="badge ${user.role == 'Admin' ? 'badge-role-admin' : 'badge-role-customer'} px-3 py-2">
                                                        ${user.role}
                                                    </span>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty user.createdAt}">
                                                            <fmt:parseDate value="${user.createdAt}"
                                                                pattern="yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
                                                                var="parsedDate" />
                                                            <fmt:formatDate value="${parsedDate}"
                                                                pattern="dd MMM yyyy" />
                                                        </c:when>
                                                        <c:otherwise>-</c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="text-end pe-4">
                                                    <div class="btn-group">
                                                        <button type="button"
                                                            class="btn btn-sm btn-outline-info btn-view-user"
                                                            data-id="${user.id}" title="View Details">
                                                            <i class="bi bi-info-circle"></i>
                                                        </button>
                                                        <a href="${pageContext.request.contextPath}/admin/edit-user?id=${user.id}"
                                                            class="btn btn-sm btn-outline-primary ms-1"
                                                            title="Edit User">
                                                            <i class="bi bi-pencil"></i>
                                                        </a>
                                                        <button type="button"
                                                            class="btn btn-sm btn-outline-warning ms-1 btn-reset-password"
                                                            data-id="${user.id}"
                                                            data-name="<c:out value='${user.fullName}' />"
                                                            data-role="${user.role}" title="Reset Password">
                                                            <i class="bi bi-key"></i>
                                                        </button>
                                                        <button type="button"
                                                            class="btn btn-sm btn-outline-danger ms-1 btn-delete-user"
                                                            data-id="${user.id}"
                                                            data-name="<c:out value='${user.fullName}' />"
                                                            title="Delete User">
                                                            <i class="bi bi-trash"></i>
                                                        </button>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty users}">
                                            <tr>
                                                <td colspan="5" class="text-center py-5 text-muted">
                                                    <i class="bi bi-people fs-1 d-block mb-3"></i>
                                                    No users found.
                                                </td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Reset Password Modal -->
                <div class="modal fade" id="resetPasswordModal" tabindex="-1">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content border-0 shadow">
                            <div class="modal-header bg-warning text-dark">
                                <h5 class="modal-title"><i class="bi bi-key me-2"></i>Reset Password</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                            </div>
                            <form id="resetPasswordForm"
                                action="${pageContext.request.contextPath}/admin/reset-password" method="POST">
                                <div class="modal-body p-4">
                                    <p>Resetting password for <strong id="resetPasswordUserName"></strong></p>
                                    <input type="hidden" name="userId" id="resetPasswordUserId">
                                    <input type="hidden" name="role" id="resetPasswordUserRole">
                                    <div class="mb-3">
                                        <label class="form-label fw-bold">New Password</label>
                                        <div class="input-group">
                                            <span class="input-group-text"><i class="bi bi-lock"></i></span>
                                            <input type="password" name="newPassword" id="newPassword"
                                                class="form-control" placeholder="Enter new password" required>
                                            <button class="btn btn-outline-secondary" type="button" id="togglePassword">
                                                <i class="bi bi-eye"></i>
                                            </button>
                                        </div>
                                    </div>
                                    <div class="alert alert-info py-2 small border-0">
                                        <i class="bi bi-info-circle me-2"></i>The user will be able to log in with this
                                        new password immediately.
                                    </div>
                                </div>
                                <div class="modal-footer border-top-0">
                                    <button type="button" class="btn btn-light" data-bs-dismiss="modal">Cancel</button>
                                    <button type="submit" class="btn btn-warning">Reset Password</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <!-- User Details Modal -->
                <div class="modal fade" id="userDetailsModal" tabindex="-1">
                    <div class="modal-dialog modal-lg modal-dialog-centered">
                        <div class="modal-content border-0 shadow">
                            <div class="modal-header bg-primary text-white">
                                <h5 class="modal-title"><i class="bi bi-person-badge me-2"></i>User Profile & History
                                </h5>
                                <button type="button" class="btn-close btn-close-white"
                                    data-bs-dismiss="modal"></button>
                            </div>
                            <div class="modal-body p-0" id="userDetailsContent">
                                <div class="text-center py-5">
                                    <div class="spinner-border text-primary" role="status">
                                        <span class="visually-hidden">Loading...</span>
                                    </div>
                                    <p class="mt-2 text-muted">Retrieving records...</p>
                                </div>
                            </div>
                            <div class="modal-footer border-top-0">
                                <button type="button" class="btn btn-light" data-bs-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Delete Confirmation Modal -->
                <div class="modal fade" id="deleteModal" tabindex="-1">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content border-0 shadow">
                            <div class="modal-header bg-danger text-white">
                                <h5 class="modal-title">Confirm Delete</h5>
                                <button type="button" class="btn-close btn-close-white"
                                    data-bs-dismiss="modal"></button>
                            </div>
                            <div class="modal-body p-4">
                                <p>Are you sure you want to delete user <strong id="deleteUserName"></strong>?</p>
                                <p class="text-danger small mb-0"><i class="bi bi-exclamation-triangle me-2"></i>This
                                    action cannot be undone.</p>
                            </div>
                            <div class="modal-footer border-top-0">
                                <form id="deleteForm" action="${pageContext.request.contextPath}/admin/delete-user"
                                    method="POST">
                                    <input type="hidden" name="id" id="deleteUserId">
                                    <button type="button" class="btn btn-light" data-bs-dismiss="modal">Cancel</button>
                                    <button type="submit" class="btn btn-danger">Delete User</button>
                                </form>
                            </div>
                        </div>
                    </div>
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
                    });

                    const deleteModal = new bootstrap.Modal(document.getElementById('deleteModal'));
                    const resetModal = new bootstrap.Modal(document.getElementById('resetPasswordModal'));
                    const detailsModal = new bootstrap.Modal(document.getElementById('userDetailsModal'));

                    document.addEventListener('click', function (e) {
                        const viewBtn = e.target.closest('.btn-view-user');
                        if (viewBtn) {
                            const id = viewBtn.getAttribute('data-id');
                            loadUserDetails(id);
                            detailsModal.show();
                        }

                        const deleteBtn = e.target.closest('.btn-delete-user');
                        if (deleteBtn) {
                            const id = deleteBtn.getAttribute('data-id');
                            const name = deleteBtn.getAttribute('data-name');
                            document.getElementById('deleteUserId').value = id;
                            document.getElementById('deleteUserName').textContent = name;
                            deleteModal.show();
                        }

                        const resetBtn = e.target.closest('.btn-reset-password');
                        if (resetBtn) {
                            const id = resetBtn.getAttribute('data-id');
                            const name = resetBtn.getAttribute('data-name');
                            const role = resetBtn.getAttribute('data-role');
                            document.getElementById('resetPasswordUserId').value = id;
                            document.getElementById('resetPasswordUserName').textContent = name;
                            document.getElementById('resetPasswordUserRole').value = role;
                            document.getElementById('newPassword').value = '';
                            resetModal.show();
                        }
                    });

                    document.getElementById('togglePassword')?.addEventListener('click', function () {
                        const passwordInput = document.getElementById('newPassword');
                        const icon = this.querySelector('i');
                        if (passwordInput.type === 'password') {
                            passwordInput.type = 'text';
                            icon.classList.replace('bi-eye', 'bi-eye-slash');
                        } else {
                            passwordInput.type = 'password';
                            icon.classList.replace('bi-eye-slash', 'bi-eye');
                        }
                    });
                    function loadUserDetails(id) {
                        const content = document.getElementById('userDetailsContent');
                        content.innerHTML = `
                            <div class="text-center py-5">
                                <div class="spinner-border text-primary" role="status">
                                    <span class="visually-hidden">Loading...</span>
                                </div>
                                <p class="mt-2 text-muted">Retrieving records...</p>
                            </div>
                        `;

                        fetch("${pageContext.request.contextPath}/admin/user-details-json?id=\${id}")
                            .then(response => response.json())
                            .then(data => {
                                if (data.error) {
                                    content.innerHTML = `<div class="alert alert-danger m-3">${data.error}</div>`;
                                    return;
                                }

                                const user = data.user;
                                const bookings = data.bookings || [];

                                let bookingsHtml = bookings.length > 0 ? `
                                    <div class="table-responsive">
                                        <table class="table table-sm table-hover mb-0">
                                            <thead class="bg-light">
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Date</th>
                                                    <th>Amount</th>
                                                    <th>Status</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                \${bookings.map(b => `
                                    < tr >
                                                        <td>#\${b.id}</td>
                                                        <td>\${new Date(b.bookingDate).toLocaleDateString()}</td>
                                                        <td>$\${b.totalAmount.toFixed(2)}</td>
                                                        <td><span class="badge bg-\${b.status === 'Confirmed' ? 'success' : 'warning'}">\${b.status}</span></td>
                                                    </tr >
                                    `).join('')}
                                            </tbody>
                                        </table>
                                    </div>
                                ` : '<p class="text-muted fst-italic">No booking history found.</p>';

                                content.innerHTML = `
                                    <div class="p-4">
                                        <div class="row">
                                            <div class="col-md-4 text-center border-end">
                                                <img src="${pageContext.request.contextPath}/\${user.profilePicture || 'https://ui-avatars.com/api/?name=' + user.fullName + '&background=random'}" 
                                                     class="rounded-circle mb-3 shadow-sm" style="width: 120px; height: 120px; object-fit: cover;">
                                                <h4 class="mb-1">\${user.fullName}</h4>
                                                <span class="badge bg-secondary mb-3">\${user.role}</span>
                                                <div class="text-start small">
                                                    <p class="mb-1"><strong>Email:</strong> \${user.email}</p>
                                                    <p class="mb-1"><strong>Phone:</strong> \${user.phone || '-'}</p>
                                                    <p class="mb-1"><strong>Gender:</strong> \${user.gender || '-'}</p>
                                                </div>
                                            </div>
                                            <div class="col-md-8">
                                                <h6 class="fw-bold mb-3 border-bottom pb-2">Medical History & Notes</h6>
                                                <div class="bg-light p-3 rounded mb-4" style="min-height: 100px;">
                                                    \${user.medicalInfo ? user.medicalInfo.replace(/\n/g, '<br>') : 'No medical information recorded.'}
                                                </div>

                                                <h6 class="fw-bold mb-3 border-bottom pb-2">Booking History</h6>
                                                \${bookingsHtml}
                                            </div>
                                        </div>
                                    </div>
                                `;
                            })
                            .catch(err => {
                                content.innerHTML = `<div class="alert alert-danger m-3">Error: ${err.message}</div>`;
                            });
                    }
                </script>
            </body>

            </html>