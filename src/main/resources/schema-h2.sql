DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users
(
    id            bigint      NOT NULL AUTO_INCREMENT, --사용자 PK
    name          varchar(20) NOT NULL,                --사용자명
    age           int         NOT NULL,                --사용자 나이
    PRIMARY KEY (id)
);