# 🏗️ Integrated Construction Management System (ICMS)

A comprehensive backend system designed to digitalize construction operations including projects, tasks, materials, contractors, safety, attendance, and financial workflows.  
Built with **Java 21** and **Spring Boot 3**, ICMS demonstrates enterprise-grade architecture, real-world domain design, and JWT-based authentication with multi-role access control.

---

## 🚀 Tech Stack

| Category | Technology |
|---------|------------|
| **Language** | Java 21 |
| **Framework** | Spring Boot 3.x |
| **Database** | PostgreSQL |
| **Security** | Spring Security 6 + JWT |
| **Build Tool** | Maven |
| **Other** | JPA/Hibernate, Lombok, Validation API |

---
# ⚡️ Core Features
### 🔏 Authentication & Security
### ☑️ RBAC + JWT
- Stateless JWT Authentication  
- Role-Based Access Control (RBAC) using Spring Security
- BCrypt-based password hashing  
- Secure access using both **global roles** and **project-level roles**  
- Endpoints: `/auth/login`, `/auth/register`, `/me`

### Project Module 
- Introduces the Project entity with Project Name, Client, Location, Start Date, Status, and Planned Budget.
- Secure CRUD APIs: Create / Update / Get / List / Delete projects
- Role-restricted access: ADMIN, PROJECT_MANAGER, ACCOUNTANT
- Uses a ProjectStatus enum (PLANNED, ACTIVE, ON_HOLD, COMPLETED) with auditing timestamps.
- Prepares foundation for upcoming modules like project members, tasks, and assignments.


---

## 📈 Roadmap

* [x] **Phase 1:** Foundation, Auth, Security Configuration, RBAC core.
* [x] **Phase 2:** Project Management (System Foundation).
* [ ] **Phase 3:** Task Management (Daily Operations).
* [ ] **Phase 4:** Materials, Inventory & Requests (Construction-specific core).
