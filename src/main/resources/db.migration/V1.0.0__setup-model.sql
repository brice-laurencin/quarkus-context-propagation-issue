-- Business Tables

CREATE TABLE thing
(
    id     BIGSERIAL PRIMARY KEY,
    status SMALLINT  NOT NULL,
    reason VARCHAR(255),
    elements_counts tinyint
);

CREATE INDEX thing_status_idx ON prescription (status);
