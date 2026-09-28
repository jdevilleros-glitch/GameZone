# Integration Analysis

This document describes the integration adjustments performed in GameZone after combining the Product, Person, Sale, Accessory, Promotion, Return, and Warranty modules.

The purpose of these adjustments was to solve incompatibilities between modules while preserving the layered architecture and the responsibilities of each component.

## A1 - Accessory Category Discount

### Cause

The Category Discount promotion was originally designed to recognize the existing product categories, but accessories were later integrated into the sales flow. As a result, accessory purchases could not participate correctly in category-based promotions.

### Solution

The promotion logic was extended to recognize `ACCESSORY` as a valid category.

`CategoryDiscount` was updated so accessories can be evaluated when calculating category discounts, and `PromotionService` was updated to accept the accessory category.

The promotion registration menu was also updated to allow the user to select `ACCESSORY`.

This allows accessories to participate in promotions without introducing separate discount logic outside the Promotion module.

---

## A2 - Warranty Circular Dependency

### Cause

The initial warranty persistence design required the persistence layer to resolve complete `Product` and `Sale` objects while loading warranty records.

This created unnecessary dependencies between `WarrantyDAO`, `SaleDAO`, and `ProductDAO`, increasing coupling between persistence components.

### Solution

`WarrantyDAO` was refactored so it only reads and writes warranty persistence data.

Stored warranty information is loaded as lightweight `WarrantyRecord` objects containing identifiers and warranty data.

`WarrantyService` is responsible for resolving the corresponding `Product` and `Sale` objects through the appropriate application components.

This keeps persistence focused on file operations and moves object resolution to the service layer.

---

## A3 - Unified Sale Registration

### Cause

After integrating Accessories, Promotions, and Warranties, the sale registration process contained multiple independent operations that affected the same sale.

Without a defined execution order, discounts, warranty costs, inventory updates, and persistence could produce inconsistent totals or duplicated processing.

### Solution

The sale registration flow was reorganized into a single coordinated process.

The integrated flow performs the following operations:

1. Validate the requested items.
2. Resolve products and accessories and validate stock.
3. Create the sale and calculate its subtotal.
4. Evaluate active promotions and apply the promotion with the highest discount.
5. Assign the basic warranty to each console and the optional extended warranty when requested.
6. Calculate the final total using the subtotal, discount, and extended warranty cost.
7. Update product and accessory inventory through their corresponding services.
8. Persist the completed sale and its warranties.

The final sale calculation follows:

`Final Total = Subtotal - Discount + Extended Warranty Cost`

The sale receipt displays the subtotal, applied promotion, discount amount, extended warranty cost, and final total.

---

## A4 - Accessory Stock Restoration on Returns

### Cause

The Return module originally restored inventory through `ProductService`.

After accessories became valid sale items, returning an accessory required restoring its stock through the Accessory module instead of treating every returned item as a regular product.

Additionally, stored returns needed to resolve accessory identifiers correctly.

### Solution

`AccessoryService` was extended with a stock restoration operation.

`ReturnService` now determines the returned item type:

- Regular products are restored through `ProductService`.
- Accessories are restored through `AccessoryService`.

`ReturnDAO` was also updated to resolve accessory identifiers through `AccessoryRepository` when loading stored returns.

This ensures that returned accessories are correctly restored and persisted.

---

## A5 - Discounted Return Refund

### Cause

The original return calculation refunded the complete list price of every returned item.

When a sale had received a promotion, this could refund more money than the customer had effectively paid for the returned item.

### Solution

The refund calculation was changed to apply the original sale discount proportionally to each returned item.

The proportional discount rate is calculated as:

`Discount Rate = Sale Discount / Sale Subtotal`

The refund for each returned item is calculated as:

`Item Refund = Item Price × (1 - Discount Rate)`

The return receipt now displays:

- Original item price.
- Proportional discount.
- Refunded amount.

This keeps partial returns consistent with the discount applied to the original sale.

---

## A6 - Monthly Balance Report

### Cause

The original monthly balance operation returned only the final difference between sales and returns.

This made it impossible for the user to see the individual monthly sales and return totals that produced the balance.

### Solution

The monthly report logic was separated into three operations:

- `calculateMonthlySales(month, year)`
- `calculateMonthlyReturns(month, year)`
- `generateMonthlyBalance(month, year)`

The balance is calculated as:

`Monthly Balance = Monthly Sales - Monthly Returns`

Monthly sales use the final sale total, which already includes promotions and extended warranty costs.

The user interface now displays monthly sales, monthly returns, and the resulting balance separately.

---

## A7 - Warranty Cancellation on Console Returns

### Cause

When a console was returned, its associated warranties remained registered even though the original product was no longer part of the completed purchase.

Additionally, an extended warranty represents an additional amount paid by the customer and therefore needs to be included in the refund when the associated console is returned.

### Solution

`WarrantyService` was extended with a warranty cancellation operation based on product and sale identifiers.

When a console is returned:

- Its Basic Warranty is cancelled without an additional refund.
- Its Extended Warranty is cancelled and its additional cost is returned to the customer.
- Cancelled warranties are removed from warranty persistence.

`ReturnService` coordinates the cancellation during the return process.

The total return amount is therefore calculated using the discounted item refund plus the refundable extended warranty cost.

The return receipt displays the extended warranty refund separately.

---

## Final Integration Result

After the integration adjustments, the main modules cooperate through the service layer while maintaining their individual responsibilities.

The final business flow supports:

- Products and accessories in the same sale.
- Automatic promotion evaluation.
- Category discounts for accessories.
- Basic and extended warranties for consoles.
- Sale totals including discounts and extended warranty costs.
- Partial returns.
- Proportional refunds for discounted sales.
- Product and accessory stock restoration.
- Warranty cancellation during console returns.
- Extended warranty refunds.
- Monthly sales, returns, and balance reporting.

The resulting application maintains the layered architecture while allowing the different modules to operate as an integrated system.
