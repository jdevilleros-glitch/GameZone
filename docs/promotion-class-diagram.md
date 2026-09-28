# Promotion Module Class Diagram

The following class diagram represents the integration of the promotion module with the existing GameZone system.

```mermaid
classDiagram

    class Promotion {
        -String id
        -String name
        -LocalDate startDate
        -LocalDate endDate
        +Promotion(String id, String name, LocalDate startDate, LocalDate endDate)
        +String getId()
        +void setId(String id)
        +String getName()
        +void setName(String name)
        +LocalDate getStartDate()
        +void setStartDate(LocalDate startDate)
        +LocalDate getEndDate()
        +void setEndDate(LocalDate endDate)
        +boolean isActive(LocalDate date)
        +double calculateDiscount(Sale sale)*
    }

    class PercentageDiscount {
        -double percentage
        +double calculateDiscount(Sale sale)
    }

    class CategoryDiscount {
        -double percentage
        -String targetCategory
        +double calculateDiscount(Sale sale)
    }

    class BulkPurchaseDiscount {
        -int minimumQuantity
        -double percentage
        +double calculateDiscount(Sale sale)
    }

    class Sale {
        -String saleId
        -Date date
        -double total
        -String appliedPromotionName
        -double discountAmount
        +String generateReceipt()
    }

    class PromotionDAO {
        -String filePath
        +List~Promotion~ loadAll()
        +void saveAll(List~Promotion~ promotions)
    }

    class PromotionService {
        -PromotionDAO promotionDAO
        -List~Promotion~ promotions
        +void registerPercentageDiscount()
        +void registerCategoryDiscount()
        +void registerBulkPurchaseDiscount()
        +List~Promotion~ listAllPromotions()
        +List~Promotion~ listActivePromotions()
        +Promotion findBestPromotionFor(Sale sale)
        +Promotion findById(String id)
    }

    class SaleService {
        -PromotionService promotionService
        +boolean registerSale()
        +Sale findSaleById(String saleId)
    }

    Promotion <|-- PercentageDiscount
    Promotion <|-- CategoryDiscount
    Promotion <|-- BulkPurchaseDiscount

    Promotion ..> Sale : calculates discount

    PromotionDAO --> Promotion : persists
    PromotionService --> PromotionDAO : uses
    PromotionService --> Promotion : manages
    PromotionService ..> Sale : evaluates

    SaleService --> PromotionService : selects best promotion
    SaleService --> Sale : creates and manages
```

## Main Relationships

- `PercentageDiscount`, `CategoryDiscount`, and `BulkPurchaseDiscount` inherit from the abstract `Promotion` class.
- Each promotion implements its own discount calculation through polymorphism.
- `PromotionDAO` handles promotion persistence.
- `PromotionService` manages promotions and determines the best active promotion for a sale.
- `SaleService` uses `PromotionService` when registering a sale.
- `Sale` stores the applied promotion name and discount amount.
