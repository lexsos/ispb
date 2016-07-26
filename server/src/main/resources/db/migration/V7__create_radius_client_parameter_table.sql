CREATE TABLE radius_client_parameter (
  id BIGSERIAL NOT NULL,
  delete_at TIMESTAMP,
  client_id INT8 NOT NULL,
  parameter VARCHAR (255) NOT NULL,
  value VARCHAR (255) NOT NULL,

  PRIMARY KEY  (id)
);

ALTER TABLE radius_client_parameter ADD CONSTRAINT FK__radius_client_parameter__to__radius_client FOREIGN KEY (client_id) REFERENCES radius_client;