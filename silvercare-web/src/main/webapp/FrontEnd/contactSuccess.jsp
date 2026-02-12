<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%-- Include Header --%>
            <jsp:include page="header.jsp" />

            <main class="container my-5">
                <div class="row justify-content-center">
                    <div class="col-md-8 text-center">
                        <div class="card shadow-lg border-0 rounded-4">
                            <div class="card-body p-5">
                                <div class="mb-4">
                                    <i class="fas fa-paper-plane text-primary" style="font-size: 5rem;"></i>
                                </div>

                                <h1 class="display-4 fw-bold text-primary mb-3">Message Sent!</h1>
                                <p class="lead mb-4">Thank you for reaching out to us.</p>

                                <p class="text-muted mb-5">
                                    Your inquiry has been received. Our team will review your message and get back to
                                    you as soon as possible, usually within 24 hours.
                                </p>

                                <div class="d-grid gap-3 d-sm-flex justify-content-sm-center">
                                    <a href="${pageContext.request.contextPath}/home"
                                        class="btn btn-primary btn-lg px-5">
                                        <i class="fas fa-home me-2"></i>Home Page
                                    </a>
                                    <a href="${pageContext.request.contextPath}/contact"
                                        class="btn btn-outline-secondary btn-lg px-5">
                                        <i class="fas fa-undo me-2"></i>Send Another Message
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </main>

            <%-- Include Footer --%>
                <jsp:include page="footer.jsp"></jsp:include>