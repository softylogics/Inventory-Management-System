-- phpMyAdmin SQL Dump
-- version 4.0.4.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Sep 14, 2014 at 10:42 AM
-- Server version: 5.6.11
-- PHP Version: 5.5.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `computer_shop`
--
CREATE DATABASE IF NOT EXISTS `computer_shop` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `computer_shop`;

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE IF NOT EXISTS `category` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_name` char(255) NOT NULL,
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `category_name` (`category_name`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=65 ;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`category_id`, `category_name`) VALUES
(61, 'Accessories'),
(64, 'Keyboard'),
(62, 'Laptop'),
(63, 'System');

-- --------------------------------------------------------

--
-- Table structure for table `del`
--

CREATE TABLE IF NOT EXISTS `del` (
  `delete_id` int(11) NOT NULL AUTO_INCREMENT,
  `voucher_no` int(11) NOT NULL,
  `date_deleted` varchar(255) NOT NULL,
  `qty` int(11) NOT NULL,
  `price` int(11) NOT NULL,
  `purchase_sale` varchar(255) NOT NULL,
  `note` varchar(255) NOT NULL,
  `model_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  PRIMARY KEY (`delete_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=38 ;

--
-- Dumping data for table `del`
--

INSERT INTO `del` (`delete_id`, `voucher_no`, `date_deleted`, `qty`, `price`, `purchase_sale`, `note`, `model_id`, `item_id`) VALUES
(37, 1, '', 0, 0, '', '', 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `item`
--

CREATE TABLE IF NOT EXISTS `item` (
  `item_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_id` int(11) NOT NULL,
  `item_name` char(255) NOT NULL,
  `item_qty` int(11) NOT NULL,
  PRIMARY KEY (`item_id`),
  UNIQUE KEY `item_name` (`item_name`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT AUTO_INCREMENT=72 ;

--
-- Dumping data for table `item`
--

INSERT INTO `item` (`item_id`, `category_id`, `item_name`, `item_qty`) VALUES
(68, 61, 'USB 4GB', 100),
(69, 62, 'Dell Core2duo', 9),
(70, 63, 'qweqw', 111),
(71, 64, 'Hp', 111);

-- --------------------------------------------------------

--
-- Table structure for table `model`
--

CREATE TABLE IF NOT EXISTS `model` (
  `model_id` int(11) NOT NULL AUTO_INCREMENT,
  `item_id` int(11) NOT NULL,
  `model_no` varchar(255) NOT NULL,
  `qty` int(11) NOT NULL,
  `sale_price` int(11) NOT NULL,
  `discount` int(11) NOT NULL,
  PRIMARY KEY (`model_id`),
  UNIQUE KEY `model_no` (`model_no`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=56 ;

--
-- Dumping data for table `model`
--

INSERT INTO `model` (`model_id`, `item_id`, `model_no`, `qty`, `sale_price`, `discount`) VALUES
(51, 68, 'kingstone', 100, 550, 0),
(52, 69, 'Latitude', 9, 16000, 0),
(53, 70, 'qweqwe', 111, 234, 0),
(54, 71, 'qwerty', 111, 123, 0),
(55, 69, 'D420', 1, 9000, 0);

-- --------------------------------------------------------

--
-- Table structure for table `purchase`
--

CREATE TABLE IF NOT EXISTS `purchase` (
  `purchase_id` int(11) NOT NULL AUTO_INCREMENT,
  `item_id` int(11) NOT NULL,
  `qty_purchase` int(11) NOT NULL,
  `date_purchase` varchar(255) NOT NULL,
  `supplier_name` varchar(255) NOT NULL,
  `total_bill` int(11) NOT NULL,
  `item_purchase_price` int(11) NOT NULL,
  `model_id` int(11) NOT NULL,
  `discount` int(11) NOT NULL,
  `voucher_no` int(11) NOT NULL,
  PRIMARY KEY (`purchase_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=78 ;

--
-- Dumping data for table `purchase`
--

INSERT INTO `purchase` (`purchase_id`, `item_id`, `qty_purchase`, `date_purchase`, `supplier_name`, `total_bill`, `item_purchase_price`, `model_id`, `discount`, `voucher_no`) VALUES
(48, 0, 0, '0', '', 0, 0, 0, 0, 1),
(73, 68, 100, 'Sep 12, 2014', 'Flash World', 50000, 500, 51, 0, 2),
(74, 69, 10, 'Sep 12, 2014', 'BN', 150000, 15000, 52, 0, 3),
(75, 70, 123, 'Sep 12, 2014', 'qwewqe', 15129, 123, 53, 0, 4),
(76, 71, 123, 'Sep 12, 2014', 'asd', 15129, 123, 54, 0, 5),
(77, 69, 1, 'Sep 12, 2014', 'fa', 8000, 8000, 55, 0, 6);

-- --------------------------------------------------------

--
-- Table structure for table `sale`
--

CREATE TABLE IF NOT EXISTS `sale` (
  `sale_id` int(11) NOT NULL AUTO_INCREMENT,
  `item_id` int(11) NOT NULL,
  `customer_name` char(11) NOT NULL,
  `qty_sale` int(11) NOT NULL,
  `date_sale` varchar(255) NOT NULL,
  `total_bill` int(100) NOT NULL,
  `voucher_no` int(11) NOT NULL,
  `model_id` int(11) NOT NULL,
  `discount` int(11) NOT NULL,
  PRIMARY KEY (`sale_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Dumping data for table `sale`
--

INSERT INTO `sale` (`sale_id`, `item_id`, `customer_name`, `qty_sale`, `date_sale`, `total_bill`, `voucher_no`, `model_id`, `discount`) VALUES
(1, 0, '', 0, '0', 0, 1, 0, 0),
(4, 71, 'Samraiz', 12, 'Sep 12, 2014', 1476, 2, 54, 0),
(5, 69, '', 1, 'Sep 12, 2014', 16000, 3, 52, 0),
(6, 70, 'sa', 12, 'Sep 12, 2014', 2808, 4, 53, 0);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `user_name`, `password`) VALUES
(1, 'samraiz', '12345');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
