
INSERT INTO users (id, name, balance) VALUES (null, 'PARK', 1000);
INSERT INTO users (id, name, balance) VALUES (null, 'KIM', 1000);
INSERT INTO users (id, name, balance) VALUES (null, 'KANG', 3000);

INSERT INTO bank_statements (id, from_user_id, to_user_id, amount)
VALUES (null, 2, 3, 2000);