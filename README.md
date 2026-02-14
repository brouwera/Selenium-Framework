# Selenium Framework – Practice Test Automation (Login Tests)

A clean, modular, and scalable Selenium + TestNG automation framework built as part of a structured 60‑day QA Automation Upskill Plan.  
This project demonstrates professional Page Object Model (POM) design, explicit waits, stable test execution, and clean test organization.

---

## 🚀 Project Overview

This framework automates the login functionality of:

**https://practicetestautomation.com/practice-test-login/**

It includes:

- A reusable **BaseTest** class for WebDriver lifecycle management
- A **LoginPage** and **HomePage** built using the Page Object Model
- Full use of **explicit waits** for stability
- A suite of **positive, negative, and UI behavior tests**
- Clean, readable, and maintainable code suitable for real‑world SDET work

All tests currently pass with **zero failures**.

---

## 🧱 Project Structure

```
src
└── test
├── java
│    ├── base
│    │     └── BaseTest.java
│    ├── pages
│    │     ├── LoginPage.java
│    │     └── HomePage.java
│    └── tests
│          └── LoginTest.java
└── resources
```

---

## 🧩 Key Features

### ✔ Page Object Model (POM)
Each page encapsulates:

- Locators
- Actions
- Explicit waits
- Page‑specific behavior

This keeps tests clean and readable.

---

### ✔ Explicit Waits Everywhere They Matter

All element interactions use:

```java
wait.until(ExpectedConditions.visibilityOfElementLocated(...));
wait.until(ExpectedConditions.elementToBeClickable(...));
This eliminates flakiness and ensures stable execution.

✔ Clean Test Lifecycle
BaseTest handles:

WebDriver setup

Window management

WebDriverWait initialization

Navigation

Cleanup

Every test inherits this behavior.

✔ Full Test Coverage for Login Page
Positive Test
Valid login with correct credentials

Negative Tests
Invalid username

Invalid password

Empty fields

Error message validation

UI Behavior Test
Password masking

Error message visibility when clicking Login with empty fields

🧪 Running the Tests
From IntelliJ:

Right‑click the tests package

Select Run 'LoginTest'

Or via Maven (if configured):

Code
mvn test
All 6 tests should pass.

📸 Sample Output
Code
===============================================
Default Suite
Total tests run: 6, Passes: 6, Failures: 0, Skips: 0
===============================================
🔧 Tools & Technologies
Java 17

Selenium 4

TestNG

ChromeDriver

Page Object Model (POM)

Explicit Waits (WebDriverWait + ExpectedConditions)

📚 Next Steps (Day 6 and Beyond)
Add a BasePage class for shared utilities

Introduce WaitUtils for reusable wait logic

Add logging

Add reporting (Allure or Extent)

Expand test coverage to additional pages

Add cross‑browser support

🏁 Status
This framework is fully functional, stable, and ready for expansion.
All tests pass consistently using explicit waits and clean POM design.

👤 Author
Adam Brouwer  
QA Analyst / SDET in training
Building a full automation framework as part of a structured 60‑day upskilling plan.