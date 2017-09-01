SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Uncomment to drop all
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `mindlevel` ;
DROP TABLE IF EXISTS `mindlevel`.`user` ;
DROP TABLE IF EXISTS `mindlevel`.`session` ;
DROP TABLE IF EXISTS `mindlevel`.`mission` ;
DROP TABLE IF EXISTS `mindlevel`.`accomplishment` ;
DROP TABLE IF EXISTS `mindlevel`.`user_accomplishment` ;
DROP TABLE IF EXISTS `mindlevel`.`accomplishment_like` ;

-- -----------------------------------------------------
-- Schema mindlevel
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mindlevel` DEFAULT CHARACTER SET utf8 ;
USE `mindlevel` ;

-- -----------------------------------------------------
-- Table `mindlevel`.`user`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `mindlevel`.`user` (
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `description` VARCHAR(1024) NULL,
  `image` VARCHAR(255) NULL,
  `score` INT NOT NULL,
  `created` BIGINT NOT NULL,
  `last_active` BIGINT,
  PRIMARY KEY (`username`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mindlevel`.`session`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `mindlevel`.`session` (
  `username` VARCHAR(255) NOT NULL,
  `session` VARCHAR(255),
  PRIMARY KEY (`username`),
  INDEX `fk_session_user_idx` (`username` ASC),
  CONSTRAINT `fk_session_user`
    FOREIGN KEY (`username`)
    REFERENCES `mindlevel`.`user` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mindlevel`.`mission`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `mindlevel`.`mission` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL,
  `description` VARCHAR(1024) NOT NULL,
  `image` VARCHAR(255) NOT NULL,
  `created` BIGINT NOT NULL,
  `creator` VARCHAR(255) NOT NULL,
  `validated` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_mission_user_idx` (`creator` ASC),
  CONSTRAINT `fk_mission_user`
    FOREIGN KEY (`creator`)
    REFERENCES `mindlevel`.`user` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mindlevel`.`accomplishment`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `mindlevel`.`accomplishment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL,
  `description` VARCHAR(1024) NOT NULL,
  `image` VARCHAR(255) NOT NULL,
  `mission_id` INT NOT NULL,
  `score` INT NOT NULL DEFAULT 0,
  `created` BIGINT,
  PRIMARY KEY (`id`),
  INDEX `fk_accomplishment_mission_idx` (`mission_id` ASC),
  CONSTRAINT `fk_accomplishment_mission`
    FOREIGN KEY (`mission_id`)
    REFERENCES `mindlevel`.`mission` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mindlevel`.`user_accomplishment`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `mindlevel`.`user_accomplishment` (
  `username` VARCHAR(255) NOT NULL,
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
  `username` VARCHAR(255) NOT NULL,
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

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
