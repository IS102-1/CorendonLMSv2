/*
*	Vergeet niet de constants in de DbManager class aan te passen.
*	Sla daar je MySQL username en password op, net als je database naam (default: LMS_DB)
*/

CREATE TABLE customers(
	customer_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	address VARCHAR(75) NOT NULL,
    country VARCHAR(50),
	email_address VARCHAR(50),
	phone_number VARCHAR(25) NOT NULL UNIQUE
);

CREATE TABLE logs(
	log_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	username VARCHAR(15) NOT NULL,
	user_role VARCHAR(20) NOT NULL,
	date_time VARCHAR(25) NOT NULL,
	log_message VARCHAR(100) NOT NULL
);

CREATE TABLE luggage(
	luggage_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    brand_name VARCHAR(20),
	color VARCHAR(25),
	customer_id VARCHAR(11) NOT NULL,
	details VARCHAR(100),
    location VARCHAR(75),
    status VARCHAR(25),
    date_missing VARCHAR(25)
);

CREATE TABLE users(
	user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
	username VARCHAR(15) NOT NULL UNIQUE,
	password VARCHAR(25) NOT NULL,
	user_role VARCHAR(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8