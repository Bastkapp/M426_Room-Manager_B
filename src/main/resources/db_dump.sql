-- -----------------------------------------------------
-- Schema RoomMgmt
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `RoomMgmt` DEFAULT CHARACTER SET utf8;
USE `RoomMgmt`;

-- -----------------------------------------------------
-- Table `RoomMgmt`.`Room`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `RoomMgmt`.`Room`
(
    `roomId`      INT         NOT NULL,
    `name`        VARCHAR(45) NOT NULL,
    `description` TEXT        NULL,
    PRIMARY KEY (`roomId`)
);


-- -----------------------------------------------------
-- Table `RoomMgmt`.`Event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `RoomMgmt`.`Event`
(
    `eventId`     INT         NOT NULL,
    `title`       VARCHAR(45) NOT NULL,
    `description` VARCHAR(45) NULL,
    `organiser`   TEXT        NULL,
    PRIMARY KEY (`eventId`)
);


-- -----------------------------------------------------
-- Table `RoomMgmt`.`Reservation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `RoomMgmt`.`Reservation`
(
    `reservationId` INT      NOT NULL,
    `roomId`        INT      NOT NULL,
    `eventId`       INT      NOT NULL,
    `start`         DATETIME NOT NULL,
    `end`           DATETIME NOT NULL,
    PRIMARY KEY (`reservationId`),
    INDEX `fk_Reservation_Room_idx` (`roomId` ASC),
    INDEX `fk_Reservation_Event1_idx` (`eventId` ASC),
    CONSTRAINT `fk_Reservation_Room`
        FOREIGN KEY (`roomId`)
            REFERENCES `RoomMgmt`.`Room` (`roomId`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_Reservation_Event1`
        FOREIGN KEY (`eventId`)
            REFERENCES `RoomMgmt`.`Event` (`eventId`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);


-- -----------------------------------------------------
-- Table `RoomMgmt`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `RoomMgmt`.`Event`
(
    `username`     INT         NOT NULL,
    `password`       VARCHAR(45) NOT NULL,
    PRIMARY KEY (`username`, 'password')
);

INSERT INTO `RoomMgmt`.`Event` (username, password) VALUES ('admin', 'Password');
