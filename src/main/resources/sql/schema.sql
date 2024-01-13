-------------------Domain types-----------------------------------
CREATE DOMAIN email AS TEXT
CHECK ( value ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$' );
CREATE DOMAIN quantity AS BIGINT
    DEFAULT 1
    CHECK ( value >= 0 );

CREATE DOMAIN EMPLOYEE_ID AS VARCHAR(50);
CREATE DOMAIN PRICE AS BIGINT
    NOT NULL
    CHECK(value > 0);

CREATE DOMAIN RATING AS INT
    DEFAULT 0
    CHECK ( value between 0 and 5 );

---------------------Table definition---------------------------------
CREATE TABLE IF NOT EXISTS category (
    category_name TEXT PRIMARY KEY ,
    parent VARCHAR(20),
    category_schema JSONB, -- json schema for validation of the specification of a category
    CONSTRAINT fk_parent_category
        FOREIGN KEY (parent) REFERENCES category(category_name)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS supplier(
    code TEXT PRIMARY KEY ,
    name TEXT UNIQUE
);

CREATE TABLE IF NOT EXISTS product_line(
    product_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    category TEXT,
    supplier TEXT NOT NULL ,
    name VARCHAR(30) NOT NULL ,
    description text DEFAULT 'Đang cập nhật',
    CONSTRAINT fk_product_category
        FOREIGN KEY (category) REFERENCES category(category_name)
            ON DELETE set NULL
            ON UPDATE CASCADE,
    CONSTRAINT fk_product_supplier
        FOREIGN KEY (supplier) REFERENCES supplier(code)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS product(
    product_id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY  ,
    name TEXT not null ,
    sku CHAR(10) UNIQUE ,
    images text[],
    local_specs JSONB,
    quantity QUANTITY DEFAULT 1,
    price PRICE,
    is_standard BOOLEAN DEFAULT FALSE,
    product_line SERIAL NOT NULL ,
    CONSTRAINT fk_productLine
        FOREIGN KEY (product_line) REFERENCES product_line(product_id)
             ON DELETE CASCADE
             ON UPDATE CASCADE
);

CREATE TYPE ORDER_STATUS AS ENUM ('unresolved', 'processing', 'shipping', 'success', 'canceled');
CREATE TABLE IF NOT EXISTS orders(
    order_id UUID PRIMARY KEY,
    shipping_addr INT NOT NULL ,
    owner_id UUID NOT NULL ,
    status ORDER_STATUS DEFAULT 'unresolved',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    last_updated TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE IF NOT EXISTS order_item (
    order_id UUID,
    product_id INT,
    quantity QUANTITY,
    CONSTRAINT pk_order_item PRIMARY KEY (order_id, product_id),
    CONSTRAINT fk_orderId
        FOREIGN KEY (order_id) REFERENCES orders(order_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_product_id
        FOREIGN KEY (product_id) REFERENCES product(product_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS account(
    account_id UUID PRIMARY KEY ,
    username VARCHAR(20) NOT NULL UNIQUE ,
    password VARCHAR(30) NOT NULL ,
    email EMAIL NOT NULL,
    lname VARCHAR(20),
    fname VARCHAR(20),
    gender char(1) CHECK ( gender in ('M', 'F') ),
    phonenumber CHAR(15),
    dob DATE,
    is_active BOOLEAN DEFAULT FALSE
);
CREATE TABLE IF NOT EXISTS customer(account_id UUID PRIMARY KEY REFERENCES account(account_id));

CREATE TABLE IF NOT EXISTS role (
    role_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    role_name VARCHAR(20) NOT NULL UNIQUE ,
    description TEXT,
    created_by UUID
);

CREATE TABLE IF NOT EXISTS admin(
    account_id UUID PRIMARY KEY REFERENCES account(account_id),
    employee_id EMPLOYEE_ID,
    role INT REFERENCES role(role_id)
);




ALTER TABLE role ADD CONSTRAINT fk_created_by
        FOREIGN KEY (created_by) REFERENCES admin(account_id)
            ON DELETE SET NULL;
CREATE TABLE IF NOT EXISTS permission (
    permission_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    resource_name varchar(20),
    action VARCHAR(20) CHECK ( action in ('WRITE', 'READ', 'UPDATE', 'DELETE') )
);

CREATE TABLE IF NOT EXISTS permission_role(
    permission INT NOT NULL ,
    role INT NOT NULL ,
    CONSTRAINT pk PRIMARY KEY (permission, role),
    CONSTRAINT fk_permission FOREIGN KEY (permission) REFERENCES permission(permission_id),
    CONSTRAINT fk_role FOREIGN KEY (role) REFERENCES role(role_id)
);

CREATE TABLE IF NOT EXISTS review (
    owner UUID NOT NULL ,
    product INT NOT NULL,
    rating INT DEFAULT 0 CHECK ( rating BETWEEN 0 AND 5),
    comment TEXT,
    posted_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    images text[],
    CONSTRAINT pk_review PRIMARY KEY (owner, product),
    CONSTRAINT fk_owner FOREIGN KEY (owner) REFERENCES customer(account_id) ON DELETE CASCADE,
    CONSTRAINT fk_product FOREIGN KEY (product) REFERENCES product_line(product_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS wishlist (
    customer UUID NOT NULL ,
    item INT NOT NULL ,
    added_time timestamp WITH TIME ZONE DEFAULT now(),
    CONSTRAINT pk_wishlist PRIMARY KEY (customer, item),
    CONSTRAINT fk_wishlist_customer
        FOREIGN KEY (customer) REFERENCES customer(account_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_wishlist_item
        FOREIGN KEY (item) REFERENCES product(product_id)
);

CREATE TABLE IF NOT EXISTS customer_orders(
    customer UUID NOT NULL ,
    order_id UUID NOT NULL ,
    CONSTRAINT pk_customer_orders PRIMARY KEY (customer, order_id),
    CONSTRAINT fk_customer
        FOREIGN KEY (customer) REFERENCES customer(account_id),
    CONSTRAINT fk_order_id
           FOREIGN KEY (order_id) REFERENCES orders(order_id)
               ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS message(
    message_id BIGSERIAL PRIMARY KEY ,
    content TEXT NOT NULL ,
    sender UUID NOT NULL ,
    receiver UUID ,
    sent_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    CONSTRAINT fk_sender
        FOREIGN KEY (sender) REFERENCES account(account_id)
            ON DELETE CASCADE,
    CONSTRAINT fk_receiver
        FOREIGN KEY (receiver) REFERENCES account(account_id)
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS address(
    address_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    street VARCHAR(50) NOT NULL ,
    city VARCHAR(30) NOT NULL ,
    state VARCHAR(30),
    zipcode VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS customer_address(
    customer UUID NOT NULL ,
    address_id INT NOT NULL ,
    CONSTRAINT pk_customer_address PRIMARY KEY (customer, address_id),
    CONSTRAINT fk_customer
        FOREIGN KEY (customer) REFERENCES customer(account_id)
            ON DELETE CASCADE ,
    CONSTRAINT fk_address
        FOREIGN KEY (address_id) REFERENCES address(address_id)
            ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS bot_conservation_log(
    id UUID PRIMARY KEY ,
    customer UUID,
    memory TEXT,
    conservation TEXT,
    is_active BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_customer FOREIGN KEY (customer) REFERENCES customer(account_id)
);
