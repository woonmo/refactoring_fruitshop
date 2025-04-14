## 목차
- [1. 회원 API](#1-회원-api)
    - [1.1 회원가입](#11-회원가입)
- [2. 상품](#2-상품)
    - [2.1 상품 등록](#21-상품-등록)
- [3. 장바구니](#3-장바구니)
    - [3.1 장바구니 추가 & 수정](#31-장바구니-추가--수정)


## 1.  회원 API
### 1.1 회원가입
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
### 2.1 상품 등록
- **URL** : `/api/products`
- **Method**: `POST`
- **Request Body**:
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
- **Request Body**:
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

