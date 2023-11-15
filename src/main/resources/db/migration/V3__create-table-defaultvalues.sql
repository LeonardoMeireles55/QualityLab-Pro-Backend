CREATE TABLE default_values (
    id BIGINT NOT NULL AUTO_INCREMENT,
    fk_lot BIGINT NOT NULL,
    fk_user BIGINT NOT NULL,
    enable BOOLEAN DEFAULT TRUE,
    name VARCHAR(25),
    normal_mean DOUBLE,
    normal_sd DOUBLE,
    high_mean DOUBLE,
    high_sd DOUBLE,
    normal_max_value DOUBLE,
    high_max_value DOUBLE,
    PRIMARY KEY (id),
    CONSTRAINT default_values_lot FOREIGN KEY (fk_lot) REFERENCES lot(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT default_values_users FOREIGN KEY (fk_user) REFERENCES users(id),
    UNIQUE (name)
);
