# Selenium Test Automation Framework
A clean, maintainable, and professional Selenium + TestNG automation framework built for the **Practice Test Automation** site as part of a structured 60‑day QA Automation Upskill Plan.

---

# 📛 Badges
![Java](https://img.shields.io/badge/Java-17-blue)  
![Selenium](https://img.shields.io/badge/Selenium-4.18-brightgreen)  
![TestNG](https://img.shields.io/badge/TestNG-7.10-orange)  
![Allure](https://img.shields.io/badge/Allure-2.13.9-purple)  
![Build](https://img.shields.io/badge/Build-Passing-brightgreen)  
![License](https://img.shields.io/badge/License-MIT-lightgrey)

---

# 📚 Table of Contents
1. [Project Overview](#-project-overview)
2. [Current Scope](#-current-scope-aligned-with-the-real-ui)
3. [Key Features](#-completed-features)
4. [Project Structure](#-project-structure)
5. [How to Run Locally](#-how-to-run)
6. [How to Run in CI](#-how-to-run-in-ci)
7. [Day-by-Day Progress Log](#-day-by-day-progress-log)
8. [Upcoming Enhancements](#-upcoming-enhancements-planned)
9. [Author](#-author)

---

# 🚀 Project Overview

This framework automates the login functionality of:

**https://practicetestautomation.com/practice-test-login/**

It demonstrates:

- Clean Page Object Model (POM) architecture
- Explicit waits for stability
- Thread‑safe WebDriver setup
- Data‑driven testing
- Allure reporting
- Multi‑browser execution (Chrome, Edge, Firefox)
- CI/CD readiness

All tests currently pass with **zero failures** across all supported browsers.

---

# 📌 Current Scope (Aligned With the Real UI)

The Practice page currently includes:

- **Test Login Page**
- **Test Exceptions** (coming soon)
- **Test Table** (coming soon)

Removed modules (no longer present on the site):

- Test Inputs
- Test Alerts

This keeps the framework aligned with the real application under test.

---

# ✅ Completed Features

### ✔ Login Automation
- Full POM structure
- Positive + negative login tests
- Data‑driven testing via CSV
- Navigation flow validation
- Explicit waits everywhere
- ThreadLocal WebDriver
- Allure step annotations

### ✔ Framework Cleanup (Day 13)
- Removed deprecated modules
- Cleaned HomePage to match real UI
- Updated testng.xml
- Full green suite with `mvn clean test`
- TestListener integrated

### ✔ Multi‑Browser Support (Day 14)
- Chrome via WebDriverManager
- Edge via manual driver path (offline‑safe)
- Firefox via manual GeckoDriver path
- ConfigManager now controls all driver paths
- Fully portable + CI‑ready

---

# 📁 Project Structure



---

```
src
└── test
├── java
│   ├── base
│   │   └── BaseTest.java
│   ├── pages
│   │   ├── BasePage.java
│   │   ├── HomePage.java
│   │   ├── LoginPage.java
│   │   └── SuccessfulLoginPage.java
│   ├── tests
│   │   └── LoginTest.java
│   ├── dataproviders
│   │   └── LoginDataProvider.java
│   ├── listeners
│   │   └── TestListener.java
│   └── utils
│       ├── CSVUtils.java
│       └── CsvParsingException.java
└── resources
└── testdata
└── loginData.csv
```
---


---

# 🏁 How to Run

### Run full suite:

mvn clean test

### Run a specific test:

mvn -Dtest=LoginTest test

### Override browser:

mvn clean test -Dbrowser=edge

mvn clean test -Dbrowser=firefox

mvn clean test -Dbrowser=chrome


### Run headless:

mvn clean test -Dheadless=true


---

# 🏗️ How to Run in CI

This framework is fully CI‑ready and supports:

- Multi‑browser execution
- Headless mode
- Environment‑driven configuration
- Allure reporting

### Basic CI command:

---

mvn clean test -Denv=local -Dbrowser=chrome -Dheadless=true


### Example: GitHub Actions workflow

```yaml
name: Run Selenium Tests

on:
  push:
    branches: [ main ]

jobs:
  test:
    runs-on: windows-latest

    steps:
      - uses: actions/checkout@v3

      - name: Set up JDK
        uses: actions/setup-java@v3
        with:
          distribution: 'temurin'
          java-version: '17'

      - name: Run Tests
        run: mvn clean test -Denv=local -Dbrowser=chrome -Dheadless=true

      - name: Generate Allure Report
        run: mvn allure:aggregate
```

### Example: GitHub Actions workflow

```yaml
name: Run Selenium Tests

on:
  push:
    branches: [ main ]

jobs:
  test:
    runs-on: windows-latest

    steps:
      - uses: actions/checkout@v3

      - name: Set up JDK
        uses: actions/setup-java@v3
        with:
          distribution: 'temurin'
          java-version: '17'

      - name: Run Tests
        run: mvn clean test -Denv=local -Dbrowser=chrome -Dheadless=true

      - name: Generate Allure Report
        run: mvn allure:aggregate
```

### 📅 Day-by-Day Progress Log

### Day 3 — First Working Selenium + TestNG Login Automation
Built initial BaseTest

Created first LoginTest

Validated environment + WebDriver setup

### Day 5 — Explicit Waits + Stability

Added WebDriverWait

Replaced all direct interactions with explicit waits

Eliminated flakiness

### Day 7 — Page Object Model (POM)

Added LoginPage + HomePage

Clean locators + explicit waits

Tests rewritten to use POM

### Day 8 — Multi‑Page Navigation

Added SuccessfulLoginPage

Added full login flow test

Improved readability + maintainability

### Day 9 — Assertions + Allure + Stability

Added AssertionHelper

Added TestListener

Cleaned pom.xml

Achieved full green run

### Day 10 — Flow-Based Scenarios

Added navigation + logout flow tests

Added missing helper methods

Improved Allure structure

### Day 11 — Login Module Refactor

Removed legacy tests

Updated Page Objects

Added Inputs module (later removed)

Achieved full green suite

### Day 12 — Alerts Testing + Full Validation

Added Alerts module (later removed)

Validated entire framework

Added Allure environment metadata

### Day 13 — Framework Cleanup

Removed Inputs + Alerts

Cleaned navigation

Updated HomePage

Full green run

### Day 14 — Multi‑Browser + ConfigManager Upgrade

Added Edge + Firefox support

Added manual driver paths

Updated ConfigManager

Framework now fully portable + CI‑ready

### 🚧 Upcoming Enhancements (Planned)

### Exceptions Module

Page object

Error handling tests

### Table Module

Table parsing utilities

Sorting/filtering tests

### Framework Enhancements

Allure screenshots on failure

.env support for secrets

Multi‑environment execution

README visuals + architecture diagram

---

👤 Author: 
Adam Brouwer    
QA Analyst / SDET in training  
Building a full automation framework as part of a structured 60‑day upskilling plan.