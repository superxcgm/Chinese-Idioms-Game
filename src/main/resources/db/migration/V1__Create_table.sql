CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` char(20) NOT NULL,
  `password` char(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `level` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `totalTime` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `stage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `levelId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `levelId` (`levelId`),
  CONSTRAINT `stage_ibfk_1` FOREIGN KEY (`levelId`) REFERENCES `level` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `idiom` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `stageId` int(11) NOT NULL,
  `value` char(10) NOT NULL,
  `description` text NOT NULL,
  PRIMARY KEY (`id`),
  KEY `stageId` (`stageId`),
  CONSTRAINT `idiom_ibfk_1` FOREIGN KEY (`stageId`) REFERENCES `stage` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `clearStage` (
  `userId` int(11) NOT NULL,
  `stageId` int(11) NOT NULL,
  `starCount` int(11) NOT NULL,
  KEY `userId` (`userId`),
  KEY `stageId` (`stageId`),
  CONSTRAINT `clearstage_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`id`),
  CONSTRAINT `clearstage_ibfk_2` FOREIGN KEY (`stageId`) REFERENCES `stage` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;