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

After the blueprint is created, you need to manually set these environment variables in the Render dashboard:

#### For silvercare-api service:

Navigate to: Dashboard → silvercare-api → Environment

**Required Variables (must be set manually):**

| Variable Name | Value | Where to Get It |
|--------------|-------|-----------------|
| `DB_URL` | `jdbc:postgresql://your-neon-host.neon.tech/neondb?sslmode=require&channelBinding=require` | Your Neon database connection string |
| `DB_USER` | `your_db_username` | Your Neon database username |
| `DB_PASSWORD` | `your_db_password` | Your Neon database password |
| `STRIPE_SECRET_KEY` | `sk_test_...` | From Stripe Dashboard → Developers → API Keys |
| `STRIPE_PUBLISHABLE_KEY` | `pk_test_...` | From Stripe Dashboard → Developers → API Keys |
| `STRIPE_WEBHOOK_SECRET` | `whsec_...` | From Stripe Dashboard → Developers → Webhooks (after creating webhook) |

**Auto-configured Variables (already set in render.yaml):**
- `STRIPE_MOCK_ENABLED`: `false`
- `CLIENT_URL`: `https://silvercare-web.onrender.com`
- `PORT`: `8081`

#### For silvercare-web service:

Navigate to: Dashboard → silvercare-web → Environment

**Required Variables (must be set manually):**

| Variable Name | Value | Where to Get It |
|--------------|-------|-----------------|
| `DB_URL` | `jdbc:postgresql://your-neon-host.neon.tech/neondb?sslmode=require&channelBinding=require` | Your Neon database connection string (same as API) |
| `DB_USER` | `your_db_username` | Your Neon database username (same as API) |
| `DB_PASSWORD` | `your_db_password` | Your Neon database password (same as API) |
| `STRIPE_PUBLISHABLE_KEY` | `pk_test_...` | From Stripe Dashboard → Developers → API Keys (same as API) |

**Auto-configured Variables (already set in render.yaml):**
- `API_BASE_URL`: `http://silvercare-api:8081/s-api`

#### Quick Setup Checklist:

1. ✅ Copy your Neon database credentials (DB_URL, DB_USER, DB_PASSWORD)
2. ✅ Copy your Stripe test keys from Stripe Dashboard
3. ✅ Set all required variables for silvercare-api
4. ✅ Set all required variables for silvercare-web
5. ✅ Click "Save Changes" after adding each variable
6. ✅ Services will automatically redeploy with new variables

**Important Notes:**
- Use the EXACT values from your .env file for consistency
- Test keys (sk_test_, pk_test_) are fine for demo/module purposes
- Don't commit these values to GitHub - they're already in .gitignore
- Variables marked as "sync: false" in render.yaml must be set manually

### 3. Database Setup

The PostgreSQL database will be automatically created by Render. The connection details will be automatically injected into both services.

---

## Quick Reference: Environment Variables Copy-Paste Guide

### silvercare-api Environment Variables

```
DB_URL=jdbc:postgresql://your-neon-host.neon.tech/neondb?sslmode=require&channelBinding=require
DB_USER=your_db_username
DB_PASSWORD=your_db_password
STRIPE_SECRET_KEY=sk_test_your_stripe_secret_key
STRIPE_PUBLISHABLE_KEY=pk_test_your_stripe_publishable_key
STRIPE_WEBHOOK_SECRET=whsec_your_webhook_secret
```

### silvercare-web Environment Variables

```
DB_URL=jdbc:postgresql://your-neon-host.neon.tech/neondb?sslmode=require&channelBinding=require
DB_USER=your_db_username
DB_PASSWORD=your_db_password
STRIPE_PUBLISHABLE_KEY=pk_test_your_stripe_publishable_key
```

**How to add these in Render:**
1. Go to your service (silvercare-api or silvercare-web)
2. Click "Environment" in the left sidebar
3. Click "Add Environment Variable"
4. Copy the variable name, then paste YOUR ACTUAL VALUE from .env file
5. Click "Save Changes"
6. Repeat for each variable

**Important:** Replace the placeholder values above with your actual credentials from your local .env file.

---

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
