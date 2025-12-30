# 🏗️ Integrated Construction Management System (ICMS)

A comprehensive backend system designed to digitalize construction operations including projects, tasks, materials, contractors, safety, attendance, and financial workflows.  
Built with **Java 21** and **Spring Boot 3**, ICMS demonstrates enterprise-grade architecture, real-world domain design, and JWT-based authentication with multi-role access control.

---

## 🚀 Tech Stack

| Category | Technology                            |
|---------|---------------------------------------|
| **Language** | Java 21                               |
| **Framework** | Spring Boot 6.5.6                     |
| **Database** | PostgreSQL                            |
| **Security** | Spring Security 6 + JWT               |
| **Build Tool** | Maven                                 |
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

### Task Management Module (Phase 3 – Daily Operations)
- Project-scoped task creation, update, assignment, and soft deletion
- Strict **role-based access** using project membership and project roles
- Tasks can be created by Project Managers and Site Engineers
- Task assignment restricted to **authorized roles** and valid project members
- Supervisors can update task status only for assigned tasks
- **Admin** and **Super Manager** have global override permissions
- Controlled task status transitions using `TaskStatus` enum
- Prevents invalid operations such as updating completed or unassigned tasks
- Authorization enforced at the service layer for business safety

---

## 📈 Roadmap

* [x] **Phase 1:** Foundation, Auth, Security Configuration, RBAC core.
* [x] **Phase 2:** Project Management (System Foundation).
* [x] **Phase 3:** Task Management (Daily Operations).
* [ ] **Phase 4:** Materials, Inventory & Requests (Construction-specific core).
