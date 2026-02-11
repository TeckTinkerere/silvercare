<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%-- Include Header --%>
<jsp:include page="header.jsp" />

<main class="container my-5">

    <!-- Hero Section with Image -->
    <section class="about-hero bg-white p-5 rounded shadow-sm mb-5 fade-in">
        <div class="row align-items-center g-4">
            <div class="col-lg-6">
                <div class="about-content">
                    <div class="badge bg-primary bg-opacity-10 text-primary px-3 py-2 mb-3">
                        <i class="fas fa-heart me-2"></i>About Us
                    </div>
                    <h1 class="display-4 fw-bold mb-4">About SilverCare</h1>
                    <p class="lead text-primary mb-4" style="font-size: 1.25rem;">
                        Your trusted partner in senior care.
                    </p>
                    <p class="text-muted" style="line-height: 1.8; font-size: 1.05rem;">
                        SilverCare was founded on a simple yet profound principle: to provide seniors with the 
                        <strong class="text-dark">compassionate, respectful, and high-quality care</strong> they deserve, 
                        right in the comfort of their own homes. We understand that every individual has unique needs, 
                        and our mission is to create personalized care plans that promote independence, dignity, and a fulfilling life.
                    </p>
                    <div class="mt-4 d-flex flex-wrap gap-3">
                        <a href="${pageContext.request.contextPath}/ServiceServlet?action=category" class="btn btn-primary">
                            <i class="fas fa-hands-helping me-2"></i>View Our Services
                        </a>
                        <a href="${pageContext.request.contextPath}/contact" class="btn btn-outline-primary">
                            <i class="fas fa-envelope me-2"></i>Contact Us
                        </a>
                    </div>
                </div>
            </div>
            <div class="col-lg-6">
                <div class="about-image-wrapper position-relative">
                    <div class="image-decoration"></div>
                    <img src="${pageContext.request.contextPath}/FrontEnd/images/about-us.png" class="img-fluid rounded shadow" alt="Caregiver with a senior">
                </div>
            </div>
        </div>
    </section>

   <!-- Stats Section -->
<section class="stats-section mb-5 slide-up">
    <div class="bg-gradient text-white p-5 rounded shadow-sm" 
         style="background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-dark) 100%);">
        <div class="row text-center g-4">
            <div class="col-md-3 col-6">
                <div class="stat-item">
                    <div class="stat-icon mb-3">
                        <i class="fas fa-users" style="font-size: 2.5rem; color: #000000 !important; opacity: 0.9;"></i>
                    </div>
                    <h3 class="display-5 fw-bold mb-2">500+</h3>
                    <p class="mb-0 opacity-75">Families Served</p>
                </div>
            </div>
            <div class="col-md-3 col-6">
                <div class="stat-item">
                    <div class="stat-icon mb-3">
                        <i class="fas fa-user-nurse" style="font-size: 2.5rem; color: #000000 !important; opacity: 0.9;"></i>
                    </div>
                    <h3 class="display-5 fw-bold mb-2">50+</h3>
                    <p class="mb-0 opacity-75">Certified Caregivers</p>
                </div>
            </div>
            <div class="col-md-3 col-6">
                <div class="stat-item">
                    <div class="stat-icon mb-3">
                        <i class="fas fa-calendar-check" style="font-size: 2.5rem; color: #000000 !important; opacity: 0.9;"></i>
                    </div>
                    <h3 class="display-5 fw-bold mb-2">10+</h3>
                    <p class="mb-0 opacity-75">Years Experience</p>
                </div>
            </div>
            <div class="col-md-3 col-6">
                <div class="stat-item">
                    <div class="stat-icon mb-3">
                        <i class="fas fa-star" style="font-size: 2.5rem; color: #000000 !important; opacity: 0.9;"></i>
                    </div>
                    <h3 class="display-5 fw-bold mb-2">98%</h3>
                    <p class="mb-0 opacity-75">Satisfaction Rate</p>
                </div>
            </div>
        </div>
    </div>
</section>

    <!-- Mission &amp; Values Section -->
    <section class="mission-values-section slide-up">
        <div class="text-center mb-5">
            <h2 class="h1 fw-bold mb-3">Our Mission &amp; Values</h2>
            <p class="text-muted" style="max-width: 600px; margin: 0 auto;">
                Guided by our core principles, we strive to make a meaningful difference in the lives of seniors
            </p>
        </div>
        
        <div class="row g-4">
            <!-- Mission Card -->
            <div class="col-lg-6">
                <div class="mission-card card h-100 border-0 shadow-sm p-4">
                    <div class="card-icon mb-4">
                        <div class="icon-wrapper bg-primary bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center" 
                             style="width: 70px; height: 70px;">
                            <i class="fas fa-bullseye text-primary" style="font-size: 1.75rem;"></i>
                        </div>
                    </div>
                    <h2 class="h3 fw-bold mb-3">Our Mission</h2>
                    <p class="text-muted mb-0" style="line-height: 1.8; font-size: 1.05rem;">
                        To enhance the lives of aging adults and their families by providing exceptional, 
                        personalized in-home care services that are delivered with <strong class="text-dark">integrity, 
                        compassion, and professionalism</strong>.
                    </p>
                </div>
            </div>
            
            <!-- Values Card -->
            <div class="col-lg-6">
                <div class="values-card card h-100 border-0 shadow-sm p-4">
                    <div class="card-icon mb-4">
                        <div class="icon-wrapper bg-primary bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center" 
                             style="width: 70px; height: 70px;">
                            <i class="fas fa-heart text-primary" style="font-size: 1.75rem;"></i>
                        </div>
                    </div>
                    <h2 class="h3 fw-bold mb-3">Our Core Values</h2>
                    <ul class="values-list list-unstyled mb-0">
                        <li class="mb-3 d-flex">
                            <div class="value-icon me-3">
                                <i class="fas fa-check-circle text-primary"></i>
                            </div>
                            <div>
                                <strong class="text-dark">Compassion:</strong>
                                <span class="text-muted"> We treat every client with empathy and kindness.</span>
                            </div>
                        </li>
                        <li class="mb-3 d-flex">
                            <div class="value-icon me-3">
                                <i class="fas fa-check-circle text-primary"></i>
                            </div>
                            <div>
                                <strong class="text-dark">Respect:</strong>
                                <span class="text-muted"> We honor the dignity and choices of every individual.</span>
                            </div>
                        </li>
                        <li class="mb-3 d-flex">
                            <div class="value-icon me-3">
                                <i class="fas fa-check-circle text-primary"></i>
                            </div>
                            <div>
                                <strong class="text-dark">Integrity:</strong>
                                <span class="text-muted"> We build trust through honest and ethical practices.</span>
                            </div>
                        </li>
                        <li class="d-flex">
                            <div class="value-icon me-3">
                                <i class="fas fa-check-circle text-primary"></i>
                            </div>
                            <div>
                                <strong class="text-dark">Excellence:</strong>
                                <span class="text-muted"> We are committed to the highest standards of care.</span>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </section>

    <!-- Why Choose Us Section -->
    <section class="why-choose-section my-5 slide-up">
        <div class="bg-white p-5 rounded shadow-sm">
            <div class="text-center mb-5">
                <h2 class="h1 fw-bold mb-3">Why Choose SilverCare</h2>
                <p class="text-muted" style="max-width: 600px; margin: 0 auto;">
                    We go above and beyond to ensure the highest quality of care for your loved ones
                </p>
            </div>
            
            <div class="row g-4">
                <div class="col-md-4">
                    <div class="feature-item text-center">
                        <div class="feature-icon mb-3">
                            <div class="bg-primary bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center" 
                                 style="width: 60px; height: 60px;">
                                <i class="fas fa-certificate text-primary" style="font-size: 1.5rem;"></i>
                            </div>
                        </div>
                        <h3 class="h5 fw-semibold mb-2">Certified Professionals</h3>
                        <p class="text-muted mb-0">All our caregivers are licensed, trained, and background-checked</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="feature-item text-center">
                        <div class="feature-icon mb-3">
                            <div class="bg-primary bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center" 
                                 style="width: 60px; height: 60px;">
                                <i class="fas fa-clipboard-list text-primary" style="font-size: 1.5rem;"></i>
                            </div>
                        </div>
                        <h3 class="h5 fw-semibold mb-2">Personalized Care Plans</h3>
                        <p class="text-muted mb-0">Tailored services that meet the unique needs of each individual</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="feature-item text-center">
                        <div class="feature-icon mb-3">
                            <div class="bg-primary bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center" 
                                 style="width: 60px; height: 60px;">
                                <i class="fas fa-clock text-primary" style="font-size: 1.5rem;"></i>
                            </div>
                        </div>
                        <h3 class="h5 fw-semibold mb-2">24/7 Support</h3>
                        <p class="text-muted mb-0">Round-the-clock availability for emergency assistance and inquiries</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

</main>

<!-- Additional Styles -->
<style>
    /* About Hero Image Decoration */
    .about-image-wrapper {
        position: relative;
    }
    
    .image-decoration {
        position: absolute;
        top: -20px;
        right: -20px;
        width: 100%;
        height: 100%;
        border: 3px solid var(--accent-color);
        border-radius: var(--border-radius-lg);
        z-index: -1;
    }
    
    /* Mission &amp; Values Cards */
    .mission-card, .values-card {
        transition: all var(--transition-medium);
        border-radius: var(--border-radius-lg);
    }
    
    .mission-card:hover, .values-card:hover {
        transform: translateY(-8px);
        box-shadow: var(--shadow-lg);
    }
    
    .mission-card .icon-wrapper, .values-card .icon-wrapper {
        transition: all var(--transition-fast);
    }
    
    .mission-card:hover .icon-wrapper, .values-card:hover .icon-wrapper {
        transform: scale(1.1);
        background-color: var(--primary-color) !important;
    }
    
    .mission-card:hover .icon-wrapper i, .values-card:hover .icon-wrapper i {
        color: white !important;
    }
    
    /* Values List Icons */
    .value-icon {
        width: 24px;
        display: flex;
        align-items: flex-start;
        padding-top: 2px;
    }
    
    .values-list li {
        transition: all var(--transition-fast);
        padding: 0.5rem;
        border-radius: var(--border-radius);
    }
    
    .values-list li:hover {
        background: rgba(91, 124, 153, 0.05);
        padding-left: 1rem;
    }
    
    /* Feature Items Animation */
    .feature-item {
        transition: all var(--transition-medium);
    }
    
    .feature-item:hover .feature-icon .bg-primary {
        background-color: var(--primary-color) !important;
    }
    
    .feature-item:hover .feature-icon i {
        color: white !important;
        transform: scale(1.1);
    }
    
    .feature-icon i {
        transition: all var(--transition-fast);
    }
    
    /* Responsive Adjustments */
    @media (max-width: 768px) {
        .about-hero h1 {
            font-size: 2rem;
        }
        
        .image-decoration {
            display: none;
        }
        
        .stat-item h3 {
            font-size: 2rem;
        }
    }
</style>

<%-- Include Footer --%>
<jsp:include page="footer.jsp"></jsp:include>