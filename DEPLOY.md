# Deployment on VPS

## Prerequisites

- VPS with Docker and Docker Compose installed
- Images are published to Docker Hub via CI/CD (push on `main`)

## 1. Connect to the VPS

```bash
ssh user@<VPS_IP>
```

## 2. Clone the repository

```bash
git clone <REPO_URL> shopwise
cd shopwise
```

## 3. Configure environment variables

```bash
cp .env.example .env
nano .env
```

Fill `.env` with secure values:

```env
POSTGRES_DB=shopwise
POSTGRES_USER=shopwise_user
POSTGRES_PASSWORD=<strong_password>
```

## 4. Start the application

```bash
docker compose -f docker-compose.prod.yml up -d
```

The application is available at `http://<VPS_IP>`.

## 5. Update the application

After a push on `main`, CI/CD publishes new images. To deploy:

```bash
docker compose -f docker-compose.prod.yml pull
docker compose -f docker-compose.prod.yml up -d
```

## Useful commands

```bash
# View logs
docker compose -f docker-compose.prod.yml logs -f

# Check container status
docker compose -f docker-compose.prod.yml ps

# Stop the application
docker compose -f docker-compose.prod.yml down
```

## Architecture

```
Internet → frontend (Nginx :80) → backend (Spring Boot :8080) → db (PostgreSQL)
```

- The frontend proxies `/api/` to the backend via Docker's internal network.
- Port 5432 (PostgreSQL) is not exposed externally.
