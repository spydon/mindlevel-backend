SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Uncomment to drop all
-- -----------------------------------------------------
-- DROP SCHEMA IF EXISTS `mindlevel` ;
-- DROP TABLE IF EXISTS `mindlevel`.`user` ;
-- DROP TABLE IF EXISTS `mindlevel`.`session` ;
-- DROP TABLE IF EXISTS `mindlevel`.`challenge` ;
-- DROP TABLE IF EXISTS `mindlevel`.`accomplishment` ;
-- DROP TABLE IF EXISTS `mindlevel`.`user_accomplishment` ;
-- DROP TABLE IF EXISTS `mindlevel`.`accomplishment_like` ;

-- -----------------------------------------------------
-- Schema mindlevel
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mindlevel` DEFAULT CHARACTER SET utf8mb4 ;
USE `mindlevel` ;


-- -----------------------------------------------------
-- Table `mindlevel`.`user`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `mindlevel`.`user` (
  `username` VARCHAR(191) NOT NULL,
  `description` VARCHAR(1024) NULL,
  `image` VARCHAR(191) NULL DEFAULT 'user.jpg',
  `score` INT DEFAULT '0',
  `level` INT DEFAULT '0',
  `created` BIGINT NOT NULL,
  `last_active` BIGINT,
  PRIMARY KEY (`username`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mindlevel`.`user_extra`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `mindlevel`.`user_extra` (
  `username` VARCHAR(191) NOT NULL,
  `password` VARCHAR(191) NOT NULL,
  `email` VARCHAR(191) NOT NULL,
  PRIMARY KEY (`username`),
  INDEX `fk_user_extra_idx` (`username` ASC),
  CONSTRAINT `fk_user_extra`
    FOREIGN KEY (`username`)
    REFERENCES `mindlevel`.`user` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mindlevel`.`session`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `mindlevel`.`session` (
  `username` VARCHAR(191) NOT NULL,
  `session` VARCHAR(191),
  PRIMARY KEY (`username`),
  INDEX `fk_session_user_idx` (`username` ASC),
  CONSTRAINT `fk_session_user`
    FOREIGN KEY (`username`)
    REFERENCES `mindlevel`.`user` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mindlevel`.`challenge`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `mindlevel`.`challenge` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(191) NOT NULL,
  `description` VARCHAR(1024) NOT NULL,
  `image` VARCHAR(191) NOT NULL,
  `created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `creator` VARCHAR(191) NOT NULL,
  `validated` TINYINT(1) NOT NULL,
  `level_restriction` INT DEFAULT 0,
  `score_restriction` INT DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `fk_challenge_user_idx` (`creator` ASC),
  CONSTRAINT `fk_challenge_user`
    FOREIGN KEY (`creator`)
    REFERENCES `mindlevel`.`user` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mindlevel`.`category`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `mindlevel`.`category` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(191) NOT NULL,
  `image` VARCHAR(191) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mindlevel`.`challenge_category`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `mindlevel`.`challenge_category` (
  `challenge_id` INT NOT NULL,
  `category_id` INT NOT NULL,
  PRIMARY KEY (`challenge_id`, `category_id`),
  INDEX `fk_challenge_category_idx` (`category_id` ASC),
  CONSTRAINT `fk_challenge_category_1`
    FOREIGN KEY (`challenge_id`)
    REFERENCES `mindlevel`.`challenge` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_challenge_category_2`
    FOREIGN KEY (`category_id`)
    REFERENCES `mindlevel`.`category` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mindlevel`.`accomplishment`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `mindlevel`.`accomplishment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(191) NOT NULL,
  `description` VARCHAR(1024) NOT NULL,
  `image` VARCHAR(191) NOT NULL,
  `challenge_id` INT NOT NULL,
  `score` INT NOT NULL DEFAULT 0,
  `created` BIGINT,
  `level_restriction` INT NOT NULL DEFAULT 0,
  `score_restriction` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `fk_accomplishment_challenge_idx` (`challenge_id` ASC),
  CONSTRAINT `fk_accomplishment_challenge`
    FOREIGN KEY (`challenge_id`)
    REFERENCES `mindlevel`.`challenge` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mindlevel`.`user_accomplishment`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `mindlevel`.`user_accomplishment` (
  `username` VARCHAR(191) NOT NULL,
  `accomplishment_id` INT NOT NULL,
  PRIMARY KEY (`username`, `accomplishment_id`),
  INDEX `fk_user_accomplishment_2_idx` (`accomplishment_id` ASC),
  CONSTRAINT `fk_user_accomplishment_1`
    FOREIGN KEY (`username`)
    REFERENCES `mindlevel`.`user` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_accomplishment_2`
    FOREIGN KEY (`accomplishment_id`)
    REFERENCES `mindlevel`.`accomplishment` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mindlevel`.`accomplishment_like`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `mindlevel`.`accomplishment_like` (
  `username` VARCHAR(191) NOT NULL,
  `accomplishment_id` INT NOT NULL,
  `created` BIGINT NOT NULL,
  `score` INT NOT NULL,
  PRIMARY KEY (`username`, `accomplishment_id`),
  INDEX `fk_accomplishment_like_2_idx` (`accomplishment_id` ASC),
  CONSTRAINT `fk_accomplishment_like_1`
    FOREIGN KEY (`username`)
    REFERENCES `mindlevel`.`user` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_accomplishment_like_2`
    FOREIGN KEY (`accomplishment_id`)
    REFERENCES `mindlevel`.`accomplishment` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mindlevel`.`notification`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `mindlevel`.`notification` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(191) NOT NULL,
  `description` VARCHAR(1024) NOT NULL,
  `image` VARCHAR(191) NOT NULL,
  `created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `priority` INT DEFAULT 0,
  `target_id` INT DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `fk_notification_target_idx` (`target_id` ASC),
  CONSTRAINT `fk_notification_target`
    FOREIGN KEY (`id`)
    REFERENCES `mindlevel`.`notification_target` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mindlevel`.`notification_user`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `mindlevel`.`notification_user` (
  `notification_id` INT NOT NULL,
  `username` VARCHAR(191) NOT NULL,
  `seen` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`notification_id`, `username`),
  INDEX `fk_notification_user_idx` (`username` ASC),
  CONSTRAINT `fk_notification_user_1`
    FOREIGN KEY (`notification_id`)
    REFERENCES `mindlevel`.`notification` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_notification_user_2`
    FOREIGN KEY (`username`)
    REFERENCES `mindlevel`.`user` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mindlevel`.`notification_target`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `mindlevel`.`notification_target` (
  `id` INT NOT NULL,
  `name` VARCHAR(191) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mindlevel`.`custom_db`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `mindlevel`.`custom_db` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `pass` VARCHAR(191) NOT NULL,
  `db` VARCHAR(191) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
