<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

        <%-- Include Header --%>
            <jsp:include page="header.jsp" />

            <main class="container my-5">
                <div class="row">
                    <div class="col-lg-8 mx-auto">
                        <!-- Back to Bookings Link -->
                        <div class="mb-3">
                            <a href="${pageContext.request.contextPath}/BookingServlet?action=list"
                                class="text-decoration-none">
                                <i class="fas fa-arrow-left me-2"></i>Back to My Bookings
                            </a>
                        </div>

                        <div class="card shadow-sm">
                            <div class="card-body p-4">
                                <!-- Page Header -->
                                <div class="text-center mb-4">
                                    <div class="feedback-icon mb-3">
                                        <div class="icon-wrapper bg-primary bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center"
                                            style="width: 70px; height: 70px;">
                                            <i class="fas fa-${isEditMode ? 'edit' : 'star'} text-primary"
                                                style="font-size: 1.8rem;"></i>
                                        </div>
                                    </div>
                                    <h1 class="h3 mb-2 fw-bold">${isEditMode ? 'Edit Feedback' : 'Leave Feedback'}</h1>
                                    <p class="text-muted">${isEditMode ? 'Update your feedback for this service' :
                                        'Share your experience with this service'}</p>
                                </div>

                                <!-- Service and Booking Details -->
                                <div class="alert alert-info mb-4">
                                    <div class="row">
                                        <div class="col-md-6">
                                            <strong><i class="fas fa-concierge-bell me-2"></i>Service:</strong>
                                            ${serviceName}
                                        </div>
                                        <div class="col-md-6">
                                            <strong><i class="fas fa-calendar me-2"></i>Booking Date:</strong>
                                            ${bookingDate}
                                        </div>
                                    </div>
                                    <div class="mt-2">
                                        <strong><i class="fas fa-info-circle me-2"></i>Booking ID:</strong>
                                        #${bookingId}
                                        <span class="badge bg-success ms-2">${bookingStatus}</span>
                                    </div>
                                </div>

                                <!-- Feedback Form -->
                                <form action="${pageContext.request.contextPath}/FeedbackServlet?action=save"
                                    method="POST">
                                    <input type="hidden" name="service_id" value="${serviceId}">
                                    <input type="hidden" name="booking_id" value="${bookingId}">

                                    <!-- Rating -->
                                    <div class="mb-4">
                                        <label class="form-label fw-semibold d-block mb-3">
                                            <i class="fas fa-star text-warning me-2"></i>Overall Rating *
                                        </label>
                                        <c:choose>
                                            <c:when test="${isEditMode}">
                                                <!-- Edit Mode: Rating is readonly -->
                                                <input type="hidden" name="rating" value="${existingRating}">
                                                <div class="rating-display mb-2">
                                                    <c:forEach begin="1" end="5" var="i">
                                                        <i class="fas fa-star ${i <= existingRating ? 'text-warning' : 'text-muted opacity-25'}"
                                                            style="font-size: 1.5rem;"></i>
                                                    </c:forEach>
                                                    <span class="ms-2 fw-semibold text-primary">
                                                        <c:choose>
                                                            <c:when test="${existingRating == 5}">Excellent (5 stars)
                                                            </c:when>
                                                            <c:when test="${existingRating == 4}">Very Good (4 stars)
                                                            </c:when>
                                                            <c:when test="${existingRating == 3}">Good (3 stars)
                                                            </c:when>
                                                            <c:when test="${existingRating == 2}">Fair (2 stars)
                                                            </c:when>
                                                            <c:when test="${existingRating == 1}">Poor (1 star)</c:when>
                                                        </c:choose>
                                                    </span>
                                                </div>
                                                <div class="form-text text-info">
                                                    <i class="fas fa-lock me-1"></i>Rating cannot be changed once
                                                    submitted
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <!-- New Feedback Mode: Interactive Stars -->
                                                <div class="star-rating-container p-3 bg-light rounded-3 border">
                                                    <div class="star-rating">
                                                        <input type="radio" name="rating" id="star-5" value="5"
                                                            required /><label for="star-5"
                                                            title="Excellent - 5 stars"><i
                                                                class="fas fa-star"></i></label>
                                                        <input type="radio" name="rating" id="star-4" value="4" /><label
                                                            for="star-4" title="Very Good - 4 stars"><i
                                                                class="fas fa-star"></i></label>
                                                        <input type="radio" name="rating" id="star-3" value="3" /><label
                                                            for="star-3" title="Good - 3 stars"><i
                                                                class="fas fa-star"></i></label>
                                                        <input type="radio" name="rating" id="star-2" value="2" /><label
                                                            for="star-2" title="Fair - 2 stars"><i
                                                                class="fas fa-star"></i></label>
                                                        <input type="radio" name="rating" id="star-1" value="1" /><label
                                                            for="star-1" title="Poor - 1 star"><i
                                                                class="fas fa-star"></i></label>
                                                    </div>
                                                    <div id="rating-text" class="mt-2 text-center text-primary fw-bold"
                                                        style="min-height: 24px;">
                                                        Please select a rating
                                                    </div>
                                                </div>
                                                <div class="form-text mt-2">Click on a star to rate your overall
                                                    experience</div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>

                                    <!-- Comment -->
                                    <div class="mb-4">
                                        <label for="comment" class="form-label fw-semibold">
                                            <i class="fas fa-comment-dots me-2"></i>Comments (Optional)
                                        </label>
                                        <textarea class="form-control form-control-lg" id="comment" name="comment"
                                            rows="5"
                                            placeholder="Tell us more about your experience... What did you like? What could be improved?">${isEditMode ? existingComment : ''}</textarea>
                                        <div class="form-text">${isEditMode ? 'You can update your comments anytime' :
                                            'Your feedback helps us improve our services'}</div>
                                    </div>

                                    <!-- Submit Buttons -->
                                    <div class="d-grid gap-2 d-md-flex justify-content-md-end mt-4">
                                        <a href="${pageContext.request.contextPath}/BookingServlet?action=list"
                                            class="btn btn-outline-secondary btn-lg">
                                            <i class="fas fa-times me-2"></i>Cancel
                                        </a>
                                        <button type="submit" class="btn btn-primary btn-lg">
                                            <i
                                                class="fas fa-${isEditMode ? 'save' : 'paper-plane'} me-2"></i>${isEditMode
                                            ? 'Update Feedback' : 'Submit Feedback'}
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>

                        <!-- Help Text -->
                        <div class="text-center mt-4 text-muted">
                            <small>
                                <i class="fas fa-lock me-1"></i>Your feedback is valuable and helps us serve you better
                            </small>
                        </div>
                    </div>
                </div>
            </main>

            <!-- Additional Styles -->
            <style>
                .feedback-icon .icon-wrapper {
                    transition: all 0.3s ease;
                }

                .feedback-icon .icon-wrapper:hover {
                    transform: scale(1.1);
                    background-color: var(--primary-color) !important;
                }

                .feedback-icon .icon-wrapper:hover i {
                    color: white !important;
                }

                .form-control,
                .form-select {
                    border: 2px solid #e9ecef;
                    transition: all 0.3s ease;
                }

                .form-control:focus,
                .form-select:focus {
                    border-color: var(--primary-color);
                    box-shadow: 0 0 0 0.25rem rgba(91, 124, 153, 0.15);
                }

                .btn-primary {
                    transition: all 0.3s ease;
                }

                .btn-primary:hover {
                    transform: translateY(-2px);
                    box-shadow: 0 6px 16px rgba(91, 124, 153, 0.3);
                }

                .alert-info {
                    background-color: #e7f3ff;
                    border-color: #b3d9ff;
                    color: #004085;
                }

                /* Star Rating Interactive System */
                .star-rating {
                    display: flex;
                    flex-direction: row-reverse;
                    justify-content: center;
                    gap: 0.75rem;
                }

                .star-rating input {
                    display: none;
                }

                .star-rating label {
                    font-size: 2.5rem;
                    color: #cbd5e0;
                    cursor: pointer;
                    transition: all 0.2s ease;
                }

                /* Hover effect: illuminate hovered star and siblings to the right (visually left because of row-reverse) */
                .star-rating label:hover,
                .star-rating label:hover~label {
                    color: #fbbf24;
                    transform: scale(1.2);
                }

                /* Selected state: illuminate checked star and siblings to the right */
                .star-rating input:checked~label {
                    color: #ffc107;
                    text-shadow: 0 0 10px rgba(255, 193, 7, 0.3);
                }

                .star-rating label:active {
                    transform: scale(0.9);
                }
            </style>

            <script>
                document.addEventListener('DOMContentLoaded', function () {
                    const ratingInputs = document.querySelectorAll('input[name="rating"]');
                    const ratingText = document.getElementById('rating-text');
                    const isFeedbackEditMode = ${ isEditMode == true ? 'true' : 'false'
                };

                const ratingDescriptions = {
                    '5': 'Excellent - 5 Stars',
                    '4': 'Very Good - 4 Stars',
                    '3': 'Good - 3 Stars',
                    '2': 'Fair - 2 Stars',
                    '1': 'Poor - 1 Star'
                };

                if (!isFeedbackEditMode && ratingInputs.length > 0) {
                    ratingInputs.forEach(input => {
                        input.addEventListener('change', function () {
                            if (this.checked) {
                                ratingText.textContent = ratingDescriptions[this.value];
                                ratingText.classList.remove('text-primary');
                                ratingText.classList.add('text-warning');
                            }
                        });
                    });
                }
                });
            </script>

            <%-- Include Footer --%>
                <jsp:include page="footer.jsp"></jsp:include>