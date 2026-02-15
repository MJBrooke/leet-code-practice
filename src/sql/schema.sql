-- Create Merchants Table
CREATE TABLE merchants (
                           id VARCHAR(50) PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           country_code CHAR(2),
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Transactions Table
CREATE TABLE transactions (
                              id SERIAL PRIMARY KEY,
                              merchant_id VARCHAR(50) REFERENCES merchants(id),
                              amount DECIMAL(19, 4) NOT NULL,
                              currency CHAR(3) NOT NULL,
                              status VARCHAR(20) NOT NULL, -- SUCCESS, FAILURE, REFUNDED, PENDING
                              created_at TIMESTAMP NOT NULL
);

-- Indices for performance
CREATE INDEX idx_tx_merchant_status ON transactions(merchant_id, status);
CREATE INDEX idx_tx_created_at ON transactions(created_at);

-- Insert Merchants
INSERT INTO merchants (id, name, country_code) VALUES
                                                   ('M_001', 'Starlight Coffee', 'NL'),
                                                   ('M_002', 'Global Gadgets', 'US'),
                                                   ('M_003', 'Nordic Knits', 'SE');

-- Insert Transactions
INSERT INTO transactions (merchant_id, amount, currency, status, created_at) VALUES
-- Merchant A: Standard Volume
('M_001', 10.50, 'EUR', 'SUCCESS', '2025-02-15 10:00:00'),
('M_001', 15.00, 'EUR', 'SUCCESS', '2025-02-15 10:05:00'),
('M_001', 500.00, 'EUR', 'FAILURE', '2025-02-15 10:10:00'),

-- Merchant B: High Volume + Duplicate Trigger
('M_002', 4500.00, 'USD', 'SUCCESS', '2025-02-15 12:00:00'),
('M_002', 6000.00, 'USD', 'SUCCESS', '2025-02-15 12:01:00'),
-- DUPLICATE: Same merchant, amount, and currency within 30 seconds
('M_002', 6000.00, 'USD', 'SUCCESS', '2025-02-15 12:01:30'),

-- Merchant C: Only failures
('M_003', 100.00, 'SEK', 'FAILURE', '2025-02-15 09:00:00'),

-- Mix of 2025 and 2026 data to test date filtering
('M_001', 25.00, 'EUR', 'SUCCESS', '2025-12-31 23:59:00'),
('M_001', 30.00, 'EUR', 'SUCCESS', '2026-01-01 00:01:00');

-- Insert refund txns
INSERT INTO transactions (merchant_id, amount, currency, status, created_at) VALUES
('M_001', 10.50, 'EUR', 'REFUNDED', '2025-02-20 09:00:00'),
('M_001', 5.00, 'EUR', 'REFUNDED', '2025-02-21 11:00:00'),
('M_002', 100.00, 'USD', 'REFUNDED', '2025-03-01 15:00:00'),
('M_002', 150.00, 'USD', 'REFUNDED', '2025-03-05 10:00:00');