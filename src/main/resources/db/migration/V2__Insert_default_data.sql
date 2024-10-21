-- Insert default user into the User table
INSERT INTO "User" (user_id, username, password, email, role, created_at, updated_at)
VALUES
('user-1', 'testuser1', 'password1', 'testuser1@example.com', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user-2', 'testuser2', 'password2', 'testuser2@example.com', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert default wallet data for test users
INSERT INTO Wallet (user_id, crypto_type, balance)
VALUES
('user-1', 'USDT', 50000.00),
('user-1', 'BTC', 0.25),
('user-1', 'ETH', 1.5),
('user-2', 'USDT', 50000.00),
('user-2', 'BTC', 0.1),
('user-2', 'ETH', 2.0);

INSERT INTO Price (price_id, crypto_pair, bid_price, ask_price, price_source, timestamp) VALUES
('1', 'ETHUSDT', 1800.00, 1805.00, 'Binance', CURRENT_TIMESTAMP),
('2', 'BTCUSDT', 30000.00, 30050.00, 'Binance', CURRENT_TIMESTAMP);

-- Insert a default trade entry for testing
INSERT INTO Trade (trade_id, user_id, crypto_pair, trade_type, trade_amount, trade_price, trade_timestamp) VALUES
('1', 'user-1', 'ETHUSDT', 'BUY', 1.0, 1805.00, CURRENT_TIMESTAMP);
