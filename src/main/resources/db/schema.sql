-- 1) Create DB
CREATE DATABASE IF NOT EXISTS ocean_view_resort
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE ocean_view_resort;

-- 2) Users (for Login)
CREATE TABLE IF NOT EXISTS users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(20) NOT NULL DEFAULT 'STAFF',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3) Rooms
CREATE TABLE IF NOT EXISTS rooms (
  room_id INT AUTO_INCREMENT PRIMARY KEY,
  room_no VARCHAR(10) NOT NULL UNIQUE,
  room_type VARCHAR(20) NOT NULL,        -- SINGLE/DOUBLE/SUITE
  ac_type VARCHAR(10) NOT NULL DEFAULT 'NON_AC', -- AC/NON_AC
  price_per_night DECIMAL(10,2) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE'  -- AVAILABLE/MAINTENANCE
);

-- 4) Customers
CREATE TABLE IF NOT EXISTS customers (
  customer_id INT AUTO_INCREMENT PRIMARY KEY,
  full_name VARCHAR(100) NOT NULL,
  phone VARCHAR(20) NOT NULL,
  email VARCHAR(100),
  nic VARCHAR(20)
);

-- 5) Reservations
CREATE TABLE IF NOT EXISTS reservations (
  reservation_id INT AUTO_INCREMENT PRIMARY KEY,
  customer_id INT NOT NULL,
  room_id INT NOT NULL,
  check_in DATE NOT NULL,
  check_out DATE NOT NULL,
  nights INT NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED', -- CONFIRMED/CANCELLED
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_res_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
  CONSTRAINT fk_res_room FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);

-- Helpful index for date searches
CREATE INDEX idx_res_dates ON reservations(check_in, check_out);

-- 5.1) Bills (Payment status tracking)
CREATE TABLE IF NOT EXISTS bills (
  bill_id INT AUTO_INCREMENT PRIMARY KEY,
  reservation_id INT NOT NULL UNIQUE,
  sub_total DECIMAL(10,2) NOT NULL,
  service_charge DECIMAL(10,2) NOT NULL,
  tax DECIMAL(10,2) NOT NULL,
  total DECIMAL(10,2) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'UNPAID', -- UNPAID/PAID
  paid_at TIMESTAMP NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_bill_reservation FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id)
);

-- 5.2) Reservation audit actions (for manager overrides)
CREATE TABLE IF NOT EXISTS reservation_actions (
  action_id INT AUTO_INCREMENT PRIMARY KEY,
  reservation_id INT NOT NULL,
  user_id INT NOT NULL,
  username VARCHAR(50) NOT NULL,
  role VARCHAR(20) NOT NULL,
  action VARCHAR(40) NOT NULL,          -- CANCEL, CANCEL_PAID_OVERRIDE, etc.
  reason VARCHAR(500),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_ra_res (reservation_id),
  CONSTRAINT fk_ra_reservation FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id),
  CONSTRAINT fk_ra_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- 6) Seed rooms (sample)
INSERT INTO rooms (room_no, room_type, ac_type, price_per_night, status) VALUES
('R101','SINGLE', 'AC',     12000.00,'AVAILABLE'),
('R102','SINGLE', 'NON_AC', 11000.00,'AVAILABLE'),
('R201','DOUBLE', 'AC',     18000.00,'AVAILABLE'),
('R202','DOUBLE', 'NON_AC', 17000.00,'AVAILABLE'),
('R301','SUITE',  'AC',     30000.00,'AVAILABLE')
ON DUPLICATE KEY UPDATE room_no=room_no;

-- 7) Seed admin user
-- NOTE: for now password_hash is plain text "admin123" (we will replace with hashing in coding step)
INSERT INTO users (username, password_hash, role) VALUES
('admin', 'admin123', 'MANAGER')
ON DUPLICATE KEY UPDATE username=username;