-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema 111
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema 111
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `111` DEFAULT CHARACTER SET utf8 ;
-- -----------------------------------------------------
-- Schema tracker_database
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema tracker_database
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `tracker_database` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ;
USE `111` ;

-- -----------------------------------------------------
-- Table `111`.`real_name1`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `111`.`real_name1` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date_heure` DATETIME NOT NULL,
  `statut` VARCHAR(20) NOT NULL,
  `score_equipe1` INT NULL,
  `score_equipe2` INT NULL,
  `temps_reglementaire` INT NULL,
  `prolongations` TINYINT NULL,
  `MATCHcol` TINYINT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '			';


-- -----------------------------------------------------
-- Table `111`.`real_name2`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `111`.`real_name2` (
  `id` INT NOT NULL,
  `col` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

USE `tracker_database` ;

-- -----------------------------------------------------
-- Table `tracker_database`.`groupe`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tracker_database`.`groupe` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `lettre` VARCHAR(1) NOT NULL,
  `nom` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `tracker_database`.`equipe`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tracker_database`.`equipe` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `code_pays` VARCHAR(3) NOT NULL,
  `confederation` VARCHAR(50) NULL DEFAULT NULL,
  `drapeau_url` VARCHAR(255) NULL DEFAULT NULL,
  `fifa_ranking` INT NULL DEFAULT NULL,
  `groupe_code` VARCHAR(10) NULL DEFAULT NULL,
  `nom` VARCHAR(100) NOT NULL,
  `groupe_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_equipe_code_pays` (`code_pays` ASC) VISIBLE,
  INDEX `FKhmbensvry4xt47na2wrepeqsw` (`groupe_id` ASC) VISIBLE,
  CONSTRAINT `FKhmbensvry4xt47na2wrepeqsw`
    FOREIGN KEY (`groupe_id`)
    REFERENCES `tracker_database`.`groupe` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `tracker_database`.`classement_groupe`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tracker_database`.`classement_groupe` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `buts_contre` INT NULL DEFAULT NULL,
  `buts_pour` INT NULL DEFAULT NULL,
  `defaites` INT NULL DEFAULT NULL,
  `difference_buts` INT NULL DEFAULT NULL,
  `matchs_joues` INT NULL DEFAULT NULL,
  `nuls` INT NULL DEFAULT NULL,
  `points` INT NULL DEFAULT NULL,
  `position` INT NULL DEFAULT NULL,
  `victoires` INT NULL DEFAULT NULL,
  `equipe_id` BIGINT NOT NULL,
  `groupe_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_classement_groupe_equipe` (`groupe_id` ASC, `equipe_id` ASC) VISIBLE,
  INDEX `FK7brcqvkcn3o54f3lvwj3xm471` (`equipe_id` ASC) VISIBLE,
  CONSTRAINT `FK7brcqvkcn3o54f3lvwj3xm471`
    FOREIGN KEY (`equipe_id`)
    REFERENCES `tracker_database`.`equipe` (`id`),
  CONSTRAINT `FKov1hs4b2u0c66v5qf18f79uwp`
    FOREIGN KEY (`groupe_id`)
    REFERENCES `tracker_database`.`groupe` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `tracker_database`.`phase_competition`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tracker_database`.`phase_competition` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `date_debut` DATE NULL DEFAULT NULL,
  `date_fin` DATE NULL DEFAULT NULL,
  `description` TEXT NULL DEFAULT NULL,
  `nom` ENUM('DEMI_FINALES', 'FINALE', 'HUITIEMES_FINALE', 'PETITE_FINALE', 'PHASE_GROUPES', 'QUARTS_FINALE', 'SEIZIEMES_FINALE') NOT NULL,
  `nombre_matchs` INT NULL DEFAULT NULL,
  `ordre` INT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `tracker_database`.`stade`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tracker_database`.`stade` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `capacite` INT NULL DEFAULT NULL,
  `latitude` DECIMAL(10,8) NULL DEFAULT NULL,
  `longitude` DECIMAL(11,8) NULL DEFAULT NULL,
  `nom` VARCHAR(150) NOT NULL,
  `pays` VARCHAR(100) NULL DEFAULT NULL,
  `timezone` VARCHAR(50) NULL DEFAULT NULL,
  `ville` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `tracker_database`.`matches`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tracker_database`.`matches` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `date_heure` DATETIME(6) NOT NULL,
  `groupe` VARCHAR(10) NULL DEFAULT NULL,
  `prolongations` BIT(1) NULL DEFAULT NULL,
  `score_equipe1` INT NULL DEFAULT NULL,
  `score_equipe2` INT NULL DEFAULT NULL,
  `statut` ENUM('ANNULE', 'A_VENIR', 'EN_COURS', 'REPORTE', 'TERMINE') NOT NULL,
  `temps_reglementaire` INT NULL DEFAULT NULL,
  `tirs_au_but` BIT(1) NULL DEFAULT NULL,
  `equipe1_id` BIGINT NOT NULL,
  `equipe2_id` BIGINT NOT NULL,
  `phase_id` BIGINT NOT NULL,
  `stade_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_match_phase` (`phase_id` ASC) VISIBLE,
  INDEX `idx_match_date_heure` (`date_heure` ASC) VISIBLE,
  INDEX `idx_match_statut` (`statut` ASC) VISIBLE,
  INDEX `FK36v3iry21o90crrgg1t62ekf1` (`equipe1_id` ASC) VISIBLE,
  INDEX `FK7gf3dprsv1rjhvqhr0ok4yarf` (`equipe2_id` ASC) VISIBLE,
  INDEX `FKnuom06b8o119qk4opler1p7e6` (`stade_id` ASC) VISIBLE,
  CONSTRAINT `FK1wh73vu7kjd0d8aur91uoyp6k`
    FOREIGN KEY (`phase_id`)
    REFERENCES `tracker_database`.`phase_competition` (`id`),
  CONSTRAINT `FK36v3iry21o90crrgg1t62ekf1`
    FOREIGN KEY (`equipe1_id`)
    REFERENCES `tracker_database`.`equipe` (`id`),
  CONSTRAINT `FK7gf3dprsv1rjhvqhr0ok4yarf`
    FOREIGN KEY (`equipe2_id`)
    REFERENCES `tracker_database`.`equipe` (`id`),
  CONSTRAINT `FKnuom06b8o119qk4opler1p7e6`
    FOREIGN KEY (`stade_id`)
    REFERENCES `tracker_database`.`stade` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `tracker_database`.`evenement_match`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tracker_database`.`evenement_match` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `description` TEXT NULL DEFAULT NULL,
  `joueur_nom` VARCHAR(100) NULL DEFAULT NULL,
  `joueur_numero` INT NULL DEFAULT NULL,
  `minute` INT NULL DEFAULT NULL,
  `minute_additionnelle` INT NULL DEFAULT NULL,
  `type_evenement` VARCHAR(50) NOT NULL,
  `equipe_id` BIGINT NULL DEFAULT NULL,
  `match_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_event_match` (`match_id` ASC) VISIBLE,
  INDEX `idx_event_equipe` (`equipe_id` ASC) VISIBLE,
  CONSTRAINT `FK4wi0hg2bprdqsj7p9tnr8esht`
    FOREIGN KEY (`equipe_id`)
    REFERENCES `tracker_database`.`equipe` (`id`),
  CONSTRAINT `FKrtsjsng9xs3kkf7epnhgcju11`
    FOREIGN KEY (`match_id`)
    REFERENCES `tracker_database`.`matches` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `tracker_database`.`statistique_match`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tracker_database`.`statistique_match` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `cartons_jaunes_equipe1` INT NULL DEFAULT NULL,
  `cartons_jaunes_equipe2` INT NULL DEFAULT NULL,
  `cartons_rouges_equipe1` INT NULL DEFAULT NULL,
  `cartons_rouges_equipe2` INT NULL DEFAULT NULL,
  `corners_equipe1` INT NULL DEFAULT NULL,
  `corners_equipe2` INT NULL DEFAULT NULL,
  `fautes_equipe1` INT NULL DEFAULT NULL,
  `fautes_equipe2` INT NULL DEFAULT NULL,
  `hors_jeu_equipe1` INT NULL DEFAULT NULL,
  `hors_jeu_equipe2` INT NULL DEFAULT NULL,
  `possession_equipe1` DECIMAL(5,2) NULL DEFAULT NULL,
  `possession_equipe2` DECIMAL(5,2) NULL DEFAULT NULL,
  `tirs_cadres_equipe1` INT NULL DEFAULT NULL,
  `tirs_cadres_equipe2` INT NULL DEFAULT NULL,
  `tirs_equipe1` INT NULL DEFAULT NULL,
  `tirs_equipe2` INT NULL DEFAULT NULL,
  `match_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_stat_match` (`match_id` ASC) VISIBLE,
  CONSTRAINT `FK69psru8jvaobrrlnne41jy72i`
    FOREIGN KEY (`match_id`)
    REFERENCES `tracker_database`.`matches` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
