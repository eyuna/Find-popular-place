DROP TABLE IF EXISTS connections CASCADE;
DROP TABLE IF EXISTS posts CASCADE;
DROP TABLE IF EXISTS stores CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- 사용자 데이터
CREATE TABLE users
(
    seq                 bigint      NOT NULL AUTO_INCREMENT,              -- 사용자 PK
    name                varchar(10) NOT NULL,                             -- 이름
    email               varchar(50) NOT NULL,                             -- 로그인 이메일
    profile_image_url   varchar(255)         DEFAULT NULL,
    passwd              varchar(80) NOT NULL,                             -- 비밀번호
    login_count         int         NOT NULL DEFAULT 0,                   -- 로그인횟수
    last_login_at       datetime             DEFAULT NULL,                -- 마지막 로그인일시
    create_at           datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP(), -- 생성일시
    PRIMARY KEY (seq),
    CONSTRAINT unq_user_email UNIQUE (email)
);

CREATE TABLE stores
(
    seq                 bigint          NOT NULL AUTO_INCREMENT,
    name                varchar(50)     NOT NULL,
    zip_code            varchar(10)     NOT NULL,
    address             varchar(500)    NOT NULL,
    lat                 double,
    lng                 double,
    likes               int                     DEFAULT 0,
    PRIMARY KEY (seq),
    CONSTRAINT unq_stores UNIQUE (name, zip_code)
);

CREATE TABLE posts
(
    seq           bigint       NOT NULL AUTO_INCREMENT,              -- POST PK
    store_seq     bigint       NOT NULL,                             -- STORE PK
    user_seq      bigint       NOT NULL,                             -- POST 작성자 PK
    contents      varchar(500) NOT NULL,                             -- 내용
    like_count    int          NOT NULL DEFAULT 0,                   -- 좋아요수
    comment_count int          NOT NULL DEFAULT 0,                   -- 댓글수
    create_at     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP(), -- 생성일시
    PRIMARY KEY (seq),
    CONSTRAINT fk_post_to_user FOREIGN KEY (user_seq) REFERENCES users (seq) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_post_to_store FOREIGN KEY (store_seq) REFERENCES stores (seq) ON DELETE RESTRICT ON UPDATE RESTRICT
);


-- 사용자들의 친구 관계 데이터이다.
-- 해당 테이블의 Row 데이터는 `user_seq`, `target_seq`가 친구 관계임을 의미한다.
-- 단, `user_seq` -> `target_seq` 방향으로만 친구관계가 되며, 역방향 `target_seq` -> `user_seq`은 친구가 아니다.
-- ("`target_seq`는 `user_seq`의 친구이다"는 성립하지만, "`user_seq`는 `target_seq`의 친구이다"는 성립하지 않는다.)
CREATE TABLE connections
(
    seq        bigint   NOT NULL AUTO_INCREMENT,              -- 친구관계 PK
    user_seq   bigint   NOT NULL,                             -- 사용자 PK
    target_seq bigint   NOT NULL,                             -- 친구 사용자 PK
    granted_at datetime          DEFAULT NULL,                -- 친구수락일시
    create_at  datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(), -- 생성일시
    PRIMARY KEY (seq),
    CONSTRAINT fk_connection_to_user FOREIGN KEY (user_seq) REFERENCES users (seq) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_connection_to_user2 FOREIGN KEY (target_seq) REFERENCES users (seq) ON DELETE RESTRICT ON UPDATE RESTRICT
);