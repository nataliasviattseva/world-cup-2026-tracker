# World Cup 2026 Tracker

[![GitHub release](https://img.shields.io/github/v/release/nataliasviattseva/world-cup-2026-tracker)](https://github.com/nataliasviattseva/world-cup-2026-tracker/releases)
[![GitHub stars](https://img.shields.io/github/stars/nataliasviattseva/world-cup-2026-tracker?style=social)](https://github.com/nataliasviattseva/world-cup-2026-tracker/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/nataliasviattseva/world-cup-2026-tracker?style=social)](https://github.com/nataliasviattseva/world-cup-2026-tracker/network/members)
[![GitHub issues](https://img.shields.io/github/issues/nataliasviattseva/world-cup-2026-tracker)](https://github.com/nataliasviattseva/world-cup-2026-tracker/issues)
[![GitHub license](https://img.shields.io/github/license/nataliasviattseva/world-cup-2026-tracker)](https://github.com/nataliasviattseva/world-cup-2026-tracker/blob/main/LICENSE)

![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-6DB33F?logo=spring&logoColor=white)
![React](https://img.shields.io/badge/React-18.3-61DAFB?logo=react&logoColor=black)
![TypeScript](https://img.shields.io/badge/TypeScript-5-3178C6?logo=typescript&logoColor=white)
![Vite](https://img.shields.io/badge/Vite-6.3-646CFF?logo=vite&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white)
![Tailwind CSS](https://img.shields.io/badge/Tailwind-4.1-06B6D4?logo=tailwindcss&logoColor=white)

A comprehensive web application for tracking and following the FIFA World Cup 2026 in real-time, featuring live match updates, team statistics, group standings, and detailed match information across all competition phases.

---

## Table of Contents
- [Features](#-features)
- [Technologies](#️-technologies)
- [Architecture](#-architecture)
- [Getting Started](#-getting-started)
- [Configuration](#️-configuration)
- [API Documentation](#-api-documentation)
- [Security](#-security)
- [Development](#-development)
- [Contributing](#-contributing)

---

## Features

- **Live Match Tracking**: Real-time match updates with scores and status
- **Competition Phases**: Navigate through all stages from group phase to final
- **Team Management**: View all 48 qualified teams with FIFA rankings and confederations
- **Group Standings**: Track team positions, points, and statistics for all groups
- **Match Details**: Comprehensive match information including events, statistics, and lineups
- **Team Statistics**: Detailed team performance metrics and match history
- **Responsive Design**: Optimized for desktop and mobile devices
- **Intuitive UI**: User-friendly interface built with modern design principles

---

## Technologies

### Frontend
- **React 18.3** - UI library
- **TypeScript 5** - Type-safe development
- **Vite 6.3** - Fast build tool and development server
- **Tailwind CSS 4.1** - Utility-first CSS framework
- **Radix UI** - Accessible component primitives

### Backend
- **Java 17** - Programming language
- **Spring Boot 3.5.7** - Application framework
- **Spring Data JPA** - Data persistence
- **MySQL 8.0** - Relational database
- **Maven** - Dependency management

### Development Tools
- **Git** - Version control
- **GitHub** - Repository hosting
- **Kanban** - Task management

---

## Architecture

- **Client-Server Architecture**: Clear separation between frontend and backend
- **RESTful API**: JSON-based API for client-server communication
- **MVC Pattern**: Model-View-Controller design on backend
- **SPA**: Single Page Application on frontend
- **Repository Pattern**: Data access abstraction layer
- **Service Layer**: Business logic separation

---

## Getting Started

### Prerequisites

Ensure you have the following installed:
- **Java JDK 17** or higher ([Download](https://adoptium.net/))
- **Node.js 18+** and **npm** ([Download](https://nodejs.org/))
- **MySQL 8.0+** ([Download](https://dev.mysql.com/downloads/))
- **Git** ([Download](https://git-scm.com/))

### Installation

#### 1. Clone the Repository
```bash
git clone https://github.com/nataliasviattseva/world-cup-2026-tracker.git
cd world-cup-2026-tracker
```

#### 2. Database Setup
```bash
# Login to MySQL
mysql -u root -p

# Create database
CREATE DATABASE tracker_database;
CREATE USER 'wc_user'@'localhost' IDENTIFIED BY 'your_secure_password';
GRANT ALL PRIVILEGES ON tracker_database.* TO 'wc_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

#### 3. Environment Configuration

**Backend Configuration:**

Copy the backend environment template:
```bash
cp backend/.env.example backend/.env
```

Edit `backend/.env` with your database credentials:
```properties
DB_URL=jdbc:mysql://localhost:3306/tracker_database?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
DB_USERNAME=wc_user
DB_PASSWORD=your_secure_password
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password
```

**Docker Compose Configuration (Optional):**

If using Docker Compose for MySQL:
```bash
cp .env.example .env
```

Edit root `.env` for Docker Compose:
```properties
DB_NAME=tracker_database
DB_USERNAME=wc_user
DB_PASSWORD=your_secure_password
DB_ROOT_PASSWORD=root
```

Then start MySQL with Docker:
```bash
docker-compose up -d
```

#### 4. Start Backend
```bash
cd backend
./mvnw spring-boot:run
```
On Windows: `mvnw.cmd spring-boot:run`

Backend will start on **http://localhost:8080**

#### 5. Start Frontend
```bash
cd frontend
npm install
npm run dev
```

Frontend will start on **http://localhost:3001**

#### 6. Access Application
Open your browser and navigate to **http://localhost:3001**

---

## Configuration

### Environment Variables

For production deployment, use environment variables instead of hardcoded credentials:

**Backend** (application.properties):
```properties
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/tracker_database}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
```

**Frontend** (vite.config.ts):
```typescript
server: {
  proxy: {
    '/api': process.env.VITE_API_URL || 'http://localhost:8080'
  }
}
```

### Database Configuration

The application uses Spring Data JPA with automatic schema generation:
- **Development**: `spring.jpa.hibernate.ddl-auto=update`
- **Production**: `spring.jpa.hibernate.ddl-auto=validate` (recommended)

---

## API Documentation

### Base URL
```
http://localhost:8080/api
```

### Endpoints

#### Matches
- `GET /api/matches` - Get all matches
- `GET /api/matches/{id}` - Get match by ID
- `GET /api/matches/phase/{phaseId}` - Get matches by phase

#### Teams
- `GET /api/equipes` - Get all teams
- `GET /api/equipes/{id}` - Get team by ID

#### Groups
- `GET /api/groupes` - Get all groups
- `GET /api/groupes/{id}` - Get group by ID

#### Standings
- `GET /api/classements` - Get all standings
- `GET /api/classements/groupe/{groupId}` - Get standings by group

#### Phases
- `GET /api/phases` - Get all competition phases
- `GET /api/phases/{id}` - Get phase by ID

#### Statistics
- `GET /api/statistics/match/{matchId}` - Get match statistics

---

## Security

### Best Practices Implemented

1. **No Hardcoded Credentials**: Use environment variables for sensitive data
2. **Password Security**: Never commit passwords to version control
3. **SQL Injection Protection**: Parameterized queries via JPA
4. **CORS Configuration**: Controlled cross-origin requests
5. **Input Validation**: Server-side validation for all inputs
6. **Secure Communication**: HTTPS recommended for production

### Important Security Notes

**Never commit the following to version control:**
- Database passwords
- Email credentials
- API keys
- Secret tokens

**Always use:**
- Environment variables for sensitive configuration
- `.env` files (added to `.gitignore`)
- Secrets management tools for production
- Strong, unique passwords

---

## Development

### Team Organization
- 4-member development team
- Agile methodology with mini-sprints
- Kanban board for task tracking
- Code review process via Pull Requests

### Development Workflow
```bash
# Create feature branch
git checkout -b feature/your-feature-name

# Make changes and commit
git add .
git commit -m "Description of changes"

# Push to remote
git push origin feature/your-feature-name

# Create Pull Request on GitHub
```

### Code Quality
- TypeScript for type safety
- Consistent code formatting
- Component-based architecture
- Service layer separation
- Repository pattern implementation

---

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## License

This project is part of an academic assignment.

---

## Contact

Project Link: [https://github.com/nataliasviattseva/world-cup-2026-tracker](https://github.com/nataliasviattseva/world-cup-2026-tracker)

---

<div align="center">

**Built with ❤️ for FIFA World Cup 2026**

[![Made with Java](https://img.shields.io/badge/Made%20with-Java-ED8B00?logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Made with Spring Boot](https://img.shields.io/badge/Made%20with-Spring%20Boot-6DB33F?logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![Made with React](https://img.shields.io/badge/Made%20with-React-61DAFB?logo=react&logoColor=black)](https://reactjs.org/)
[![Made with TypeScript](https://img.shields.io/badge/Made%20with-TypeScript-3178C6?logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![Made with Vite](https://img.shields.io/badge/Made%20with-Vite-646CFF?logo=vite&logoColor=white)](https://vitejs.dev/)
[![Made with Tailwind CSS](https://img.shields.io/badge/Made%20with-Tailwind%20CSS-06B6D4?logo=tailwindcss&logoColor=white)](https://tailwindcss.com/)

</div>

