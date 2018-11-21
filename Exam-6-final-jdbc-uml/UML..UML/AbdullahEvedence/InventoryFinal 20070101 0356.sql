-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.0.22-community-nt


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema inventory
--

CREATE DATABASE IF NOT EXISTS inventory;
USE inventory;

--
-- Definition of table `client`
--

DROP TABLE IF EXISTS `client`;
CREATE TABLE `client` (
  `clientID` varchar(20) NOT NULL,
  `clientTypeId` varchar(12) default NULL,
  `clientName` varchar(20) default NULL,
  `address` varchar(20) default NULL,
  PRIMARY KEY  (`clientID`),
  KEY `FK_client_clientTypeId` (`clientTypeId`),
  CONSTRAINT `FK_client_clientTypeId` FOREIGN KEY (`clientTypeId`) REFERENCES `client_type` (`clientTypeID`),
  CONSTRAINT `FK_client_1` FOREIGN KEY (`clientTypeId`) REFERENCES `client_type` (`clientTypeID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `client`
--

/*!40000 ALTER TABLE `client` DISABLE KEYS */;
/*!40000 ALTER TABLE `client` ENABLE KEYS */;


--
-- Definition of table `client_type`
--

DROP TABLE IF EXISTS `client_type`;
CREATE TABLE `client_type` (
  `clientTypeID` varchar(12) NOT NULL,
  `clientTitle` varchar(20) default NULL,
  PRIMARY KEY  (`clientTypeID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `client_type`
--

/*!40000 ALTER TABLE `client_type` DISABLE KEYS */;
INSERT INTO `client_type` (`clientTypeID`,`clientTitle`) VALUES 
 ('1','pen'),
 ('2','pencil'),
 ('3','shart');
/*!40000 ALTER TABLE `client_type` ENABLE KEYS */;


--
-- Definition of table `order_line`
--

DROP TABLE IF EXISTS `order_line`;
CREATE TABLE `order_line` (
  `orderlineid` int(11) NOT NULL,
  `total` float default NULL,
  `rate` float default NULL,
  `stockid` int(11) default NULL,
  `orderId` int(11) default NULL,
  PRIMARY KEY  (`orderlineid`),
  KEY `FK_order_line_stockid` (`stockid`),
  KEY `FK_order_line_orderId` (`orderId`),
  CONSTRAINT `FK_order_line_orderId` FOREIGN KEY (`orderId`) REFERENCES `oreder` (`orderId`),
  CONSTRAINT `FK_order_line_stockid` FOREIGN KEY (`stockid`) REFERENCES `stock` (`stockid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `order_line`
--

/*!40000 ALTER TABLE `order_line` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_line` ENABLE KEYS */;


--
-- Definition of table `order_type`
--

DROP TABLE IF EXISTS `order_type`;
CREATE TABLE `order_type` (
  `orderTypeId` varchar(255) NOT NULL,
  `orderTitle` varchar(255) default NULL,
  PRIMARY KEY  (`orderTypeId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `order_type`
--

/*!40000 ALTER TABLE `order_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_type` ENABLE KEYS */;


--
-- Definition of table `orders`
--

DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `orderID` int(11) NOT NULL,
  `orderDate` date default NULL,
  `clientID` varchar(20) default NULL,
  `totalAmount` float(15,2) default NULL,
  `commission` float(10,2) default NULL,
  `tax` float(10,2) default NULL,
  PRIMARY KEY  (`orderID`),
  KEY `FK_orders_1` (`clientID`),
  CONSTRAINT `FK_orders_1` FOREIGN KEY (`clientID`) REFERENCES `client` (`clientID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `orders`
--

/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;


--
-- Definition of table `oreder`
--

DROP TABLE IF EXISTS `oreder`;
CREATE TABLE `oreder` (
  `orderId` int(11) NOT NULL,
  `tax` float default NULL,
  `orderDate` date default NULL,
  `totalAmount` float default NULL,
  `commission` float default NULL,
  `orderTypeId` varchar(255) default NULL,
  `clientId` varchar(255) default NULL,
  PRIMARY KEY  (`orderId`),
  KEY `FK_oreder_clientId` (`clientId`),
  KEY `FK_oreder_orderTypeId` (`orderTypeId`),
  CONSTRAINT `FK_oreder_orderTypeId` FOREIGN KEY (`orderTypeId`) REFERENCES `order_type` (`orderTypeId`),
  CONSTRAINT `FK_oreder_clientId` FOREIGN KEY (`clientId`) REFERENCES `client` (`clientID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `oreder`
--

/*!40000 ALTER TABLE `oreder` DISABLE KEYS */;
/*!40000 ALTER TABLE `oreder` ENABLE KEYS */;


--
-- Definition of table `product`
--

DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `productId` int(11) NOT NULL,
  `price` float default NULL,
  `brand` varchar(255) default NULL,
  `productName` varchar(255) default NULL,
  PRIMARY KEY  (`productId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `product`
--

/*!40000 ALTER TABLE `product` DISABLE KEYS */;
/*!40000 ALTER TABLE `product` ENABLE KEYS */;


--
-- Definition of table `stock`
--

DROP TABLE IF EXISTS `stock`;
CREATE TABLE `stock` (
  `stockid` int(11) NOT NULL,
  `stockInHand` int(11) default NULL,
  `stocklevel` int(11) default NULL,
  `productId` int(11) default NULL,
  PRIMARY KEY  (`stockid`),
  KEY `FK_stock_productId` (`productId`),
  CONSTRAINT `FK_stock_productId` FOREIGN KEY (`productId`) REFERENCES `product` (`productId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `stock`
--

/*!40000 ALTER TABLE `stock` DISABLE KEYS */;
/*!40000 ALTER TABLE `stock` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
