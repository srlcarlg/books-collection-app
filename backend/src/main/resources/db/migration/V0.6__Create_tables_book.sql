CREATE SEQUENCE book_id_seq;

CREATE TABLE book
(
    id bigint NOT NULL DEFAULT nextval('book_id_seq'),
    title character varying(255) NOT NULL,
    publication_year integer,
    book_status character varying(255) NOT NULL,
    description text,
    book_file_id bigint,
    cover_file_id bigint,

    CONSTRAINT book_pkey PRIMARY KEY (id)
)
