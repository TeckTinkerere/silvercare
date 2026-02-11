<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%-- Include Header --%>
<jsp:include page="header.jsp" />

<main class="container my-5">
    <div class="row">
        <div class="col-lg-8 mx-auto">
            <div class="card text-center shadow-sm p-5">
                <div class="card-body">
                    <h1 class="display-4 text-success">✔</h1>
                    <h2 class="card-title fw-bold">Message Sent</h2>
                    <p class="lead">
                        Thank you. Your message has been received. We'll get back to you as soon as possible.
                    </p>
                    <div class="mt-4">
                        <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Back to Home</a>
                        <a href="${pageContext.request.contextPath}/ServiceServlet?action=category" class="btn btn-secondary">Browse Services</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<%-- Include Footer --%>
<jsp:include page="footer.jsp"></jsp:include>
