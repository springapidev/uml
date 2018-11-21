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
-- Create schema classproject
--

CREATE DATABASE IF NOT EXISTS classproject;
USE classproject;

--
-- Definition of table `customer`
--

DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `cusid` int(11) NOT NULL,
  `cusname` varchar(20) default NULL,
  `sex` varchar(10) default NULL,
  `age` int(11) default NULL,
  `cell` int(11) default NULL,
  `address` varchar(200) default NULL,
  `membertype` varchar(20) default NULL,
  PRIMARY KEY  (`cusid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `customer`
--

/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;


--
-- Definition of table `product`
--

DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `pid` int(11) NOT NULL,
  `pname` varchar(20) default NULL,
  `brand` varchar(20) default NULL,
  `unit` int(11) default NULL,
  `orderlevel` varchar(20) default NULL,
  `price` float(12,2) default NULL,
  PRIMARY KEY  (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `product`
--

/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` (`pid`,`pname`,`brand`,`unit`,`orderlevel`,`price`) VALUES 
 (2000,'jhh','UNILEVER',1266,'jkhi',256.00),
 (2001,'sunsilk','UNILEVER',12,'ouuj',125.00),
 (2002,'IRON','UNILEVER',12,'ooo',45.00),
 (2003,'tsfsd','MERIL',22,'dfg',544.20),
 (2004,'yweui','UNILEVER',21,'pp',251.00),
 (2005,'closeup','UNILEVER',20,'ee',500.00),
 (2006,'closeup','UNILEVER',20,'ee',500.00),
 (2007,'laptop','UNILEVER',100,'hk',4454.00);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;


--
-- Definition of table `purchaseorder`
--

DROP TABLE IF EXISTS `purchaseorder`;
CREATE TABLE `purchaseorder` (
  `purorderid` int(11) NOT NULL,
  `purdate` varchar(20) default NULL,
  `totalprice` int(11) default NULL,
  `supplierid` int(11) default NULL,
  PRIMARY KEY  (`purorderid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `purchaseorder`
--

/*!40000 ALTER TABLE `purchaseorder` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchaseorder` ENABLE KEYS */;


--
-- Definition of table `purchaseorderline`
--

DROP TABLE IF EXISTS `purchaseorderline`;
CREATE TABLE `purchaseorderline` (
  `slno` int(11) NOT NULL,
  `purorderid` int(11) default NULL,
  `pid` int(11) default NULL,
  `unit` varchar(20) default NULL,
  `rate` float(12,2) default NULL,
  `total` float(12,2) default NULL,
  `tax` float(12,2) default NULL,
  PRIMARY KEY  (`slno`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `purchaseorderline`
--

/*!40000 ALTER TABLE `purchaseorderline` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchaseorderline` ENABLE KEYS */;


--
-- Definition of table `salesorder`
--

DROP TABLE IF EXISTS `salesorder`;
CREATE TABLE `salesorder` (
  `sid` int(11) NOT NULL,
  `pid` int(11) default NULL,
  `cusid` int(11) default NULL,
  `date` varchar(20) default NULL,
  PRIMARY KEY  (`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `salesorder`
--

/*!40000 ALTER TABLE `salesorder` DISABLE KEYS */;
/*!40000 ALTER TABLE `salesorder` ENABLE KEYS */;


--
-- Definition of table `salesorderline`
--

DROP TABLE IF EXISTS `salesorderline`;
CREATE TABLE `salesorderline` (
  `slno` int(11) NOT NULL,
  `purorderid` int(11) default NULL,
  `pid` int(11) default NULL,
  `unit` varchar(20) default NULL,
  `rate` float(12,2) default NULL,
  `total` float(12,2) default NULL,
  `tax` float(12,2) default NULL,
  PRIMARY KEY  (`slno`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `salesorderline`
--

/*!40000 ALTER TABLE `salesorderline` DISABLE KEYS */;
/*!40000 ALTER TABLE `salesorderline` ENABLE KEYS */;


--
-- Definition of table `stock`
--

DROP TABLE IF EXISTS `stock`;
CREATE TABLE `stock` (
  `stockid` int(11) NOT NULL,
  `pid` int(11) default NULL,
  `stockdate` varchar(20) default NULL,
  `quality` int(11) default NULL,
  PRIMARY KEY  (`stockid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `stock`
--

/*!40000 ALTER TABLE `stock` DISABLE KEYS */;
/*!40000 ALTER TABLE `stock` ENABLE KEYS */;


--
-- Definition of table `supplier`
--

DROP TABLE IF EXISTS `supplier`;
CREATE TABLE `supplier` (
  `supid` int(11) NOT NULL,
  `supname` varchar(20) default NULL,
  `address` varchar(200) default NULL,
  `cell` int(11) default NULL,
  PRIMARY KEY  (`supid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `supplier`
--

/*!40000 ALTER TABLE `supplier` DISABLE KEYS */;
/*!40000 ALTER TABLE `supplier` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
