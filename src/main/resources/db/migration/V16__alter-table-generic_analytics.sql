UPDATE generic_analytics
SET date = STR_TO_DATE(date, '%Y-%m-%d %H:%i:%s')
WHERE date IS NOT NULL;


ALTER TABLE generic_analytics 
MODIFY COLUMN date DATETIME;
