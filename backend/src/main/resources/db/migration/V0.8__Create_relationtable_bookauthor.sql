CREATE TABLE book_author_id
(
    CONSTRAINT book_author_id_pkey PRIMARY KEY (book_id, author_id),
    author_id bigint NOT NULL  REFERENCES author (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    book_id bigint NOT NULL REFERENCES book (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
