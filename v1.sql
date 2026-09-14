-- ==============================================================================
-- Aushadha Pharmacy Portal - Production Database Schema & Sample Data
-- ==============================================================================

CREATE DATABASE IF NOT EXISTS aushadha_db;
USE aushadha_db;

-- ------------------------------------------------------------------------------
-- 1. User & Authentication Domain
-- ------------------------------------------------------------------------------
CREATE TABLE role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    access_type VARCHAR(100)
);

CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone_no VARCHAR(20),
    address TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    role_id BIGINT,
    FOREIGN KEY (role_id) REFERENCES role(id)
);

-- ------------------------------------------------------------------------------
-- 2. Catalog & Medicine Domain
-- ------------------------------------------------------------------------------
CREATE TABLE category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE medicine (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    type VARCHAR(50),
    description TEXT,
    instructions TEXT,
    intake VARCHAR(255),
    price DECIMAL(10, 2) NOT NULL,
    image_url VARCHAR(255),
    category_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES category(id)
);

-- ------------------------------------------------------------------------------
-- 3. Inventory & Supplier Procurement Domain
-- ------------------------------------------------------------------------------
CREATE TABLE supplier (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_name VARCHAR(150) NOT NULL,
    person_name VARCHAR(100),
    email VARCHAR(100),
    phone_no VARCHAR(20),
    address TEXT,
    active_status BOOLEAN DEFAULT TRUE
);

CREATE TABLE stock (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    batch_number VARCHAR(50) NOT NULL,
    quantity INT NOT NULL,
    mfg_date DATE NOT NULL,
    exp_date DATE NOT NULL,
    note TEXT,
    medicine_id BIGINT,
    supplier_id BIGINT,
    FOREIGN KEY (medicine_id) REFERENCES medicine(id),
    FOREIGN KEY (supplier_id) REFERENCES supplier(id)
);

CREATE TABLE purchase_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_date DATETIME NOT NULL,
    expected_date DATE,
    status VARCHAR(50),
    supplier_id BIGINT,
    FOREIGN KEY (supplier_id) REFERENCES supplier(id)
);

-- ------------------------------------------------------------------------------
-- 4. Prescription & Order Management Domain
-- ------------------------------------------------------------------------------
CREATE TABLE prescription (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    document_url VARCHAR(255) NOT NULL,
    upload_at DATETIME NOT NULL,
    note TEXT,
    status VARCHAR(50) NOT NULL, -- PENDING, APPROVED, REJECTED
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE customer_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_date DATETIME NOT NULL,
    status VARCHAR(50) NOT NULL, -- PROCESSING, PREPARED, DISPATCHED, DELIVERED
    shipping_address TEXT,
    user_id BIGINT,
    prescription_id BIGINT NULL,
    assigned_to VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (prescription_id) REFERENCES prescription(id)
);

CREATE TABLE order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    order_id BIGINT,
    medicine_id BIGINT,
    FOREIGN KEY (order_id) REFERENCES customer_order(id),
    FOREIGN KEY (medicine_id) REFERENCES medicine(id)
);

-- ------------------------------------------------------------------------------
-- 5. Billing & Payment Domain
-- ------------------------------------------------------------------------------
CREATE TABLE invoice (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date DATETIME NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    discount DECIMAL(10, 2) DEFAULT 0.00,
    tax DECIMAL(10, 2) DEFAULT 0.00,
    net_total DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL, -- PAID, UNPAID, REFUNDED
    order_id BIGINT,
    FOREIGN KEY (order_id) REFERENCES customer_order(id)
);

CREATE TABLE pay_type (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description TEXT
);

CREATE TABLE payment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date DATETIME NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL, -- SUCCESS, PENDING, FAILED
    invoice_id BIGINT,
    pay_type_id BIGINT,
    FOREIGN KEY (invoice_id) REFERENCES invoice(id),
    FOREIGN KEY (pay_type_id) REFERENCES pay_type(id)
);

-- ==============================================================================
-- SAMPLE DATA INSERTS
-- ==============================================================================

-- Roles
INSERT INTO role (name, access_type) VALUES 
('ROLE_ADMIN', 'Full System Access'),
('ROLE_PHARMACIST', 'Prescription & Order Verification'),
('ROLE_INVENTORY_SUPERVISOR', 'Catalog & Stock Management'),
('ROLE_FINANCE_OFFICER', 'Billing & Ledger'),
('ROLE_CUSTOMER', 'Standard Web User');

-- Users (Passwords should be BCrypt hashed in the app, providing plain text placeholders here)
INSERT INTO user (full_name, email, password, phone_no, address, is_active, role_id) VALUES 
('System Admin', 'admin@aushadha.lk', '$2a$10$hashed_password_string', '0771234567', 'Colombo 03', 1, 1),
('Amal Pharmacist', 'pharmacist@aushadha.lk', '$2a$10$hashed_password_string', '0711234567', 'Kandy', 1, 2),
('Nimal Inventory', 'inventory@aushadha.lk', '$2a$10$hashed_password_string', '0721234567', 'Galle', 1, 3),
('Kamal Customer', 'kamal@gmail.com', '$2a$10$hashed_password_string', '0751234567', 'Matara', 1, 5);

-- Categories
INSERT INTO category (name, description) VALUES 
('Arishta', 'Fermented herbal decoctions used as tonics.'),
('Thaila', 'Medicated herbal oils for external and internal use.'),
('Kalka', 'Herbal pastes made from fresh plants.'),
('Kwatha', 'Concentrated herbal decoctions.'),
('Churnes', 'Fine powders of dried herbal medicines.');

-- Medicines
INSERT INTO medicine (name, type, description, instructions, intake, price, category_id) VALUES 
('Dasamoola Arishta', 'Liquid Tonic', 'Effective for respiratory issues and fatigue.', 'Store in a cool dry place.', '2 tablespoons twice a day after meals', 850.00, 1),
('Siddhalepa Thaila', 'Oil', 'Relieves muscle pain and headaches.', 'For external application only.', 'Apply gently on affected area', 450.00, 2),
('Sithopaladi Churna', 'Powder', 'Used for cough, cold, and digestive issues.', 'Keep away from moisture.', '1 teaspoon with honey', 600.00, 5);

-- Suppliers
INSERT INTO supplier (company_name, person_name, email, phone_no, address, active_status) VALUES 
('Lanka Herbal Roots Ltd', 'Sunil Perera', 'sunil@lankaherbal.lk', '0112345678', 'Kurunegala', 1),
('Ayurveda Growers Coop', 'Nayana Fernando', 'nayana@agc.lk', '0812345678', 'Matale', 1);

-- Stock (Batches)
INSERT INTO stock (batch_number, quantity, mfg_date, exp_date, note, medicine_id, supplier_id) VALUES 
('BAT-26-001', 150, '2026-01-10', '2027-01-10', 'Initial January production', 1, 1),
('BAT-26-002', 300, '2026-02-15', '2028-02-15', 'External oil batch', 2, 2);

-- Prescriptions
INSERT INTO prescription (document_url, upload_at, note, status, user_id) VALUES 
('/uploads/prescriptions/kamal_doc_1.pdf', '2026-09-14 10:00:00', 'Please check for alternatives if unavailable', 'APPROVED', 4);

-- Orders
INSERT INTO customer_order (order_date, status, shipping_address, user_id, prescription_id) VALUES 
('2026-09-14 10:30:00', 'PROCESSING', '123 Main St, Matara', 4, 1);

-- Order Items
INSERT INTO order_item (quantity, unit_price, order_id, medicine_id) VALUES 
(2, 850.00, 1, 1),
(1, 450.00, 1, 2);

-- Invoices
INSERT INTO invoice (date, total, discount, tax, net_total, status, order_id) VALUES 
('2026-09-14 10:35:00', 2150.00, 0.00, 0.00, 2150.00, 'PAID', 1);

-- Payment Types
INSERT INTO pay_type (name, description) VALUES 
('Credit Card', 'Visa/Mastercard Gateway'),
('COD', 'Cash on Delivery');

-- Payments
INSERT INTO payment (date, amount, status, invoice_id, pay_type_id) VALUES 
('2026-09-14 10:36:00', 2150.00, 'SUCCESS', 1, 1);