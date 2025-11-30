# Cafe Management System  
### Java | JDBC | MySQL | Console Application

A complete **Console-based Cafe Management System** built using **Java + JDBC + MySQL**.  
This project supports item management (CRUD), category-based menu organization, and a full **Billing System with GST**.

---

## Features

### Item Management (CRUD)
- Add new cafe items  
- View items category-wise  
- Update item price  
- Delete items  

### Categories Supported
- Food  
- Beverages  
- Dessert  
- Savouries  

### Billing System
- Select items and quantity  
- Auto calculation of total  
- 5% GST calculation  
- Grand total display  
- Stores bill + bill items in MySQL  

### Database Integration
Uses MySQL with 3 tables:
- `cafe_items`
- `bills`
- `bill_items`

---

## Project Structure

projectwipro

Files included in this project:
- cafeApp.java (source code)
- CafeApp.class (compiled output)
- mysql-connector-j-9.5.0.jar (JDBC driver)



##  Tech Stack

- **Java (JDK 17/21/22+)**  
- **MySQL Database**  
- **JDBC (MySQL Connector)**  
- **CMD / Terminal Execution**



## How to Run

### 1. Compile the Java file
Use this command in CMD inside your project folder:
javac -cp ".;mysql-connector/mysql-connector-j-9.5.0.jar" CafeApp.java

This will compile the file and create **CafeApp.class**.

---

### 2. Run the application
After compiling, run the program using:
java -cp ".;mysql-connector/mysql-connector-j-9.5.0.jar" CafeApp


This will start the Cafe Management System in the console.


---

## Database Setup (SQL Schema)
CREATE DATABASE cafe_db;
USE cafe_db;

CREATE TABLE cafe_items (
id INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(100) NOT NULL,
price DOUBLE NOT NULL,
category VARCHAR(50)
);

CREATE TABLE bills (
bill_id INT PRIMARY KEY AUTO_INCREMENT,
bill_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
subtotal DOUBLE NOT NULL,
gst DOUBLE NOT NULL,
grand_total DOUBLE NOT NULL
);

CREATE TABLE bill_items (
item_id INT PRIMARY KEY AUTO_INCREMENT,
bill_id INT,
product_name VARCHAR(100),
price DOUBLE,
quantity INT,
total DOUBLE,
FOREIGN KEY (bill_id) REFERENCES bills(bill_id) ON DELETE CASCADE
);


---

## Sample Output (Console)

====== CAFE MANAGEMENT SYSTEM ======

Add Item

View Items

Update Price

Delete Item

Billing System

Exit

--- FINAL BILL ---
Item Price Qty Total

coffee 30 2 60
sandwich 99 1 99

Subtotal : ₹159
GST (5%) : ₹7.95
Grand Total : ₹166.95


---

##  Learning Outcomes
- JDBC connectivity  
- Console-based UI  
- CRUD operations  
- Using PreparedStatement  
- MySQL database design  
- Error handling  
- Real-world billing logic  

---

##  Author
**Vimal Vasu K**  
B.E ECE student  
GitHub: https://github.com/vasu387  
LinkedIn: https://www.linkedin.com/in/vimal-vasu-089850258/









