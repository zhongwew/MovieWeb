CREATE DATABASE IF NOT EXISTS moviedb;

use moviedb

-- CREATE TABLE MOVIES
CREATE TABLE movies (
	`id` varchar(10) NOT NULL,
	`title` varchar(100) NOT NULL,
	`year` int(11) NOT NULL,
	`director` varchar(100) NOT NULL,
	PRIMARY KEY (`id`)
) ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ROW_FORMAT=DYNAMIC COMMENT='' CHECKSUM=0 DELAY_KEY_WRITE=0;

-- CREATE TABLE STARS
CREATE TABLE stars (
	`id` varchar(10) NOT NULL,
	`name` varchar(100) NOT NULL,
	`birthYear` int(11) DEFAULT NULL,
	PRIMARY KEY (`id`)
) ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ROW_FORMAT=DYNAMIC COMMENT='' CHECKSUM=0 DELAY_KEY_WRITE=0;

-- CREATE TABLE STARS_IN_MOVIES
CREATE TABLE stars_in_movies (
	`starId` varchar(10) NOT NULL,
	`movieId` varchar(10) NOT NULL,
	CONSTRAINT `stars_in_movies_ibfk_1` FOREIGN KEY (`starId`) REFERENCES `stars` (`id`),
	CONSTRAINT `stars_in_movies_ibfk_2` FOREIGN KEY (`movieId`) REFERENCES `movies` (`id`)
) ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ROW_FORMAT=DYNAMIC COMMENT='' CHECKSUM=0 DELAY_KEY_WRITE=0;

-- CREATE TABLE GENRES
CREATE TABLE genres (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`name` varchar(32) NOT NULL,
	PRIMARY KEY (`id`)
) ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ROW_FORMAT=DYNAMIC COMMENT='' CHECKSUM=0 DELAY_KEY_WRITE=0;

-- CREATE TABLE GENRES_IN_MOVIES
CREATE TABLE genres_in_movies (
	`genreId` int(11) NOT NULL,
	`movieId` varchar(10) NOT NULL,
	CONSTRAINT `genres_in_movies_ibfk_1` FOREIGN KEY (`genreId`) REFERENCES `genres` (`id`),
	CONSTRAINT `genres_in_movies_ibfk_2` FOREIGN KEY (`movieId`) REFERENCES `movies` (`id`)
) ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ROW_FORMAT=DYNAMIC COMMENT='' CHECKSUM=0 DELAY_KEY_WRITE=0;

-- CREATE TABLE CREDITCARDS
CREATE TABLE creditcards (
	`id` varchar(20) NOT NULL,
	`firstName` varchar(50) NOT NULL,
	`lastName` varchar(50) NOT NULL,
	`expiration` date NOT NULL,
	PRIMARY KEY (`id`)
) ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ROW_FORMAT=DYNAMIC COMMENT='' CHECKSUM=0 DELAY_KEY_WRITE=0;

-- CREATE TBALE CUSTOMERS
CREATE TABLE customers (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`firstName` varchar(50) NOT NULL,
	`lastName` varchar(50) NOT NULL,
	`ccId` varchar(20) NOT NULL,
	`address` varchar(200) NOT NULL,
	`email` varchar(50) NOT NULL,
	`password` varchar(20) NOT NULL,
	PRIMARY KEY (`id`),
	CONSTRAINT `customers_ibfk_1` FOREIGN KEY (`ccId`) REFERENCES `creditcards` (`id`)
) ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ROW_FORMAT=DYNAMIC COMMENT='' CHECKSUM=0 DELAY_KEY_WRITE=0;

-- CREATE TABLE SALES
CREATE TABLE sales (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`customerId` int(11) NOT NULL,
	`movieId` varchar(10) NOT NULL,
	`saleDate` date NOT NULL,
	PRIMARY KEY (`id`),
	CONSTRAINT `sales_ibfk_1` FOREIGN KEY (`customerId`) REFERENCES `customers` (`id`),
	CONSTRAINT `sales_ibfk_2` FOREIGN KEY (`movieId`) REFERENCES `movies` (`id`)
) ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ROW_FORMAT=DYNAMIC COMMENT='' CHECKSUM=0 DELAY_KEY_WRITE=0;

-- CREATE TABLE RATINGS
CREATE TABLE ratings (
	`movieId` varchar(10) NOT NULL,
	`rating` float NOT NULL,
	`numVotes` int(11) NOT NULL,
	CONSTRAINT `ratings_ibfk_1` FOREIGN KEY (`movieId`) REFERENCES `movies` (`id`)
) ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ROW_FORMAT=DYNAMIC COMMENT='' CHECKSUM=0 DELAY_KEY_WRITE=0;
