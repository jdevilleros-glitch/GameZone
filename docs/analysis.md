
# Analysis

## 1. Common and specific attributes of persons

According to the context of the project, every person in the system has an id, a name, and a cellphone number. These common attributes are defined in the abstract `Person` class.

A `Customer` has an additional email attribute, while a `Seller` has an employee code. Since `Person` contains the common attributes shared by customers and sellers, these attributes are inherited and each subclass adds its own specific information.

This creates the following hierarchy:

Person → Customer  
Person → Seller

## 2. Generic person class

A generic person class should exist, but it should be abstract because the system works with specific roles. A person must be represented as either a customer or a seller.

Making `Person` abstract allows the common attributes and behavior to be defined in one place while preventing the creation of generic `Person` objects without a specific role.

## 3. Common and specific attributes of products

Every product has an id, name, price, and stock quantity. These common attributes are defined in the abstract `Product` class.

A `Videogame` additionally has a genre and a platform, while a `Console` has a brand and a model.

This allows the common product information to be inherited while each product type keeps its own specific characteristics.

## 4. Product-specific behavior

The behavior that identifies the specific type of a product is declared as an abstract method in the `Product` class:

`getProductType()`

Each subclass overrides this method according to its own type. `Videogame` returns `VIDEOGAME` and `Console` returns `CONSOLE`.

This design uses abstraction, inheritance, and polymorphism because the base class defines the required behavior while each subclass provides its own implementation.

## 5. Relationships involved in a sale

A `Sale` is associated with a `Customer`, a `Seller`, and one or more `Product` objects.

These are association relationships rather than inheritance relationships because a sale is not a type of customer, seller, or product. Instead, it needs references to these objects to represent the transaction.

Inheritance is used separately in the following hierarchies:

Person → Customer  
Person → Seller  
Product → Videogame  
Product → Console

## 6. Responsibility for calculating the sale total

The `Sale` class should calculate its own total because the total is information that belongs directly to the sale and can be obtained from the products included in it.

For this reason, the `Sale` class contains the `calculateTotal()` method, which adds the prices of all products included in the sale.

## 7. Minimum product requirement

The system must verify that a sale contains at least one product before it is registered.

This validation belongs in the service layer because it is a business rule. In this project, `SaleService` verifies that the product list is not empty and that all requested products exist before creating and saving the sale.

## 8. Automatic inventory update

When a sale is registered, the system verifies that the selected products have available stock. After the validation succeeds, the stock of each sold product is reduced and the updated product information is saved.

The main classes involved in this process are `SaleService`, `Product`, `ProductDAO`, `Sale`, and `SaleDAO`.

`SaleService` coordinates the operation, the `Product` objects contain the stock information, `ProductDAO` persists the updated inventory, and `SaleDAO` persists the completed sale.

## 9. Organization into layers

The main criterion for deciding the layer of a class is its responsibility.

The model layer contains the domain classes that represent the main objects of the system, such as `Person`, `Customer`, `Seller`, `Product`, `Videogame`, `Console`, and `Sale`.

The persistence layer contains the classes responsible for loading and saving data. In this project these classes are `PersonDAO`, `ProductDAO`, and `SaleDAO`.

The service layer contains the business operations and rules. It includes `PersonService`, `ProductService`, and `SaleService`.

The user interface layer is responsible for interaction with the user. In this project, `Menu` provides the console interface, while `Main` initializes the application and its dependencies.

## 10. Separation of domain and persistence responsibilities

File loading and saving logic should not be placed inside domain classes because this would mix different responsibilities.

Domain classes should represent the business entities and their behavior, while persistence classes should be responsible for storing and retrieving information.

Separating these responsibilities makes the code easier to understand, maintain, test, and modify.

## 11. Dependencies between layers

The model layer should remain independent from the other application layers.

The persistence layer depends on the model because it loads and saves domain objects. The service layer depends on the model and persistence layers because it coordinates business operations and uses the DAOs to retrieve and store information.

The user interface depends on the service layer to execute the application's operations. It may use model objects to display the results returned by the services, but it should not access persistence directly.

Therefore, dependencies such as UI → DAO or Model → Service/UI/DAO should be avoided because they would break the separation of responsibilities between layers.