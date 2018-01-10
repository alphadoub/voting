DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM dishes;
DELETE FROM restaurants;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('Admin', 'admin@gmail.com', 'adminPassword'),
  ('User', 'user@gmail.com', 'userPassword'),
  ('User2', 'user2@gmail.com', 'userPassword2'),
  ('User3', 'user3@gmail.com', 'userPassword3');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_ADMIN', 100000),
  ('ROLE_USER', 100000),
  ('ROLE_USER', 100001),
  ('ROLE_USER', 100002),
  ('ROLE_USER', 100003);

INSERT INTO restaurants (name) VALUES 'Restaurant1', 'Restaurant2', 'Restaurant3';

INSERT INTO dishes (name, price, date, restaurant_id) VALUES
  ('salad1', 600, now(), 100004),
  ('steak1', 800, now(), 100004),
  ('vine1',  550, now(), 100004),
  ('salad1', 500, '2017-12-31', 100004),
  ('steak1', 700, '2017-12-31', 100004),
  ('vine1',  450, '2017-12-31', 100004),
  ('salad1', 700, '2118-12-31', 100004),
  ('steak1', 900, '2118-12-31', 100004),
  ('vine1',  650, '2118-12-31', 100004),
  ('salad2', 540, now(), 100005),
  ('steak2', 750, now(), 100005),
  ('vine2',  500, now(), 100005),
  ('salad3', 570, now(), 100006),
  ('steak3', 770, now(), 100006),
  ('vine3',  530, now(), 100006);
