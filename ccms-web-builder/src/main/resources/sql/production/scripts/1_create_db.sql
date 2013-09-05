###-----------------------------------------------------------------------------------------------------------------------------
###  1_create_db.sql
###-----------------------------------------------------------------------------------------------------------------------------
--
-- Definition for database ccms
--
-- DROP DATABASE IF EXISTS ccms;
-- CREATE DATABASE IF NOT EXISTS ccms
-- CHARACTER SET utf8
-- COLLATE utf8_bin;

-- CREATE USER
-- FLUSH PRIVILEGES;
-- CREATE USER ccms IDENTIFIED BY 'ccms';
GRANT ALL PRIVILEGES ON *.* TO ccms@'%'  IDENTIFIED BY 'ccms' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON *.* TO ccms@'localhost' IDENTIFIED BY 'ccms' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON *.* TO ccms@'127.0.0.1'  IDENTIFIED BY 'ccms' WITH GRANT OPTION;
-- FLUSH PRIVILEGES;

--
-- Set default database
--
-- USE ccms;
