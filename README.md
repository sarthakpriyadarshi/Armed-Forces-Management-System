
<p align="center">
  <img src="https://raw.githubusercontent.com/sarthakpriyadarshi/Armed-Forces-Management-System/master/img/afms.png" width=25% height=25% align="center" />
</p>


# Armed Forces Management System

The "Armed Forces Management System" is a software application designed to facilitate the efficient management of personnel within the armed forces. The primary focus of this program is to automate and streamline various tasks related to the handling of information about service personnel, enhancing the overall management and decision-making processes within the military.

## Server Running
<img src="https://raw.githubusercontent.com/sarthakpriyadarshi/Armed-Forces-Management-System/master/img/afms-server.png">

## Client GUI App
<img src="https://raw.githubusercontent.com/sarthakpriyadarshi/Armed-Forces-Management-System/master/img/afms-client.png">

## Features

- Personnel Information Management
- CRUD Operations (Create, Read, Update, Delete)
- GUI (Graphical User Interface)
- Database Integration
- Client-Server Architecture
- Security Measures
- Error Handling


## MySQL Initialization

Create Database for AFMS Project

```bash
  CREATE DATABASE afms;
```

Create Table for AFMS Project

```bash
  CREATE TABLE service_personnel (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    service_number VARCHAR(20) NOT NULL,
    service VARCHAR(50) NOT NULL,
    trade VARCHAR(50) NOT NULL
    );
```
## Java Server DBMS Connect

Look at the following code from Server.java
Change the DB_URL, DB_USER, DB_PASSWORD based on your MySQL Server URL, User and Password at line 13, 14 and 15.
```bash
  private static final String DB_URL = "jdbc:mysql://localhost:3306/afms";
  private static final String DB_USER = "root";
  private static final String DB_PASSWORD = "toor";
```

## Java Server-Client Connect

Look at the following code from Client.java
Change the "localhost" and 12345 from the following code as IP Address of Server and the Port Number the Server is listening to at Line 63.
```bash
  Socket socket = new Socket("localhost", 12345);
```

Look at the following code from Server.java
Change the 12345 from the following code as the Port Number the Server is listening to at Line 18.
```bash
  try (ServerSocket serverSocket = new ServerSocket(12345)) {
```
