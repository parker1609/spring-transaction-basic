DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS bank_statements CASCADE;

CREATE TABLE users
(
    id            bigint      NOT NULL AUTO_INCREMENT,  -- 사용자 PK
    name          varchar(20) NOT NULL,                 -- 사용자명
    balance       bigint      NOT NULL,                 -- 사용자 잔액
    PRIMARY KEY (id)
);

CREATE TABLE bank_statements
(
    id               bigint      NOT NULL AUTO_INCREMENT,   -- 거래 내역서 PK
    from_user_id     bigint      NOT NULL,                  -- 출금한 사용자
    to_user_id       bigint      NOT NULL,                  -- 입금된 사용자
    amount           bigint      NOT NULL,                  -- 거래 금액
    PRIMARY KEY (id)
);