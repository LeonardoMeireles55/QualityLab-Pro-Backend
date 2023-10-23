CREATE TABLE analytics (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(20),
    data VARCHAR(25),
    normal_value DOUBLE,
    high_value DOUBLE,
    normal_valid VARCHAR(10),
    high_valid VARCHAR(10),
    normal_obs VARCHAR(10),
    high_obs VARCHAR(10),
    PRIMARY KEY (id),
    UNIQUE (name)
);
