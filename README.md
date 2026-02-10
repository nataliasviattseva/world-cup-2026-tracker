# world-cup-2026-tracker

# World Cup 2026 Tracker

## Project Overview
This project is a web application that allows users to follow the FIFA World Cup 2026 in real time.
Users can view matches, scores, match status, and navigate through all competition phases
(group stage to final).

---

## Objectives
- Display World Cup 2026 matches by competition phase
- Show match details (teams, date, time, score, status)
- Update match data dynamically (real-time or near real-time)
- Provide a clear and user-friendly interface

---

## Team & Organization
- Team project (4 students)
- Agile-inspired organization (mini-sprints)
- Task tracking with Kanban
- Version control using GitHub

---

## Technologies Used
### Front-end
- React 18
- TypeScript
- Vite
- Tailwind CSS
- Radix UI

### Back-end
- Java 17
- Spring Boot 3
- Spring Data JPA
- MySQL

### Data
- MySQL Database

---

## 🚀 How to Run the Project

### Prerequisites
- **Java JDK 17** or higher
- **Node.js** (v18+ recommended) and **npm**
- **MySQL Server** running locally

### 1. Database Setup
1. Open your MySQL client (Workbench, CLI, etc.).
2. Create a new database named `tracker_database`:
   ```sql
   CREATE DATABASE tracker_database;
   ```
3. Verify the database credentials in `backend/src/main/resources/application.properties`:
   - Default username: `User2025ensitech?`
   - Default password: `User2025ensitech?`
   - *Update these values if your local MySQL configuration is different.*

### 2. Backend Setup (Spring Boot)
1. Navigate to the backend directory:
   ```bash
   cd backend
   ```
2. Run the application using Maven:
   ```bash
   ./mvnw spring-boot:run
   ```
   (On Windows, use `mvnw.cmd spring-boot:run` or just open the project in IntelliJ IDEA/Eclipse and run the main application class).

   The backend API will start on **http://localhost:8080**.

### 3. Frontend Setup (React + Vite)
1. Open a new terminal and navigate to the frontend directory:
   ```bash
   cd frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm run dev
   ```

   The frontend application will start (usually on **http://localhost:5173**).

### 4. Access the Application
- Open your browser and go to the URL provided by the frontend terminal (e.g., `http://localhost:5173`).
- You can now navigate through the World Cup 2026 phases and matches.


---

## Project Architecture
- Client / Server architecture
- RESTful API
- MVC / SPA principles

