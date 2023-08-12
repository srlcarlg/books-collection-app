CREATE TABLE book_category_id
(
    CONSTRAINT book_category_id_pkey PRIMARY KEY (book_id, category_id),
    category_id bigint NOT NULL REFERENCES category (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    book_id bigint NOT NULL REFERENCES book (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
