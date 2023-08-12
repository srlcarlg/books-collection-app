CREATE SEQUENCE users_id_seq;

CREATE TABLE users
(
    id bigint NOT NULL DEFAULT nextval('users_id_seq'),
    email character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    type_user character varying(255) NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    UNIQUE (email)
)
