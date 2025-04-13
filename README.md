# refactoring_fruitshop

JSP Servlet 기반 쇼핑몰 리팩토링 프로젝트

## 리팩토링 동기

Servlet-JSP의 백엔드-프론트엔드 강결합
REST API 기반으로 작성되지 않은 코드를 REST API 기반으로 리팩토링
JDBC의 수동 트랜지션 관리로 인한 데이터 유실 및 데이터베이스 락 가능성, 
코드 가독성 및 JPA로 전환을 통한 유지보수성 향상을 위함

기술적 도전
기존 JDBC 및 Mybatis 로 SQL 강의존 방식에서 ORM 방식을 통한 데이터베이스 의존성 탈피
JPA 의 N+1 문제, 동시성 문제 이해 및 해결 도전

## 프로젝트 개요

- 기존 JSP/Servlet 기반 쇼핑몰 프로젝트를 Spring Boot, JPA 로 리팩토링
- 목표: 유지보수성 및 확장성 향상

## 주요 개선점

- JDBC → JPA: Entity 연관관계 매핑으로 쿼리 관리 간소화
- Servlet 기반 BackEnd 시스템 → REST API 시스템으로 설계

## ERD
![FruitShop ERD](https://www.erdcloud.com/d/7S9xt8SMDxY87zEFz)



##  개발환경

- 기존환경
	- JAVA
		- JDK 17
		- Servlet
		- JSP
	- DB
		- Oracle 18c
		- JDBC
	- 배포
		- ubuntu 24.04
		- tomcat 10.1

- 리팩토링 환경
	- SPRING BOOT
		- JDK 17
		- 3.4.4
		- Gradle
		- Thymeleaf
	- DB
		- local: H2
		- prod: Oracle 18c
		- SPRING DATA JPA 사용
	- 배포
		- ubuntu 24.04
		- Docker

## 고민 사항

- Entity 생성 시 전략
	- `IDNETITY` vs `SEQUENCE`
	- 차이점
		- `IDENTITY`는 `PK`를 데이터베이스 insert 시점에 생성하기에 비즈니스 로직에서 활용하려면 추가 조회가 필요 MySQL 의 auto_increament 전략에 대응
		- `SEQUENCE`는 select 절로 채번 후 영속화 상태로 있기에 비즈니스 로직으로 활용 후 commit 가능
	- 오라클 데이터베이스도 12c 버전 이후 `IDENTITY` 전략을 지원하나 오라클의 `SEQUENCE` 전략을 활용해 JPA와 `SEQUENCE` 동기화 경험 (allocationSize 설정)
	- 쿼리는 한 시점에 작동하는게 자원 부담이 적으므로 `SEQUENCE` 전략으로 결정 (주문번호 생성 시 날짜-시퀀스 형태로 만들어보기 등 비즈니스 로직으로 활용)


