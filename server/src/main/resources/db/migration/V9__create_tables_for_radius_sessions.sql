create table radius_session (
  id  bigserial not null,
  create_at timestamp not null,
  delete_at timestamp,
  expire_at timestamp,
  start_at timestamp not null,
  stop_at timestamp,
  customer_id int8 not null,
  radiusClient_id int8 not null,
  radiusUser_id int8 not null,

  primary key (id)
);

create table radius_session_attribute (
  id  bigserial not null,
  attribute varchar(255) not null,
  create_at timestamp not null,
  delete_at timestamp,
  packet_at timestamp not null,
  packet_type int8 not null,
  value varchar(255) not null,
  session_id int8 not null,
  primary key (id)
);

create table radius_session_ip (
  id  bigserial not null,
  delete_at timestamp,
  ipAddress varchar(255) not null,
  session_id int8 not null,
  primary key (id)
);


alter table radius_session add constraint FK__radius_session__to__customer foreign key (customer_id) references customer;
alter table radius_session add constraint FK__radius_session__to__radius_client foreign key (radiusClient_id) references radius_client;
alter table radius_session add constraint FK__radius_session__to__radius_user foreign key (radiusUser_id) references radius_user;

alter table radius_session_attribute add constraint FK__radius_session_attribute__to__radius_session foreign key (session_id) references radius_session;

alter table radius_session_ip add constraint FK__radius_session_ip__to__radius_session foreign key (session_id) references radius_session;