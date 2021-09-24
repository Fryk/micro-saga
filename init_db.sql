create table orders
(
    id               uuid    not null
        constraint orders_pk
            primary key,
    user_id          uuid    not null,
    product_id       uuid    not null,
    order_status     varchar not null,
    payment_status   varchar,
    inventory_status varchar,
    price            integer not null
);

alter table orders
    owner to postgres;

create table payments
(
);

alter table payments
    owner to postgres;

create table inventory
(
    id     uuid    not null
        constraint inventory_pk
            primary key,
    name   varchar not null,
    price  integer not null,
    amount integer not null
);

alter table inventory
    owner to postgres;

create table reserved_inventory
(
    order_id   uuid    not null
        constraint reserved_inventory_pk
            primary key,
    product_id uuid    not null,
    quantity   integer not null
);

alter table reserved_inventory
    owner to postgres;

