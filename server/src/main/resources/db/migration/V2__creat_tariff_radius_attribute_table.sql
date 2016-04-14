CREATE TABLE tariff_radius_attribute (
  id  BIGSERIAL NOT NULL,
  delete_at TIMESTAMP,
  tariff_id INT8 NOT NULL,
  attributeName VARCHAR (255) NOT NULL,
  attributeValue VARCHAR (255) NOT NULL,
  condition VARCHAR (255) NOT NULL,

  PRIMARY KEY  (id)
);

ALTER TABLE tariff_radius_attribute ADD CONSTRAINT FK__tariff_radius_attribute__to__tariff FOREIGN KEY (tariff_id) REFERENCES  tariff;

