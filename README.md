# Library Management System

## Overview

This is a Java-based library management system designed to manage books, users, and book loans. The application provides a user-friendly interface for library administrators to manage library resources efficiently.

## Features

- **User Authentication:** Secure sign-in and registration for library administrators.
- **Book Management:**
  - Add new books with details like title, author, ISBN, and genre.
  - View a list of all books.
  - Update existing book information.
  - Delete books.
- **Loan Management:**
  - Loan books to users.
  - View a list of currently loaned books.
  - Delete loan records.
- **Genre Support:** Books can be categorized by genre.

## Installation

To run the Library Management System, you need to have the following installed:

- **Java Development Kit (JDK):** Ensure you have JDK 11 or later installed.
- **Maven:** The project is built using Maven. Install Maven if you haven't already.

**Steps to run the application:**

1.  **Clone the repository:**

    ```bash
    git clone [repository URL]
    cd java-lib-management
    ```

    _(Replace `[repository URL]` with the actual repository URL once it's available)_

2.  **Build the project using Maven:**

    ```bash
    mvn clean install
    ```

3.  **Run the application:**
    ```bash
    mvn javafx:run
    ```

The application should now be running.

## Further Development

- Implement user-facing features for book searching and borrowing.
- Add more robust error handling and input validation.
- Enhance the UI/UX for improved user experience.
