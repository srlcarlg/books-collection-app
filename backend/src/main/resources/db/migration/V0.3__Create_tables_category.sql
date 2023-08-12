CREATE SEQUENCE category_id_seq;

CREATE TABLE category
(
    id bigint NOT NULL DEFAULT nextval('category_id_seq'),
    name character varying(255) NOT NULL,
    CONSTRAINT category_pkey PRIMARY KEY (id)
)
