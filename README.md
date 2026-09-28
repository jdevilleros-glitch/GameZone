# GameZone

GameZone is a console-based Java application developed as part of the Programming III course.

The application manages products, accessories, customers, sellers, sales, promotions, returns, and warranties for a videogame store using object-oriented programming and a layered architecture.

## Technologies

- Java
- Maven
- NetBeans
- Git and GitHub
- Text and CSV files for data persistence

## Project Structure

The application is organized into the following main packages:

- `model`: Contains the domain classes of the system.
- `dao`: Handles persistence for products, persons, sales, promotions, returns, and warranties.
- `persistence`: Contains the repository used by the Accessory module.
- `service`: Contains the application and business logic.
- `ui`: Handles user interaction through the console.
- `Main`: Initializes the application components, injects dependencies, and starts the program.

The application stores its persistent information in files located in the `data` directory.

## How to Run

1. Clone or download the repository.
2. Open the project in NetBeans.
3. Wait for Maven to load the project dependencies.
4. Run **Clean and Build Project**.
5. Run `Main.java`.
6. Use the console menu to access the different GameZone operations.

## Main Features

- Register and manage videogames and consoles.
- Register and manage accessories.
- Register customers and manage sellers.
- Register sales containing products and accessories.
- Automatically update inventory after sales.
- Register and evaluate promotions.
- Apply the promotion that provides the highest discount.
- Apply category discounts to videogames, consoles, and accessories.
- Automatically assign basic warranties to consoles.
- Optionally assign extended warranties to consoles.
- Calculate sale totals including discounts and extended warranty costs.
- Register partial returns.
- Calculate proportional refunds for discounted sales.
- Restore product and accessory stock after returns.
- Cancel warranties when consoles are returned.
- Refund extended warranty costs when applicable.
- View purchase and sales histories.
- View registered warranties and upcoming warranty expirations.
- Generate monthly sales, returns, and balance reports.

## Product Module

The Product module manages the main products sold by GameZone.

The system supports two main product types:

- **Videogame**
- **Console**

The module allows users to:

- Register videogames.
- Register consoles.
- List registered products.
- Manage product stock.
- Find products by identifier.
- Restore product stock when an item is returned.

Product information is persisted using `ProductDAO`.

## Person Module

The Person module manages customers and sellers.

The system uses an abstract `Person` class with two specialized types:

- **Customer**
- **Seller**

The module allows users to:

- Register customers.
- List customers.
- List sellers.
- Associate customers and sellers with registered sales.

Person information is persisted using `PersonDAO`.

## Accessory Module

The Accessory module manages accessories that can be included in GameZone sales.

Accessories participate in the integrated sales process together with the other products.

The module supports:

- Accessory registration and management.
- Accessory inventory management.
- Inclusion of accessories in sales.
- Category discounts for accessories.
- Stock reduction when accessories are sold.
- Stock restoration when accessories are returned.

Accessory persistence is managed through `AccessoryRepository`.

## Promotion Module

The Promotion module allows GameZone to register promotions and automatically evaluate discounts during the sales process.

### Promotion Types

The system supports three types of promotions:

- **Percentage Discount:** applies a percentage discount according to its promotion rules.
- **Category Discount:** applies a percentage discount to items belonging to a specific category.
- **Bulk Purchase Discount:** applies a discount when the required quantity of items is reached.

The supported categories for category discounts include:

- `VIDEOGAME`
- `CONSOLE`
- `ACCESSORY`

### Promotion Features

The Promotion module allows users to:

- Register percentage, category, and bulk purchase promotions.
- List all registered promotions.
- List currently active promotions.
- Store and load promotions from `data/promotions.csv`.
- Automatically evaluate active promotions when registering a sale.
- Apply only the promotion that provides the highest monetary discount.
- Store the applied promotion and discount amount in each sale.
- Display promotion information in the sale receipt.

### Promotion Architecture

The Promotion module follows the layered architecture of the application:

`UI → Service → DAO → Model`

The abstract `Promotion` class defines common promotion information and behavior.

The specialized promotion classes are:

- `PercentageDiscount`
- `CategoryDiscount`
- `BulkPurchaseDiscount`

`PromotionService` contains the business logic required to manage promotions and participate in discount evaluation during sale registration.

## Sale Module

The Sale module coordinates the main commercial operation of GameZone.

A sale can contain products and accessories and is associated with a customer and a seller.

After the integration of the different modules, the sale registration process coordinates inventory, promotions, and warranties.

### Integrated Sale Flow

The sale process performs the following operations:

1. Validates the requested items.
2. Resolves products and accessories and verifies their stock.
3. Creates the sale and calculates its subtotal.
4. Evaluates active promotions and applies the promotion with the highest discount.
5. Assigns warranties to consoles.
6. Includes the cost of extended warranties when selected.
7. Calculates the final sale total.
8. Updates product and accessory inventory.
9. Persists the completed sale and its related information.

The final sale amount is calculated using:

`Final Total = Subtotal - Discount + Extended Warranty Cost`

### Sale Receipt

The sale receipt includes:

- Sale identifier.
- Customer and seller information.
- Sold items.
- Subtotal.
- Applied promotion.
- Discount amount.
- Extended warranty cost.
- Final total.

Sales are persisted using `SaleDAO`.

## Warranty Module

The Warranty module manages warranties associated with consoles sold by GameZone.

The system includes two warranty types:

- **Basic Warranty:** 6 months with no additional cost.
- **Extended Warranty:** 12 months with an additional cost equal to 10% of the console price.

### Warranty Features

The module supports:

- Automatic basic warranty assignment during console sales.
- Optional extended warranty selection during the sales process.
- Warranty persistence using `WarrantyDAO`.
- Warranty lookup by product.
- Listing all registered warranties.
- Listing currently active warranties.
- Listing warranties that expire within a specified number of days.
- Warranty certificate generation.
- Warranty cancellation when a console is returned.

Warranty persistence stores identifiers, while `WarrantyService` resolves the corresponding sale and product information.

### Warranty Cancellation

When a console is returned, warranties associated with that console and the original sale are cancelled.

A Basic Warranty is cancelled without generating an additional refund.

If the console has an Extended Warranty, its additional cost is included in the customer's return refund.

## Return Module

The Return module manages partial returns associated with previously registered sales.

### Return Features

The module allows users to:

- Register partial returns from an existing sale.
- Validate that the original sale exists.
- Validate the 30-day return period.
- Validate that returned items belong to the original sale.
- Prevent the same item from being returned more than once for the same sale.
- Calculate refunds considering the discount applied to the original sale.
- Restore product stock.
- Restore accessory stock.
- Cancel warranties associated with returned consoles.
- Refund extended warranty costs when applicable.
- List all registered returns.
- View returns by customer.
- View returns by sale.
- Generate monthly sales, returns, and balance information.

### Discounted Refund Calculation

When the original sale contains a discount, the return does not refund the original list price directly.

The proportional discount rate is calculated using:

`Discount Rate = Sale Discount / Sale Subtotal`

The refundable value of each returned item is calculated using:

`Item Refund = Item Price × (1 - Discount Rate)`

If a returned console has an Extended Warranty, its refundable warranty cost is added to the return.

Therefore:

`Total Refund = Discounted Item Refunds + Extended Warranty Refund`

### Return Receipt

The return receipt displays:

- Return identifier.
- Return date.
- Original sale identifier.
- Returned items.
- Original item price.
- Proportional discount.
- Refunded item amount.
- Extended warranty refund.
- Return reason.
- Total refund.

Returns are persisted in:

`data/returns.csv`

## Monthly Report

The application provides a monthly financial report based on registered sales and returns.

The report displays:

- Total monthly sales.
- Total monthly returns.
- Final monthly balance.

The balance is calculated using:

`Monthly Balance = Monthly Sales - Monthly Returns`

Monthly sales use the final sale total, including discounts and extended warranty costs.

Monthly returns use the final refund amount.

Sales and returns are filtered according to the selected month and year.

## Layered Architecture

GameZone follows a layered architecture that separates the main responsibilities of the application.

### User Interface Layer

The `Menu` class manages console interaction and provides access to the different modules.

`Main` initializes the application dependencies and starts the menu.

### Service Layer

The Service Layer contains the application business logic.

It includes:

- `ProductService`
- `PersonService`
- `AccessoryService`
- `PromotionService`
- `SaleService`
- `ReturnService`
- `WarrantyService`

The service layer also coordinates operations involving multiple modules.

For example, `SaleService` integrates accessories, promotions, and warranties during sale registration, while `ReturnService` coordinates sales, inventory restoration, refunds, and warranty cancellation.

### Persistence Layer

The Persistence Layer is responsible for storing and loading application information.

The following components are used:

- `ProductDAO`
- `PersonDAO`
- `SaleDAO`
- `PromotionDAO`
- `ReturnDAO`
- `WarrantyDAO`
- `AccessoryRepository`

Most modules use the DAO pattern. The Accessory module uses its existing repository implementation.

### Model Layer

The Model Layer contains the domain entities and their inheritance relationships.

The main model groups are:

- Products and their specialized types.
- Accessories and their specialized types.
- Persons, customers, and sellers.
- Sales.
- Promotions.
- Returns.
- Warranties.

## Integration Adjustments

The final integration required several adjustments so the independently developed modules could operate together correctly.

The main integration changes include:

- Accessory support in category-based promotions.
- Refactoring warranty persistence to reduce coupling between persistence components.
- Unified sale registration for products, accessories, promotions, and warranties.
- Accessory stock restoration during returns.
- Proportional refund calculation for discounted sales.
- Detailed monthly sales, returns, and balance reporting.
- Warranty cancellation and extended warranty refund during console returns.

Additional information about these adjustments is available in:

`docs/integration-analysis.md`

The integrated class diagram is available in:

`docs/integrated-class-diagram.md`

The layered architecture diagram is available in:

`docs/layers-diagram.md`

## Data Persistence

The application uses files in the `data` directory to preserve information between executions.

The main persistence files include:

- Product data.
- Person data.
- Sale data.
- Accessory data.
- Promotion data.
- Return data.
- Warranty data.

The persistence components reconstruct the required domain objects when the application starts.

## Object-Oriented Programming

The project applies the main object-oriented programming concepts required by the course:

- **Abstraction:** through abstract classes such as `Product`, `Person`, `Promotion`, and `Warranty`.
- **Inheritance:** through specialized products, persons, promotions, accessories, and warranties.
- **Polymorphism:** through overridden behavior in specialized domain classes.
- **Encapsulation:** through private attributes and controlled access using methods.
- **Layer separation:** through model, persistence, service, and user interface responsibilities.

## Integrated Modules

The final GameZone application integrates the following modules:

1. Product
2. Person
3. Accessory
4. Sale
5. Promotion
6. Return
7. Warranty

These modules cooperate through the service layer while maintaining their individual responsibilities.