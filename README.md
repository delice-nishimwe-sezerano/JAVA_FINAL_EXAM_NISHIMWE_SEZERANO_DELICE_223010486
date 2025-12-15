# E-Commerce Monitoring System (Java Swing)

## Overview
The **E-Commerce Monitoring System** is a standalone desktop application developed using **Java Swing** and **MySQL** to provide 
centralized management of e-commerce operations for small to medium-sized businesses. The system integrates user management, product catalog control, 
order processing, payment tracking, and shipment monitoring within a single, user-friendly interface.

The application follows the **Model-View-Controller (MVC)** architectural pattern to ensure modularity, maintainability, and scalability.

## Key Features
- Secure role-based authentication (Admin, Seller, Customer)
- User, product, and category management (CRUD operations)
- Order processing and status tracking
- Payment recording and shipment monitoring
- Search and filtering functionality
- MySQL database integration using JDBC
- Desktop-based graphical user interface

## Technologies Used
- **Java (JDK 8)**
- **Java Swing** (GUI)
- **MySQL 9.3.0** (Database)
- **JDBC** (Database connectivity)
- **MVC Architecture**
- **JUnit** (Testing)
- **GitHub**

## Project Structure

COM.FORM – Controllers and core logic
COM.PANEL – GUI panels (View layer)
COM.UTIL – Database utilities (JDBC, Singleton DB)
COM.TEST – Unit and integration tests

**Testing**

The system was tested using JUnit, covering unit, integration, and system-level tests to ensure reliability, performance, and correctness.

**Future Enhancements**

Web-based and mobile versions
Payment gateway integration (PayPal, Stripe)
Advanced analytics and reporting dashboards
RESTful API support
Enhanced security (2FA, audit logs)



