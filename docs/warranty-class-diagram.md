# Warranty Module Class Diagram

```mermaid
classDiagram

    class Warranty {
        <<abstract>>
        -String warrantyId
        -Product product
        -Sale sale
        -LocalDate startDate
        -LocalDate endDate
        +getWarrantyId() String
        +getProduct() Product
        +getSale() Sale
        +getStartDate() LocalDate
        +getEndDate() LocalDate
        +getDurationInMonths()* int
        +getWarrantyType()* String
        +getAdditionalCost()* double
        +isActive(LocalDate date) boolean
        +generateWarrantyCertificate() String
    }

    class BasicWarranty {
        +getDurationInMonths() int
        +getWarrantyType() String
        +getAdditionalCost() double
    }

    class ExtendedWarranty {
        +getDurationInMonths() int
        +getWarrantyType() String
        +getAdditionalCost() double
    }

    class WarrantyDAO {
        -String filePath
        -SaleDAO saleDAO
        -ProductDAO productDAO
        +saveAll(List~Warranty~ warranties) void
        +loadAll() List~Warranty~
    }

    class WarrantyService {
        -WarrantyDAO warrantyDAO
        -List~Warranty~ warranties
        +assignBasicWarranty(Product, Sale, LocalDate) BasicWarranty
        +assignExtendedWarranty(Product, Sale, LocalDate) ExtendedWarranty
        +findWarrantyByProduct(String, String) Warranty
        +listAllWarranties() List~Warranty~
        +listActiveWarranties() List~Warranty~
        +listWarrantiesExpiringSoon(int) List~Warranty~
    }

    class Product
    class Sale
    class SaleService
    class Menu

    Warranty <|-- BasicWarranty
    Warranty <|-- ExtendedWarranty

    Warranty --> Product
    Warranty --> Sale

    WarrantyDAO --> Warranty
    WarrantyDAO --> SaleDAO
    WarrantyDAO --> ProductDAO

    WarrantyService --> WarrantyDAO
    WarrantyService --> Warranty

    SaleService --> WarrantyService
    Menu --> WarrantyService
