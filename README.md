Similar Products API

API REST desarrollada en Spring Boot que expone un endpoint para obtener productos similares a uno dado, integrándose con un servicio externo simulado mediante WebClient.

Este proyecto implementa:

Arquitectura en capas (API / Application / Domain / Infrastructure)

Manejo de errores consistente

Timeouts y degradación ante fallos del upstream

Tests unitarios del caso de uso principal

Tecnologías

Java 17

Spring Boot 3.x

Spring Web + WebClient (WebFlux)

Maven

Docker (para el servicio mock)

JUnit 5 + Mockito

Lombok

Arquitectura (alto nivel)
com.globant.similarproducts
├── api
│   └── controller
├── application
│   ├── dto
│   └── usecase
├── domain
│   ├── model
│   └── exception
└── infrastructure
    ├── client
    ├── config
    └── repository

Endpoint principal
Obtener productos similares
GET /product/{productId}/similar

Ejemplo
curl http://localhost:5000/product/1/similar

Respuesta exitosa (200)
[
  {
    "id": "2",
    "name": "Dress",
    "price": 19.99,
    "availability": true
  },
  {
    "id": "4",
    "name": "Boots",
    "price": 39.99,
    "availability": true
  }
]


⚠️ Los productos no disponibles o con error en el upstream se omiten (degradación).

Manejo de errores
Escenario	Código
Producto base inexistente	404 Not Found
Upstream caído / timeout	502 Bad Gateway
Error inesperado	500 Internal Server Error

Ejemplo:

curl http://localhost:5000/product/999/similar

{
  "status": 404,
  "error": "Not Found",
  "message": "Product 999 not found"
}

Servicio externo (mock)

La API depende de un servicio mock que expone:

GET /product/{id}

GET /product/{id}/similarids

Levantar el mock

Desde el repo del mock:

docker compose up -d simulado


El mock corre en:

http://localhost:3001

Configuración

application.properties

server.port=5000

external.product-api.base-url=http://localhost:3001
external.product-api.timeout-ms=2000

Actuator

Disponible en:

curl http://localhost:5000/actuator
curl http://localhost:5000/actuator/mappings

Build & Run
Compilar
./mvnw clean package

Ejecutar
./mvnw spring-boot:run


O directamente desde IntelliJ usando la clase:

SimilarProductsApiApplication

Tests

Se incluyen tests unitarios para el caso de uso principal:

./mvnw test


Clases testeadas:

GetSimilarProductsUseCase

Commits recomendados (convención)

Ejemplos usados en este proyecto:

feat: add similar products endpoint

feat: add upstream timeout and error handling

test: add unit tests for GetSimilarProductsUseCase

docs: add README with setup and usage instructions

## Design decisions

### Reactive client with imperative application layer
This project uses `WebClient` (Spring WebFlux) to consume the external product API because it provides strong support for timeouts and fine-grained HTTP error handling.
The application/use-case layer remains imperative and returns plain Java types (e.g. `List<Product>`). For that reason the infrastructure client uses `.block()` at the boundary.
This keeps the domain/application layer free of reactive types (`Mono`/`Flux`) and aligns with a hexagonal/DDD approach.

**Trade-off:** blocking reduces the benefit of reactive end-to-end execution. A possible improvement would be to migrate the use case and controller to reactive types and use `flatMapSequential` to keep ordering while fetching in parallel.

### Sequential vs parallel resolution
Similar product details are resolved in the same order returned by the external API. The current implementation is sequential for simplicity and predictable behavior.
A potential improvement is to fetch details concurrently while preserving order (e.g. using `flatMapSequential` in a reactive flow or `CompletableFuture` with a bounded executor).

### Testing strategy
Given the scope and time constraints of this exercise, tests focus on:
- unit tests for the main use case (business rules)
- controller tests (`@WebMvcTest`) to validate REST behavior and error mapping

End-to-end integration tests with Docker were intentionally avoided to reduce flakiness and environment dependencies (Docker daemon availability, ports, startup timing), while integration was validated manually using the provided mock service.