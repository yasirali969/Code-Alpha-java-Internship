CREATE DATABASE hotel_db;
USE hotel_db;

CREATE TABLE rooms (
    room_id VARCHAR(50) PRIMARY KEY,
    category VARCHAR(50),
    price DOUBLE,
    availability BOOLEAN
);

CREATE TABLE customers (
    customer_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100)
);

CREATE TABLE reservations (
    reservation_id VARCHAR(50) PRIMARY KEY,
    customer_id VARCHAR(50),
    room_id VARCHAR(50),
    check_in_date VARCHAR(50),
    check_out_date VARCHAR(50),
    payment_status BOOLEAN,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);

-- Seed initial data just like your original constructor did
INSERT INTO rooms VALUES 
('R1', 'Suite', 4500, true),
('R2', 'Deluxe', 3500, true),
('R3', 'Standard', 2500, true),
('R4', 'Suite', 5000, true),
('R5', 'Economy', 1500, true);

insert into rooms values
('R6', 'Suite', 6000.00, TRUE),
('R7', 'Deluxe', 4000.00, FALSE),  -- Yeh occupied dikhayega
('R8', 'Standard', 2800.00, TRUE),
('R9', 'Economy', 1800.00, TRUE),
('R10', 'Suite', 5500.00, TRUE),
('R11', 'Deluxe', 3800.00, TRUE),
('R12', 'Standard', 2600.00, FALSE), -- Yeh occupied dikhayega
('R13', 'Economy', 1600.00, TRUE),
('R14', 'Deluxe', 4200.00, TRUE),
('R15', 'Suite', 7000.00, TRUE);

INSERT INTO customers VALUES 
('C1', 'Yasir Ali', '03283028969', 'yasir1@gmail.com'),
('C2', 'Ali Khan', '03001234567', 'ali@gmail.com'),
('C3', 'Ahmed', '03111222333', 'ahmed@gmail.com'),
('C4', 'Usman', '03339876543', 'usman@gmail.com'),
('C5', 'Hassan', '03445566778', 'hassan@gmail.com');


select * from hotel_db.rooms;
select * from customers;
select * from reservations;

drop database hotel_db;