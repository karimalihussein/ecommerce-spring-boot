-- Add two users
INSERT INTO users (id, email, username, password, first_name, last_name) 
VALUES 
(1, 'user1@example.com', 'user1', '$2a$10$bYAcfToJhWZIKwUAFWlHJe0Xucg7D7Xxf/zRJyIIPbM8P.t9ZVrFK', 'John', 'Doe'),
(2, 'user2@example.com', 'user2', '$2a$10$bYAcfToJhWZIKwUAFWlHJe0Xucg7D7Xxf/zRJyIIPbM8P.t9ZVrFK', 'Jane', 'Smith');

-- Replace the id here with the first user id you want to have ownership of the orders.
SET @userId1 = 1;
-- Replace the id here with the second user id you want to have ownership of the orders.
SET @userId2 = 2;

-- Products
INSERT INTO products (`name`, description, price) VALUES ('Product #1', 'Product one short description.',  5.50);
INSERT INTO products (`name`, description, price) VALUES ('Product #2', 'Product two short description.',  10.56);
INSERT INTO products (`name`, description, price) VALUES ('Product #3', 'Product three short description.',  2.74);
INSERT INTO products (`name`, description, price) VALUES ('Product #4', 'Product four short description.',  15.69);
INSERT INTO products (`name`, description, price) VALUES ('Product #5', 'Product five short description.', 42.59);

-- Get Product IDs
SET @product1 = 0;
SET @product2 = 0;
SET @product3 = 0;
SET @product4 = 0;
SET @product5 = 0;

SELECT @product1 := id FROM products WHERE `name` = 'Product #1';
SELECT @product2 := id FROM products WHERE `name` = 'Product #2';
SELECT @product3 := id FROM products WHERE name = 'Product #3';
SELECT @product4 := id FROM products WHERE name = 'Product #4';
SELECT @product5 := id FROM products WHERE name = 'Product #5';

-- Inventories
INSERT INTO inventories (product_id, quantity) VALUES (@product1, 5);
INSERT INTO inventories (product_id, quantity) VALUES (@product2, 8);
INSERT INTO inventories (product_id, quantity) VALUES (@product3, 12);
INSERT INTO inventories (product_id, quantity) VALUES (@product4, 73);
INSERT INTO inventories (product_id, quantity) VALUES (@product5, 2);

-- Addresses
INSERT INTO address (line_1, city, country, user_id) VALUES ('123 Tester Hill', 'Testerton', 'England', @userId1);
INSERT INTO address (line_1, city, country, user_id) VALUES ('312 Spring Boot', 'Hibernate', 'England', @userId2);

SET @address1 = 0;
SET @address2 = 0;

SELECT @address1 := id FROM address WHERE user_id = @userId1 ORDER BY id DESC LIMIT 1;
SELECT @address2 := id FROM address WHERE user_id = @userId2 ORDER BY id DESC LIMIT 1;

-- Orders
INSERT INTO orders (address_id, user_id) VALUES (@address1, @userId1);
INSERT INTO orders (address_id, user_id) VALUES (@address1, @userId1);
INSERT INTO orders (address_id, user_id) VALUES (@address1, @userId1);
INSERT INTO orders (address_id, user_id) VALUES (@address2, @userId2);
INSERT INTO orders (address_id, user_id) VALUES (@address2, @userId2);

SET @order1 = 0;
SET @order2 = 0;
SET @order3 = 0;
SET @order4 = 0;
SET @order5 = 0;

SELECT @order1 := id FROM orders WHERE address_id = @address1 AND user_id = @userId1 ORDER BY id DESC LIMIT 1;
SELECT @order2 := id FROM orders WHERE address_id = @address1 AND user_id = @userId1 ORDER BY id DESC LIMIT 1 OFFSET 1;
SELECT @order3 := id FROM orders WHERE address_id = @address1 AND user_id = @userId1 ORDER BY id DESC LIMIT 1 OFFSET 2;
SELECT @order4 := id FROM orders WHERE address_id = @address2 AND user_id = @userId2 ORDER BY id DESC LIMIT 1;
SELECT @order5 := id FROM orders WHERE address_id = @address2 AND user_id = @userId2 ORDER BY id DESC LIMIT 1 OFFSET 1;

-- Order Products
INSERT INTO order_products (order_id, product_id, quantity) VALUES (@order1, @product1, 5);
INSERT INTO order_products (order_id, product_id, quantity) VALUES (@order1, @product2, 5);
INSERT INTO order_products (order_id, product_id, quantity) VALUES (@order2, @product3, 5);
INSERT INTO order_products (order_id, product_id, quantity) VALUES (@order2, @product2, 5);
INSERT INTO order_products (order_id, product_id, quantity) VALUES (@order2, @product5, 5);
INSERT INTO order_products (order_id, product_id, quantity) VALUES (@order3, @product3, 5);
INSERT INTO order_products (order_id, product_id, quantity) VALUES (@order4, @product4, 5);
INSERT INTO order_products (order_id, product_id, quantity) VALUES (@order4, @product2, 5);
INSERT INTO order_products (order_id, product_id, quantity) VALUES (@order5, @product3, 5);
INSERT INTO order_products (order_id, product_id, quantity) VALUES (@order5, @product1, 5);