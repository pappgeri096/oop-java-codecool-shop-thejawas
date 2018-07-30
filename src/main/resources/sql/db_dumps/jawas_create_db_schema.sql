DROP TABLE IF EXISTS logging_event_property;
DROP TABLE IF EXISTS logging_event_exception;
DROP TABLE IF EXISTS logging_event;
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