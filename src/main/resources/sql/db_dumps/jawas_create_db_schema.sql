DROP TABLE IF EXISTS logging_event_property;
DROP TABLE IF EXISTS logging_event_exception;
DROP TABLE IF EXISTS logging_event;
DROP TABLE IF EXISTS order_user;
DROP TABLE IF EXISTS "order";
DROP TABLE IF EXISTS "user";
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS product_category;
DROP TABLE IF EXISTS supplier;


CREATE TABLE public.logging_event
(
  event_id          bigserial PRIMARY KEY,
  timestamp         bigint       NOT NULL,
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
  caller_line       char         NOT NULL
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
  name                varchar(254) NOT NULL,
  description         text         NOT NULL,
  default_price       numeric(100, 2)        NOT NULL,
  default_currency    char(3)      NOT NULL,
  product_category_id int,
  supplier_id         int,
  CONSTRAINT fk_product_product_category FOREIGN KEY (product_category_id) REFERENCES public.product_category (id),
  CONSTRAINT fk_product_supplier_id FOREIGN KEY (supplier_id) REFERENCES public.supplier (id)
);

CREATE TABLE public."user"
(
  id bigserial PRIMARY KEY,
  name varchar(256) NOT NULL,
  password_hash varchar(256) NOT NULL,
  email varchar(256) NOT NULL,
  phone_number int NOT NULL,
  billing_country varchar(256) NOT NULL,
  billing_city varchar(256) NOT NULL,
  billing_zipcode varchar(256) NOT NULL,
  billing_address varchar(256) NOT NULL,
  shipping_country varchar(256) NOT NULL,
  shipping_city varchar(256) NOT NULL,
  shipping_zipcode varchar(256) NOT NULL,
  shipping_address varchar(256) NOT NULL
);

CREATE TABLE public."order"
(
  id bigserial PRIMARY KEY,
  user_id int NOT NULL,
  status varchar(50) NOT NULL,
  total_price numeric(100,2) NOT NULL,
  CONSTRAINT fk_order_user_id FOREIGN KEY (user_id) REFERENCES public."user" (id)
);
COMMENT ON COLUMN public."order".status IS 'unshipped';

CREATE TABLE public.order_user
(
  id bigserial PRIMARY KEY,
  order_id int NOT NULL,
  product_id bigint NOT NULL,
  product_quantity int NOT NULL,
  CONSTRAINT order_user_fk_order_id FOREIGN KEY (order_id) REFERENCES public."order" (id),
  CONSTRAINT order_user_fk_product_id FOREIGN KEY (product_id) REFERENCES public.product (id)
);
