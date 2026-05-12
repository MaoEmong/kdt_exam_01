# Product Service

상품 정보를 관리하고, 외부 서비스의 요청에 따라 상품 재고를 감소시키는 RESTful API 서비스입니다.

## 주요 기능

- 상품 ID, 이름, 재고 관리
- 상품 재고 감소 API 제공
- 재고 부족 시 예외 처리
- H2 데이터베이스 기반 상품 저장
- 애플리케이션 시작 시 더미 상품 데이터 생성

## 실행 환경

- Java 21
- Spring Boot 4.0.6
- Gradle
- H2 Database

## 서버 설정

기본 포트는 `8082`입니다.

```properties
spring.application.name=product_service
server.port=8082
```

## 더미 데이터

애플리케이션 시작 시 상품 데이터가 없으면 다음 데이터가 자동으로 생성됩니다.

| ID | 이름 | 재고 |
| --- | --- | ---: |
| 1 | Keyboard | 10 |
| 2 | Mouse | 20 |
| 3 | Monitor | 5 |

## API

### 상품 재고 감소

```http
POST /products/{productId}/decrease-stock
Content-Type: application/json
```

요청 예시:

```json
{
  "quantity": 3
}
```

성공 응답:

```http
204 No Content
```

재고 부족 응답:

```http
400 Bad Request
```

상품 없음 응답:

```http
404 Not Found
```

## 실행 방법

Windows:

```bash
./gradlew.bat bootRun
```

## 테스트 범위

- 상품 도메인 필드 검증
- 더미 상품 데이터 생성 검증
- 재고 감소 서비스 검증
- 재고 부족 예외 검증
- 재고 감소 REST API 검증
