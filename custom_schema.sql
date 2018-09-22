SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Uncomment to drop all
-- -----------------------------------------------------
-- DROP SCHEMA IF EXISTS `custom` ;
-- DROP TABLE IF EXISTS `custom`.`user` ;
-- DROP TABLE IF EXISTS `custom`.`session` ;
-- DROP TABLE IF EXISTS `custom`.`challenge` ;
-- DROP TABLE IF EXISTS `custom`.`accomplishment` ;
-- DROP TABLE IF EXISTS `custom`.`user_accomplishment` ;
-- DROP TABLE IF EXISTS `custom`.`accomplishment_like` ;

-- -----------------------------------------------------
-- Schema custom
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `custom` DEFAULT CHARACTER SET utf8mb4 ;
USE `custom` ;


-- -----------------------------------------------------
-- Table `custom`.`user`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `custom`.`user` (
  `username` VARCHAR(191) NOT NULL,
  `description` VARCHAR(1024) NULL,
  `image` VARCHAR(191) NULL DEFAULT 'user.jpg',
  `score` INT NOT NULL,
  `created` BIGINT NOT NULL,
  `last_active` BIGINT,
  PRIMARY KEY (`username`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `custom`.`user_extra`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `custom`.`user_extra` (
  `username` VARCHAR(191) NOT NULL,
  `password` VARCHAR(191) NOT NULL,
  `email` VARCHAR(191) NOT NULL,
  PRIMARY KEY (`username`),
  INDEX `fk_user_extra_idx` (`username` ASC),
  CONSTRAINT `fk_user_extra`
    FOREIGN KEY (`username`)
    REFERENCES `custom`.`user` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `custom`.`session`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `custom`.`session` (
  `username` VARCHAR(191) NOT NULL,
  `session` VARCHAR(191),
  PRIMARY KEY (`username`),
  INDEX `fk_session_user_idx` (`username` ASC),
  CONSTRAINT `fk_session_user`
    FOREIGN KEY (`username`)
    REFERENCES `custom`.`user` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `custom`.`challenge`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `custom`.`challenge` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(191) NOT NULL,
  `description` VARCHAR(1024) NOT NULL,
  `image` VARCHAR(191) NOT NULL,
  `created` BIGINT NOT NULL DEFAULT 0,
  `creator` VARCHAR(191) NOT NULL,
  `validated` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_challenge_user_idx` (`creator` ASC),
  CONSTRAINT `fk_challenge_user`
    FOREIGN KEY (`creator`)
    REFERENCES `custom`.`user` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `custom`.`category`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `custom`.`category` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(191) NOT NULL,
  `image` VARCHAR(191) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `custom`.`challenge_category`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `custom`.`challenge_category` (
  `challenge_id` INT NOT NULL,
  `category_id` INT NOT NULL,
  PRIMARY KEY (`challenge_id`, `category_id`),
  INDEX `fk_challenge_category_idx` (`category_id` ASC),
  CONSTRAINT `fk_challenge_category_1`
    FOREIGN KEY (`challenge_id`)
    REFERENCES `custom`.`challenge` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_challenge_category_2`
    FOREIGN KEY (`category_id`)
    REFERENCES `custom`.`category` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `custom`.`accomplishment`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `custom`.`accomplishment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(191) NOT NULL,
  `description` VARCHAR(1024) NOT NULL,
  `image` VARCHAR(191) NOT NULL,
  `challenge_id` INT NOT NULL,
  `score` INT NOT NULL DEFAULT 0,
  `created` BIGINT,
  PRIMARY KEY (`id`),
  INDEX `fk_accomplishment_challenge_idx` (`challenge_id` ASC),
  CONSTRAINT `fk_accomplishment_challenge`
    FOREIGN KEY (`challenge_id`)
    REFERENCES `custom`.`challenge` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `custom`.`user_accomplishment`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `custom`.`user_accomplishment` (
  `username` VARCHAR(191) NOT NULL,
  `accomplishment_id` INT NOT NULL,
  PRIMARY KEY (`username`, `accomplishment_id`),
  INDEX `fk_user_accomplishment_2_idx` (`accomplishment_id` ASC),
  CONSTRAINT `fk_user_accomplishment_1`
    FOREIGN KEY (`username`)
    REFERENCES `custom`.`user` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_accomplishment_2`
    FOREIGN KEY (`accomplishment_id`)
    REFERENCES `custom`.`accomplishment` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `custom`.`accomplishment_like`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `custom`.`accomplishment_like` (
  `username` VARCHAR(191) NOT NULL,
  `accomplishment_id` INT NOT NULL,
  `created` BIGINT NOT NULL,
  `score` INT NOT NULL,
  PRIMARY KEY (`username`, `accomplishment_id`),
  INDEX `fk_accomplishment_like_2_idx` (`accomplishment_id` ASC),
  CONSTRAINT `fk_accomplishment_like_1`
    FOREIGN KEY (`username`)
    REFERENCES `custom`.`user` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_accomplishment_like_2`
    FOREIGN KEY (`accomplishment_id`)
    REFERENCES `custom`.`accomplishment` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `custom`.`custom_db`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `custom`.`custom_db` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `pass` VARCHAR(191) NOT NULL,
  `db` VARCHAR(191) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
