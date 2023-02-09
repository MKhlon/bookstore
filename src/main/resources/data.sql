INSERT INTO booking_status (name) VALUES ('SUBMITTED');
INSERT INTO booking_status (name) VALUES ('REJECTED');
INSERT INTO booking_status (name) VALUES ('APPROVED');
INSERT INTO booking_status (name) VALUES ('CANCELLED');
INSERT INTO booking_status (name) VALUES ('IN_DELIVERY');
INSERT INTO booking_status (name) VALUES ('COMPLETED');

INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('MANAGER');
INSERT INTO roles (name) VALUES ('CUSTOMER');

INSERT INTO products (name, description, author, price, image_path) VALUES ('Harry Potter', 'Science fiction best seller', 'J.K.Rowling', '12.50', 'http://www.testimages.harry_potter.jpg');
INSERT INTO products (name, description, author, price, image_path) VALUES ('Baby Shark', 'Kids story about shark family', 'Internet', '200', 'http://www.testimages.baby_shark.jpg');
INSERT INTO users (name, address, email, phone, role_id, login, password) VALUES ('Maria Khlon', 'Krakow, Dobrego Pasterza','maria_khlon@test.com', '+48111222333', '1', 'testLogin', 'testPassword');

INSERT INTO bookings (user_id, product_id, delivery_address, delivery_date, delivery_time, status_id, quantity) VALUES ('1', '1', 'Poland Krakow', '2022-05-15', '02:00:00', '1', '1');




