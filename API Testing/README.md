# API Testing – FieldForceConnect

## Tool Used
Postman

## Environment
- Environment Name: FieldForceConnect - QA
- Base URL: `https://test.fieldforceconnect.com`

## Login API
- Method: POST
- Endpoint: `/api/account/authenticate`
- Request fields: `username`, `password`

## Test Scenarios

### 1. Valid Login
- Used registered login credentials.
- Expected: Successful login response.
- Actual: HTTP 200 and `success: true`.
- Result: PASS

### 2. Invalid Login
- Used an incorrect password.
- Expected: Login should be rejected.
- Actual: HTTP 200, `success: false`, and an invalid-login message.
- Result: PASS

## API Automation / Add Customer
Add Customer API and GET API testing were not completed before submission.

## Note
Credentials and authentication tokens have not been included for security reasons.
