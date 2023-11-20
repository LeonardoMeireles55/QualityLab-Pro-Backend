CREATE TABLE integra_400 (
                           id BIGINT NOT NULL AUTO_INCREMENT,
                           PRIMARY KEY (id),
                           date DATETIME,
                           level_lot VARCHAR(25),
                           test_lot VARCHAR(25),
                           name VARCHAR(20),
                           level VARCHAR(20),
                           value DOUBLE(10, 2),
                           mean DOUBLE(10,2),
                           sd DOUBLE(10,2),
                           unit_value VARCHAR(10),
                           rules VARCHAR(10),
                           description VARCHAR(10)
#                           validated_by VARCHAR(10),
#                           user_description VARCHAR(50)
);