CREATE USER 'bd2_user'@'localhost' IDENTIFIED BY 'tpbd2';
GRANT CREATE, DROP, ALTER, SELECT, INSERT, DELETE, UPDATE ON bd2_tours_12.* TO 'bd2_user'@'localhost';
