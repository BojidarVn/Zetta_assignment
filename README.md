# Zetta QA Automation Task

## Overview

This project contains solutions for all four tasks from the take-home assignment. The focus was not only to automate the requested scenarios, but also to keep the code simple, maintainable and aligned with the scope of the task.

The solution includes UI automation, API testing, a standalone link checker utility and manual testing deliverables.

---

## Tech Stack

* Java 21
* Maven
* Selenium WebDriver 4
* TestNG
* Rest Assured
* WebDriverManager
* OpenCSV

TestNG was selected as the test framework because it provides everything required for this assignment, including assertions and data-driven testing, without introducing additional complexity.

---

## Task 1 - UI Automation

Target application:

https://www.saucedemo.com/

The automated scenario covers:

* Login page validation
* User authentication
* Product sorting by price (low to high)
* Verification that products are actually sorted correctly
* Adding the three cheapest products to the cart
* Cart validation
* Checkout flow
* Checkout overview validation
* Subtotal verification

### Design Decisions

* Page Object Model (POM) is used to separate page interactions from test logic.
* Stable `data-test` attributes are preferred over XPath locators whenever possible.
* Explicit waits are used instead of `Thread.sleep()`.
* Product prices are compared using `BigDecimal` to avoid floating-point precision issues.

---

## Task 2 - Link Checker

Target application:

https://the-internet.herokuapp.com/

The link checker is implemented as a separate utility and is independent from the UI automation tests.

The utility:

* Collects all links from the main content area
* Sends HTTP requests to each link
* Captures the HTTP status code
* Marks each link as `OK` or `Dead Link`
* Generates a timestamped CSV report

Reports are generated under:

```text
target/link-checker-results/
```

### Notes

* Network failures, connection issues and timeouts are treated as dead links.
* A failed request is retried once before the link is marked as dead.
* Results are exported using OpenCSV.
* Running the utility multiple times generates separate reports.

---

## Task 3 - API Testing

Target API:

https://jsonplaceholder.typicode.com/

Implemented scenarios:

* Posts per user (data-driven)
* Empty result for unknown user
* Unique post ID validation
* Response field and type validation
* Create post scenario

### Notes

* Base URL is configurable.
* Assertions contain descriptive failure messages.
* Tests avoid time-based validations.

---

## Task 4 - Manual Testing

The `manual` folder contains:

### Test Cases

The test case file covers:

* Positive scenarios
* Negative scenarios
* Boundary value checks
* Business rule validation

### Bug Report

The bug report describes a validation issue where transfers can be processed even when the reference field contains special characters that are not allowed by the specification.

---

## Assumptions

* SauceDemo credentials are public demo credentials provided by the application.
* JSONPlaceholder is treated as a stable mock API.
* Currency mismatch validation may be implemented through UI restrictions or server-side validation.

---

## Running the Project

### Run all tests

```bash
mvn clean test
```

### Requirements

* JDK 21+
* Maven 3.9+
* Google Chrome
* Internet connection

No manual browser driver installation is required because WebDriverManager is used.

### Configuration

Configuration can be overridden with environment variables or JVM properties:

* `SAUCE_DEMO_URL`
* `JSON_PLACEHOLDER_URL`
* `INTERNET_PAGE_URL`
* `HEADLESS`
* `DEFAULT_TIMEOUT_SECONDS`

---

## Trade-offs

* The link checker runs sequentially. This keeps the implementation simple for the current number of links.
* The UI suite contains the requested checkout flow only. Additional negative login and checkout validation scenarios would be the next extension.
* CSV reports are written under `target/`, so they are treated as generated output.

---

## Future Improvements

If additional time was available, I would consider:

* Adding CI execution with GitHub Actions
* Adding JSON schema files for the API responses
* Adding focused UI negative scenarios
