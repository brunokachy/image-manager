CREATE TABLE IF NOT EXISTS `image_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `size` int(11) NOT NULL,
  `description` varchar(255) NOT NULL,
  `file_type` varchar(255) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_image_name` (`name`)
);
