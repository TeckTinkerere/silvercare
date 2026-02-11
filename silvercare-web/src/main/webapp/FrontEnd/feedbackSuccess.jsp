<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%-- Include Header --%>
<jsp:include page="header.jsp" />

<main class="container my-5">
    <div class="row">
        <div class="col-lg-8 mx-auto">
            <div class="card text-center shadow-sm p-5">
                <div class="card-body">
                    <h1 class="display-4 text-success">✔</h1>
                    <h2 class="card-title fw-bold">Thank You!</h2>
                    <p class="lead">
                        Your feedback has been submitted successfully. We appreciate you taking the time to share your thoughts.
                    </p>
                    <div class="mt-4">
                        <a href="${pageContext.request.contextPath}/bookings" class="btn btn-primary">Back to My Bookings</a>
                        <a href="${pageContext.request.contextPath}/home" class="btn btn-secondary">Back to Home</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<%-- Include Footer --%>
<jsp:include page="footer.jsp"></jsp:include>
