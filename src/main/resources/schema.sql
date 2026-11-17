CREATE TABLE member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO member (name, email, password, role) VALUES ('어드민', 'admin@email.com', 'password', 'ADMIN');
INSERT INTO member (name, email, password, role) VALUES ('브라운', 'brown@email.com', 'password', 'USER');