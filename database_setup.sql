-- ============================================================
-- PlaceTrack – Placement Cell Management System
-- MySQL Database Setup Script
-- ============================================================
-- Run this in MySQL Workbench BEFORE starting the application
-- OR let Spring Boot auto-create (spring.jpa.hibernate.ddl-auto=update)
-- ============================================================

-- 1. Create Database
CREATE DATABASE IF NOT EXISTS placement_cell_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE placement_cell_db;

-- ============================================================
-- TABLE: users
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  first_name  VARCHAR(100) NOT NULL,
  last_name   VARCHAR(100) NOT NULL,
  email       VARCHAR(255) NOT NULL UNIQUE,
  password    VARCHAR(255) NOT NULL,
  role        ENUM('ADMIN','STUDENT','RECRUITER') NOT NULL DEFAULT 'STUDENT',
  phone       VARCHAR(20),
  is_active   TINYINT(1)   DEFAULT 1,
  created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- TABLE: students
-- ============================================================
CREATE TABLE IF NOT EXISTS students (
  id               BIGINT         NOT NULL AUTO_INCREMENT,
  user_id          BIGINT         NOT NULL,
  roll_number      VARCHAR(50)    UNIQUE,
  department       VARCHAR(100),
  batch_year       INT,
  cgpa             DOUBLE,
  skills           TEXT,
  resume_url       VARCHAR(500),
  linkedin_url     VARCHAR(500),
  github_url       VARCHAR(500),
  address          TEXT,
  placement_status ENUM('PLACED','UNPLACED','IN_PROCESS') DEFAULT 'UNPLACED',
  placed_company   VARCHAR(200),
  package_lpa      DOUBLE,
  created_at       DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- TABLE: companies
-- ============================================================
CREATE TABLE IF NOT EXISTS companies (
  id             BIGINT       NOT NULL AUTO_INCREMENT,
  company_name   VARCHAR(200) NOT NULL,
  industry       VARCHAR(100),
  website        VARCHAR(500),
  description    TEXT,
  contact_person VARCHAR(200),
  contact_email  VARCHAR(255),
  contact_phone  VARCHAR(20),
  location       VARCHAR(200),
  logo_url       VARCHAR(500),
  is_active      TINYINT(1) DEFAULT 1,
  created_at     DATETIME   DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- TABLE: job_postings
-- ============================================================
CREATE TABLE IF NOT EXISTS job_postings (
  id                   BIGINT       NOT NULL AUTO_INCREMENT,
  company_id           BIGINT       NOT NULL,
  job_title            VARCHAR(200) NOT NULL,
  job_description      TEXT,
  required_skills      TEXT,
  min_cgpa             DOUBLE,
  package_lpa          DOUBLE,
  job_type             VARCHAR(50),
  location             VARCHAR(200),
  vacancies            INT,
  application_deadline DATE,
  batch_year           INT,
  department_eligible  VARCHAR(300),
  status               ENUM('ACTIVE','CLOSED','DRAFT') DEFAULT 'ACTIVE',
  created_at           DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- TABLE: applications
-- ============================================================
CREATE TABLE IF NOT EXISTS applications (
  id             BIGINT NOT NULL AUTO_INCREMENT,
  student_id     BIGINT NOT NULL,
  job_posting_id BIGINT NOT NULL,
  status         ENUM('APPLIED','SHORTLISTED','INTERVIEW_SCHEDULED','SELECTED','REJECTED','WITHDRAWN') DEFAULT 'APPLIED',
  cover_letter   TEXT,
  notes          TEXT,
  applied_at     DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_student_job (student_id, job_posting_id),
  FOREIGN KEY (student_id)     REFERENCES students(id)     ON DELETE CASCADE,
  FOREIGN KEY (job_posting_id) REFERENCES job_postings(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- SEED DATA (optional – Spring DataInitializer does this too)
-- ============================================================
-- NOTE: The application auto-seeds data on first startup.
-- You can manually insert data here if needed.

-- Verify tables
SELECT 'Database setup complete!' AS Status;
SHOW TABLES;
