
# Exception Handling Analysis - GameZone Unicesar

## 1. Checked vs. Unchecked Exceptions

GameZoneException extends RuntimeException, making it an unchecked exception.

This approach is appropriate because most errors handled by the application represent invalid input, violated business rules, missing resources, or persistence failures that cannot necessarily be resolved by the calling method.

Using RuntimeException also allows services and repositories to propagate errors without declaring them in every method signature with the throws keyword.

As a result, the existing public method signatures can be preserved while introducing more specific exception types.

## 2. Benefits of a Custom Exception Hierarchy

A custom exception hierarchy provides more meaningful information than using IllegalArgumentException for every error.

GameZoneException serves as the common superclass for four specialized exceptions:

- ResourceNotFoundException: identifies missing resources.
- BusinessRuleException: identifies violations of business rules.
- InvalidDataException: identifies invalid input data.
- PersistenceException: identifies failures when accessing stored data.

This design applies the principles of separation of concerns, abstraction, and the Single Responsibility Principle.

It also improves maintainability because each exception represents a specific category of failure, allowing the user interface to respond appropriately.

## 3. Validator Integration and Layered Architecture

Validators are invoked by the service layer before executing business operations that require validation.

The application follows this general flow:

User Interface -> Service -> Validator

The service coordinates the operation, while the validator checks the relevant input data and business conditions.

The project includes three dedicated validators:

- ProductValidator
- PersonValidator
- SaleValidator

Validators should not access repositories or perform file operations.

Persistence responsibilities belong to the repository layer, while validators remain focused on checking data and business conditions.

This separation reduces coupling and makes validation logic easier to maintain and reuse.

## 4. Preserving Existing System Behavior

The refactoring strategy focuses on replacing generic error handling with specialized exceptions without intentionally changing the existing business operations.

The team preserves existing method signatures, menu options, data formats, and business rules wherever possible.

The following verification activities help detect regressions:

- Compile the application using Clean and Build.
- Verify that existing menu options remain accessible.
- Test product registration with valid data.
- Attempt to register a product with a duplicate identifier.
- Test invalid numeric input.
- Test a sale involving a nonexistent customer.
- Test invalid date input.
- Confirm that handled errors do not terminate the application unexpectedly.

These tests provide initial evidence that the modified application continues to operate correctly.

However, a complete regression test of all existing operations is still necessary before claiming full functional compatibility.

## 5. Exception Handling in the Console Menu

The application uses one try block with multiple specific catch blocks inside the executeSafely method.

Each catch block handles a particular exception category:

- ResourceNotFoundException
- BusinessRuleException
- InvalidDataException
- PersistenceException

This approach is preferable to catching only GameZoneException because it allows the console menu to display different messages depending on the type of failure.

The menu also handles certain additional input and date-related errors when necessary.

Using specific catch blocks improves readability, provides clearer feedback, and prevents expected application errors from being presented as raw Java exception traces.

The exception hierarchy therefore supports centralized and differentiated error handling without requiring every menu operation to implement its own error-handling logic.
