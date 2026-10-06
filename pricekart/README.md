# PriceKart

**Tagline:** "Compare. Save. Shop Smarter."

A full‑stack e‑commerce marketplace built with:
- **Frontend:** React, Vite, TypeScript, Tailwind CSS, TanStack Query, Redux Toolkit
- **Backend:** Spring Boot (Java 17), Spring Security, JWT, JPA (Hibernate)
- **Database:** MySQL (Docker)
- **Containerisation:** Docker & Docker‑Compose

## Project Structure
```
pricekart/
├── backend/            # Spring Boot API
├── frontend/           # React SPA
├── docker-compose.yml  # Orchestrates services
└── README.md           # You are here!
```

## Getting Started
```sh
# Clone the repo (if remote) or navigate to this folder
cd pricekart

# Start all services (MySQL, backend, frontend)
docker compose up --build
```
The frontend will be available at `http://localhost:5173` and the backend at `http://localhost:8080`.

---
*The rest of the codebase will be added in subsequent phases.*
