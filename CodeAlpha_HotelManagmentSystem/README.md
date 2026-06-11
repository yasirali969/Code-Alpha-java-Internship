
# 🏨 Hotel Reservation Management System

A Java-based **Hotel Reservation Management System** built using **Java Swing**, **JDBC**, and **MySQL**.  
This project provides a complete desktop application for managing hotel operations like room booking, customer management, and reservation tracking with a modern GUI.

---

## 🚀 Features

- 🛏 View all hotel rooms with availability status  

- 👤 Add and manage customers  

- 📋 Book rooms with check-in & check-out system  

- ❌ Cancel reservations instantly  

- 📑 View all reservations in a structured table  

- 🔍 Search rooms by category  

- 💰 Automatic room availability update  

- 🗄 MySQL database integration using JDBC  

- 🎨 Modern dark-themed Swing UI  

---

## 🛠️ Technologies Used

- Java (Core + OOP)  

- Java Swing (GUI)  

- JDBC (Database Connectivity)  

- MySQL Database  

- AWT (UI styling)  

---

## 🗄️ Database Structure

### 🏠 Rooms Table

- room_id  
- category  
- price  
- availability  

---

### 👤 Customers Table

- customer_id  
- name  
- phone  
- email  

---

### 📋 Reservations Table

- reservation_id  
- customer_id  
- room_id  
- check_in_date  
- check_out_date  
- payment_status  

---

## ⚙️ Setup Instructions

### 1. Install Requirements

- Java JDK (8 or above)  
- MySQL Server  
- Any IDE (IntelliJ / Eclipse / NetBeans / VS Code)  

---

### 2. Create Database

```sql
CREATE DATABASE hotel_db;
