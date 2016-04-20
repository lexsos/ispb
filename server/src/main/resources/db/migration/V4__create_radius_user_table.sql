CREATE TABLE radius_user (
  id BIGSERIAL NOT NULL,
  delete_at TIMESTAMP,
  create_at TIMESTAMP NOT NULL,
  userName VARCHAR (255) NOT NULL,
  password VARCHAR (255) NOT NULL,
  ip4Address VARCHAR (255),
  customer_id INT8,

  PRIMARY KEY  (id)
);

ALTER TABLE radius_user ADD CONSTRAINT FK__radius_user__to__customer FOREIGN KEY (customer_id) REFERENCES  customer;
create unique index index_radius_user__ip4address__delete_at ON radius_user (ip4Address) WHERE delete_at IS NULL AND ip4Address IS NOT NULL;
create unique index index_radius_user__username__delete_at ON radius_user (userName) WHERE delete_at IS NULL;