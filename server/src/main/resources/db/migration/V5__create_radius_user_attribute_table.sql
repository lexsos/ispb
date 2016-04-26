CREATE TABLE radius_user_attribute (
  id BIGSERIAL NOT NULL,
  delete_at TIMESTAMP,
  user_id INT8,
  attributeName VARCHAR (255) NOT NULL,
  attributeValue VARCHAR (255) NOT NULL,
  condition VARCHAR (255) NOT NULL,

  PRIMARY KEY  (id)
);

ALTER TABLE radius_user_attribute ADD CONSTRAINT FK__radius_user_attribute__to__radius_user FOREIGN KEY (user_id) REFERENCES radius_user;
