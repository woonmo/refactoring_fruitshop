## 목차
- [1. 회원 API](#1-회원-api)
  - [1.1 회원 가입](#11-회원가입)
- [2. 상품](#2-상품)
  - [2.1 상품 조회](#21-상품-조회)
    - [2.1.1 전체 상품 조회](#211-전체-상품-조회)
    - [2.1.2 계절 상품 조회](#212-계절-상품-조회)
  - [2.2 상품 등록](#22-상품-등록)
- [3. 장바구니](#3-장바구니)
  - [3.1 장바구니 추가 & 수정](#31-장바구니-추가--수정)
  - [3.2 장바구니 상품 삭제](#32-장바구니-상품--삭제)
- [4. 주문](#4-주문)


## 1.  회원 API
### 1.1 회원 가입
- **URL** : `/api/users`
- **Method**:  `POST`
- **Request Body**:
```json
{
  "userid": "hongildong1",
  "password": "qwer1234$",
  "email": "test@tes1t.com",
  "name": "홍길동",
  "tel": "010-2322-3211",
  "gender": "남",
  "zipcode": "12345",
  "address": "안산시",
  "detailAddress": "김김동",
  "extraAddress": "아아동",
  "birthday": "1993-02-12"
}
```
- **Response** :
```json
{
  "userNo": 1,
  "userId": "hongildong1",
  "name": "홍길동",
  "birthday": "1993-02-12",
  "tel": "010-2322-3211",
  "email": "test@tes1t.com",
  "zipcode": "12345",
  "address": "안산시",
  "detailAddress": "김김동",
  "extraAddress": "아아동",
  "gender": "남",
  "point": 0,
  "createdAt": "2025-04-13"
}
```


## 2. 상품

### 2.1 상품 조회

#### 2.1.1 전체 상품 조회
- **URL** : `/products`
- **Method**: `GET`
- **Request Parameter**:
  - `size` (Optional, Integer): 기본값 12
  - `page` (Optional, Integer): 기본값 1
  - `product` (Optional, String): 상품명 검색어
- **Response**:
```json
List<ProductSearchResponse> productList

[ProductSearchResponse{no=4, name='수박', price=7500, thumbnail='watermelon.png', season='SUMMER', inventory=20}, 
ProductSearchResponse{no=3, name='딸기', price=4500, thumbnail='strawberry.png', season='SPRING', inventory=0}, 
ProductSearchResponse{no=2, name='배', price=3500, thumbnail='pear.png', season='AUTUMN', inventory=30}, 
ProductSearchResponse{no=1, name='사과', price=3000, thumbnail='apple.png', season='AUTUMN', inventory=50}...]
```
#### 2.1.2 계절 상품 조회
- **URL** : `/products/{season}`
- **Method**: `GET`
- **Request Parameter**:
  - `season` (String): 계절명
  - `size` (Optional, Integer): 기본값 12
  - `page` (Optional, Integer): 기본값 1
  - `product` (Optional, String): 상품명 검색어
- **Response**:
```json
List<ProductSearchResponse> productList

[ProductSearchResponse{no=24, name='참외', price=3000, thumbnail='korean_melon.png', season='SUMMER', inventory=30}, ProductSearchResponse{no=21, name='산딸기', price=5000, thumbnail='raspberry.png', season='SUMMER', inventory=0}, ProductSearchResponse{no=8, name='망고', price=6000, thumbnail='mango.png', season='SUMMER', inventory=25}, 
ProductSearchResponse{no=4, name='수박', price=7500, thumbnail='watermelon.png', season='SUMMER', inventory=20}...]
```

### 2.2 상품 등록
- **URL** : `/api/products`
- **Method**: `POST`
- **Request Parameter**:
```json
{
  "userNo": 16,
  "name": "딸기당근수박참외메론",
  "cost": 200,
  "price": 5000,
  "inventory": 20,
  "thumbnail": "melon.jpg",
  "description": "strawberry.jpg",
  "season": "WINTER"
}
```
- **Response**:
```json
{
  "prodNo": 41,
  "name": "딸기당근수박참외메론",
  "thumbnail": "melon.jpg",
  "description": "strawberry.jpg",
  "price": 5000,
  "cost": 200,
  "inventory": 20,
  "createAt": "2025-04-14 11:36:32",
  "modifiedAt": "2025-04-14 11:36:32",
  "season": "WINTER"
}
```


## 3. 장바구니
### 3.1 장바구니 추가 & 수정
- **URL** : `/api/carts`
- **Method**:  `POST`
- **Request Parameter**:
```json
{
  "userNo": 1,
  "productNo": 1,
  "quantity": 6
}
```
- **Response** :
```json
{
  "userNo": 1,
  "userId": "user1",
  "userName": "김민수",
  "productName": "딸기",
  "price": 4000,
  "quantity": 6,
  "totalPrice": 24000
}
```


### 3.2 장바구니 상품 삭제
- **URL** : `/api/carts/{id}`
- **Method**:  `DELETE`
- **Request Parameter**:
  - `cartNo`: 장바구니 번호(`PK`)
- **Response** :
  - 200 OK


## 4. 주문

### 4.1 주문서 생성
- **URL** : `/api/orders/initiate`
- **Method**:  `POST`
- **Request Parameter**:
1. 개별 상품 주문일 경우
```json
{
  "productNo": 2,
  "quantity": 1
}
```
2. 장바구니 통한 주문일 경우
```json
{
  "cartItemList": [
      {
          "productNo": 1,
          "quantity": 2,
          "isFromCart": true
      },
      {
          "productNo": 2,
          "quantity": 4,
          "isFromCart": true
      }
  ]
}
```

- **Response** :
```json
{
  "draftId": "e53f98e9-ea61-4ec1-867f-38c5035ac2b0"
}
```

### 4.2 주문 처리
- **URL** : `/api/orders/confirm`
- **Method**:  `POST`
- **Request Parameter**:
```json
{
  "receiverName": "홍길동",
  "receiverTel": "010-2322-3211",
  "zipCode": "12345",
  "address": "안산시",
  "detailAddress": "김김동",
  "extraAddress": "아아동",
  "totalPrice": 18000,
  "discount": 0,
  "finalPrice": 18000,
  "point": 180,
  "requestNote": "빠른 배송 부탁드립니다.",
  "isDefaultShip": "N"
}
```
- **Response** :
```json
{
  "orderCode": "20250422113117-1"
}
```
