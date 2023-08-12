CREATE SEQUENCE favorite_id_seq;

CREATE TABLE favorite
(
    id bigint NOT NULL DEFAULT nextval('favorite_id_seq'),
    user_id bigint NOT NULL,
    CONSTRAINT favorite_pkey PRIMARY KEY (id),
    book_id bigint REFERENCES book (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)