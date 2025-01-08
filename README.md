### Requirements

- Java 21 or higher

### Used Technologies

- Spring Boot 3.4.1
- H2 Database
- Spring Data JPA
- Spring Web
- Lombok
- Mapstruct
- JUnit5
- Gradle - Groovy

### To Run Project

`gradlew bootRun`

### REST API Root URL

`http://localhost:8080/api/v1/accounts`

#### Available Endpoints

- `GET /{accountNumber}`
    - Returns basic account info of given account number.
    - Provide `includeTransactions=true` query parameter to fetch related transactions info.
- `POST /{accountNumber}/credit`
    - Body
        - `{"amount" : 123.45}`
    - Adds specified amount to account balance.
- `POST /{accountNumber}/debit`
    - Body
        - `{"amount" : 123.45}`
    - Substracts specified amount from account balance.
- `POST /{accountNumber}/do-payment`
    - Body for **Phone Bill**
        - `{ "paymentType": "PHONE_BILL", "carrier": "Vodafone", "phoneNumber": "5554443322", "amount": 437.56 }`
    - Body for **Electric Bill**
        - `{ "paymentType": "ELECTRIC_BILL", "distributionCompany": "ESA", "customerNo": "1234567890", "amount": 754.11 }`
    - Substracts specified amount from account balance and stores additional data such as `carrier, phoneNumber, customerNo, etc.` in the transaction.