# 🎓 PlaceTrack – Placement Cell Management System

A full-stack Spring Boot web application for managing college placements.

---

## 📋 Features

| Module        | Features |
|---------------|----------|
| **Auth**      | Login, Register (Student/Admin), Role-based access |
| **Admin**     | Dashboard stats, Manage students, companies, jobs, applications, users |
| **Student**   | Dashboard, Profile editor, Browse & apply to jobs, Track applications |
| **Companies** | Add/view companies with contact info |
| **Jobs**      | Post jobs with eligibility, package, deadline |
| **Applications** | Apply with cover letter, track status updates |

---

## 🛠️ Tech Stack

- **Backend**: Java 17, Spring Boot 3.2, Spring Security, Spring Data JPA
- **Frontend**: Thymeleaf, HTML5, CSS3, Font Awesome, Google Fonts (Inter)
- **Database**: MySQL 8 (via MySQL Workbench)
- **Build**: Maven

---

## ⚙️ Setup Instructions

### 1. Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8 (MySQL Workbench recommended)

### 2. Database Setup
Open MySQL Workbench and run:
```sql
-- Option A: Let Spring Boot auto-create (recommended)
CREATE DATABASE IF NOT EXISTS placement_cell_db;

-- Option B: Run the full script
source /path/to/database_setup.sql
```

### 3. Configure Database Credentials
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/placement_cell_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root       # ← change if different
spring.datasource.password=root       # ← change to your MySQL password
```

### 4. Build & Run
```bash
cd placement-cell
mvn clean install
mvn spring-boot:run
```

### 5. Access the App
Open: **http://localhost:8080**

---

## 🔑 Demo Credentials (Auto-seeded on first run)

| Role    | Email                     | Password    |
|---------|---------------------------|-------------|
| Admin   | admin@placement.com       | admin123    |
| Student | student@placement.com     | student123  |

The app auto-creates 5 sample companies (TCS, Infosys, Wipro, Google, Amazon)
with active job postings on first startup.

---

## 📁 Project Structure

```
placement-cell/
├── src/main/java/com/placement/
│   ├── PlacementCellApplication.java      ← Main class
│   ├── config/
│   │   ├── SecurityConfig.java            ← Spring Security
│   │   └── DataInitializer.java           ← Seed data
│   ├── model/
│   │   ├── User.java
│   │   ├── Student.java
│   │   ├── Company.java
│   │   ├── JobPosting.java
│   │   └── Application.java
│   ├── repository/                        ← JPA Repositories
│   ├── service/                           ← Business Logic
│   └── controller/
│       ├── AuthController.java
│       ├── AdminController.java
│       └── StudentController.java
├── src/main/resources/
│   ├── application.properties
│   ├── static/
│   │   ├── css/
│   │   │   ├── main.css                   ← Global styles
│   │   │   ├── auth.css                   ← Login/Register
│   │   │   ├── dashboard.css              ← Sidebar & layout
│   │   │   └── components.css             ← UI components
│   │   └── js/
│   │       └── main.js
│   └── templates/
│       ├── auth/
│       │   ├── login.html
│       │   └── register.html
│       ├── admin/
│       │   ├── dashboard.html
│       │   ├── students.html
│       │   ├── student-detail.html
│       │   ├── companies.html
│       │   ├── jobs.html
│       │   ├── applications.html
│       │   └── users.html
│       ├── student/
│       │   ├── dashboard.html
│       │   ├── profile.html
│       │   ├── jobs.html
│       │   ├── job-detail.html
│       │   └── applications.html
│       └── fragments/
│           └── navbar.html
├── database_setup.sql
└── pom.xml
```

---

## 🔗 Application URLs

| URL | Description |
|-----|-------------|
| `/login` | Login page |
| `/register` | Registration page |
| `/admin/dashboard` | Admin dashboard |
| `/admin/students` | Student management |
| `/admin/companies` | Company management |
| `/admin/jobs` | Job posting management |
| `/admin/applications` | Application management |
| `/admin/users` | User management |
| `/student/dashboard` | Student dashboard |
| `/student/profile` | Edit student profile |
| `/student/jobs` | Browse open jobs |
| `/student/applications` | Track applications |

---

## 🐛 Troubleshooting

**MySQL connection refused:**
- Make sure MySQL is running on port 3306
- Check username/password in `application.properties`

**Port 8080 in use:**
- Change `server.port=8080` to another port in `application.properties`

**Table not created:**
- Ensure `spring.jpa.hibernate.ddl-auto=update` is set
- Or run `database_setup.sql` manually in MySQL Workbench

---

## 📝 Notes

- Passwords are encrypted with BCrypt
- Spring Security handles authentication and role-based authorization
- All data is auto-seeded on first startup via `DataInitializer`
- The app runs on `http://localhost:8080` by default
