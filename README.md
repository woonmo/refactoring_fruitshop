
JSP Servlet 기반 쇼핑몰 리팩토링 프로젝트

## 프로젝트 개요
기존 JSP/Servlet 기반 쇼핑몰 프로젝트를 **Spring Boot**, **JPA** 로 리팩토링하여 유지보수성과 확장성을 높임

- **기간**: 2025년 04월 13일 ~ 진행중
- **주요기능**:
	- 회원가입 및 로그인
	- 장바구니 상품 추가 및 제거
	- 상품 목록 조회 및 계절별 필터링
	- 주문 및 결제
- **프로젝트 목표**:
	- Servlet-JSP 의 강결합 구조를 REST API 기반으로 분리
	- JDBC의 수동 트랜잭션에서 JPA로 관리 전환하여 데이터 무결성 확보
	- 코드 가독성, 유지보수 및 확장성 향상


## 리팩토링 동기
기존 JSP / Servlet 코드의 문제점:

- **Servlet-JSP의 백엔드-프론트엔드 강결합**:
	- 뷰와 비즈니스 로직이 공존해 가독성이 떨어지고 유지보수가 어려움
	- REST API 기반으로 개선하여 프론트엔드 독립 가능성 확보
- **JDBC 의 한계**:
	- 수동 트랜잭션 관리로 인해 데이터 유실 및 데이터베이스 락 발생 가능성 존재
	- 동적 쿼리 생성 시 조건에 따른 PreparedStatement 위치홀더 사용 시 코드 가독성 추락 및 유지보수 난이도 상승
	- JPA 도입으로 데이터베이스 의존을 줄이고 가독성을 높임


## 주요 개선점

- **JDBC → JPA**: Entity 연관관계 매핑으로 쿼리 관리 간소화
- **Servlet → REST API**: REST API 설계로 프론트엔드와 백엔드 분리
- **JSP → Thymeleaf**: 뷰 템플릿 관리 간소화

## ERD
[FruitShop ERD](https://www.erdcloud.com/d/7S9xt8SMDxY87zEFz)


## API 정의
자세한 정보는 [API 정의서](docs/api-definition.md) 문서를 참조해주세요.


##  개발환경
### 기존 환경
- **언어 / 프레임워크**:
	- Java (JDK 17)
	- Servlet, JSP
- **데이터베이스**:
	- Oracle 18c
	- JDBC
- **배포**:
	- Ubuntu 24.04
	- Tomcat 10.1
### 리팩토링 환경
- **언어 / 프레임워크**:
	- Spring Boot 3.4.4. (Gradle)
	- JDK 17
	- Thymeleaf
- 데이터베이스:
	- Local: H2
	- Prod: Oracle 18c
	- Spring Data JPA
- 배포:
	- Ubuntu 24.04
 	- Docker(Dockerfile 및 Docker compose 사용) 예정 


## 기술적 도전
### 1. JDBC → JPA 전환
- **도전**: 
	- 기존 SQL 강의존 방식에서 ORM 방식으로 전환
- **결과**:
	- Entity 연관관계 설정 `@OneToMany`, `@ManyToOne` 활용

### 2. REST API 설계
- **도전**: 기존 Servlet-JSP 방식을 REST API 로 변환
- 결과:
	- `@RestController`와 `@Controller` 로 비즈니스 로직과 뷰 로직을 구분
	- `@ControllerAdvice`를 활용한 예외 처리 및 예외 메시지 응답


## 고민 사항

### 1. Entity 생성 시 전략 IDENTITY vs SEQUENCE
- **고민**:
	- `IDENTITY`:
		- `PK`를 데이터베이스 `INSERT`시점에 생성
		- 비즈니스 로직에서 `PK`를 활용하려면 추가 조회 필요
		- MySQL의 `AUTO_INCREMENT` 같은 속성에 대응
	- `SEQUENCE`:
		- `SELECT`절로 시퀀스를 채번하여 영속화 상태에서 활용 가능
		- 비즈니스 로직에서 `PK`를 활용 하기 위한 추가 조회 없이 사용 가능
		- `SEQUENCE` 채번이 `INSERT`와 별도로 이루어져 성능 최적화에 유리

- **결정**:
	- 오라클은 12c 이후 `IDENTITY` 전략을 지원하지만 `SEQUENCE` 전략을 활용
		- 쿼리는 한 시점에 작동하는 것으로 서버 자원 부담을 줄일 수 있다
		- 오라클의 `SEQUENCE` 전략을 활용해 JPA와 `SEQUENCE` 동기화 경험치 (allocationSize 설정)
		- 주문번호 생성 `날짜-시퀀스`형태로 비즈니스 로직 구현 가능성 확인


