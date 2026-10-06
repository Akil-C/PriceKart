# PriceKart Deployment Guide

This project is separated into a Frontend (React/Vite) and a Backend (Spring Boot), utilizing TiDB as the cloud database.

## 1. Database Setup (TiDB)
1. Go to the [TiDB Cloud Console](https://tidbcloud.com/).
2. Create a new Serverless cluster.
3. Get the MySQL connection string (it will look like `jdbc:mysql://<host>:4000/pricekart?useSSL=true&serverTimezone=UTC`).
4. Keep the username and password handy.

## 2. Backend Deployment (Render)
The backend is completely configured for one-click deployment on Render.
1. Sign up for [Render](https://render.com).
2. Go to your Dashboard and click **New+** -> **Blueprint**.
3. Connect your GitHub repository (`Akil-C/PriceKart`).
4. Render will automatically detect the `render.yaml` file in the root.
5. Provide the necessary environment variables:
   - `SPRING_DATASOURCE_URL`: The TiDB JDBC connection string.
   - `SPRING_DATASOURCE_USERNAME`: Your TiDB username.
   - `SPRING_DATASOURCE_PASSWORD`: Your TiDB password.
   - `JWT_SECRET`: A secure base-64 encoded string (at least 256 bits).
   - `CORS_ALLOWED_ORIGINS`: The URL of your frontend (e.g., `https://pricekart-frontend.vercel.app` or `*`).
6. Click **Apply** to deploy the backend.

## 3. Frontend Deployment (Vercel)
The frontend is built with React + Vite and configured for Vercel.
1. Go to [Vercel](https://vercel.com/) and create a new project.
2. Import the `Akil-C/PriceKart` repository.
3. **Important:** Change the "Root Directory" in Vercel to `frontend`.
4. The framework preset should automatically detect `Vite`.
5. Add the following Environment Variable:
   - `VITE_API_BASE_URL`: The URL of your deployed Render backend (e.g., `https://pricekart-backend.onrender.com`).
6. Click **Deploy**.

Your application should now be fully live with Vercel serving the frontend, Render serving the backend, and TiDB storing the data!
