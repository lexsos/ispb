CREATE TABLE radius_client (
  id BIGSERIAL NOT NULL,
  delete_at TIMESTAMP,
  ip4Address VARCHAR (255) NOT NULL,
  secret VARCHAR (255) NOT NULL,
  addAuthRequest BOOLEAN NOT NULL,
  rejectInactive BOOLEAN NOT NULL,
  clientType VARCHAR (255) NOT NULL,

  PRIMARY KEY  (id)
);

CREATE UNIQUE INDEX index_radius_client__ip4address__delete_at ON radius_client (ip4Address) WHERE delete_at IS NULL;
