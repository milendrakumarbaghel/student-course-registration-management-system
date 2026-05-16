# Student Course Registration & Management System

Servlet/JSP/JDBC mini project for admin-driven student, course, and registration management.

## Tech Stack
- Java 17+
- Jakarta Servlet 6.0
- JSP
- JDBC
- MySQL
- Apache Tomcat 10.1+
- Maven (WAR packaging)

## Features Implemented
- Admin login with DB credential verification
- Session-based authentication (`loggedInUser`, `loginTime`)
- Remember username using cookie (`rememberedUsername`)
- Dashboard with counts (students, courses, registrations)
- Student CRUD (add, list, edit, delete with registration restriction)
- Course CRUD (add, list, edit, delete with active-registration restriction)
- Registration CRUD (add, list, status update, delete)
- Duplicate active registration prevention
- Logout and session invalidation
- RequestDispatcher on validation/business errors
- sendRedirect on successful actions
- Servlet lifecycle logging on key servlets

## URL Map
### Public
- `/login` (GET: show page)
- `/login-action` (POST: process login)
- `/register-admin` (GET/POST)
- `/logout`

### Protected (require session)
- `/dashboard`
- `/students`, `/student/add`, `/student/edit`, `/student/delete`
- `/student/update`
- `/courses`, `/course/add`, `/course/edit`, `/course/delete`
- `/course/update`
- `/registrations`, `/registration/add`, `/registration/delete`, `/registration/status`
- `/registration/add-action`

## Controller Classes
- `LoginPageServlet`, `LoginServlet`, `LogoutServlet`, `DashboardServlet`
- `AddStudentServlet`, `ViewStudentsServlet`, `EditStudentServlet`, `UpdateStudentServlet`, `DeleteStudentServlet`
- `AddCourseServlet`, `ViewCoursesServlet`, `EditCourseServlet`, `UpdateCourseServlet`, `DeleteCourseServlet`
- `RegistrationFormServlet`, `RegisterStudentCourseServlet`, `ViewRegistrationsServlet`, `UpdateRegistrationStatusServlet`, `DeleteRegistrationServlet`

## Package Structure
- `com.studentcourse.controller` - Servlets and auth utility
- `com.studentcourse.dao` - JDBC DAO classes
- `com.studentcourse.model` - Model POJOs
- `com.studentcourse.util` - DB connection utility
- `src/main/webapp/WEB-INF/views` - JSP views

## Database Setup
Create DB and tables in MySQL:

```sql
CREATE DATABASE IF NOT EXISTS student_course_db;
USE student_course_db;

CREATE TABLE IF NOT EXISTS admin (
  admin_id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS students (
  student_id INT PRIMARY KEY AUTO_INCREMENT,
  student_name VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL,
  phone VARCHAR(15) NOT NULL,
  age INT NOT NULL,
  city VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS courses (
  course_id INT PRIMARY KEY AUTO_INCREMENT,
  course_name VARCHAR(100) NOT NULL,
  duration VARCHAR(50) NOT NULL,
  fees DOUBLE NOT NULL,
  trainer_name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS registrations (
  registration_id INT PRIMARY KEY AUTO_INCREMENT,
  student_id INT NOT NULL,
  course_id INT NOT NULL,
  registration_date DATE NOT NULL,
  status VARCHAR(20) NOT NULL,
  CONSTRAINT fk_registration_student FOREIGN KEY (student_id) REFERENCES students(student_id),
  CONSTRAINT fk_registration_course FOREIGN KEY (course_id) REFERENCES courses(course_id)
);

INSERT INTO admin(username, password)
SELECT 'admin', 'admin123'
WHERE NOT EXISTS (SELECT 1 FROM admin WHERE username='admin');
```

## DB Configuration
`DBConnection` reads env vars with defaults:
- `DB_URL` (default: `jdbc:mysql://localhost:3306/student_course_db`)
- `DB_USER` (default: `root`)
- `DB_PASS` (default: `admin`)

Example before running Tomcat:

```bash
export DB_URL="jdbc:mysql://localhost:3306/student_course_db"
export DB_USER="root"
export DB_PASS="admin"
```

## Build & Run
```bash
mvn clean package
```

Deploy generated WAR:
- `target/StudentCourseRegistrationAndManagementSystem.war`

Open:
- `http://localhost:8080/StudentCourseRegistrationAndManagementSystem/`

## Navigation Rules Used
- **Forward (`RequestDispatcher`)**:
  - Invalid login
  - Validation failures on forms
  - Business rule failures (delete restrictions)
  - Displaying lists/forms
- **Redirect (`sendRedirect`)**:
  - Successful login
  - Successful insert/update/delete
  - Logout
  - Unauthorized access to protected routes

## Lifecycle Demonstration
Console logs are present in:
- `LoginServlet`
- `DashboardServlet`
- `StudentServlet`
- `CourseServlet`

## Notes
- This is a learning project; plain-text password handling is intentionally kept simple for classroom scope.
- In real systems, use password hashing, CSRF protection, and centralized authorization filters.

