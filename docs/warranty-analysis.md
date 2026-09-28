# Warranty Module Analysis

## 1. Why is Warranty an abstract class?

Warranty is modeled as an abstract class because all warranties share common
information and behavior, such as an identifier, associated product, sale,
start date, expiration date, validity verification, and certificate generation.

However, each warranty type defines different rules regarding duration,
additional cost, and warranty type. Using an abstract class allows the common
behavior to remain centralized while subclasses implement their specific rules.

## 2. How is inheritance applied?

Inheritance is applied through the Warranty hierarchy.

Warranty is the abstract superclass, while BasicWarranty and ExtendedWarranty
are concrete subclasses.

Both subclasses inherit the common attributes and behavior from Warranty and
override the methods that define their specific characteristics:

- getDurationInMonths()
- getWarrantyType()
- getAdditionalCost()

This avoids duplicating common warranty logic.

## 3. How is polymorphism applied?

Polymorphism allows the system to manage BasicWarranty and ExtendedWarranty
objects through the common Warranty type.

For example, WarrantyService stores warranties in a List<Warranty>. When the
system calls methods such as getDurationInMonths(), getWarrantyType(), or
getAdditionalCost(), Java executes the implementation corresponding to the
actual warranty object.

This allows new warranty types to be added with minimal changes to the existing
business logic.

## 4. How is the warranty module integrated with sales?

The warranty module is integrated into the sale registration process through
SaleService.

When a sale contains a Console, the system determines whether the customer
selected an extended warranty.

If no extended warranty is selected, the console receives a BasicWarranty with
a duration of six months and no additional cost.

If an extended warranty is selected, the console receives an ExtendedWarranty
with a duration of twelve months. Its additional cost is calculated as 10% of
the product price and is added to the sale total.

Videogames and accessories do not automatically receive warranties.

## 5. How is warranty persistence handled?

Warranty persistence is handled by WarrantyDAO.

The DAO stores the warranty type, warranty identifier, product identifier, sale
identifier, and start date in the warranties.csv file.

When warranties are loaded, WarrantyDAO uses the stored type discriminator to
reconstruct either a BasicWarranty or an ExtendedWarranty object. The associated
Product and Sale objects are recovered using ProductDAO and SaleDAO.

The expiration date and additional cost do not need to be stored because they
can be calculated from the warranty type, start date, and product information.
