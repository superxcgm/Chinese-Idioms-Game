CREATE TABLE `idiom` (
  `groupId` int(3) NOT NULL,
  `value` char(10) NOT NULL,
  `description` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
  `username` char(20) NOT NULL,
  `password` char(32) NOT NULL,
  `process` int(3) DEFAULT '0',
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;