-- Database: "DiaryAPI"

-- DROP DATABASE "DiaryAPI";

CREATE DATABASE "DiaryAPI"
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'English_United Kingdom.1252'
       LC_CTYPE = 'English_United Kingdom.1252'
       CONNECTION LIMIT = -1;

USE "DiaryAPI";

-- Table: entries

-- DROP TABLE entries;

CREATE TABLE entries
(
  entry_id serial NOT NULL,
  date date,
  entry_text text,
  CONSTRAINT entries_pkey PRIMARY KEY (entry_id )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE entries
  OWNER TO postgres;
