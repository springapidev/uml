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
-- Create schema stock
--

CREATE DATABASE IF NOT EXISTS stock;
USE stock;

--
-- Definition of table `emp`
--

DROP TABLE IF EXISTS `emp`;
CREATE TABLE `emp` (
  `empid` int(11) default NULL,
  `empname` varchar(20) default NULL,
  `age` int(11) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `emp`
--

/*!40000 ALTER TABLE `emp` DISABLE KEYS */;
INSERT INTO `emp` (`empid`,`empname`,`age`) VALUES 
 (101,'rana',25);
/*!40000 ALTER TABLE `emp` ENABLE KEYS */;


--
-- Definition of table `product`
--

DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `pid` int(11) NOT NULL,
  `pname` varchar(20) default NULL,
  `pstock` int(11) default NULL,
  `reorder` int(11) default NULL,
  `price` double default NULL,
  PRIMARY KEY  (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `product`
--

/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` (`pid`,`pname`,`pstock`,`reorder`,`price`) VALUES 
 (1000,' hh',70,4,50),
 (1001,'ll',400,4,50),
 (1002,' tt',33,20,100),
 (1003,'pp',260,20,100),
 (1004,'pp',150,20,100),
 (1005,'pen',10,100,150),
 (1006,'book',10,50,200),
 (1007,'pencil',2,120,300),
 (1008,'jarnal',40,130,300),
 (1009,'lux',50,10,400);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;


--
-- Definition of table `purchase`
--

DROP TABLE IF EXISTS `purchase`;
CREATE TABLE `purchase` (
  `purid` int(11) NOT NULL,
  `pid` int(11) default NULL,
  `pdate` varchar(16) default NULL,
  `qty` int(11) default NULL,
  `uprice` float(10,2) default NULL,
  `total` float(12,2) default NULL,
  PRIMARY KEY  (`purid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `purchase`
--

/*!40000 ALTER TABLE `purchase` DISABLE KEYS */;
INSERT INTO `purchase` (`purid`,`pid`,`pdate`,`qty`,`uprice`,`total`) VALUES 
 (2000,100,'50',30,5000.00,2011.00),
 (2001,150,'50',30,7000.00,2010.00),
 (2002,200,'50',30,7000.00,2009.00),
 (2003,1003,'24-5-2011',10,20.00,200.00),
 (2004,1004,'24-5-2011',100,40.00,4000.00),
 (2005,1003,'24-5-2011',100,20.00,2000.00),
 (2006,1003,'24-5-2011',100,20.00,2000.00),
 (2007,1000,'7-6-2011',50,5000.00,250000.00);
/*!40000 ALTER TABLE `purchase` ENABLE KEYS */;


--
-- Definition of table `sales`
--

DROP TABLE IF EXISTS `sales`;
CREATE TABLE `sales` (
  `sid` int(11) NOT NULL,
  `pid` int(11) default NULL,
  `date` varchar(20) default NULL,
  `qry` int(11) default NULL,
  `price` float(20,2) default NULL,
  `total` float(16,2) default NULL,
  PRIMARY KEY  (`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `sales`
--

/*!40000 ALTER TABLE `sales` DISABLE KEYS */;
INSERT INTO `sales` (`sid`,`pid`,`date`,`qry`,`price`,`total`) VALUES 
 (1000,1006,'23-5-2011',20,200.00,400.00),
 (1001,1006,'23-5-2011',19,200.00,380.00),
 (1002,1004,'23-5-2011',20,100.00,1000.00),
 (1003,1007,'23-5-2011',10,300.00,120.00),
 (1004,1006,'23-5-2011',15,200.00,300.00),
 (1005,1007,'23-5-2011',10,300.00,120.00),
 (1006,1006,'28-5-2011',10,200.00,200.00),
 (1007,1000,'7-6-2011',21,50.00,861.00),
 (1008,1001,'19-6-2011',45,50.00,20025.00);
/*!40000 ALTER TABLE `sales` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
