DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
	`login` VARCHAR(16) UNIQUE NOT NULL,
	`password` VARCHAR(32) NOT NULL,
	`login_date` BIGINT,
	`login_ip` VARCHAR(15),
	`logout_date` BIGINT
);

DROP TABLE IF EXISTS `account_block`;
CREATE TABLE `account_block` (
	`login` VARCHAR(16) UNIQUE NOT NULL,
	`block_date` BIGINT,
	`unblock_date` BIGINT
);

DROP TABLE IF EXISTS `account_block_history`; 
CREATE TABLE `account_block_history` (
	`login` VARCHAR(16),
	`block_date` BIGINT,
	`unblock_date` BIGINT
);

DROP PROCEDURE IF EXISTS `login`;
CREATE PROCEDURE `login` (
	$login VARCHAR(16),
	$login_date BIGINT,
	$login_ip VARCHAR(15))
BEGIN
	UPDATE `account` SET `login_date` = $login_date, `login_ip` = $login_ip WHERE (`login` = $login);
END

DROP PROCEDURE IF EXISTS `logout`;
CREATE PROCEDURE `logout` (
	$login VARCHAR(16),
	$logout_date BIGINT)
BEGIN
	UPDATE `account` SET `logout_date` = $logout_date WHERE (`login` = $login);
END

DROP PROCEDURE IF EXISTS `block_account`;
CREATE PROCEDURE `block_account` (
	$login VARCHAR(16),
	$block_date BIGINT,
	$unblock_date BIGINT)
BEGIN
	INSERT INTO `account_block` VALUES ($login,$block_date,$unblock_date);
END

DROP PROCEDURE IF EXISTS `unblock_account`;
CREATE PROCEDURE unblock_account (
	$login VARCHAR(16))
BEGIN
	DECLARE $block_date TIMESTAMP;
	
	DECLARE $unblock_date TIMESTAMP;
	
	SELECT `block_date`,`unblock_date` INTO $block_date,$unblock_date FROM `account_block` WHERE `login` = $login;
	
	DELETE FROM `account_block` WHERE (`login` = $login);
	
	INSERT INTO `account_block_history` VALUES ($login, $block_date, $unblock_date);
END
	