# ERP System Application
## 1. Project Description
This Java Desktop application is developed to manage core functions of an Enterprise Resource Planning (ERP) system. The application enables users (e.g., administrators/staff) to log in, manage data, and visualize statistical insights using charts. The system connects to a MySQL database, applies Object-Oriented Programming (OOP) principles, leverages Java Collections Framework for data structures, and displays charts using JFreeChart.
## 2. Mandatory Skill Components
This application integrates five core professional competencies in software development:
1.  **Object-Oriented Programming (OOP):** The application demonstrates clear use of classes, inheritance, encapsulation, and polymorphism[cite: 27].
2.  **Database Integration (MySQL):** The application connects to a MySQL database and supports CRUD (Create, Read, Update, Delete) operations using SQL[cite: 28].
3.  **Data Structures:** The program utilizes appropriate data structures from the Java Collections Framework (e.g., ArrayList, HashMap, TreeMap) for storing, retrieving, and processing data efficiently[cite: 30].
4.  **User Authentication and Profile Management:** A login/logout mechanism with secure user session handling and a user profile interface is implemented[cite: 31].
5.  **Data Visualization with Charts (JFreeChart):** The application includes at least one dynamic data visualization (e.g., bar chart, pie chart, line chart) to summarize or analyze data[cite: 32].
## 3. Installation and Running the Application
### 3.1. Prerequisites
* Java Development Kit (JDK): Version 11 or later.
* MySQL Server: Installed and configured.
### 3.2. Installation Steps
1.  Clone the repository:
    ```bash
    git clone <your_repository_url>
    cd <your_project_directory>
    ```
2.  Create the MySQL database:
    * Use the provided SQL script (`erp_system.sql`) to create the database and tables.
    ```bash
    mysql -u <your_mysql_username> -p < erp_system.sql
    ```
3.  Configure the database connection:
    * Set the following environment variables:
        * `DB_USER`: Your MySQL username.
        * `DB_PASSWORD`: Your MySQL password.
    * Alternatively (for testing only - NOT recommended for production):
        * Set system properties within the code (e.g., `System.setProperty("DB_USER", "your_user");`). **Do not hardcode passwords!**
4.  Compile the Java code:
    ```bash
    javac -d bin src/main/java/**/*.java
    ```
5.  Run the application:
    ```bash
    java -cp bin view.Main
    ```
## 4. Project Structure
erp_system/
├── src/main/java/
│   ├── DAO/
│   ├── model/
│   ├── view/
│   ├── controller/
│   └── utils/
├── erp_system.sql
├── README.md
└── ...
## 5. Main Features
* **Login/Logout:** User authentication with secure password storage (hashing)[cite: 31, 35].
* **Data Management:** CRUD operations on data tables (minimum 5 tables)[cite: 28, 35].
* **Dashboard:** Statistical charts (e.g., BarChart, PieChart) for data visualization[cite: 32, 35].
## 6. Technologies Used
* Java
* MySQL
* JFreeChart
* ... (other libraries/frameworks)
## 7. Usage Instructions
* (Briefly describe how to use the application)
## 8. Authors
* Your Name
* Your Student ID
## 9. Notes
* (Limitations, issues encountered, future development plans, etc.)
https://www.youtube.com/watch?v=TI-Ct8CrhIg&feature=youtu.be
