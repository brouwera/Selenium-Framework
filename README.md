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

```
wait.until(ExpectedConditions.visibilityOfElementLocated(...));
wait.until(ExpectedConditions.elementToBeClickable(...));
This eliminates flakiness and ensures stable execution.
```
---
✔ Clean Test Lifecycle
---
BaseTest handles:

WebDriver setup

Window management

WebDriverWait initialization

Navigation

Cleanup

Every test inherits this behavior.

✔ Full Test Coverage for Login Page
---

Positive Test
---
Valid login with correct credentials

Negative Tests
---
Invalid username

Invalid password

Empty fields

Error message validation

UI Behavior Test
---
Password masking

Error message visibility when clicking Login with empty fields

🧪 Running the Tests From IntelliJ:
---
Right‑click the tests package

Select Run 'LoginTest'

Or via Maven:

mvn test
All tests should pass.

📸 Sample Output
---
```
===============================================
Default Suite
Total tests run: 6, Passes: 6, Failures: 0, Skips: 0
===============================================
```
🔧 Tools & Technologies

Java 17

Selenium 4

TestNG

ChromeDriver

Page Object Model (POM)

Explicit Waits (WebDriverWait + ExpectedConditions)

📚 Next Steps (Future Enhancements)
---
Add a BasePage class for shared utilities

Introduce WaitUtils for reusable wait logic

Add logging

Add reporting (Allure or Extent)

Expand test coverage to additional pages

Add cross‑browser support

---

📅 Day 3 — First Working Selenium + TestNG Login Automation
---
Overview
---
Day 3 marked the first major functional milestone:
I successfully built and executed working Selenium + TestNG login tests using direct WebDriver interactions.

This established the foundation for the framework.

🏗️ What I Built
---
Created the initial BaseTest class

Wrote the first LoginTest using raw Selenium commands

Validated:

Browser launches correctly

Elements can be located

Login workflow executes end‑to‑end

Assertions work as expected

🧪 Test Coverage Achieved
---
Successful login with valid credentials

Error message validation for invalid username/password

This proved the environment, dependencies, and WebDriver setup were all functioning correctly.

---

📅 Day 5 — Explicit Waits + Stability Improvements
---
Overview
---
Day 5 focused on stability and reliability.
I replaced all direct element interactions with explicit waits, eliminating timing issues and improving test consistency.

🏗️ What I Improved
---
Added WebDriverWait to BaseTest

Replaced all findElement() calls with:
```
wait.until(ExpectedConditions.visibilityOfElementLocated(...));
```
Ensured elements are:

Visible before interacting

Clickable before clicking

Present before retrieving text

📈 Why This Matters
---
Explicit waits dramatically reduce flakiness and are a core best practice in real automation frameworks.
This upgrade made the tests stable, repeatable, and production‑ready.

---

📅 Day 7 — Page Object Model (POM) Implementation
---
Overview
---
Day 7 introduced the Page Object Model (POM), transforming the framework from simple scripts into a scalable, maintainable automation architecture.

🏗️ What I Built
---
Added Page Object Classes

Created a new pages package and added:

LoginPage.java

HomePage.java

Each Page Object now contains:

Private, stable locators

Explicit waits for all interactions

Clean, action‑based methods

No assertions or test logic

Updated BaseTest for Cleaner Setup
---
Disabled implicit waits

Centralized WebDriver and WebDriverWait creation

Ensured consistent navigation before each test

Improved teardown logic

Rewrote Login Tests Using POM
---
Tests now:
---
Contain no locators

Contain no Selenium logic

Read like user workflows

Focus only on assertions

This is a major step toward a professional SDET‑level framework.

🧪 Test Coverage Achieved Today
---
Positive Tests
---

Valid login

Password masking

Negative Tests
---

Invalid username

Invalid password

Empty fields

UI Behavior Tests
---

Error message appears when clicking Login with empty fields

📈 Why This Matters
---
Implementing POM provides:
---

Cleaner test code

Centralized UI logic

Easier maintenance when UI changes

Reusable page actions

Better scalability for future test suites

This is the point where the framework starts looking like something used on an actual QA automation team.

### Day 8 – Multi‑Page Navigation & Full Login Workflow Test
- Added HomePage, LoginPage, and SuccessfulLoginPage with full POM structure
- Implemented a complete navigation flow test using TestNG
- Added professional comments across all Page Objects and BaseTest
- Improved readability, maintainability, and clarity of the framework

### 📌 Day 9 Update — Framework Stability, Assertions, and Allure Integration
✅ Major Achievements
Day 9 was all about strengthening the reliability, clarity, and professionalism of the automation framework. Today’s work focused on stabilizing the test suite, improving assertion structure, and ensuring the entire system runs cleanly from end to end.

🔹 1. Added AssertionHelper.java

A centralized assertion utility was introduced to improve readability and consistency across tests.

Key features:

verifyEquals, verifyTrue, verifyFalse, verifyNotNull

UI‑specific helpers like verifyElementDisplayed and verifyTextContains

Clean, reusable, and TestNG‑aligned

🔹 2. Completed and validated all Page Objects

All Page Objects now include:

Explicit waits

Clean locators

Allure @Step annotations

Missing methods added (isErrorMessageVisible(), getPageTitle(), getCurrentUrl())

Pages validated:

LoginPage

SuccessfulLoginPage

HomePage

🔹 3. Added and configured TestNG Listener

Implemented TestListener.java to capture:

Automatic screenshots on failure

Allure attachments

Clean lifecycle integration

🔹 4. Cleaned up pom.xml

Removed the unnecessary AspectJ plugin that was causing build failures.
Framework now uses a clean, modern Selenium/TestNG/Allure setup.

🔹 5. Fixed navigation flow in tests

All Login tests now correctly call:

java
login.open(baseUrl);
This resolved all TimeoutExceptions and stabilized the suite.

🔹 6. Achieved a full green test run

All 7 tests passed successfully:

Code
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
This confirms:

Page Objects are stable

Test flows are correct

Waits are reliable

Listener is working

Framework is production‑ready

🔹 7. Cleaned and standardized the repository

   As part of stabilizing the framework, I also cleaned up the project structure:

Added a proper .gitignore to exclude build artifacts, IDE files, and Allure output

Removed previously tracked IntelliJ and Maven-generated files

Ensured only source code, configuration, and documentation remain under version control

This keeps the repository clean, lightweight, and professional for future development and CI/CD integration.


### 🚀 What This Means
Day 9 marks the moment the framework became fully operational.
This is the point where many engineers finally feel their automation project “click” — and you hit it cleanly.

Your foundation is now strong enough to expand into:

Allure reporting polish

BasePage abstraction

Reusable wait helpers

Additional test suites

CI/CD integration

👤 Author
Adam Brouwer  
QA Analyst / SDET in training
Building a full automation framework as part of a structured 60‑day upskilling plan.