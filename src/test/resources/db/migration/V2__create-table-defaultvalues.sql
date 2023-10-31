CREATE TABLE default_values (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(20),
    normal_mean DOUBLE,
    normal_dp DOUBLE,
    high_mean DOUBLE,
    high_dp DOUBLE,
    normal_max_value DOUBLE,
    high_max_value DOUBLE,
    PRIMARY KEY (id),
    UNIQUE (name)
);
