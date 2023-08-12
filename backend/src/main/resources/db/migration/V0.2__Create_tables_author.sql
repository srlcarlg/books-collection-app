CREATE SEQUENCE author_id_seq;

CREATE TABLE author
(
    id bigint NOT NULL DEFAULT nextval('author_id_seq'),
    name character varying(255) NOT NULL,
    CONSTRAINT author_pkey PRIMARY KEY (id)
)
