-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: prodavnicais1
-- ------------------------------------------------------
-- Server version	5.7.20-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `stanje`
--

DROP TABLE IF EXISTS `stanje`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stanje` (
  `sifraProdavnice` varchar(15) NOT NULL,
  `sifraArtikla` varchar(15) NOT NULL,
  `kolicina` int(11) DEFAULT NULL,
  PRIMARY KEY (`sifraProdavnice`,`sifraArtikla`),
  KEY `sifraArtikla_idx` (`sifraArtikla`),
  CONSTRAINT `sifraArtikla` FOREIGN KEY (`sifraArtikla`) REFERENCES `artikal` (`sifra`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `sifraProdavnice` FOREIGN KEY (`sifraProdavnice`) REFERENCES `prodavnica` (`sifra`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stanje`
--

LOCK TABLES `stanje` WRITE;
/*!40000 ALTER TABLE `stanje` DISABLE KEYS */;
INSERT INTO `stanje` VALUES ('Tehnomanija','AsusRuter',3),('Tehnomanija','BeatsByDre',25),('Tehnomanija','Iphone7',20),('Tehnomanija','Iphone7s',15),('Tehnomanija','SamsungG8',15),('WinWin','AsusRuter',5),('WinWin','BeatsByDre',5),('WinWin','BenQMonitor',7),('WinWin','DellMonitor',10),('WinWin','SamsungMonitor',5),('WinWin','VerbatimCD',40),('WinWin','VerbatimDVD',50);
/*!40000 ALTER TABLE `stanje` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-26 18:46:52
