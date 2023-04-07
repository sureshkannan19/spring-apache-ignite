CREATE TABLE IF NOT EXISTS `NOTIFICATION`
(
   `FEED_ID` int PRIMARY KEY auto_increment,
   `HEADLINE` varchar (3000) DEFAULT NULL,
   `CONTENT` varchar (3000) DEFAULT NULL,
   `LAST_EDITED_DATE` date DEFAULT NULL
);

create table IF NOT EXISTS `USERS` (
	`id` int primary key auto_increment,
	`first_name` VARCHAR(50),
	`last_name` VARCHAR(50),
	`email` VARCHAR(50),
	`gender` VARCHAR(50),
	`date_of_birth` DATE,
	`last_login_ts` DATE
);