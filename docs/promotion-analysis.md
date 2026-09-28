# Promotion Module Analysis

## 1. How should promotions be represented in the existing object-oriented model?

Promotions should be represented through an abstract `Promotion` class that defines the common attributes and behavior shared by all promotion types.

The `Promotion` class contains the promotion ID, name, start date, and end date. It also provides the `isActive(LocalDate date)` method to determine whether a promotion is valid on a specific date.

Because each promotion calculates its discount differently, the class declares the abstract method `calculateDiscount(Sale sale)`. Each specific promotion type implements this method according to its own rules.

Three concrete subclasses extend `Promotion`:

- `PercentageDiscount`: applies a percentage discount to the total value of the sale.
- `CategoryDiscount`: applies a percentage discount only to products belonging to a specified category.
- `BulkPurchaseDiscount`: applies a percentage discount to the total sale when a minimum number of products is reached.

This design uses inheritance and polymorphism and makes it possible to add new promotion types without changing the basic promotion abstraction.

## 2. How should the promotion module interact with the existing sales module?

The promotion module should interact with the sales module through the service layer.

When `SaleService` creates a sale, it requests the best applicable promotion from `PromotionService`. The promotion service evaluates the active promotions and uses each promotion's `calculateDiscount(Sale sale)` implementation to determine the monetary discount.

After the best promotion is selected, `SaleService` stores the promotion name and discount amount in the `Sale` object and adjusts the final total.

This approach keeps promotion selection logic outside the user interface and prevents the model classes from handling file persistence.

## 3. How should the system determine the best promotion for a sale?

The system should first obtain all promotions that are active on the current date.

`PromotionService.findBestPromotionFor(Sale sale)` evaluates every active promotion by calling its `calculateDiscount(Sale sale)` method. It compares the monetary discount produced by each promotion and keeps the promotion that provides the highest discount.

Only one promotion is selected for a sale, so discounts are not cumulative.

If no active promotion produces a discount greater than zero, the method returns `null` and the sale keeps its original total.

## 4. How should promotions be persisted?

Promotion persistence should be handled by `PromotionDAO`, following the same DAO approach already used by the project for products, people, and sales.

Promotions are stored in `data/promotions.csv`. Each record contains a discriminator that identifies the promotion type followed by its corresponding data.

The formats used are:

- `PERCENTAGE;id;name;startDate;endDate;percentage`
- `CATEGORY;id;name;startDate;endDate;percentage;targetCategory`
- `BULK;id;name;startDate;endDate;minimumQuantity;percentage`

`PromotionDAO.loadAll()` reconstructs the appropriate promotion subclass according to the discriminator.

`PromotionDAO.saveAll()` writes the current promotion collection to the file.

If the promotions file does not exist, the DAO creates it and returns an empty promotion list, allowing the application to continue operating without a file-not-found error.

## 5. What changes are required in the existing system?

The existing system requires changes in the model, DAO, service, and UI layers.

In the model layer, the promotion hierarchy is added with `Promotion`, `PercentageDiscount`, `CategoryDiscount`, and `BulkPurchaseDiscount`. The `Sale` class is extended with `appliedPromotionName` and `discountAmount`, and its receipt includes the subtotal, applied promotion, discount, and final total.

In the DAO layer, `PromotionDAO` is added to manage promotion persistence. `SaleDAO` is also updated so that the applied promotion and discount amount remain available after restarting the application.

In the service layer, `PromotionService` manages promotion registration, listing, active promotion filtering, promotion searches, and selection of the promotion that produces the highest monetary discount. `SaleService` uses this service when registering a sale.

In the UI layer, the main menu includes promotion management options for registering the three promotion types and listing all or currently active promotions. The sales menu also allows the user to view the details of a specific sale, including the promotion and discount applied.

These changes preserve the layered structure of the application and keep responsibilities separated between the UI, service, DAO, and model layers.
