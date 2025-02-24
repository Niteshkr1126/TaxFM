use TaxFM;

INSERT INTO roles (role_id, description, role) VALUES
(1, 'Administrator Employee', 'ROLE_ADMIN'),
(2, 'Senior Accountant Employee', 'ROLE_SENIOR_ACCOUNTANT'),
(3, 'Accountant Employee', 'ROLE_ACCOUNTANT'),
(4, 'Customer', 'ROLE_CUSTOMER');

-- Insert Authorities
INSERT INTO authorities (authority_id, authority, description) VALUES
(1, 'ADD_EMPLOYEE', 'Add new employee'),
(2, 'VIEW_ALL_EMPLOYEES', 'View all employees'),
(3, 'VIEW_EMPLOYEE', 'View employee details'),
(4, 'UPDATE_EMPLOYEE', 'Edit employee details'),
(5, 'DELETE_EMPLOYEE', 'Delete an employee'),
(6, 'ASSIGN_SUBORDINATE', 'Assign subordinates to an employee'),
(7, 'VIEW_ASSIGNED_SUBORDINATES', 'View assigned subordinates'),
(8, 'VIEW_SUBORDINATE', 'View assigned subordinates'),
(9, 'REMOVE_ASSIGNED_SUBORDINATE', 'Remove assigned subordinate'),
(10, 'ADD_CUSTOMER', 'Add new customer'),
(11, 'VIEW_ALL_CUSTOMERS', 'View all customers'),
(12, 'VIEW_CUSTOMER', 'View customer details'),
(13, 'UPDATE_CUSTOMER', 'Edit customer details'),
(14, 'DELETE_CUSTOMER', 'Delete a customer'),
(15, 'ASSIGN_CUSTOMER', 'Assign customers to employees'),
(16, 'VIEW_ASSIGNED_CUSTOMERS', 'View customers assigned to an employee'),
(17, 'REMOVE_ASSIGNED_CUSTOMER', 'Remove assigned customer'),
(18, 'VIEW_ASSIGNED_SERVICES', 'View services assigned to customers'),
(19, 'VIEW_SERVICE', 'View details of a specific service'),
(20, 'UPDATE_SERVICE', 'Edit details of a specific service'),
(21, 'DELETE_SERVICE', 'Delete a specific service'),
(22, 'ASSIGN_SERVICE', 'Assign services to customers'),
(23, 'REMOVE_ASSIGNED_SERVICE', 'Remove assigned service'),
(24, 'LOCK_UNLOCK_ACCOUNT', 'Lock or unlock accounts'),
(25, 'ENABLE_DISABLE_ACCOUNT', 'Enable or disable accounts'),
(26, 'VIEW_PROFILE', 'View own profile'),
(27, 'RESET_PASSWORD', 'Reset own password'),
(28, 'VIEW_AGREEMENT', 'View employee agreements'),
(29, 'VIEW_ATTENDANCE', 'View attendance'),
(30, 'VIEW_EMPLOYEE_ATTENDANCE', 'View employee attendance');

-- ROLE_ADMIN Assignments
INSERT INTO roles_authorities (role_id, authority_id) VALUES
(1, 1),  -- ADD_EMPLOYEE
(1, 2),  -- VIEW_ALL_EMPLOYEES
(1, 3),  -- VIEW_EMPLOYEE
(1, 4),  -- UPDATE_EMPLOYEE
(1, 5),  -- DELETE_EMPLOYEE
(1, 6),  -- ASSIGN_SUBORDINATE
(1, 7),  -- VIEW_ASSIGNED_SUBORDINATES
(1, 8),  -- VIEW_SUBORDINATE
(1, 9),  -- REMOVE_ASSIGNED_SUBORDINATE
(1, 10), -- ADD_CUSTOMER
(1, 11), -- VIEW_ALL_CUSTOMERS
(1, 12), -- VIEW_CUSTOMER
(1, 13), -- UPDATE_CUSTOMER
(1, 14), -- DELETE_CUSTOMER
(1, 15), -- ASSIGN_CUSTOMER
(1, 16), -- VIEW_ASSIGNED_CUSTOMERS
(1, 17), -- REMOVE_ASSIGNED_CUSTOMER
(1, 18), -- VIEW_ASSIGNED_SERVICES
(1, 19), -- VIEW_SERVICE
(1, 20), -- UPDATE_SERVICE
(1, 21), -- DELETE_SERVICE
(1, 22), -- ASSIGN_SERVICE
(1, 23), -- REMOVE_ASSIGNED_SERVICE
(1, 24), -- LOCK_UNLOCK_ACCOUNT
(1, 25), -- ENABLE_DISABLE_ACCOUNT
(1, 26), -- VIEW_PROFILE
(1, 27), -- RESET_PASSWORD
(1, 28), -- VIEW_AGREEMENT
(1, 29), -- VIEW_ATTENDANCE
(1, 30); -- VIEW_EMPLOYEE_ATTENDANCE


-- ROLE_SENIOR_ACCOUNTANT Assignments
INSERT INTO roles_authorities (role_id, authority_id) VALUES
(2, 6),  -- ASSIGN_SUBORDINATE
(2, 7),  -- VIEW_ASSIGNED_SUBORDINATES
(2, 8),  -- VIEW_SUBORDINATE
(2, 9),  -- REMOVE_ASSIGNED_SUBORDINATE
(2, 12), -- VIEW_CUSTOMER
(2, 15), -- ASSIGN_CUSTOMER
(2, 16), -- VIEW_ASSIGNED_CUSTOMERS
(2, 17), -- REMOVE_ASSIGNED_CUSTOMER
(2, 18), -- VIEW_ASSIGNED_SERVICES
(2, 19), -- VIEW_SERVICE
(2, 26), -- VIEW_PROFILE
(2, 27), -- RESET_PASSWORD
(2, 28), -- VIEW_AGREEMENT
(2, 29); -- VIEW_ATTENDANCE


-- ROLE_ACCOUNTANT Assignments
INSERT INTO roles_authorities (role_id, authority_id) VALUES
(3, 12), -- VIEW_CUSTOMER
(3, 16), -- VIEW_ASSIGNED_CUSTOMERS
(3, 18), -- VIEW_ASSIGNED_SERVICES
(3, 19), -- VIEW_SERVICE
(3, 26), -- VIEW_PROFILE
(3, 27), -- RESET_PASSWORD
(3, 28), -- VIEW_AGREEMENT
(3, 29); -- VIEW_ATTENDANCE

-- ROLE_CUSTOMER Assignments
INSERT INTO roles_authorities (role_id, authority_id) VALUES
(4, 12), -- VIEW_CUSTOMER
(4, 16), -- VIEW_ASSIGNED_CUSTOMERS
(4, 18), -- VIEW_ASSIGNED_SERVICES
(4, 19), -- VIEW_SERVICE
(4, 26), -- VIEW_PROFILE
(4, 27), -- RESET_PASSWORD
(4, 28); -- VIEW_AGREEMENT

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

INSERT INTO employees_roles (employee_id, role_id) VALUES (1, 1);
INSERT INTO employees_roles (employee_id, role_id) VALUES (2, 2);
INSERT INTO employees_roles (employee_id, role_id) VALUES (3, 3);
INSERT INTO employees_roles (employee_id, role_id) VALUES (4, 3);

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

INSERT INTO customers_roles (customer_id, role_id) VALUES (1, 4);
INSERT INTO customers_roles (customer_id, role_id) VALUES (2, 4);

DROP TRIGGER IF EXISTS after_session_delete;
DELIMITER $$

CREATE TRIGGER after_session_delete
AFTER DELETE ON SPRING_SESSION
FOR EACH ROW
BEGIN
    DECLARE computed_logout_time DATETIME;

    -- Calculate the logout_time based on last access time + max inactive interval
    SET computed_logout_time = FROM_UNIXTIME((OLD.LAST_ACCESS_TIME + (OLD.MAX_INACTIVE_INTERVAL * 1000)) / 1000);

    -- Only update if computed_logout_time is in the past
    IF computed_logout_time <= NOW() THEN
        UPDATE attendance
        SET logout_time = computed_logout_time
        WHERE username = OLD.PRINCIPAL_NAME
        ORDER BY login_time DESC
        LIMIT 1;
    END IF;
END$$

DELIMITER ;

UPDATE employees SET supervisor_id = 1 WHERE employee_id = 2;
UPDATE employees SET supervisor_id = 2 WHERE employee_id = 3;
UPDATE employees SET supervisor_id = 2 WHERE employee_id = 4;

INSERT INTO employees_customers (employee_id, customer_id) VALUES (2, 1), (3, 2);

INSERT INTO authorities (authority_id, authority, description) VALUES (30, 'VIEW_EMPLOYEE_ATTENDANCE', 'View employee attendance');
INSERT INTO roles_authorities (role_id, authority_id) VALUES (1, 30);