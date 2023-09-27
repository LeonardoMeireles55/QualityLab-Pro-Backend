CREATE TABLE Analitos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(10),
    data VARCHAR(100),
    value_normal DOUBLE,
    value_high DOUBLE,
    valid_normal VARCHAR(10),
    valid_high VARCHAR(10),
    obs_normal VARCHAR(10),
    obs_high VARCHAR(10),
    PRIMARY KEY (id)
);
