create table city (
    id  bigserial not null,
    delete_at timestamp,
    name varchar(255) not null,
    primary key (id)
);

create table street (
    id  bigserial not null,
    delete_at timestamp,
    name varchar(255) not null,
    city_id int8 not null,
    primary key (id)
);

create table building (
    id  bigserial not null,
    delete_at timestamp,
    name varchar(255) not null,
    street_id int8 not null,
    primary key (id)
);

create table customer (
    id  bigserial not null,
    create_at timestamp not null,
    delete_at timestamp,
    name varchar(255) not null,
    surname varchar(255) not null,
    patronymic varchar(255) not null,
    passport varchar(255) not null,
    phone varchar(255) not null,
    comment text not null,
    room varchar(255) not null,
    contract_number varchar(255) not null,
    building_id int8 not null,
    primary key (id)
);

create table users (
    id  bigserial not null,
    login varchar(255) not null,
    password varchar(255) not null,
    salt varchar(255) not null,
    name varchar(255) not null,
    surname varchar(255) not null,
    accessLevel int4 not null,
    active boolean not null,
    delete_at timestamp,
    primary key (id)
);

create table payment_group (
    id  bigserial not null,
    comment varchar(255) not null,
    create_at timestamp not null,
    delete_at timestamp,
    primary key (id)
);

create table payment (
    id  bigserial not null,
    apply_at timestamp not null,
    create_at timestamp not null,
    delete_at timestamp,
    paymentSum float8 not null DEFAULT 0,
    processed boolean not null,
    customer_id int8 not null,
    group_id int8 not null,
    primary key (id)
);

create table tariff (
    id  bigserial not null,
    delete_at timestamp,
    name varchar(255) not null,
    auto_daily_payment boolean not null,
    daily_payment float8 not null,
    off_threshold float8 not null,
    down_rate float8 not null,
    up_rate float8 not null,
    primary key (id)
);

create table tariff_assignment (
    id  bigserial not null,
    create_at timestamp not null,
    delete_at timestamp,
    apply_at timestamp not null,
    processed boolean not null,
    customer_id int8 not null,
    tariff_id int8 not null,
    primary key (id)
);


create unique index index_city__name__delete_at ON city (name) WHERE delete_at IS NULL;

alter table street add constraint FK__street_city__to__city foreign key (city_id) references city;
create unique index index_street__name__delete_at ON street (name, city_id) WHERE delete_at IS NULL;

alter table building add constraint FK__building_street__to__street foreign key (street_id) references street;
create unique index index_building__name__delete_at ON building (name, street_id) WHERE delete_at IS NULL;

alter table customer add constraint FK__customer_building__to__building foreign key (building_id) references building;
create unique index index_customer__contract_number__delete_at ON customer (contract_number) WHERE delete_at IS NULL;

create unique index index_users__login__delete_at ON users (login) WHERE delete_at IS NULL;

alter table payment add constraint FK__payment__to__customer foreign key (customer_id) references customer;
alter table payment add constraint FK__payment__to__payment_group foreign key (group_id) references payment_group;

create unique index index_tariff__name__delete_at ON tariff (name) WHERE delete_at IS NULL;

alter table tariff_assignment add constraint FK__tariff_assignment__to__customer foreign key (customer_id) references customer;
alter table tariff_assignment add constraint FK__tariff_assignment__to__tariff foreign key (tariff_id) references tariff;


CREATE OR REPLACE VIEW customer_view AS
    SELECT
        id AS id,
        id AS customer_id,
        coalesce( (SELECT sum(paymentSum) FROM payment WHERE payment.customer_id = customer.id AND payment.processed = TRUE AND payment.delete_at IS NULL), 0) AS balance,
        (SELECT tariff_id FROM tariff_assignment WHERE tariff_assignment.processed = TRUE AND tariff_assignment.customer_id = customer.id AND tariff_assignment.delete_at IS NULL ORDER BY tariff_assignment.apply_at DESC LIMIT 1) as tariff_id
    FROM customer;


