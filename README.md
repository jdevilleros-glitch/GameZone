# GameZone 

GameZone is a console-based Java application developed as part of the Programming III course.

The application manages products, customers, sellers, and sales for a videogame store using object-oriented programming and a layered architecture.

## Technologies

- Java
- Maven
- NetBeans
- Git and GitHub
- Text files for data persistence

## Project Structure

The application is organized into the following main packages:

- `model`: Contains the domain classes of the system.
- `dao`: Handles data persistence using text files.
- `service`: Contains the application and business logic.
- `ui`: Handles user interaction through the console.
- `Main`: Initializes the application components and starts the program.

## How to Run

1. Clone or download the repository.
2. Open the project in NetBeans.
3. Wait for Maven to load the project dependencies.
4. Run **Clean and Build Project**.
5. Run `Main.java`.
6. Use the console menu to access the different GameZone operations.

The application uses the files located in the `data` directory to store and retrieve products, persons, and sales.

## Main Features

- Register videogames.
- Register consoles.
- List products.
- Register customers.
- List customers.
- List sellers.
- Register sales.
- List all sales.
- View purchase history by customer.
- View sales history by seller.
- Update product inventory when a sale is registered.

## Promotion Module

The system includes a promotion management module that allows GameZone Unicesar to register and automatically apply discounts to sales.

### Promotion Types

The system supports three types of promotions:

- **Percentage Discount:** applies a percentage discount to the total sale amount.
- **Category Discount:** applies a percentage discount only to products that belong to a specific category (`VIDEOGAME` or `CONSOLE`).
- **Bulk Purchase Discount:** applies a percentage discount to the total sale when the minimum required number of products is reached.

### Promotion Features

The promotion module allows users to:

- Register percentage, category, and bulk purchase promotions.
- List all registered promotions.
- List promotions that are currently active.
- Store and load promotions from `data/promotions.csv`.
- Automatically evaluate active promotions when registering a sale.
- Apply only the promotion that provides the highest monetary discount.
- Store the applied promotion and discount amount in each sale.
- Display the subtotal, applied promotion, discount amount, and final total in the sale receipt.
- View the details of a specific sale, including its applied discount.

### Promotion Architecture

The promotion module follows the layered architecture of the project:

`UI → Service → DAO → Model`

The abstract `Promotion` class defines the common structure and behavior of promotions, while `PercentageDiscount`, `CategoryDiscount`, and `BulkPurchaseDiscount` implement their specific discount calculation rules through polymorphism.

`PromotionService` contains the business logic for identifying active promotions and selecting the promotion that provides the highest discount for a sale.

Promotion data is persisted in `data/promotions.csv`.

## Return Module

The Return Module allows GameZone to manage product returns associated with previously registered sales.

### Main Features

- Register partial product returns from an existing sale.
- Validate that the original sale exists.
- Validate the 30-day return period.
- Validate that returned products belong to the original sale.
- Prevent the same product from being returned more than once for the same sale.
- Automatically calculate the refund amount.
- Automatically restore returned products to inventory.
- View all registered returns.
- View returns by customer.
- View returns by sale.
- Generate a monthly balance based on sales and returns.

### Return Persistence

Returns are stored in:

`data/returns.csv`

Each return stores the return identifier, return date, original sale identifier, returned product identifiers, reason, and refund amount.

### Return Management Menu

The application includes a Returns Management menu with the following options:

1. Register Return
2. List All Returns
3. Returns by Customer
4. Returns by Sale
5. Monthly Balance

### Monthly Balance

The monthly balance is calculated using:

`Monthly Balance = Sales Total - Returns Total`

Sales and returns are filtered according to the month and year selected by the user.

### Warranty Module

The Warranty module manages warranties associated with consoles sold by GameZone.

It includes two warranty types:

- **Basic Warranty:** 6 months, no additional cost.
- **Extended Warranty:** 12 months, with an additional cost equal to 10% of the product price.

The module supports:

- Automatic warranty assignment during console sales.
- Optional extended warranty selection during the sales process.
- Warranty persistence using `WarrantyDAO`.
- Warranty lookup by product and sale.
- Listing all registered warranties.
- Listing currently active warranties.
- Listing warranties that expire within a specified number of days.
- Warranty certificate generation.