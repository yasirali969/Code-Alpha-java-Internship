
# 📈 Stock Trading Management System

A Java-based Stock Trading Management System developed using **Java Swing**, **JDBC**, and **MySQL**.
The application allows users to view live stock market data, buy stocks, manage their portfolio, and simulate market trends using an interactive graphical interface.

---

## 🚀 Features

* User account management with balance tracking

* Real-time stock market display

* Buy stocks from available market shares

* Portfolio management system

* Stock price visualization using custom graphs

* Market trend simulation (random price fluctuations)

* MySQL database integration using JDBC

* Modern Java Swing GUI interface

---

## 🛠️ Technologies Used

* Java

* Java Swing (GUI)

* JDBC (Database Connectivity)

* MySQL Database

* Object-Oriented Programming (OOP)

---

## 📂 Database Structure

### 🟦 Stocks Table

* stock_id (Primary Key)

* symbol

* company_name

* current_price

* available_shares

---

### 🟩 Users Table

* user_id (Primary Key)

* username

* phone_no

* balance

---

### 🟨 Holdings Table

* holding_id (Primary Key)

* user_id (Foreign Key)

* stock_id (Foreign Key)

* quantity

* avg_price

---

### 🟥 Transactions Table

* txn_id (Primary Key)

* user_id

* stock_id

* type (BUY/SELL)

* quantity

* price

* total

* txn_date (Timestamp)

---

## 📊 Main Functionalities

### 📌 View Market Data

Displays all available stocks with current prices and available shares.

---

### 📌 Buy Stocks

Users can purchase stocks if they have enough balance and shares are available.

---

### 📌 Portfolio Management

Users can view all purchased stocks along with investment details.

---

### 📌 Market Simulation

Simulates real market conditions by randomly increasing or decreasing stock prices.

---

### 📌 Graph Visualization

Displays stock price trends using a custom Java Swing graph panel.

---

## ⚙️ Setup Instructions

1. Install MySQL Server

2. Create database using provided SQL script

3. Update credentials in Java code:

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/stocktrading";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "your_password";
```

4. Add MySQL JDBC Connector to your project

5. Compile and run the application

---

## 🎯 Learning Outcomes

* Java Swing GUI development

* JDBC database connectivity

* MySQL database handling

* SQL queries and transactions

* Object-Oriented Programming concepts

* Real-time data simulation

* Data visualization using Java

---

## 👨‍💻 Author

**Yasir Ali**

Computer Science Student
Java Developer
Learning Database Systems & Software Development
