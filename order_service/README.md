# Order Service

사용자로부터 상품 ID와 수량을 입력받아 구매 요청을 처리하는 RESTful API 서비스입니다.

## 주요 기능

- 구매 요청 접수
- `RestClient`를 사용한 Product Service 재고 감소 API 호출
- 구매 성공/실패 상태 저장
- 재고 부족 등 외부 API 호출 실패 시 보상 트랜잭션 처리
- H2 데이터베이스 기반 주문 저장

## 실행 환경

- Java 21
- Spring Boot 4.0.6
- Gradle
- H2 Database

## 서버 설정

기본 포트는 `8081`입니다.

```properties
spring.application.name=order_service
server.port=8081
product-service.url=http://localhost:8082
```

`product-service.url`은 Product Service의 호출 주소입니다.

## 주문 상태

| 상태 | 설명 |
| --- | --- |
| SUCCESS | Product Service 재고 감소 API 호출에 성공한 주문 |
| FAILED | Product Service 재고 감소 API 호출에 실패한 주문 |

## API

### 구매 요청

```http
POST /orders
Content-Type: application/json
```

요청 예시:

```json
{
  "productId": 1,
  "quantity": 3
}
```

성공 응답 예시:

```http
201 Created
```

```json
{
  "id": 1,
  "productId": 1,
  "quantity": 3,
  "status": "SUCCESS"
}
```

재고 부족 등 Product Service 호출 실패 시 응답 예시:

```json
{
  "id": 2,
  "productId": 1,
  "quantity": 30,
  "status": "FAILED"
}
```

## 처리 흐름

1. 사용자가 상품 ID와 수량으로 구매 요청을 보냅니다.
2. Order Service가 Product Service의 재고 감소 API를 호출합니다.
3. 재고 감소에 성공하면 주문을 `SUCCESS` 상태로 저장합니다.
4. 재고 부족 등으로 Product Service 호출이 실패하면 주문을 `FAILED` 상태로 저장합니다.

## 테스트 범위

- 주문 도메인 필드 검증
- 구매 요청 시 Product Service 재고 감소 API 호출 검증
- 구매 성공 시 `SUCCESS` 상태 저장 검증
- Product Service 호출 실패 시 `FAILED` 상태 저장 검증
