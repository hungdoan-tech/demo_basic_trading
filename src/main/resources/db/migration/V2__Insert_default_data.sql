-- User Table: Insert test data
INSERT INTO "User" (id, username, password, email, role) VALUES
('0025546592RqxzReee8b', 'john_doe', '$2a$12$KUT1KCeD18EMlXubU5Gatu3juzW7vDZluLlmJ/W/zn0vsIjzabxsu', 'john@example.com', 'USER'),
('0025546592Ed4fgKpNA8', 'jane_smith', '$2a$12$KUT1KCeD18EMlXubU5Gatu3juzW7vDZluLlmJ/W/zn0vsIjzabxsu', 'jane@example.com', 'USER'),
('0025546592HIJvbMloIu', 'alice_jones', '$2a$12$KUT1KCeD18EMlXubU5Gatu3juzW7vDZluLlmJ/W/zn0vsIjzabxsu', 'alice@example.com', 'USER'),
('0025546592WSh8dyNZD1', 'bob_brown', '$2a$12$KUT1KCeD18EMlXubU5Gatu3juzW7vDZluLlmJ/W/zn0vsIjzabxsu', 'bob@example.com', 'USER'),
('0025546592dSQPSTyaRi', 'charlie_black', '$2a$12$KUT1KCeD18EMlXubU5Gatu3juzW7vDZluLlmJ/W/zn0vsIjzabxsu', 'charlie@example.com', 'USER'),
('0025546592TaaPq2UhAJ', 'david_white', '$2a$12$KUT1KCeD18EMlXubU5Gatu3juzW7vDZluLlmJ/W/zn0vsIjzabxsu', 'david@example.com', 'ADMIN'),
('0025546592UrL2HGTSfA', 'eve_green', '$2a$12$KUT1KCeD18EMlXubU5Gatu3juzW7vDZluLlmJ/W/zn0vsIjzabxsu', 'eve@example.com', 'USER'),
('0025546592mbnQjtPVHL', 'frank_yellow', '$2a$12$KUT1KCeD18EMlXubU5Gatu3juzW7vDZluLlmJ/W/zn0vsIjzabxsu', 'frank@example.com', 'USER'),
('0025546592Dekm6JTMTJ', 'grace_purple', '$2a$12$KUT1KCeD18EMlXubU5Gatu3juzW7vDZluLlmJ/W/zn0vsIjzabxsu', 'grace@example.com', 'USER'),
('0025546592eX2WqbB49h', 'henry_orange', '$2a$12$KUT1KCeD18EMlXubU5Gatu3juzW7vDZluLlmJ/W/zn0vsIjzabxsu', 'henry@example.com', 'USER');

-- Asset Table: Insert test data for users' wallets (USDT, ETH, BTC only)
INSERT INTO Asset (user_id, crypto_type, balance, version) VALUES
('0025546592RqxzReee8b', 'USDT', 50000.00, 0),
('0025546592RqxzReee8b', 'BTC', 0.5, 0),
('0025546592RqxzReee8b', 'ETH', 2.0, 0),
('0025546592Ed4fgKpNA8', 'USDT', 50000.00, 0),
('0025546592Ed4fgKpNA8', 'BTC', 1.0, 0),
('0025546592Ed4fgKpNA8', 'ETH', 5.0, 0),
('0025546592HIJvbMloIu', 'USDT', 50000.00, 0),
('0025546592HIJvbMloIu', 'ETH', 7.5, 0),
('0025546592WSh8dyNZD1', 'USDT', 50000.00, 0),
('0025546592WSh8dyNZD1', 'BTC', 0.25, 0),
('0025546592dSQPSTyaRi', 'USDT', 50000.00, 0),
('0025546592dSQPSTyaRi', 'BTC', 0.1, 0),
('0025546592TaaPq2UhAJ', 'USDT', 50000.00, 0),
('0025546592TaaPq2UhAJ', 'ETH', 3.0, 0),
('0025546592UrL2HGTSfA', 'USDT', 50000.00, 0),
('0025546592UrL2HGTSfA', 'BTC', 2.0, 0),
('0025546592mbnQjtPVHL', 'USDT', 50000.00, 0),
('0025546592mbnQjtPVHL', 'ETH', 1.5, 0),
('0025546592Dekm6JTMTJ', 'USDT', 50000.00, 0),
('0025546592Dekm6JTMTJ', 'ETH', 2.5, 0),
('0025546592eX2WqbB49h', 'USDT', 50000.00, 0),
('0025546592eX2WqbB49h', 'BTC', 0.8, 0);

-- Trade Table: Insert test trade transactions for ETH and BTC only
INSERT INTO Trade (id, user_id, crypto_pair, trade_type, trade_amount, trade_price, trade_timestamp) VALUES
('0025546592jdjIdk8eJA', '0025546592RqxzReee8b', 'BTCUSDT', 'BUY', 0.1, 50000.00, NOW()),
('0025546592dC8WDrHhfI', '0025546592RqxzReee8b', 'ETHUSDT', 'SELL', 1.0, 4000.00, NOW()),
('0025546592llg4MoGOV9', '0025546592Ed4fgKpNA8', 'BTCUSDT', 'BUY', 0.5, 48000.00, NOW()),
('0025546592k427dwhcZe', '0025546592Ed4fgKpNA8', 'ETHUSDT', 'SELL', 2.0, 4100.00, NOW()),
('0025546592Ny1Os65uVB', '0025546592HIJvbMloIu', 'ETHUSDT', 'BUY', 1.5, 4500.00, NOW()),
('0025546592rpoZVTjqMT', '0025546592WSh8dyNZD1', 'BTCUSDT', 'SELL', 0.2, 49000.00, NOW()),
('0025546592isUIHmaswW', '0025546592dSQPSTyaRi', 'BTCUSDT', 'BUY', 0.05, 49500.00, NOW()),
('0025546592wgQts2yKky', '0025546592TaaPq2UhAJ', 'ETHUSDT', 'SELL', 0.75, 4300.00, NOW()),
('0025546592FJ09fuo6cB', '0025546592UrL2HGTSfA', 'BTCUSDT', 'BUY', 0.3, 50500.00, NOW()),
('0025546592feDyWuRrd2', '0025546592mbnQjtPVHL', 'ETHUSDT', 'SELL', 0.5, 4400.00, NOW()),
('0025546592KVC67Fsph3', '0025546592Dekm6JTMTJ', 'ETHUSDT', 'BUY', 2.0, 4200.00, NOW()),
('0025546592nrDgm8gqQG', '0025546592eX2WqbB49h', 'BTCUSDT', 'SELL', 0.1, 51000.00, NOW());

-- Price Table: Insert test price data for ETH/USDT and BTC/USDT
INSERT INTO Price (id, crypto_pair, bid_price, ask_price, "timestamp") VALUES
('0025546592WP7ZRkQ0GN', 'BTCUSDT', 49900.00, 50100.00, NOW()),
('0025546592yJRjugtPVJ', 'ETHUSDT', 4050.00, 4070.00, NOW()),
('0025546592FCpfYpZ3rI', 'BTCUSDT', 50050.00, 50200.00, NOW()),
('0025546592KJCNtGyMrg', 'ETHUSDT', 4080.00, 4100.00, NOW());