# GameZone - Team

## Team Members

| Full Name                       | Student Code | Role             | Assigned Module |
| ------------------------------- | -----------: | ---------------- | --------------- |
| Juan Miguel Devilleros Pimienta |   1067599775 | Technical Leader | Sales           |
| Mariana Michelle Ahumada Rangel |   1066872844 | Developer 1      | Products        |
| Isaura Jireth Manjarrez         |   1067600652 | Developer 2      | People          |

---

## Class Distribution

### Technical Leader - Juan Miguel Devilleros Pimienta

**Module:** Sales

**Classes:**

1. `Sale`
2. `SalePersistence`
3. `SaleService`
4. `SaleUI`
5. `Main`

**Responsibilities:**

* Implement the sales domain class.
* Implement the persistence class for the sales module.
* Implement the service class for the sales module.
* Implement the user interface for the application.
* Implement the main application class.
* Integrate the modules developed by the other team members.
* Verify that the dependencies between the four layers are respected.
* Coordinate the integration of the team's features.

**Feature branch:**

```text
feature/sales-module
```

---

### Developer 1 - Mariana Michelle Ahumada Rangel

**Module:** Products

**Classes:**

1. Abstract product class.
2. First derived product class (Videogame).
3. Second derived product class (Console).
4. Product persistence class.
5. Product service class.

**Responsibilities:**

* Implement the abstract product class.
* Implement the two specialized product classes.
* Implement persistence for the products module.
* Implement the service for the products module.
* Apply the inheritance hierarchy defined for products.
* Respect the model, persistence and services layer responsibilities.

**Feature branch:**

```text
feature/products-module
```

---

### Developer 2 - Isaura Jireth Manjarrez

**Module:** People

**Classes:**

1. Abstract person class.
2. First derived person class (Client).
3. Second derived person class (Seller).
4. People persistence class.
5. People service class.

**Responsibilities:**

* Implement the abstract person class.
* Implement the two specialized person classes.
* Implement persistence for the people module.
* Implement the service for the people module.
* Apply the inheritance hierarchy defined for people.
* Respect the model, persistence and services layer responsibilities.

**Feature branch:**

```text
feature/people-module
```

---

## Committed Activities

### Technical Leader - Juan Miguel Devilleros Pimienta

* Develop the sales module.
* Develop the application user interface.
* Develop the main application entry point.
* Integrate the products and people modules.
* Verify compliance with the four-layer architecture.
* Coordinate the integration and resolution of conflicts between branches.
* Validate the final operation of the application.

### Developer 1 - Mariana Michelle Ahumada Rangel

* Develop the products domain hierarchy.
* Implement the two specialized product classes.
* Develop product persistence.
* Develop the product service.
* Test the products module.
* Commit and merge the completed feature into the integration branch.

### Developer 2 - Isaura Jireth Manjarrez

* Develop the people domain hierarchy.
* Implement the two specialized person classes.
* Develop people persistence.
* Develop the people service.
* Test the people module.
* Commit and merge the completed feature into the integration branch.

---

## Final Integration

During the final integration stage, the Technical Leader reviewed and integrated the application modules, including the model, DAO, service, and console UI layers.

The final implementation uses DAO classes for file persistence and a centralized `Menu` class for console interaction. The integrated application is initialized from `Main`.