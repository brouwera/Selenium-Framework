📘 Selenium Automation Framework (Java + TestNG + POM)
A clean, modular Selenium automation framework built in Java, using TestNG and the Page Object Model (POM) design pattern.
This project includes a complete suite of positive, negative, and UI behavior tests for a sample login application.

This framework is part of a structured 60‑day QA Automation Upskilling Plan, with Day 4 representing the completion of the initial POM structure and login test suite.

🚀 Features
✔ Page Object Model (POM)
Clean separation of test logic and UI interactions

Reusable, maintainable page classes

Easy to scale as the project grows

✔ TestNG Integration
Organized test structure

Assertions for functional and UI validation

Supports grouping, parallel execution, and reporting

✔ Login Test Suite
Includes:

Positive tests (valid login)

Negative tests (invalid username, invalid password, empty fields)

UI behavior tests (error message visibility, password masking)

✔ Professional Code Structure
Clear comments explaining test intent

Logical grouping of tests

Readable, beginner‑friendly, and recruiter‑friendly

📂 Project Structure
Code
```
selenium-framework/
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── pages/
│   │           ├── LoginPage.java
│   │           └── HomePage.java
│   │
│   └── test/
│       └── java/
│           ├── base/
│           │   └── BaseTest.java
│           └── tests/
│               └── LoginTest.java
│
├── pom.xml
└── README.md
```
🧪 Test Coverage
Positive Tests
Valid login with correct username and password

Password field masking behavior

Negative Tests
Invalid username

Invalid password

Empty username and password fields

UI Behavior Tests
Error message appears when clicking Login with empty fields

🛠 Technologies Used
Tool / Library	Purpose
Java 17	Programming language
Selenium WebDriver 4.18	Browser automation
TestNG 7.10	Test framework
Maven	Dependency management
ChromeDriver	Browser driver
📌 Current Status (End of Day 4)
Framework structure complete

POM implemented

Login tests written and organized

Tests run successfully, with expected timing‑related failures

Explicit waits will be added in Day 5 to stabilize negative tests

🔜 Next Steps (Day 5)
Add WaitUtils.java

Implement WebDriverWait and ExpectedConditions

Update page classes to use explicit waits

Re‑run tests for full stability

Push second commit

👤 Author
Adam Brouwer  
QA Analyst / SDET in training
Building a full automation framework as part of a structured 60‑day upskilling plan.