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
    room varchar(255) not null,
    surname varchar(255) not null,
    contract_number varchar(255) not null,
    building_id int8 not null,
    primary key (id)
);

create table users (
    id  bigserial not null,
    login varchar(255) not null,
    password varchar(255) not null,
    name varchar(255) not null,
    surname varchar(255) not null,
    accessLevel int4 not null,
    active boolean not null,
    delete_at timestamp,
    primary key (id)
);

create unique index index_city__name__delete_at ON city (name) WHERE delete_at IS NULL;

alter table street add constraint FK__street_city__to__city foreign key (city_id) references city;
create unique index index_street__name__delete_at ON street (name) WHERE delete_at IS NULL;

alter table building add constraint FK__building_street__to__street foreign key (street_id) references street;
create unique index index_building__name__delete_at ON building (name) WHERE delete_at IS NULL;

alter table customer add constraint FK__customer_building__to__building foreign key (building_id) references building;
alter table customer add constraint Unique__customer__contract_number unique (contract_number);

create unique index index_users__login__delete_at ON users (login) WHERE delete_at IS NULL;