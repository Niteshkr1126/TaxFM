INSERT INTO `authorities` (`authority_id`, `authority`) VALUES (1,'ADMIN');
INSERT INTO `authorities` (`authority_id`, `authority`) VALUES (2,'SENIOR');
INSERT INTO `authorities` (`authority_id`, `authority`) VALUES (3,'JUNIOR');
INSERT INTO `authorities` (`authority_id`, `authority`) VALUES (4,'CUSTOMER');

INSERT INTO `employees` (`account_non_expired`, `account_non_locked`, `credentials_non_expired`, `email`, `enabled`, `first_name`, `last_name`, `password`, `username`)
VALUES (1, 1, 1, 'admin@gmail.com',1, 'John','Doe','$2a$10$mgnm0snevhBzlcDT2UE5mebTV8ZaxY1mWjBOwZtadstn82nqHIaE.','admin');
INSERT INTO `employees` (`account_non_expired`, `account_non_locked`, `credentials_non_expired`, `email`, `enabled`, `first_name`, `last_name`, `password`, `username`)
VALUES (1, 1, 1, 'user@gmail.com',1, 'John','Doe','$2a$10$hULduHJA3/DshBLoLI04kOW5D52A.GGM/OK5fFVnUS57wHVdk/T9e','user');

INSERT INTO employees_authorities (employee_id, authority_id) VALUES (1, 1);
INSERT INTO employees_authorities (employee_id, authority_id) VALUES (2, 2);

INSERT INTO `customers` (`account_non_expired`, `account_non_locked`, `credentials_non_expired`, `email`, `enabled`, `first_name`, `last_name`, `password`, `username`)
VALUES (1, 1, 1, 'admin@gmail.com',1, 'John','Doe','$2a$10$mgnm0snevhBzlcDT2UE5mebTV8ZaxY1mWjBOwZtadstn82nqHIaE.','admin1');
INSERT INTO `customers` (`account_non_expired`, `account_non_locked`, `credentials_non_expired`, `email`, `enabled`, `first_name`, `last_name`, `password`, `username`)
VALUES (1, 1, 1, 'user@gmail.com',1, 'John','Doe','$2a$10$hULduHJA3/DshBLoLI04kOW5D52A.GGM/OK5fFVnUS57wHVdk/T9e','user1');

INSERT INTO customers_authorities (customer_id, authority_id) VALUES (1, 4);
INSERT INTO customers_authorities (customer_id, authority_id) VALUES (2, 4);