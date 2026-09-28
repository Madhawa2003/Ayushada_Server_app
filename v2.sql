-- ==============================================================================
-- Database Design & Development: Aushadha Pharmacy Portal
-- Complete DDL & DML Schema Script matching EER Diagram & SRS Requirements
-- ==============================================================================

DROP DATABASE IF EXISTS aushadha_db;
CREATE DATABASE aushadha_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE aushadha_db;

-- ------------------------------------------------------------------------------
-- 1. User & Access Control Domain (EER: role, user)
-- ------------------------------------------------------------------------------

CREATE TABLE role (
                      role_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(50) NOT NULL UNIQUE,
                      access_type VARCHAR(100) NOT NULL
);

CREATE TABLE user (
                      user_id VARCHAR(50) PRIMARY KEY,
                      full_name VARCHAR(150) NOT NULL,
                      email VARCHAR(100) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL,
                      phone_no VARCHAR(20) NOT NULL,
                      address VARCHAR(255) NOT NULL,
                      status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                      role_id BIGINT NOT NULL,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES role(role_id) ON UPDATE CASCADE
);

-- ------------------------------------------------------------------------------
-- 2. Catalog & Medicine Domain (EER: Category, Medicine)
-- ------------------------------------------------------------------------------

CREATE TABLE category (
                          category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL UNIQUE,
                          description TEXT
);

CREATE TABLE medicine (
                          medicine_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(150) NOT NULL,
                          sinhala_name VARCHAR(150),
                          type VARCHAR(50) NOT NULL,
                          price DECIMAL(10, 2) NOT NULL,
                          intake VARCHAR(255) NOT NULL,
                          instructions TEXT NOT NULL,
                          description TEXT NOT NULL,
                          image_url VARCHAR(255) NOT NULL,
                          prescription_required BOOLEAN NOT NULL DEFAULT FALSE,
                          is_archived BOOLEAN NOT NULL DEFAULT FALSE,
                          category_id BIGINT NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_medicine_category FOREIGN KEY (category_id) REFERENCES category(category_id) ON UPDATE CASCADE
);

-- ------------------------------------------------------------------------------
-- 3. Supplier & Inventory Domain (EER: Supplier, PurchaseOrder, Stock)
-- ------------------------------------------------------------------------------

CREATE TABLE supplier (
                          supplier_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          company_name VARCHAR(150) NOT NULL,
                          person_name VARCHAR(100) NOT NULL,
                          phone_no VARCHAR(20) NOT NULL,
                          email VARCHAR(100) NOT NULL,
                          address TEXT NOT NULL,
                          supplied_herbs VARCHAR(255),
                          rating DECIMAL(2, 1) DEFAULT 5.0,
                          active_status BOOLEAN NOT NULL DEFAULT TRUE,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE purchase_order (
                                po_id VARCHAR(50) PRIMARY KEY,
                                order_date DATE NOT NULL,
                                expected_date DATE NOT NULL,
                                status VARCHAR(30) NOT NULL DEFAULT 'Draft', -- Draft, Sent, Delivered, Cancelled
                                note TEXT,
                                total_amount DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
                                supplier_id BIGINT NOT NULL,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                CONSTRAINT fk_po_supplier FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id) ON UPDATE CASCADE
);

CREATE TABLE purchase_order_item (
                                     item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     item_name VARCHAR(150) NOT NULL,
                                     quantity_ordered INT NOT NULL,
                                     unit VARCHAR(20) NOT NULL DEFAULT 'kg',
                                     unit_cost DECIMAL(10, 2) NOT NULL,
                                     po_id VARCHAR(50) NOT NULL,
                                     CONSTRAINT fk_poitem_po FOREIGN KEY (po_id) REFERENCES purchase_order(po_id) ON DELETE CASCADE
);

CREATE TABLE stock (
                       stock_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       batch_number VARCHAR(50) NOT NULL UNIQUE,
                       quantity INT NOT NULL DEFAULT 0,
                       mfg_date DATE NOT NULL,
                       exp_date DATE NOT NULL,
                       warehouse_location VARCHAR(100) NOT NULL,
                       note TEXT,
                       medicine_id BIGINT NOT NULL,
                       supplier_id BIGINT,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       CONSTRAINT fk_stock_medicine FOREIGN KEY (medicine_id) REFERENCES medicine(medicine_id) ON UPDATE CASCADE,
                       CONSTRAINT fk_stock_supplier FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id) ON UPDATE CASCADE
);

-- ------------------------------------------------------------------------------
-- 4. Prescription & Order Management Domain (EER: prescription, Order, order_item)
-- ------------------------------------------------------------------------------

CREATE TABLE prescription (
                              prescription_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              prescription_number VARCHAR(50) NOT NULL UNIQUE,
                              upload_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              document_url LONGTEXT NOT NULL,
                              file_name VARCHAR(255),
                              file_type VARCHAR(50),
                              note TEXT,
                              status VARCHAR(40) NOT NULL DEFAULT 'Pending Verification', -- Pending Verification, Under Review, Awaiting Approval, Payment Pending, Paid, Preparing, Out for Delivery, Delivered, Rejected
                              doctor_name VARCHAR(150),
                              ayurvedic_reg_no VARCHAR(100),
                              pharmacist_note TEXT,
                              rejection_reason TEXT,
                              delivery_fee DECIMAL(10, 2) DEFAULT 250.00,
                              user_id VARCHAR(50) NOT NULL,
                              verified_by VARCHAR(50),
                              verified_at TIMESTAMP NULL,
                              CONSTRAINT fk_prescription_user FOREIGN KEY (user_id) REFERENCES user(user_id) ON UPDATE CASCADE,
                              CONSTRAINT fk_prescription_verifier FOREIGN KEY (verified_by) REFERENCES user(user_id) ON UPDATE CASCADE
);

CREATE TABLE customer_order (
                                order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                order_number VARCHAR(50) NOT NULL UNIQUE,
                                order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                status VARCHAR(40) NOT NULL DEFAULT 'Processing', -- Processing, Prepared, Dispatched, Delivered, Cancelled
                                shipping_address TEXT NOT NULL,
                                delivery_fee DECIMAL(10, 2) NOT NULL DEFAULT 250.00,
                                assigned_to VARCHAR(100),
                                user_id VARCHAR(50) NOT NULL,
                                prescription_id BIGINT NULL,
                                CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES user(user_id) ON UPDATE CASCADE,
                                CONSTRAINT fk_order_prescription FOREIGN KEY (prescription_id) REFERENCES prescription(prescription_id) ON UPDATE CASCADE
);

CREATE TABLE order_item (
                            order_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            quantity INT NOT NULL,
                            unit_price DECIMAL(10, 2) NOT NULL,
                            order_id BIGINT NOT NULL,
                            medicine_id BIGINT NOT NULL,
                            CONSTRAINT fk_orderitem_order FOREIGN KEY (order_id) REFERENCES customer_order(order_id) ON DELETE CASCADE,
                            CONSTRAINT fk_orderitem_medicine FOREIGN KEY (medicine_id) REFERENCES medicine(medicine_id) ON UPDATE CASCADE
);

-- ------------------------------------------------------------------------------
-- 5. Billing, Invoice & Payment Domain (EER: Invoice, PayType, Payment)
-- ------------------------------------------------------------------------------

CREATE TABLE invoice (
                         invoice_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         invoice_number VARCHAR(50) NOT NULL UNIQUE,
                         date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         total DECIMAL(10, 2) NOT NULL,
                         discount DECIMAL(10, 2) DEFAULT 0.00,
                         tax DECIMAL(10, 2) DEFAULT 0.00,
                         delivery_fee DECIMAL(10, 2) DEFAULT 250.00,
                         net_total DECIMAL(10, 2) NOT NULL,
                         net_term VARCHAR(50) DEFAULT 'Immediate',
                         status VARCHAR(30) NOT NULL DEFAULT 'UNPAID', -- PAID, UNPAID, REFUNDED, CANCELLED
                         order_id BIGINT NOT NULL,
                         CONSTRAINT fk_invoice_order FOREIGN KEY (order_id) REFERENCES customer_order(order_id) ON UPDATE CASCADE
);

CREATE TABLE pay_type (
                          pay_type_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(50) NOT NULL UNIQUE,
                          description TEXT
);

CREATE TABLE payment (
                         payment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         payment_reference VARCHAR(100) NOT NULL UNIQUE,
                         date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         amount DECIMAL(10, 2) NOT NULL,
                         status VARCHAR(30) NOT NULL DEFAULT 'SUCCESS', -- SUCCESS, PENDING, FAILED
                         invoice_id BIGINT NOT NULL,
                         pay_type_id BIGINT NOT NULL,
                         CONSTRAINT fk_payment_invoice FOREIGN KEY (invoice_id) REFERENCES invoice(invoice_id) ON UPDATE CASCADE,
                         CONSTRAINT fk_payment_paytype FOREIGN KEY (pay_type_id) REFERENCES pay_type(pay_type_id) ON UPDATE CASCADE
);

-- ==============================================================================
-- DML: SAMPLE DATA INSERTS
-- ==============================================================================

-- 1. Insert Roles
INSERT INTO role (name, access_type) VALUES
                                         ('ROLE_ADMIN', 'Full System Access & User Administration'),
                                         ('ROLE_OPERATIONS_MANAGER', 'Procurement, Reporting & System Workflow'),
                                         ('ROLE_PHARMACIST', 'Prescription Review & Medicine Verification'),
                                         ('ROLE_INVENTORY_SUPERVISOR', 'Stock Control, Batches & Catalog Management'),
                                         ('ROLE_FINANCE_OFFICER', 'Billing, Invoices & Payment Ledger'),
                                         ('ROLE_CUSTOMER', 'Public Web Storefront & Order Placement');

-- 2. Insert Users (BCrypt hashed placeholder password: password123)
INSERT INTO user (user_id, full_name, email, password, phone_no, address, status, role_id) VALUES
                                                                                               ('USR-1001', 'Kasun Gunasekara', 'admin@aushadha.lk', '$2a$10$w6h1pP9uJp6w9L1wPzBf1eW5zVpTqF5Y7Z4N1X3B8Q0M2V7T6Q1W.', '0771234567', 'No 45, Temple Road, Colombo 03', 'ACTIVE', 1),
                                                                                               ('USR-1002', 'Nuwan Perera', 'operations@aushadha.lk', '$2a$10$w6h1pP9uJp6w9L1wPzBf1eW5zVpTqF5Y7Z4N1X3B8Q0M2V7T6Q1W.', '0785566778', '15, Baseline Road, Borella', 'ACTIVE', 2),
                                                                                               ('USR-1003', 'Dilani Fernando', 'pharmacist@aushadha.lk', '$2a$10$w6h1pP9uJp6w9L1wPzBf1eW5zVpTqF5Y7Z4N1X3B8Q0M2V7T6Q1W.', '0719876543', '12/B, Kandy Road, Malabe', 'ACTIVE', 3),
                                                                                               ('USR-1004', 'Chaminda Bandara', 'inventory@aushadha.lk', '$2a$10$w6h1pP9uJp6w9L1wPzBf1eW5zVpTqF5Y7Z4N1X3B8Q0M2V7T6Q1W.', '0753344556', '88, Hospital Junction, Gampaha', 'ACTIVE', 4),
                                                                                               ('USR-1005', 'Udara Weerasinghe', 'finance@aushadha.lk', '$2a$10$w6h1pP9uJp6w9L1wPzBf1eW5zVpTqF5Y7Z4N1X3B8Q0M2V7T6Q1W.', '0761122334', '104, Galle Road, Kalutara', 'ACTIVE', 5),
                                                                                               ('USR-1006', 'Sahan Wijesinghe', 'sahan.w@gmail.com', '$2a$10$w6h1pP9uJp6w9L1wPzBf1eW5zVpTqF5Y7Z4N1X3B8Q0M2V7T6Q1W.', '0709988776', '34, Station Road, Kurunegala', 'ACTIVE', 6);

-- 3. Insert Categories
INSERT INTO category (name, description) VALUES
                                             ('Arishta', 'Self-fermented herbal decoctions used as restorative and digestive tonics.'),
                                             ('Thaila', 'Medicated herbal oils for therapeutic external massage and application.'),
                                             ('Kalka', 'Concentrated herbal pastes prepared from fresh and dried botanical extracts.'),
                                             ('Kwatha', 'Boiled herbal decoctions for internal consumption to balance Doshas.'),
                                             ('Churnes', 'Fine medicinal powders composed of dried Ayurvedic plant roots and leaves.'),
                                             ('Guthika', 'Compressed traditional Ayurvedic herbal pills, tablets, and vatis.'),
                                             ('Raw Herbs', 'Pure unrefined herbs, roots, barks, and spices used for custom preparations.');

-- 4. Insert Medicines (Matching Frontend Catalog Components)
INSERT INTO medicine (medicine_id, name, sinhala_name, type, price, intake, instructions, description, image_url, prescription_required, is_archived, category_id) VALUES
                                                                                                                                                                       (1, 'Dasamoolarishtaya (375ml)', 'දශමූලාරිෂ්ටය', 'Fermented Tonic', 950.00, '30ml twice daily after meals', 'Store in a cool dry place', 'General restorative tonic effective for respiratory fatigue and fatigue balance.', '🏺', FALSE, FALSE, 1),
                                                                                                                                                                       (2, 'Ashwagandharishtaya (375ml)', 'අශ්වගන්ධාරිෂ්ටය', 'Fermented Tonic', 1100.00, '30ml twice daily with water', 'Keep away from direct heat', 'Calming tonic promoting vigor, stamina, and nervous system relaxation.', '🏺', FALSE, FALSE, 1),
                                                                                                                                                                       (3, 'Pinda Thailaya (100ml)', 'පිණ්ඩ තෛලය', 'Medicated Oil', 680.00, 'Apply externally on affected joints', 'For external use only', 'Traditional cooling herbal oil formulated to relieve burning sensations and gout.', '🧴', FALSE, FALSE, 2),
                                                                                                                                                                       (4, 'Mahanarayana Thailaya (100ml)', 'මහානාරායන තෛලය', 'Medicated Oil', 1850.00, 'Warm oil and massage affected limbs', 'For external application only', 'Classical sesame oil preparation for severe Vata joint stiffness and muscle recovery.', '🧴', FALSE, FALSE, 2),
                                                                                                                                                                       (5, 'Ashwagandha Kalka (100g)', 'අශ්වගන්ධ කල්කය', 'Herbal Paste', 520.00, '1 teaspoon with warm milk before sleep', 'Keep lid tightly closed', 'Botanical herbal paste providing strength and physical rejuvenation.', '🥣', FALSE, FALSE, 3),
                                                                                                                                                                       (6, 'Paspanguwa Kwatha', 'පස්පංගුව ක්වාථය', 'Boiled Decoction', 250.00, 'Boil contents in 4 cups of water down to 1 cup', 'Consume hot', 'Traditional five-herb brew for combating cold, headache, and congestion.', '☕', FALSE, FALSE, 4),
                                                                                                                                                                       (7, 'Yogaraja Guggulu Kwatha', 'යෝගරාජ ගුග්ගුලු ක්වාථය', 'Boiled Decoction', 2350.00, '30ml twice daily before breakfast and dinner', 'Requires physician monitoring', 'Potent multi-herb formula for chronic rheumatoid arthritis and spine support.', '💊', TRUE, FALSE, 4),
                                                                                                                                                                       (8, 'Sudarshana Churna (50g)', 'සුදර්ශන චූර්ණය', 'Herbal Powder', 420.00, '1/2 teaspoon with warm water or honey', 'Store dry', 'Antipyretic bitter herb powder known for immune support and temperature balance.', '🍃', FALSE, FALSE, 5),
                                                                                                                                                                       (9, 'Triphala Churna (100g)', 'ත්‍රිඵලා චූර්ණය', 'Herbal Powder', 550.00, '1 teaspoon mixed in lukewarm water before bedtime', 'Do not refrigerate', 'Three-fruit blend promoting healthy colon cleansing, digestion, and vision.', '🍃', FALSE, FALSE, 5),
                                                                                                                                                                       (10, 'Seetharama Vati', 'සීතාරාම වටී', 'Tablet / Vati', 3200.00, '1 tablet with ginger juice under supervision', 'Schedule C restricted', 'High-potency traditional mineral-herbal pill for acute fever management.', '💊', TRUE, FALSE, 6);

-- 5. Insert Suppliers
INSERT INTO supplier (supplier_id, company_name, person_name, phone_no, email, address, supplied_herbs, rating, active_status) VALUES
                                                                                                                                   (1, 'Lanka Herbal Botanicals Ltd', 'Sunil Kariyawasam', '0714567890', 'sales@lankaherbal.lk', '14, Industrial Estate, Kaduwela', 'Raw Dasamoola roots, Ginger, Dried Pepper', 4.8, TRUE),
                                                                                                                                   (2, 'Ruhunu Ayurvedic Growers Co-Op', 'Chandrasena Mallika', '0412233445', 'ruhunu.growers@gmail.com', '88, Matara Road, Kamburupitiya', 'Ashwagandha roots, Gotukola, Beli fruit', 4.5, TRUE),
                                                                                                                                   (3, 'Central Hills Distilleries', 'Gamini Dissanayake', '0817788990', 'orders@centraloils.lk', '22, Plantations Way, Gampola', 'Pure Sesame oil, Castor oil, Medicated bases', 3.2, FALSE);

-- 6. Insert Purchase Orders & Items
INSERT INTO purchase_order (po_id, order_date, expected_date, status, note, total_amount, supplier_id) VALUES
                                                                                                           ('PO-2026-081', '2026-09-18', '2026-09-25', 'Sent', 'Priority shipment needed for Dasamoolarishtaya production line.', 115000.00, 1),
                                                                                                           ('PO-2026-079', '2026-09-10', '2026-09-16', 'Delivered', 'Inspected and added directly into raw herb warehouse.', 47500.00, 2);

INSERT INTO purchase_order_item (item_name, quantity_ordered, unit, unit_cost, po_id) VALUES
                                                                                          ('Dasamoola Dry Root Mix', 100, 'kg', 850.00, 'PO-2026-081'),
                                                                                          ('Black Pepper (Crushed)', 25, 'kg', 1200.00, 'PO-2026-081'),
                                                                                          ('Fresh Ashwagandha Root', 50, 'kg', 950.00, 'PO-2026-079');

-- 7. Insert Stock Batches
INSERT INTO stock (stock_id, batch_number, quantity, mfg_date, exp_date, warehouse_location, note, medicine_id, supplier_id) VALUES
                                                                                                                                 (1, 'BATCH-2026-AR01', 45, '2026-01-15', '2027-01-15', 'Bay A - Shelf 02', 'Optimal fermentation conditions maintained', 1, 1),
                                                                                                                                 (2, 'BATCH-2026-AR02', 30, '2026-02-01', '2027-02-01', 'Bay A - Shelf 03', 'Secondary seasonal brew', 2, 2),
                                                                                                                                 (3, 'BATCH-2026-TH03', 12, '2026-02-10', '2026-10-15', 'Bay B - Shelf 01', 'Near expiry batch - prioritize dispense', 3, 3),
                                                                                                                                 (4, 'BATCH-2026-TH04', 25, '2026-03-01', '2028-03-01', 'Bay B - Shelf 02', 'High-purity sesame base', 4, 3),
                                                                                                                                 (5, 'BATCH-2026-KL05', 8, '2026-03-01', '2027-03-01', 'Bay C - Shelf 04', 'Low stock alert active', 5, 2),
                                                                                                                                 (6, 'BATCH-2026-KW06', 120, '2026-03-12', '2027-09-12', 'Bay A - Shelf 05', 'High turnover packaging', 6, 1),
                                                                                                                                 (7, 'BATCH-2026-KW07', 15, '2026-01-10', '2027-01-10', 'Bay D - Restricted Shelf', 'Controlled dispensing only', 7, 1),
                                                                                                                                 (8, 'BATCH-2025-GT10', 5, '2025-09-01', '2026-08-30', 'Bay D - Quarantine', 'Expired batch - marked for disposal', 10, 2);

-- 8. Insert Prescriptions
INSERT INTO prescription (prescription_id, prescription_number, upload_at, document_url, file_name, file_type, note, status, doctor_name, ayurvedic_reg_no, pharmacist_note, rejection_reason, delivery_fee, user_id, verified_by, verified_at) VALUES
                                                                                                                                                                                                                                                    (1, 'RX-2026-00125', '2026-09-18 10:30:00', 'https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?auto=format&fit=crop&w=800&q=80', 'prescription_001.jpg', 'image/jpeg', 'Please verify if these can be dispatched together today.', 'Awaiting Approval', 'Dr. W. K. Wickramasinghe', 'AYU-MED-4421', 'Prescription verified successfully. Formulations mapped to available stock batches.', NULL, 250.00, 'USR-1006', 'USR-1003', '2026-09-18 11:45:00'),
                                                                                                                                                                                                                                                    (2, 'RX-2026-00126', '2026-09-21 09:15:00', 'https://images.unsplash.com/photo-1576091160550-2173dba999ef?auto=format&fit=crop&w=800&q=80', 'prescription_003.jpg', 'image/jpeg', 'Heavy cough and catarrh since yesterday.', 'Pending Verification', NULL, NULL, NULL, NULL, 250.00, 'USR-1006', NULL, NULL),
                                                                                                                                                                                                                                                    (3, 'RX-2026-00112', '2026-09-12 14:20:00', '/uploads/prescriptions/prescription_002.pdf', 'prescription_002.pdf', 'application/pdf', 'Standard digestive consultation.', 'Delivered', 'Dr. Sunil Kariyawasam', 'AYU-MED-1109', 'All bottles packaged with seal intact.', NULL, 200.00, 'USR-1006', 'USR-1003', '2026-09-12 15:00:00');

-- 9. Insert Customer Orders & Order Items
INSERT INTO customer_order (order_id, order_number, order_date, status, shipping_address, delivery_fee, assigned_to, user_id, prescription_id) VALUES
                                                                                                                                                   (1, 'ORD-2026-5001', '2026-09-12 14:30:00', 'Delivered', '34, Station Road, Kurunegala', 200.00, 'Pronto Delivery', 'USR-1006', 3),
                                                                                                                                                   (2, 'ORD-2026-5002', '2026-09-18 11:00:00', 'Processing', '34, Station Road, Kurunegala', 250.00, 'Domestic Courier Service', 'USR-1006', 1);

INSERT INTO order_item (order_item_id, quantity, unit_price, order_id, medicine_id) VALUES
                                                                                        (1, 2, 950.00, 1, 1),
                                                                                        (2, 1, 680.00, 1, 3),
                                                                                        (3, 1, 2350.00, 2, 7),
                                                                                        (4, 2, 250.00, 2, 6);

-- 10. Insert Invoices
INSERT INTO invoice (invoice_id, invoice_number, date, total, discount, tax, delivery_fee, net_total, net_term, status, order_id) VALUES
                                                                                                                                      (1, 'INV-2026-9001', '2026-09-12 14:35:00', 2580.00, 0.00, 0.00, 200.00, 2780.00, 'Immediate', 'PAID', 1),
                                                                                                                                      (2, 'INV-2026-9002', '2026-09-18 11:15:00', 2850.00, 0.00, 0.00, 250.00, 3100.00, 'Immediate', 'UNPAID', 2);

-- 11. Insert Pay Types & Payments
INSERT INTO pay_type (pay_type_id, name, description) VALUES
                                                          (1, 'Credit / Debit Card', 'Visa, MasterCard gateway processing via IPG'),
                                                          (2, 'Bank Transfer', 'Direct electronic fund transfer with slip submission'),
                                                          (3, 'Cash on Delivery (COD)', 'Payment collected in cash by courier dispatch upon receipt');

INSERT INTO payment (payment_id, payment_reference, date, amount, status, invoice_id, pay_type_id) VALUES
    (1, 'PAY-TXN-20260912-001', '2026-09-12 14:36:00', 2780.00, 'SUCCESS', 1, 1);