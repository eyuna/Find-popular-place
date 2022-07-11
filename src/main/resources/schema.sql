DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS stores CASCADE;

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
