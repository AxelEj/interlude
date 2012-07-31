DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
	`login` VARCHAR(14) UNIQUE NOT NULL,
	`password` VARCHAR(32) NOT NULL,
	`last_login_date` TIMESTAMP DEFAULT 0,
	`last_login_ip`	VARCHAR(15) DEFAULT '',
	`last_server_id` TINYINT DEFAULT 0,
	`last_logout_date` TIMESTAMP DEFAULT 0
);

DROP TABLE IF EXISTS `account_block`;
CREATE TABLE `account_block` (
	`login` VARCHAR(14) UNIQUE NOT NULL
);

DROP TABLE IF EXISTS `block_ip`;
CREATE TABLE `block_ip` (
	`ip` VARCHAR(15) UNIQUE NOT NULL
);


DROP TABLE IF EXISTS `server`;
CREATE TABLE `server` (
	`id` TINYINT UNIQUE NOT NULL,
	`host` VARCHAR(15) NOT NULL,
	`port` INT NOT NULL
);

DROP FUNCTION IF EXISTS `is_block_account`;
CREATE FUNCTION `is_block_account`($login VARCHAR(14)) RETURNS TINYINT 
BEGIN
	DECLARE $count TINYINT DEFAULT 0;
	
	SELECT COUNT(*) INTO $count FROM `account_block` WHERE `login` = $login;
	
	RETURN $count;
END

DROP FUNCTION IF EXISTS `login`;
CREATE FUNCTION `login` (
	$login VARCHAR(14),
	$password VARCHAR(32),
	$date TIMESTAMP,
	$ip VARCHAR(15)
)
RETURNS TINYINT
BEGIN 
	DECLARE $_login VARCHAR(14) DEFAULT '';
	
	DECLARE $_password VARCHAR(32) DEFAULT '';
	
	SELECT `login`,`password` INTO $_login,$_password FROM `account` WHERE `login` = $login;
	
	IF $login = $_login THEN
		IF is_block_account($login) = 0 THEN
			IF $password = $_password THEN 
				UPDATE `account` SET `last_login_date` = $date,`last_login_ip` = $ip WHERE (`login` = $login);
				
				RETURN 0;-- login success
			ELSE
				RETURN -3;-- wrong password
			END IF;	
		ELSE
			RETURN -2;-- blocked account
		END IF;
	ELSE
		RETURN -1;-- wrong login
	END IF; 
END

DROP PROCEDURE IF EXISTS `login_on_server`;
CREATE PROCEDURE `login_on_server` (
	IN $login VARCHAR(14),
	IN $id TINYINT 
) BEGIN
	UPDATE `account` SET `last_server_id` = $id WHERE (`login` = $login);
END


DROP PROCEDURE IF EXISTS `logout`;
CREATE PROCEDURE `logout` (
	IN $login VARCHAR(14),
	IN $date TIMESTAMP
) BEGIN
	UPDATE `account` SET `last_logout_date` = $date WHERE (`login` = $login);
END
