-- User Table: Insert test data
INSERT INTO "User" (id, username, password, email, role) VALUES
('user1', 'john_doe', 'hash_password123', 'john@example.com', 'USER'),
('user2', 'jane_smith', 'hash_password123', 'jane@example.com', 'USER'),
('user3', 'alice_jones', 'hash_password123', 'alice@example.com', 'USER'),
('user4', 'bob_brown', 'hash_password123', 'bob@example.com', 'USER'),
('user5', 'charlie_black', 'hash_password123', 'charlie@example.com', 'USER'),
('user6', 'david_white', 'hash_password123', 'david@example.com', 'ADMIN'),
('user7', 'eve_green', 'hash_password123', 'eve@example.com', 'USER'),
('user8', 'frank_yellow', 'hash_password123', 'frank@example.com', 'USER'),
('user9', 'grace_purple', 'hash_password123', 'grace@example.com', 'USER'),
('user10', 'henry_orange', 'hash_password123', 'henry@example.com', 'USER');

-- Asset Table: Insert test data for users' wallets (USDT, ETH, BTC only)
INSERT INTO Asset (user_id, crypto_type, balance, version) VALUES
('user1', 'USDT', 50000.00, 0),
('user1', 'BTC', 0.5, 0),
('user1', 'ETH', 2.0, 0),
('user2', 'USDT', 50000.00, 0),
('user2', 'BTC', 1.0, 0),
('user2', 'ETH', 5.0, 0),
('user3', 'USDT', 50000.00, 0),
('user3', 'ETH', 7.5, 0),
('user4', 'USDT', 50000.00, 0),
('user4', 'BTC', 0.25, 0),
('user5', 'USDT', 50000.00, 0),
('user5', 'BTC', 0.1, 0),
('user6', 'USDT', 50000.00, 0),
('user6', 'ETH', 3.0, 0),
('user7', 'USDT', 50000.00, 0),
('user7', 'BTC', 2.0, 0),
('user8', 'USDT', 50000.00, 0),
('user8', 'ETH', 1.5, 0),
('user9', 'USDT', 50000.00, 0),
('user9', 'ETH', 2.5, 0),
('user10', 'USDT', 50000.00, 0),
('user10', 'BTC', 0.8, 0);

-- Trade Table: Insert test trade transactions for ETH and BTC only
INSERT INTO Trade (id, user_id, crypto_pair, trade_type, trade_amount, trade_price, trade_timestamp) VALUES
('trade1', 'user1', 'BTC/USDT', 'BUY', 0.1, 50000.00, NOW()),
('trade2', 'user1', 'ETH/USDT', 'SELL', 1.0, 4000.00, NOW()),
('trade3', 'user2', 'BTC/USDT', 'BUY', 0.5, 48000.00, NOW()),
('trade4', 'user2', 'ETH/USDT', 'SELL', 2.0, 4100.00, NOW()),
('trade5', 'user3', 'ETH/USDT', 'BUY', 1.5, 4500.00, NOW()),
('trade6', 'user4', 'BTC/USDT', 'SELL', 0.2, 49000.00, NOW()),
('trade7', 'user5', 'BTC/USDT', 'BUY', 0.05, 49500.00, NOW()),
('trade8', 'user6', 'ETH/USDT', 'SELL', 0.75, 4300.00, NOW()),
('trade9', 'user7', 'BTC/USDT', 'BUY', 0.3, 50500.00, NOW()),
('trade10', 'user8', 'ETH/USDT', 'SELL', 0.5, 4400.00, NOW()),
('trade11', 'user9', 'ETH/USDT', 'BUY', 2.0, 4200.00, NOW()),
('trade12', 'user10', 'BTC/USDT', 'SELL', 0.1, 51000.00, NOW());

-- Price Table: Insert test price data for ETH/USDT and BTC/USDT
INSERT INTO Price (id, crypto_pair, bid_price, ask_price, "timestamp") VALUES
('price1', 'BTC/USDT', 49900.00, 50100.00, NOW()),
('price2', 'ETH/USDT', 4050.00, 4070.00, NOW()),
('price3', 'BTC/USDT', 50050.00, 50200.00, NOW()),
('price4', 'ETH/USDT', 4080.00, 4100.00, NOW());
