# SortMaster — Deployment & Hosting Guide

This archive contains everything needed to run and host the **SortMaster — DSA Sorting Academy** application on any cloud provider or server.

---

## Option 1: One-Click Cloud Deployment (Render / Railway / Fly.io)

### Deploying on Render (Free / Low Cost)
1. Push this project to a GitHub or GitLab repository (or upload the zip).
2. Go to [render.com](https://render.com) and click **New + > Web Service**.
3. Connect your repository.
4. Set the following settings:
   - **Environment:** Docker (Render will automatically detect the included `Dockerfile`)
   - **Branch:** `main` (or your default branch)
5. Click **Create Web Service**.
   - Render automatically builds and hosts the app on HTTPS.
   - Dynamic port routing (`PORT` environment variable) is pre-configured.

### Deploying on Railway
1. Go to [railway.app](https://railway.app) and create a new project.
2. Choose **Deploy from GitHub repo**.
3. Railway will detect the `Dockerfile` and deploy automatically with zero extra configuration.

---

## Option 2: Docker Container (Any VPS / Cloud Server)

Build and run the containerized application on any Linux, Mac, or Windows machine with Docker:

```bash
# 1. Build the Docker image
docker build -t sortmaster-academy .

# 2. Run the container on port 80 (or 8080)
docker run -d -p 8080:8080 --name sortmaster sortmaster-academy

# 3. Access in browser
http://localhost:8080
```

---

## Option 3: Direct Java Standalone Execution (Any VPS / Server)

If you have Java 21+ installed on your server (Ubuntu, Debian, CentOS, macOS, etc.):

```bash
# Run the pre-compiled production JAR:
java -jar target/sortmaster-academy-1.0.0.jar

# Or run in the background (as a systemd service or nohup):
nohup java -jar target/sortmaster-academy-1.0.0.jar > sortmaster.log 2>&1 &
```

To specify a custom port:
```bash
java -Dserver.port=80 -jar target/sortmaster-academy-1.0.0.jar
```

---

## Option 4: Static-Only Frontend (Optional)

All static web assets are located in:
`src/main/resources/static/`
- `index.html`
- `css/`
- `js/`

*Note: The frontend interacts with the REST API at `/api/*` for generating sorting animations and comparing algorithms. When hosted via Spring Boot or Docker (Options 1–3), both the frontend UI and the algorithm engine are seamlessly served together from the same origin.*
