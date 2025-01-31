INSERT INTO `authorities` (`authority_id`, `authority`) VALUES (1,'ADMIN');
INSERT INTO `authorities` (`authority_id`, `authority`) VALUES (2,'SENIOR');
INSERT INTO `authorities` (`authority_id`, `authority`) VALUES (3,'JUNIOR');
INSERT INTO `authorities` (`authority_id`, `authority`) VALUES (4,'CUSTOMER');

INSERT INTO `employees` (
    `employee_id`, `username`, `first_name`, `last_name`, `designation`, `email`,
    `phone_number`, `dob`, `gender`, `marital_status`, `address`, `pan`,
    `aadhar_card_number`, `agreement`, `password`, `account_non_expired`,
    `account_non_locked`, `credentials_non_expired`, `enabled`
) VALUES
(1, 'admin', 'ADMIN', 'KUMAR', 'ADMINISTRATOR', 'ADMIN@GMAIL.COM',
 '1234567890', '1990-01-01', 'MALE', 'SINGLE', '123 ADMIN ST', 'ABCDE1234F',
 123456789012, 'AGREEMENT DETAILS HERE',
 '$2a$10$Pqvmv0ODDFgg80hkA84kJ.DxiU3w0dbUQTUIpPZzudilkBOfypeb6', 1, 1, 1, 1),
(2, 'senior', 'SENIOR', 'KUMAR', 'SENIOR', 'SENIOR@GMAIL.COM',
 '9852280991', '2025-01-07', 'MALE', 'MARRIED', 'ADARSH COLONY, BARI ROAD, KATHOKAR TALAB', 'ASDF1234TY',
 123456789101, 'Agreement',
 '$2a$10$6VCNTn7dpr./pfMmCSOrDuB/PugdqnkQVTRboP6wgylAr0/dRezgC', 1, 1, 1, 1),
(3, 'junior', 'JUNIOR', 'KUMAR', 'JUNIOR', 'JUNIOR@GMAIL.COM',
 '9852280992', '2025-01-22', 'MALE', 'SINGLE', 'BLOCK C, SECTOR 22, CITY CENTER', '23123FSDFW',
 513762538621, 'Agreement',
 '$2a$10$qVGbCW3weEHGV2ar4gEuOeuJUk/Tz919J2nmTMO2sxDyJeST0.Q5G', 1, 1, 1, 1),
 (4, 'junior1', 'JUNIOR1', 'KUMARI', 'JUNIOR', 'JUNIOR1@GMAIL.COM',
  '6127182782', '2025-01-03', 'FEMALE', 'MARRIED', 'BARI ROAD', 'ABCDE2345G',
  2163872169, 'Agreement', '$2a$10$C/4ZqCu8kQxyHfyry6T6teZjy.Vg6G6ZPoR5dEMyIuSqk0k42GPeK', 1, 1, 1, 1);

INSERT INTO employees_authorities (employee_id, authority_id) VALUES (1, 1);
INSERT INTO employees_authorities (employee_id, authority_id) VALUES (2, 2);
INSERT INTO employees_authorities (employee_id, authority_id) VALUES (3, 3);
INSERT INTO employees_authorities (employee_id, authority_id) VALUES (4, 3);

INSERT INTO `customers` (
    `customer_id`, `username`, `first_name`, `last_name`, `firm_name`, `address`,
    `email`, `phone_number`, `pan`, `aadhar_card_number`, `gst_number`, `password`,
    `account_non_expired`, `account_non_locked`, `credentials_non_expired`, `enabled`
) VALUES
(1, 'customer', 'CUSTOMER', 'KUMAR', 'JOHN ENTERPRISES', '123 CUSTOMER LANE',
 'CUSTOMER@GMAIL.COM', '9876543210', 'ABCDE5678Z', 218921879391872, 'GST9876543X',
 '$2a$10$wUO.OGdgDawpMXdRUIwKKe7kAuoSLVQw6BBfu772erbWF1KBXkFIe', 1, 1, 1, 1),
(2, 'customer1', 'CUSTOMER1', 'KUMAR', 'CUSTOMER 1 ENTERPRISES', 'ADARSH COLONY, BARI ROAD, KATHOKAR TALAB, MAKHALAUTGANJ',
 'CUSTOMER1@GMAIL.COM', '9876325637', 'ASDFW1234D', 361782461824, '218y8udsjkad7812',
 '$2a$10$nIHd9mpEUh7zQNZ7SjMjhu8MLnOYB3ZvRgGQHIlg7je4NZXjT2hDO', 1, 1, 1, 1);

INSERT INTO customers_authorities (customer_id, authority_id) VALUES (1, 4);
INSERT INTO customers_authorities (customer_id, authority_id) VALUES (2, 4);

DROP TRIGGER IF EXISTS after_session_delete;
DELIMITER $$

CREATE TRIGGER after_session_delete
AFTER DELETE ON SPRING_SESSION
FOR EACH ROW
BEGIN
    -- Convert session expiry time to match attendance timezone
    UPDATE attendance
    SET logout_time = FROM_UNIXTIME((OLD.LAST_ACCESS_TIME + (OLD.MAX_INACTIVE_INTERVAL * 1000)) / 1000)
    WHERE username = OLD.PRINCIPAL_NAME
    ORDER BY login_time DESC
    LIMIT 1;
END$$

DELIMITER ;
