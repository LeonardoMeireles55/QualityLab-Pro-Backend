CREATE TABLE analytics (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(10),
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    normal_value DOUBLE,
    high_value DOUBLE,
    normal_valid VARCHAR(8),
    high_valid VARCHAR(8),
    normal_obs VARCHAR(8),
    high_obs VARCHAR(8),
    PRIMARY KEY (id)
);