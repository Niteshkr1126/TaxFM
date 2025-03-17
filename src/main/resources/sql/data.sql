INSERT INTO departments (name, description, image_name, url) VALUES
('Accounts', 'Comprehensive accounting solutions to manage your finances efficiently.', 'accounts.jpg', '/pricing/accounts'),
('Goods and Services Tax', 'Seamless GST compliance and filing services.', 'gst.jpg', '/pricing/gst'),
('Income Tax Return Filing', 'Expert assistance with income tax planning and filing.', 'itr.jpg', '/pricing/income-tax'),
('Audit', 'Professional auditing services for accurate financial reporting.', 'audit.jpg', '/pricing/audit'),
('TDS/TCS Filing', 'Timely and precise TDS/TCS compliance and filing.', 'tcs_tds.jpg', '/pricing/tds'),
('MCA Filing', 'Efficient management and filing with the Ministry of Corporate Affairs.', 'mca_filing.jpg', '/pricing/mca'),
('Company Formation', 'End-to-end assistance in setting up your company.', 'company_formation.jpg', '/pricing/company-formation'),
('LIC', 'Life insurance solutions tailored to your needs.', 'lic.jpg', '/pricing/lic'),
('Mutual Funds', 'Smart investment options through mutual funds.', 'mutual_funds.jpg', '/pricing/mutual-funds'),
('Insurance', 'Comprehensive insurance services for security and peace of mind.', 'insurance.jpg', '/pricing/insurance');

INSERT INTO service_rates (department_id, category, turnover_min, turnover_max, slab_range, rate, gst_percentage, frequency)
VALUES 
-- Accounts
(1, 'All', 0, 5000000, '0 to 50 Lacs', 2500, 18.00, 'Monthly'),
(1, 'All', 5000001, 20000000, '50 Lacs to 2 Crores', 4000, 18.00, 'Monthly'),
(1, 'All', 20000001, 50000000, '2 Crores to 5 Crores', 6000, 18.00, 'Monthly'),
(1, 'All', 50000001, 200000000, '5 Crores to 20 Crores', 15000, 18.00, 'Monthly'),

(1, 'Medicine', 0, 5000000, '0 to 50 Lacs', 2500, 18.00, 'Monthly'),
(1, 'Medicine', 5000001, 20000000, '50 Lacs to 2 Crores', 5000, 18.00, 'Monthly'),
(1, 'Medicine', 20000001, 50000000, '2 Crores to 5 Crores', 8000, 18.00, 'Monthly'),
(1, 'Medicine', 50000001, NULL, 'Above 5 Crores', NULL, NULL, 'NA'),

(1, 'General Store', 0, 5000000, '0 to 50 Lacs', 2500, 18.00, 'Monthly'),
(1, 'General Store', 5000001, 20000000, '50 Lacs to 2 Crores', 5000, 18.00, 'Monthly'),
(1, 'General Store', 20000001, 50000000, '2 Crores to 5 Crores', 8000, 18.00, 'Monthly'),
(1, 'General Store', 50000001, 200000000, '5 Crores to 20 Crores', 15000, 18.00, 'Monthly'),

(1, 'Electric Shop', 0, 5000000, '0 to 50 Lacs', 2500, 18.00, 'Monthly'),
(1, 'Electric Shop', 5000001, 20000000, '50 Lacs to 2 Crores', 5000, 18.00, 'Monthly'),
(1, 'Electric Shop', 20000001, 50000000, '2 Crores to 5 Crores', 8000, 18.00, 'Monthly'),
(1, 'Electric Shop', 50000001, 200000000, '5 Crores to 20 Crores', 15000, 18.00, 'Monthly'),

(1, 'Hardware', 0, 5000000, '0 to 50 Lacs', 4000, 18.00, 'Monthly'),
(1, 'Hardware', 5000001, 20000000, '50 Lacs to 2 Crores', 6000, 18.00, 'Monthly'),
(1, 'Hardware', 20000001, 50000000, '2 Crores to 5 Crores', 8000, 18.00, 'Monthly'),
(1, 'Hardware', 50000001, 200000000, '5 Crores to 20 Crores', 15000, 18.00, 'Monthly'),

(1, 'Work Contractor + Builder', 0, 200000000, '0 to 20 Crores', 15000, 18.00, 'Monthly'),

(2, 'GST Registration Fees', 0, NULL, 'All Turnovers', 2100, 18.00, 'One Time'),

-- Income Tax Filing
(3, 'Salaried', 0, NULL, 'All Turnovers', 1000, 18.00, 'Annually'),
(3, 'Non Salaried', 0, NULL, 'All Turnovers', 2000, 18.00, 'Annually'),

-- Other Services
(4, '-', 0, NULL, 'All Turnovers', NULL, NULL, 'NA'),
(5, '-', 0, NULL, 'All Turnovers', NULL, NULL, 'NA'),
(6, '-', 0, NULL, 'All Turnovers', NULL, NULL, 'NA'),
(7, '-', 0, NULL, 'All Turnovers', NULL, NULL, 'NA'),

-- Financial Products
(8, '-', 0, NULL, 'All Turnovers', NULL, NULL, 'NA'),
(9, '-', 0, NULL, 'All Turnovers', NULL, NULL, 'NA'),
(10, '-', 0, NULL, 'All Turnovers', NULL, NULL, 'NA');

INSERT INTO customers_services (customer_id, service_id) VALUES (1, 1), (1, 2);