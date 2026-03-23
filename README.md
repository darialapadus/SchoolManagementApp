# 🎓 School Management System
A Spring Boot backend application for managing academic processes within an educational institution.

## 📌 Functional Requirements

The School Management System provides the following core functionalities:

### 👨‍🎓 Student Management
The system allows:
- Creating students
- Updating student information
- Viewing student details
- Deleting students

### 👩‍🏫 Teacher Management
The system allows:
- Managing teachers
- Managing teacher departments

### 🏫 Classroom Management
The system allows:
- Creating classrooms
- Managing classroom capacity and identification
- Updating and deleting classrooms

### 📚 Course Management
The system allows:
- Creating courses
- Assigning courses to teachers
- Assigning courses to classrooms
- Updating and deleting courses

### 📝 Enrollment Management
The system allows:
- Enrolling students in courses
- Preventing duplicate enrollments
- Viewing and managing enrollments

### 📊 Grade Management
The system allows:
- Assigning grades to students
- Updating existing grades
- Managing student evaluations

### 📅 Attendance Management
The system allows:
- Marking student attendance
- Tracking attendance records

### ✅ Data Validation
The system ensures:
- Validation of input data
- Prevention of invalid or inconsistent data

### 💾 Data Persistence
The system ensures:
- Storage of all data in a relational database

### 📖 API Documentation
The system provides:
- Full API documentation using Swagger
- Easy testing of endpoints via Swagger UI

## 🗂️ System Architecture

The application is based on a relational database and includes the following entities:

- Student  
- Teacher  
- Classroom  
- Course  
- Enrollment  
- Grade  
- Attendance  

<p align="center">
  <img src="docs/diagram.png" alt="Database Diagram" width="700"/>
</p>

<p align="center">
  <em>Figure 1: Database diagram of the School Management System</em>
</p>
