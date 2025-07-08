 🛒 FlipZone - E-Commerce Web Application

FlipZone is a full-stack e-commerce web application built with **Spring Boot**, **MySQL**, and **Thymeleaf**, offering a complete shopping experience with user authentication, admin management, OTP verification, product filtering, cart operations, and Razorpay payment integration (test mode).

---

## 🚀 Features

1) 🧑‍💼 Admin Panel
- Dashboard showing **total products** and **total sales**
- Add, update, and delete products
- Manage order statuses (e.g., Delivered, Out for Delivery, etc.)

---

2) 👤 General Users (Unauthenticated)
- View **hero section**, **featured products**, and explore items
- Can register via OTP-based email verification

---

3) 📝 User Registration & Login
- Registration form includes:
  - Full Name
  - Mobile Number
  - Email ID
  - Password
- A **6-digit OTP** is sent to the user's email
- Registration is only successful upon correct OTP entry
- Passwords are **encrypted** in the database and **decrypted only at login** (not stored in plain text)

---

4) 🏠 Customer Home (Authenticated Users)
- **Product listing** with filters and pagination
- Clean and modern **navbar** with:
  - 🛒 Cart
  - 🔎 search products
  - 📦 My Orders
  - 🚪 Logout

5) 🔎 Product Filter Options
- Sort by:
  - Price (High to Low)
  - Price (Low to High)
  - Stock (High to Low)
- Default sorting by product name

6) 📦 Product Details
- Click on any product to view detailed description, price, stock info, and purchase options

7) 🛍 Cart Functionality
- View all products in the cart
- Adjust quantity or remove items
- Choose to:
  - Buy individual items
  - Checkout all items at once

8) 💳 Payment Integration
- Razorpay test mode is used to simulate secure payments

9) 📄 Order Management
- “My Orders” page displays:
  - All purchased products
  - Product status (Delivered, Out for Delivery, etc.)
  - **Invoice download** available for each order

---

## 🛠 Tech Stack

| Layer     | Technology                       |
|-----------|----------------------------------|
| Backend   | Java, Spring Boot                |
| Frontend  | HTML, CSS, JavaScript, Thymeleaf |
| Database  | MySQL                            |
| Payment   | Razorpay (test mode)             |

---

## 📸 Screenshots
<img width="1440" alt="Screenshot 2025-07-08 at 8 03 51 AM" src="https://github.com/user-attachments/assets/495e9c6e-683b-494d-be77-4d51d4862ca4" />


<img width="1440" alt="Screenshot 2025-07-08 at 8 04 06 AM" src="https://github.com/user-attachments/assets/225cc1c1-fb8b-49dd-8000-4231eebee100" />



