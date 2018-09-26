DROP TABLE IF EXISTS logging_event_property;
DROP TABLE IF EXISTS logging_event_exception;
DROP TABLE IF EXISTS logging_event;
DROP TABLE IF EXISTS order_product;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS product_category;
DROP TABLE IF EXISTS supplier;
DROP TABLE IF EXISTS "order";
DROP TABLE IF EXISTS "user";


CREATE TABLE public.logging_event
(
  event_id          bigserial PRIMARY KEY,
  timestmp          bigint       NOT NULL,
  formatted_message text         NOT NULL,
  logger_name       varchar(254) NOT NULL,
  level_string      varchar(254) NOT NULL,
  reference_flag    smallint,
  thread_name       varchar(254),
  arg0              varchar(254),
  arg1              varchar(254),
  arg2              varchar(254),
  arg3              varchar(254),
  caller_filename   varchar(254) NOT NULL,
  caller_class      varchar(254) NOT NULL,
  caller_method     varchar(254) NOT NULL,
  caller_line       varchar(254) NOT NULL
);

CREATE TABLE public.logging_event_property
(
  id           bigserial PRIMARY KEY,
  event_id     bigint       NOT NULL,
  mapped_key   varchar(254) NOT NULL,
  mapped_value text,
  CONSTRAINT fk_logging_event_property FOREIGN KEY (event_id) REFERENCES public.logging_event (event_id)
);

CREATE TABLE public.logging_event_exception
(
  id         bigserial PRIMARY KEY,
  event_id   bigint       NOT NULL,
  i          smallint     NOT NULL,
  trace_line varchar(254) NOT NULL,
  CONSTRAINT fk_logging_event_exception FOREIGN KEY (event_id) REFERENCES public.logging_event (event_id)
);

CREATE TABLE public.product_category
(
  id          serial PRIMARY KEY,
  name        varchar(254) NOT NULL,
  description text         NOT NULL,
  department  varchar(254) NOT NULL
);

CREATE TABLE public.supplier
(
  id          serial PRIMARY KEY,
  name        varchar(254) NOT NULL,
  description text         NOT NULL
);

CREATE TABLE public.product
(
  id                  serial PRIMARY KEY,
  name                varchar(254)    NOT NULL,
  description         text            NOT NULL,
  default_price       numeric(100, 2) NOT NULL,
  default_currency    char(3)         NOT NULL,
  product_category_id int,
  supplier_id         int,
  CONSTRAINT fk_product_product_category FOREIGN KEY (product_category_id) REFERENCES public.product_category (id),
  CONSTRAINT fk_product_supplier_id FOREIGN KEY (supplier_id) REFERENCES public.supplier (id)
);

CREATE TABLE public."user"
(
  id               bigserial PRIMARY KEY,
  name             varchar(256) NOT NULL,
  password_hash    varchar(256) NOT NULL,
  email            varchar(256) NOT NULL,
  phone_number     int          NOT NULL,
  billing_country  varchar(256) NOT NULL,
  billing_city     varchar(256) NOT NULL,
  billing_zipcode  varchar(256) NOT NULL,
  billing_address  varchar(256) NOT NULL,
  shipping_country varchar(256) NOT NULL,
  shipping_city    varchar(256) NOT NULL,
  shipping_zipcode varchar(256) NOT NULL,
  shipping_address varchar(256) NOT NULL
);

CREATE TABLE public."order"
(
  id          bigserial PRIMARY KEY,
  user_id     int             NOT NULL,
  status      varchar(50)     NOT NULL,
  total_price numeric(100, 2) NOT NULL,
  CONSTRAINT fk_order_product_id FOREIGN KEY (user_id) REFERENCES public."user" (id)
);
COMMENT ON COLUMN public."order".status
IS 'UNSHIPPED';

CREATE TABLE public.order_product
(
  order_id         int    NOT NULL,
  product_id       bigint NOT NULL,
  product_quantity int    NOT NULL,
  CONSTRAINT order_product_fk_order_id FOREIGN KEY (order_id) REFERENCES public."order" (id),
  CONSTRAINT order_product_fk_product_id FOREIGN KEY (product_id) REFERENCES public.product (id),
  CONSTRAINT order_id_product_id_composite_pk PRIMARY KEY (order_id, product_id)
);


-- Insert data

INSERT INTO public."user" (id, name, password_hash, email, phone_number, billing_country, billing_city, billing_zipcode, billing_address, shipping_country, shipping_city, shipping_zipcode, shipping_address) VALUES (DEFAULT, 'Guest', '5d9c68c6c50ed3d02a2fcf54f63993b6', 'jawas@jawas.hu', 702225555, 'Hungary', 'Budapest', '1133', 'Blaha Lujza tér 1.', 'Hungary', 'Budapest', '1133', 'Blaha Lujza tér 1.');


INSERT INTO supplier
(id, name, description)
VALUES (DEFAULT, 'Amazon', 'Digital content and services');

INSERT INTO supplier
(id, name, description)
VALUES (DEFAULT, 'Lenovo', 'Computers');

INSERT INTO supplier
(id, name, description)
VALUES (DEFAULT, 'Universe', 'Stars, black holes, atoms, space, energy');

INSERT INTO supplier
(id, name, description)
VALUES (DEFAULT, 'Earth', 'Water, breatheable air, solid land mass, energy');

INSERT INTO supplier
(id, name, description)
VALUES (DEFAULT, 'Ebay', 'Everything you can imagine');


INSERT INTO product_category
(id, name, description, department)
VALUES (DEFAULT, 'Tablet', 'A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.', 'Hardware');

INSERT INTO product_category
(id, name, description, department)
VALUES (DEFAULT, 'Esoteric', 'Paranormal energies.', 'Energy');

INSERT INTO product_category
(id, name, description, department)
VALUES (DEFAULT, 'Pillow', 'Confortable Pillows.', 'Body Pillow');



INSERT INTO product
(id, name, description, default_price, default_currency, product_category_id, supplier_id)
VALUES (DEFAULT, 'Amazon Fire', 'Digital content and services', 49.91, 'USD', 1, 1);

INSERT INTO product
(id, name, description, default_price, default_currency, product_category_id, supplier_id)
VALUES (DEFAULT, 'Lenovo IdeaPad Miix 700', 'Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports. Adjustable kickstand.', 479.11, 'USD', 1, 2);

INSERT INTO product
(id, name, description, default_price, default_currency, product_category_id, supplier_id)
VALUES (DEFAULT, 'Amazon Fire HD 8', 'Amazon''s latest Fire HD 8 tablet is a great value for media consumption.', 89.21, 'USD', 1, 1);

INSERT INTO product
(id, name, description, default_price, default_currency, product_category_id, supplier_id)
VALUES (DEFAULT, 'Health Energy', 'Coming from mother Earth, this is the most efficient energy on the market, if you want to recover from any illness or just stay healthy, our first class Health Energy is just for you.', 19.99, 'USD', 2, 4);

INSERT INTO product
(id, name, description, default_price, default_currency, product_category_id, supplier_id)
VALUES (DEFAULT, 'Life Energy', 'We are selling the most healthiest life energy available on Planet Earth. Get your package now, and get revitalized.', 99.99, 'USD', 2, 3);

INSERT INTO product
(id, name, description, default_price, default_currency, product_category_id, supplier_id)
VALUES (DEFAULT, 'Money Energy', 'You wanna get rich quick? Then, order this money energy package right now and become rich in no time. It is handy, quick to absorb and recommended by 9 out of 10 rich people', 199.89, 'USD', 2, 4);

INSERT INTO product
(id, name, description, default_price, default_currency, product_category_id, supplier_id)
VALUES (DEFAULT, 'Maiden Body Pillow', 'Maiden body pillow, designed to fulfill all your desires.', 80.33, 'USD', 3, 5);

INSERT INTO product
(id, name, description, default_price, default_currency, product_category_id, supplier_id)
VALUES (DEFAULT, 'Levi Body Pillow', 'A body pillow of Levi, designed to fulfill all your desires.', 50.76, 'USD', 3, 5);

INSERT INTO product
(id, name, description, default_price, default_currency, product_category_id, supplier_id)
VALUES (DEFAULT, 'Sebastian Body Pillow', 'A body pillow of Sebastian, designed to fulfill all your desires.', 40.43, 'USD', 3, 5);

