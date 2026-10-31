# Fintech API

REST API desarrollada con Java y Spring Boot para gestionar usuarios, cuentas bancarias y transacciones.

El proyecto fue desarrollado como trabajo práctico de la Diplomatura en Desarrollo de Software FinTech, aplicando conceptos de Programación Orientada a Objetos, arquitectura por capas, persistencia con JPA, servicios REST, manejo de excepciones, DTOs y testing unitario.

## Tecnologías utilizadas

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- Spring Validation
- MySQL
- H2 Database para tests
- JUnit 5
- Mockito
- Maven
- Swagger / OpenAPI
- Postman

## Arquitectura

El proyecto está organizado utilizando una arquitectura por capas.

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

Además, se utilizan DTOs para evitar exponer directamente las entidades JPA mediante la API.

Estructura principal:

```text
src/main/java/com/marcos/Fintech
│
├── controllers
├── dto
├── entity
├── exceptions
├── interfaces
├── repositories
├── services
└── config
```

## Entidades principales

### User

Representa un usuario del sistema.

Entre sus principales datos se encuentran:

- nombre
- apellido
- número de documento
- email
- teléfono
- fecha de registro
- estado activo/inactivo

Un usuario puede tener múltiples cuentas.

### Account

Representa una cuenta bancaria asociada a un usuario.

Contiene:

- número de cuenta
- saldo
- moneda
- fecha de creación
- estado activo/inactivo
- usuario propietario

### Transaction

Representa una operación entre cuentas.

Contiene:

- monto
- tipo de transacción
- fecha
- descripción
- cuenta origen
- cuenta destino

## Relaciones

La relación principal entre usuarios y cuentas es:

```text
User 1 -------- N Account
```

Cada cuenta pertenece a un único usuario.

Las transacciones pueden contener una cuenta origen y una cuenta destino.

## DTOs

La API utiliza DTOs para controlar la información recibida y enviada.

Ejemplos:

```text
UserCreateDTO
UserUpdateDTO
UserResponseDTO

AccountCreateDTO
AccountUpdateDTO
AccountResponseDTO

TransactionResponseDTO
```

De esta forma los controladores no exponen directamente las entidades JPA.

## Reglas de negocio

La aplicación implementa, entre otras, las siguientes reglas:

- Los depósitos deben ser mayores que cero.
- Los retiros deben ser mayores que cero.
- No se puede retirar un monto superior al saldo disponible.
- No se pueden realizar operaciones sobre cuentas inactivas.
- Una transferencia no puede tener la misma cuenta como origen y destino.
- Una cuenta con saldo distinto de cero no puede ser desactivada.
- Un usuario no puede ser desactivado mientras alguna de sus cuentas tenga saldo distinto de cero.
- Cuando un usuario es desactivado, sus cuentas también son desactivadas.
- Una cuenta no puede ser activada si su usuario está inactivo.
- No se puede crear una cuenta activa para un usuario inactivo.
- Una cuenta que posee transacciones asociadas no puede eliminarse físicamente.

## Manejo de errores

La aplicación utiliza excepciones personalizadas y un manejador global mediante:

```java
@RestControllerAdvice
```

Algunos errores manejados son:

```text
UserNotFoundException
AccountNotFoundException
TransactionNotFoundException
InvalidAmountException
InsufficientFundsException
AccountInactiveException
SameAccountTransferException
AccountHasTransactionsException
AccountHasBalanceException
UserInactiveException
```

Los errores son convertidos a códigos HTTP apropiados, entre ellos:

```text
400 Bad Request
404 Not Found
409 Conflict
```

## Endpoints principales

### Users

```text
GET    /users
GET    /users/{id}
GET    /users/document/{documentNumber}
POST   /users
PUT    /users/{id}
PUT    /users/{id}/activate
PUT    /users/{id}/deactivate
```

### Accounts

```text
GET    /accounts
GET    /accounts/{id}
GET    /accounts/number/{accountNumber}
POST   /accounts
PUT    /accounts/{id}
PUT    /accounts/{id}/activate
PUT    /accounts/{id}/deactivate
POST   /accounts/{id}/deposit
POST   /accounts/{id}/withdraw
DELETE /accounts/{id}
```

### Transactions

```text
GET  /transactions
GET  /transactions/{id}
POST /transactions/transfer
```

## Base de datos

La aplicación utiliza MySQL.

Debe existir una base de datos para la aplicación y configurarse la conexión correspondiente en:

```text
src/main/resources/application.properties
```

Ejemplo:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/fintech_api
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

## Ejecución

Ejecutar el proyecto desde IntelliJ IDEA utilizando la clase principal de Spring Boot.

La aplicación se inicia por defecto en:

```text
http://localhost:8080
```

## Swagger / OpenAPI

La documentación interactiva de la API está disponible con la aplicación iniciada en:

```text
http://localhost:8080/swagger-ui/index.html
```

La especificación OpenAPI puede consultarse en:

```text
http://localhost:8080/v3/api-docs
```

## Postman

El proyecto incluye una colección de Postman con requests organizadas para:

```text
Users
Accounts
Transactions
Error cases
```

La colección incluye pruebas de operaciones exitosas y reglas de negocio, incluyendo respuestas HTTP 200, 201, 204, 400, 404 y 409.

## Testing

Los servicios son testeados utilizando JUnit 5 y Mockito.

Las pruebas siguen principalmente la estructura:

```text
Given
When
Then
```

Se prueban tanto escenarios exitosos como excepciones y reglas de negocio.

Para ejecutar todos los tests:

```bash
mvnw.cmd test
```

Para realizar una validación completa del proyecto:

```bash
mvnw.cmd clean verify
```

## Autor

Marcos Garcia

## Estado del proyecto

Trabajo práctico finalizado con:

- API REST
- persistencia MySQL
- DTOs
- validaciones
- manejo global de excepciones
- reglas de negocio
- pruebas unitarias
- Swagger / OpenAPI
- colección Postman