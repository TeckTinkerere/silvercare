# SilverCare Deployment Guide

## Overview
This guide covers deploying the SilverCare application to Render.com using Docker containers.

## Architecture
- **silvercare-api**: Spring Boot REST API (Port 8081)
- **silvercare-web**: Tomcat JSP/Servlet Web App (Port 8080)
- **PostgreSQL Database**: Shared database on Render

## Prerequisites
1. GitHub account with your code pushed
2. Render.com account (free tier available)
3. Stripe account for payment processing

## Deployment Steps

### 1. Connect to Render

1. Go to [Render Dashboard](https://dashboard.render.com/)
2. Click "New" → "Blueprint"
3. Connect your GitHub repository: `TeckTinkerere/silvercare`
4. Render will detect the `render.yaml` file

### 2. Configure Environment Variables

After the blueprint is created, you need to set the Stripe keys manually:

#### For silvercare-api:
- `STRIPE_SECRET_KEY`: Your Stripe secret key (sk_test_...)
- `STRIPE_PUBLISHABLE_KEY`: Your Stripe publishable key (pk_test_...)
- `STRIPE_WEBHOOK_SECRET`: Your Stripe webhook secret (whsec_...)

#### For silvercare-web:
- `STRIPE_PUBLISHABLE_KEY`: Your Stripe publishable key (pk_test_...)

### 3. Database Setup

The PostgreSQL database will be automatically created by Render. The connection details will be automatically injected into both services.

### 4. Deploy

1. Click "Apply" to deploy all services
2. Wait for the build and deployment to complete (5-10 minutes)
3. Check the logs for any errors

## Service URLs

After deployment, your services will be available at:
- API: `https://silvercare-api.onrender.com/s-api`
- Web: `https://silvercare-web.onrender.com`

## Local Docker Testing

### Build and Run API
```bash
cd silvercare-api
docker build -t silvercare-api .
docker run -p 8081:8081 \
  -e DB_URL=your_db_url \
  -e DB_USER=your_db_user \
  -e DB_PASSWORD=your_db_password \
  -e STRIPE_SECRET_KEY=your_stripe_key \
  silvercare-api
```

### Build and Run Web
```bash
cd silvercare-web
docker build -t silvercare-web .
docker run -p 8080:8080 \
  -e DB_URL=your_db_url \
  -e DB_USER=your_db_user \
  -e DB_PASSWORD=your_db_password \
  silvercare-web
```

## Troubleshooting

### Build Fails
- Check that all dependencies in `pom.xml` are correct
- Ensure Java 17 is specified
- Review build logs in Render dashboard

### Database Connection Issues
- Verify environment variables are set correctly
- Check database is running and accessible
- Review connection string format

### Application Won't Start
- Check application logs in Render dashboard
- Verify all required environment variables are set
- Ensure ports are correctly exposed

## Free Tier Limitations

Render free tier includes:
- Services spin down after 15 minutes of inactivity
- First request after spin-down takes 30-60 seconds
- 750 hours/month of runtime per service
- Shared database with limited storage

## Production Considerations

For production deployment:
1. Upgrade to paid Render plans for always-on services
2. Use production Stripe keys instead of test keys
3. Enable SSL/TLS (automatic on Render)
4. Set up monitoring and alerts
5. Configure backup strategy for database
6. Implement proper logging and error tracking
