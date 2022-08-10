#  :mag_right: Find-popular-place :eyes:
> Spring Boot, Spring Security practice project

<br>

:lemon: APIs
------------

#### 1. Authentication
```
[POST] api/auth
```
- 사용자 로그인
- API 토큰 필요 X
- response
```
{
    "success": true,
    "response": {
        "apiToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaXNzIjoicGxhY2Vfc2VydmVyIiwibmFtZSI6InRlc3RlcjAxIiwiaWF0IjoxNjYwMTQ1Mjk5LCJ1c2VyS2V5IjoyLCJlbWFpbCI6InRlc3QwMUBnbWFpbC5jb20ifQ.altqZ81J7jyli8SczCs14-is706xibKwETkHn3kjI3PNXOx142FyF8MrVsS6fcPR5E_NGZ0BHQqoElsSs8pDVw",
        "user": {
            "seq": 2,
            "name": "tester01",
            "email": {
                "address": "test01@gmail.com"
            },
            "loginCount": 1,
            "lastLoginAt": "2022-08-11T00:28:19.950313",
            "createAt": "2022-08-11T00:28:01.815803"
        }
    }
}
```

<br>

#### 2. User
```
[GET] api/user/join
```
- 회원가입
- API 토큰 필요 X

```
[GET] api/user/exists
```
- 이메일 중복확인
- API 토큰 필요 X

```
[GET] api/user/me
```
- 내 정보 조회

<br>

#### 3. Post

```
[GET] api/post
```
- 글 작성

```
[GET] api/user/{userId}/post/list
```
- 작성 글 조회
- user id example: 1, 2 ... (data.sql 참고)


<br>

#### 4. Store

```
[GET] api/store/reg
```
- 가게 등록


<br>

:lemon: Dependencies
------------

- All code is written in Java 17.
- Spring Boot 2.7.1
- h2 database
- Swagger2

<br>
