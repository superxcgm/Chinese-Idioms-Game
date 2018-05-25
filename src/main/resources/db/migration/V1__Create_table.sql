CREATE TABLE `idiom` (
  `groupId` int(3) NOT NULL,
  `value` char(10) NOT NULL,
  `description` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
  `username` char(20) NOT NULL,
  `password` char(32) NOT NULL,
  `process` int(3) DEFAULT '0',
  `star` varchar(30) NOT NULL COMMENT '30关，每一位代表一关的星数，0表示该关未通过',
  `totalStars` int(11) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;