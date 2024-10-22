-- User Table: To store users' data
CREATE TABLE "User" (
    user_id VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    role VARCHAR(50) DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_user_username_password ON "User"(username, password);
CREATE INDEX idx_user_email_password ON "User"(email, password);

-- Wallet Table: To store user's wallet balance per cryptocurrency
CREATE TABLE Asset (
    user_id VARCHAR(255) NOT NULL,
    crypto_type VARCHAR(10) NOT NULL,
    balance DECIMAL(20,8) NOT NULL,
    version INT NOT NULL DEFAULT 0,
    PRIMARY KEY (user_id, crypto_type),
    FOREIGN KEY (user_id) REFERENCES "User"(user_id)
);

CREATE INDEX idx_wallet_user_crypto ON Wallet(user_id, crypto_type);

-- Trade Table: To store trade transactions
CREATE TABLE Trade (
    trade_id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    crypto_pair VARCHAR(10) NOT NULL,
    trade_type VARCHAR(4) CHECK (trade_type IN ('BUY', 'SELL')),
    trade_amount DECIMAL(20,8) NOT NULL,
    trade_price DECIMAL(20,8) NOT NULL,
    trade_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES "User"(user_id)
);

CREATE INDEX idx_trade_user_timestamp_crypto_pair ON Trade(user_id, trade_timestamp, crypto_pair);

-- Price Table: To store price aggregation from different sources
CREATE TABLE Price (
    price_id VARCHAR(255) PRIMARY KEY,
    crypto_pair VARCHAR(10) NOT NULL,
    bid_price DECIMAL(20,8) NOT NULL,
    ask_price DECIMAL(20,8) NOT NULL,
    "timestamp" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_price_crypto_pair_timestamp ON Price(crypto_pair, "timestamp");
