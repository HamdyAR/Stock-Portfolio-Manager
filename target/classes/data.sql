-- Sample data for Stock Portfolio Manager
-- This file will be automatically executed by Spring Boot on application startup

-- Insert sample stocks
INSERT INTO stocks (id, symbol, company_name, exchange, industry) VALUES
('550e8400-e29b-41d4-a716-446655440002', 'GOOGL', 'Alphabet Inc.', 'NASDAQ', 'Technology'),
('550e8400-e29b-41d4-a716-446655440005', 'AMZN', 'Amazon.com Inc.', 'NASDAQ', 'E-commerce'),
('550e8400-e29b-41d4-a716-446655440006', 'JPM', 'JPMorgan Chase & Co.', 'NYSE', 'Financial Services'),
('550e8400-e29b-41d4-a716-446655440007', 'JNJ', 'Johnson & Johnson', 'NYSE', 'Healthcare'),
('550e8400-e29b-41d4-a716-446655440008', 'V', 'Visa Inc.', 'NYSE', 'Financial Services'),
('550e8400-e29b-41d4-a716-446655440009', 'WMT', 'Walmart Inc.', 'NYSE', 'Retail');

-- Insert sample orders to create a realistic portfolio
-- These orders simulate trading activity over time


-- Google (GOOGL) transactions
INSERT INTO orders (id, stock_id, side, volume, price, order_timestamp) VALUES
('650e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440002', 'BUY', 30, 120.80, '2024-01-22 10:20:00'),
('650e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440002', 'BUY', 20, 125.40, '2024-03-05 15:30:00');


-- Amazon (AMZN) transactions
INSERT INTO orders (id, stock_id, side, volume, price, order_timestamp) VALUES
('650e8400-e29b-41d4-a716-446655440010', '550e8400-e29b-41d4-a716-446655440005', 'BUY', 60, 140.25, '2024-02-10 10:30:00');

-- JPMorgan (JPM) transactions
INSERT INTO orders (id, stock_id, side, volume, price, order_timestamp) VALUES
('650e8400-e29b-41d4-a716-446655440011', '550e8400-e29b-41d4-a716-446655440006', 'BUY', 80, 160.75, '2024-01-25 14:45:00'),
('650e8400-e29b-41d4-a716-446655440012', '550e8400-e29b-41d4-a716-446655440006', 'SELL', 30, 168.20, '2024-03-20 12:15:00');

-- Johnson & Johnson (JNJ) transactions
INSERT INTO orders (id, stock_id, side, volume, price, order_timestamp) VALUES
('650e8400-e29b-41d4-a716-446655440013', '550e8400-e29b-41d4-a716-446655440007', 'BUY', 90, 145.60, '2024-02-05 09:20:00');

-- Visa (V) transactions
INSERT INTO orders (id, stock_id, side, volume, price, order_timestamp) VALUES
('650e8400-e29b-41d4-a716-446655440014', '550e8400-e29b-41d4-a716-446655440008', 'BUY', 45, 250.30, '2024-01-18 15:50:00'),
('650e8400-e29b-41d4-a716-446655440015', '550e8400-e29b-41d4-a716-446655440008', 'BUY', 15, 255.80, '2024-03-08 11:25:00');

-- Walmart (WMT) transactions
INSERT INTO orders (id, stock_id, side, volume, price, order_timestamp) VALUES
('650e8400-e29b-41d4-a716-446655440016', '550e8400-e29b-41d4-a716-446655440009', 'BUY', 70, 55.45, '2024-02-15 13:40:00');


-- Current Portfolio Summary (calculated from orders above):
-- GOOGL: 50 shares (30 + 20)
-- AMZN: 60 shares
-- JPM: 50 shares (80 - 30)
-- JNJ: 90 shares
-- V: 60 shares (45 + 15)
-- WMT: 70 shares
