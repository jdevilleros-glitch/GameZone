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