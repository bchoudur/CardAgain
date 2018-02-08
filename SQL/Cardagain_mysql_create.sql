CREATE TABLE `Users` (
	`userid` INT NOT NULL AUTO_INCREMENT,
	`username` VARCHAR(32) NOT NULL,
	`GamesWon` INT NOT NULL,
	`Games Played` INT NOT NULL,
	PRIMARY KEY (`userid`)
);

CREATE TABLE `UPass` (
	`userid` INT NOT NULL AUTO_INCREMENT,
	`pass` VARCHAR(512) NOT NULL,
	PRIMARY KEY (`userid`)
);

CREATE TABLE `Friends` (
	`userid1` INT NOT NULL,
	`userid2` INT NOT NULL
);


ALTER TABLE `UPass` ADD CONSTRAINT `UPass_fk0` FOREIGN KEY (`userid`) REFERENCES `Users`(`userid`);

ALTER TABLE `Friends` ADD CONSTRAINT `Friends_fk0` FOREIGN KEY (`userid1`) REFERENCES `Users`(`userid`);

ALTER TABLE `Friends` ADD CONSTRAINT `Friends_fk1` FOREIGN KEY (`userid2`) REFERENCES `Users`(`userid`);


