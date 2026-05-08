CREATE TABLE VENUE
(
    `ID`             bigint NOT NULL AUTO_INCREMENT,
    `NAME`           varchar(255) DEFAULT NULL,
    `TOTAL_CAPACITY` bigint       DEFAULT NULL,
    `ADDRESS`        varchar(255) DEFAULT NULL,
    PRIMARY KEY (`ID`)
);


CREATE TABLE EVENT
(
    `ID`                 bigint       NOT NULL AUTO_INCREMENT,
    `NAME`               varchar(255) DEFAULT NULL,
    `TOTAL_CAPACITY`     bigint       DEFAULT NULL,
    `REMAINING_CAPACITY` bigint       DEFAULT NULL,
    `VENUE_ID`           bigint       DEFAULT NULL,
    `EVENT_DATE`         datetime(6)  DEFAULT NULL,
    PRIMARY KEY (`ID`),
    KEY `FK_VENUE` (`VENUE_ID`),
    CONSTRAINT `FK_VENUE` FOREIGN KEY (`VENUE_ID`) REFERENCES `VENUE` (`ID`) ON DELETE SET NULL
);