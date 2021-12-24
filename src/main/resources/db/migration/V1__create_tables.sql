CREATE TABLE book
(
    id                 SERIAL PRIMARY KEY,
    name               varchar(500) NOT NULL,
    author             varchar(300) NOT NULL,
    genre              varchar(150),
    description        varchar(1000),
    available_quantity integer
        CONSTRAINT positive_or_zero_check CHECK ( available_quantity >= 0 ),
    price              integer,
    photo_url          varchar(500),
    UNIQUE (name, author)
);

CREATE TABLE customer
(
    id              SERIAL PRIMARY KEY,
    name            varchar(50)  NOT NULL,
    surname         varchar(100) NOT NULL,
    phone           varchar(20),
    email           varchar(150) NOT NULL CONSTRAINT unique_email UNIQUE,
    gender          varchar(6)   NOT NULL,
    password        varchar(200) NOT NULL,
    activated       varchar(5) DEFAULT 'false',
    activation_code varchar(50)
);

CREATE TABLE delivery_address
(
    customer_id  integer      NOT NULL REFERENCES customer (id) ON DELETE CASCADE,
    country      varchar(150) NOT NULL,
    city         varchar(200) NOT NULL,
    street       varchar(200) NOT NULL,
    house_number varchar(20)  NOT NULL,
    note         varchar(500)
);

CREATE TABLE manager
(
    id       SERIAL PRIMARY KEY,
    name     varchar(50)  NOT NULL,
    surname  varchar(100) NOT NULL,
    email    varchar(150) NOT NULL UNIQUE,
    password varchar(200) NOT NULL
);

CREATE TABLE cart
(
    id          SERIAL PRIMARY KEY,
    customer_id integer not null REFERENCES customer (id) ON DELETE CASCADE,
    UNIQUE (id, customer_id)
);

CREATE TABLE order_table
(
    id         SERIAL PRIMARY KEY,
    cart_id    integer NOT NULL REFERENCES cart (id) ON DELETE CASCADE,
    product_id integer NOT NULL REFERENCES book (id) ON DELETE CASCADE,
    quantity   integer
        CONSTRAINT positive_or_zero_check CHECK (quantity >= 0),
    UNIQUE (cart_id, product_id)
);

CREATE TABLE subscription
(
    id          SERIAL PRIMARY KEY,
    product_id  integer REFERENCES book (id),
    customer_id integer REFERENCES customer (id),
    UNIQUE (product_id, customer_id)
)