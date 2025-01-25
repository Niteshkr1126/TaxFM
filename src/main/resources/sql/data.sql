INSERT INTO service_rates (service_type, category, turnover_min, turnover_max, slab_range, rate, gst_percentage, frequency)
VALUES 
-- GST Filing
('GST Filing', 'All', 0, 5000000, '0 to 50 Lacs', 2500, 18.00, 'Monthly'),
('GST Filing', 'All', 5000001, 20000000, '50 Lacs to 2 Crores', 4000, 18.00, 'Monthly'),
('GST Filing', 'All', 20000001, 50000000, '2 Crores to 5 Crores', 6000, 18.00, 'Monthly'),
('GST Filing', 'All', 50000001, 200000000, '5 Crores to 20 Crores', 15000, 18.00, 'Monthly'),

('GST Filing', 'Medicine', 0, 5000000, '0 to 50 Lacs', 2500, 18.00, 'Monthly'),
('GST Filing', 'Medicine', 5000001, 20000000, '50 Lacs to 2 Crores', 5000, 18.00, 'Monthly'),
('GST Filing', 'Medicine', 20000001, 50000000, '2 Crores to 5 Crores', 8000, 18.00, 'Monthly'),
('GST Filing', 'Medicine', 50000001, NULL, 'Above 5 Crores', NULL, NULL, 'NA'),

('GST Filing', 'General Store', 0, 5000000, '0 to 50 Lacs', 2500, 18.00, 'Monthly'),
('GST Filing', 'General Store', 5000001, 20000000, '50 Lacs to 2 Crores', 5000, 18.00, 'Monthly'),
('GST Filing', 'General Store', 20000001, 50000000, '2 Crores to 5 Crores', 8000, 18.00, 'Monthly'),
('GST Filing', 'General Store', 50000001, 200000000, '5 Crores to 20 Crores', 15000, 18.00, 'Monthly'),

('GST Filing', 'Electric Shop', 0, 5000000, '0 to 50 Lacs', 2500, 18.00, 'Monthly'),
('GST Filing', 'Electric Shop', 5000001, 20000000, '50 Lacs to 2 Crores', 5000, 18.00, 'Monthly'),
('GST Filing', 'Electric Shop', 20000001, 50000000, '2 Crores to 5 Crores', 8000, 18.00, 'Monthly'),
('GST Filing', 'Electric Shop', 50000001, 200000000, '5 Crores to 20 Crores', 15000, 18.00, 'Monthly'),

('GST Filing', 'Hardware', 0, 5000000, '0 to 50 Lacs', 4000, 18.00, 'Monthly'),
('GST Filing', 'Hardware', 5000001, 20000000, '50 Lacs to 2 Crores', 6000, 18.00, 'Monthly'),
('GST Filing', 'Hardware', 20000001, 50000000, '2 Crores to 5 Crores', 8000, 18.00, 'Monthly'),
('GST Filing', 'Hardware', 50000001, 200000000, '5 Crores to 20 Crores', 15000, 18.00, 'Monthly'),

('GST Filing', 'Work Contractor + Builder', 0, 200000000, '0 to 20 Crores', 15000, 18.00, 'Monthly'),

('GST Filing', 'GST Registration Fees', 0, NULL, 'All Turnovers', 2100, 18.00, 'One Time'),

-- Income Tax Filing
('Income Tax Filing', 'Salaried', 0, NULL, 'All Turnovers', 1000, 18.00, 'Annually'),
('Income Tax Filing', 'Non Salaried', 0, NULL, 'All Turnovers', 2000, 18.00, 'Annually'),

-- Other Services
('Audit', '-', 0, NULL, 'All Turnovers', NULL, NULL, 'NA'),
('TDS/TCS Filing', '-', 0, NULL, 'All Turnovers', NULL, NULL, 'NA'),
('MCA Filing', '-', 0, NULL, 'All Turnovers', NULL, NULL, 'NA'),
('Company Formation', '-', 0, NULL, 'All Turnovers', NULL, NULL, 'NA'),
('Accounts', '-', 0, NULL, 'All Turnovers', NULL, NULL, 'NA'),

-- Financial Products
('LIC', '-', 0, NULL, 'All Turnovers', NULL, NULL, 'NA'),
('Mutual Funds', '-', 0, NULL, 'All Turnovers', NULL, NULL, 'NA'),
('Insurance', '-', 0, NULL, 'All Turnovers', NULL, NULL, 'NA');