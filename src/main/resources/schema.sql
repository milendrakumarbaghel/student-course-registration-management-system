CREATE DATABASE IF NOT EXISTS student_course_registration;
USE student_course_registration;

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

  CONSTRAINT unique_student_course UNIQUE (student_id, course_id),
  CONSTRAINT fk_registration_student FOREIGN KEY (student_id) REFERENCES students(student_id),
  CONSTRAINT fk_registration_course FOREIGN KEY (course_id) REFERENCES courses(course_id)
);
