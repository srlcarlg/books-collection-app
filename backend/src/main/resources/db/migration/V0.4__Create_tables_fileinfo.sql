CREATE SEQUENCE file_info_id_seq;

CREATE TABLE file_info
(
    id bigint NOT NULL DEFAULT nextval('file_info_id_seq'),
    content_length bigint,
    content_type character varying(255),
    filename character varying(255),
    url character varying(255),
    CONSTRAINT file_info_pkey PRIMARY KEY (id),
    UNIQUE (filename)
)
