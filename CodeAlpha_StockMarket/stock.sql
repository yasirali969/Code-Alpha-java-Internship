CREATE DATABASE stocktrading;
USE stocktrading;

CREATE TABLE stocks (
    stock_id INT AUTO_INCREMENT PRIMARY KEY,
    symbol VARCHAR(20) NOT NULL UNIQUE,
    company_name VARCHAR(100) NOT NULL,
    current_price DOUBLE NOT NULL,
    available_shares DOUBLE NOT NULL
);

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    phone_no VARCHAR(20) NOT NULL,
    balance DOUBLE NOT NULL
);

CREATE TABLE holdings (
    holding_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    stock_id INT NOT NULL,
    quantity INT NOT NULL,
    avg_price DOUBLE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (stock_id) REFERENCES stocks(stock_id)
);

CREATE TABLE transactions (
    txn_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    stock_id INT NOT NULL,
    type VARCHAR(10) NOT NULL,
    quantity INT NOT NULL,
    price DOUBLE NOT NULL,
    total DOUBLE NOT NULL,
    txn_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (stock_id) REFERENCES stocks(stock_id)
);

INSERT INTO stocks(symbol, company_name, current_price, available_shares) VALUES
('AAPL','Apple Inc.',180.50,1000),
('TSLA','Tesla Inc.',250.75,800),
('GOOGL','Google LLC',140.30,1200),
('MSFT','Microsoft Corp.',320.90,900),
('AMZN','Amazon.com',175.60,600),
('NFLX','Netflix Inc.',450.25,500);

INSERT INTO users(username, phone_no, balance)
VALUES ('Yasir Ali','03283029322',45000);

INSERT INTO stocks(symbol, company_name, current_price, available_shares) VALUES
('AAPL','Apple Inc.',180.50,1000),
('TSLA','Tesla Inc.',250.75,800),
('GOOGL','Google LLC',140.30,1200),
('MSFT','Microsoft Corp.',320.90,900),
('AMZN','Amazon.com',175.60,600),
('NFLX','Netflix Inc.',450.25,500);

INSERT INTO users(username, phone_no, balance) VALUES
('Yasir Ali','03283029322',45000),
('Ali Hassan','03001234567',30000),
('Ahmed Raza','03111222333',55000);

INSERT INTO holdings(user_id, stock_id, quantity, avg_price) VALUES
(1,1,20,180.50),
(1,2,10,250.75),
(2,3,15,140.30),
(2,4,5,320.90),
(3,5,8,175.60);

INSERT INTO transactions(user_id, stock_id, type, quantity, price, total) VALUES
(1,1,'BUY',20,180.50,3610.00),
(1,2,'BUY',10,250.75,2507.50),
(2,3,'BUY',15,140.30,2104.50),
(2,4,'BUY',5,320.90,1604.50),
(3,5,'BUY',8,175.60,1404.80),
(1,1,'SELL',5,185.00,925.00);

drop database stocktrading;