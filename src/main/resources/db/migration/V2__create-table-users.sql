CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(25) NOT NULL,
    email varchar(25) NOT NULL,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE(username),
    UNIQUE(email),
    INDEX(username, email)
);