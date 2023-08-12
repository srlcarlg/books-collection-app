CREATE SEQUENCE token_black_list_id_seq;

CREATE TABLE token_black_list
(
    id bigint NOT NULL DEFAULT nextval('token_black_list_id_seq'),
    token character varying(255) NOT NULL,
    CONSTRAINT token_black_list_pkey PRIMARY KEY (id)
)
