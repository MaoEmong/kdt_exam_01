# Product-Order Service

상품 도메인 서비스와 구매 도메인 서비스를 분리하여 구현한 REST 기반 MSA 예제입니다. 사용자가 구매 요청을 보내면 Order Service가 Product Service의 재고 감소 API를 호출하고, 호출 결과에 따라 주문 상태를 `SUCCESS` 또는 `FAILED`로 저장합니다.

## 프로젝트 구성

```text
.
├── product_service
└── order_service
```

| 서비스 | 포트 | 역할 |
| --- | ---: | --- |
| Product Service | 8082 | 상품 정보와 재고 관리, 재고 감소 API 제공 |
| Order Service | 8081 | 구매 요청 처리, Product Service API 호출, 주문 상태 저장 |

## 기술 스택

- Java 21
- Spring Boot 4.0.6
- Spring Web MVC
- Spring Data JPA
- H2 Database
- Gradle
- RestClient
- JUnit 5

## 문제별 구현 설명

### 1. 상품(Product) 도메인 서비스 구현

Product Service는 상품의 `id`, `name`, `stock` 데이터를 관리합니다. 상품 데이터는 JPA Entity인 `Product`로 표현하고, `ProductRepository`를 통해 H2 데이터베이스에 저장합니다.

재고 감소 API는 다음과 같이 구현했습니다.

```http
POST /products/{productId}/decrease-stock
Content-Type: application/json
```

요청 본문:

```json
{
  "quantity": 3
}
```

재고가 충분하면 상품 재고를 요청 수량만큼 감소시키고 `204 No Content`를 반환합니다. 재고가 부족하면 `InsufficientStockException`을 발생시키고 `400 Bad Request`를 반환합니다. 상품이 존재하지 않으면 `ProductNotFoundException`을 발생시키고 `404 Not Found`를 반환합니다.

애플리케이션 시작 시 상품 데이터가 없으면 다음 더미 데이터를 생성합니다.

| ID | 이름 | 재고 |
| --- | --- | ---: |
| 1 | Keyboard | 10 |
| 2 | Mouse | 20 |
| 3 | Monitor | 5 |

### 2. 구매(Order) 도메인 서비스 구현

Order Service는 사용자로부터 상품 ID와 수량을 입력받아 구매 요청을 처리합니다.

구매 요청 API는 다음과 같이 구현했습니다.

```http
POST /orders
Content-Type: application/json
```

요청 본문:

```json
{
  "productId": 1,
  "quantity": 3
}
```

Order Service는 `RestClient`를 사용하여 Product Service의 재고 감소 API를 호출합니다. 재고 감소 호출이 성공하면 주문을 `SUCCESS` 상태로 저장합니다.

### 3. 보상 트랜잭션 구현

서비스가 분리되어 있으므로 하나의 로컬 트랜잭션으로 주문 저장과 상품 재고 감소를 동시에 묶을 수 없습니다. 이 프로젝트에서는 복잡한 분산 트랜잭션 대신 보상 트랜잭션을 단순한 상태 저장 방식으로 구현했습니다.

Product Service 재고 감소 API 호출이 실패하면 Order Service는 주문 요청을 삭제하거나 무시하지 않고 `FAILED` 상태로 저장합니다. 이를 통해 실패 이력을 남기고, 사용자 알림이나 후속 처리에 활용할 수 있도록 했습니다.

주문 상태는 다음 enum으로 관리합니다.

| 상태 | 의미 |
| --- | --- |
| SUCCESS | 재고 감소 API 호출에 성공한 주문 |
| FAILED | 재고 감소 API 호출에 실패한 주문 |

### 4. 전체 시스템 통합 및 테스트

두 서비스는 REST API로 통신합니다.

```text
Client
  -> Order Service : POST /orders
  -> Product Service : POST /products/{productId}/decrease-stock
  <- Product Service : 204 또는 오류 응답
<- Order Service : 주문 결과 반환
```

정상 구매 시에는 Product Service의 재고가 감소하고 Order Service에는 `SUCCESS` 주문이 저장됩니다. 재고 부족 시에는 Product Service가 `400 Bad Request`를 반환하고 Order Service에는 `FAILED` 주문이 저장됩니다.

## 설계 결정 사항

- Product Service와 Order Service를 독립적인 Spring Boot 프로젝트로 분리했습니다.
- Product Service가 상품 재고의 소유자가 되도록 했습니다.
- Order Service는 상품 재고를 직접 수정하지 않고 Product Service의 REST API를 호출합니다.
- 서비스 간 HTTP 통신은 Spring `RestClient`를 사용했습니다.
- 각 서비스는 독립적인 H2 데이터베이스를 사용합니다.
- 보상 트랜잭션은 주문 상태를 `FAILED`로 저장하는 방식으로 단순하게 구현했습니다.
- 테스트는 도메인, 서비스, API 레벨로 나누어 작성했습니다.

## 테스트 시나리오 및 결과

| 서비스 | 테스트 | 검증 내용 | 결과 |
| --- | --- | --- | --- |
| Product Service | `ProductTest` | 상품이 ID, 이름, 재고 데이터를 가지는지 확인 | 성공 |
| Product Service | `ProductDataInitializerTest` | 더미 상품 데이터 생성 확인 | 성공 |
| Product Service | `ProductServiceTest` | 재고 감소 시 상품 재고 감소 확인 | 성공 |
| Product Service | `ProductServiceTest` | 재고 부족 시 예외 발생 및 재고 유지 확인 | 성공 |
| Product Service | `ProductControllerTest` | 재고 감소 API 정상 호출 시 `204 No Content` 확인 | 성공 |
| Product Service | `ProductControllerTest` | 재고 부족 시 `400 Bad Request` 확인 | 성공 |
| Order Service | `OrderTest` | 주문이 상품 ID와 수량 데이터를 가지는지 확인 | 성공 |
| Order Service | `OrderServiceTest` | 구매 성공 시 Product Service 재고 감소 API 호출 및 `SUCCESS` 저장 확인 | 성공 |
| Order Service | `OrderServiceTest` | Product Service 호출 실패 시 `FAILED` 저장 확인 | 성공 |

테스트 실행 결과는 두 서비스 모두 성공했습니다.

```text
product_service: BUILD SUCCESSFUL
order_service: BUILD SUCCESSFUL
```

## API 요약

### Order Service

| Method | URL | 설명 |
| --- | --- | --- |
| POST | `/orders` | 구매 요청 처리 |

### Product Service

| Method | URL | 설명 |
| --- | --- | --- |
| POST | `/products/{productId}/decrease-stock` | 상품 재고 감소 |

## 서비스별 상세 문서

- [Product Service README](./product_service/README.md)
- [Order Service README](./order_service/README.md)
