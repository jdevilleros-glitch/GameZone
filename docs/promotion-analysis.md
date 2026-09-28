# Promotion Module Analysis

## 1. Promotion hierarchy and polymorphism

The three promotion types share common attributes and behaviors but have different discount calculation rules. This is represented through an abstract `Promotion` class containing the common attributes: ID, name, start date, and end date.

`PercentageDiscount`, `CategoryDiscount`, and `BulkPurchaseDiscount` extend `Promotion` and implement their own discount calculation.

Polymorphism allows the rest of the system to work with objects of type `Promotion` without needing to know the specific subclass. Each promotion calculates its discount through its own implementation of `calculateDiscount(Sale sale)`.

## 2. Abstract discount calculation

The base `Promotion` class cannot provide a general implementation of the discount calculation because each promotion type follows a different business rule.

For this reason, the method is declared as abstract:

`public abstract double calculateDiscount(Sale sale);`

This declaration requires every concrete subclass of `Promotion` to provide its own implementation of the method.

## 3. Selection of the best promotion

The logic for selecting the promotion that provides the highest monetary discount is located in `PromotionService`, specifically in the `findBestPromotionFor(Sale sale)` method.

The method obtains the active promotions, calculates the discount provided by each one, and returns the promotion that produces the highest monetary discount.

This responsibility belongs to the service layer because selecting the best promotion is business logic.

It should not be implemented in `Sale` because that class represents the sale and should not be responsible for searching or comparing available promotions.

It should also not be implemented in the console menu because the UI layer is responsible for user interaction, not business rules.

## 4. Changes to Sale and generateReceipt

The `Sale` class is extended with two additional private attributes:

- `appliedPromotionName`
- `discountAmount`

Their corresponding getters and setters allow the applied promotion information to be stored in the sale.

The `generateReceipt()` method is updated to display the subtotal, the name of the applied promotion, the discount amount, and the final total after the discount.

These are additive modifications. The existing sale information and behavior remain available while the new fields extend the information stored and displayed for each sale.

## 5. Promotion validity validation

Promotion validity is handled by both `Promotion` and `PromotionService`, but each class has a different responsibility.

The `Promotion` class implements `isActive(LocalDate date)`, which determines whether a specific date is within the promotion's start and end dates. This keeps the rule related to the promotion's own state inside the model.

`PromotionService` uses this method in `listActivePromotions()` with the current date to obtain only the promotions that are currently valid.

This separation keeps the date-range validation in the model while the service coordinates the business operation of finding the promotions that can currently be applied.