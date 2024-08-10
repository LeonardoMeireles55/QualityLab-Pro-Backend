CREATE TABLE hematology (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(15),
    mean DOUBLE,
    sd DOUBLE,
    value DOUBLE,
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    rules VARCHAR(155),
    description VARCHAR(155),
    PRIMARY KEY (id)
);