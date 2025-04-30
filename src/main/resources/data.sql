INSERT INTO PRODUCTS (name, price, quantity)
VALUES
    ('Kingston 32GB (2x16GB) DDR5 6000MHz CL30 FURY Beast Svart AMD EXPO/XMP 3.0', 129000, 50),
    ('Acer Predator Connect X7 5G WiFi 7 Router', 349000, 25),
    ('Gigabyte 27in MO27Q2 OLED QHD 240 Hz', 849000, 10),
    ('Logitech Pro X Superlight 2 Svart', 169000, 200),
    ('SteelSeries QcK XXL', 34900, 1000),
    ('Arozzi Arena Gaming Medium Desk Black', 199900, 10),
    ('APC Back-UPS PRO 2200 VA för Gaming', 479900, 5);

INSERT INTO CUSTOMERS (email)
VALUES
    ('user1@example.com'),
    ('user2@example.com'),
    ('user3@example.com');

INSERT INTO ORDERS (customerId, ordertimestamp)
VALUES
    (1, '20250401');

INSERT INTO ORDEREDPRODUCTS (productid, orderid, price, quantity)
VALUES
    (1, 1, 129000, 4);