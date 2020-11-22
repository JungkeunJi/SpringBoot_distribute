-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`distribution`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`distribution` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `room_id` VARCHAR(45) NOT NULL,
  `total_money` BIGINT(20) NOT NULL,
  `total_distribute_num` INT NOT NULL,
  `token` CHAR(3) NOT NULL,
  `registered_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `token_UNIQUE` (`token` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`distribution_state`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`distribution_state` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `token` CHAR(3) NOT NULL,
  `allocated_money` BIGINT(20) NOT NULL,
  `allocated_status` VARCHAR(20) NOT NULL,
  `allocated_user_id` INT NULL,
  `distribution_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_distribution_state_distribution_idx` (`distribution_id` ASC) VISIBLE,
  CONSTRAINT `fk_distribution_state_distribution`
    FOREIGN KEY (`distribution_id`)
    REFERENCES `mydb`.`distribution` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
