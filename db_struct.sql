--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.18
-- Dumped by pg_dump version 9.1.18
-- Started on 2015-10-01 22:45:44

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 167 (class 3079 OID 11639)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 1890 (class 0 OID 0)
-- Dependencies: 167
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 162 (class 1259 OID 16396)
-- Dependencies: 5
-- Name: entries; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE entries (
    entry_id integer NOT NULL,
    entry_date date NOT NULL,
    entry_text text NOT NULL,
    login_id integer NOT NULL
);


ALTER TABLE public.entries OWNER TO postgres;

--
-- TOC entry 161 (class 1259 OID 16394)
-- Dependencies: 5 162
-- Name: entries_entry_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE entries_entry_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.entries_entry_id_seq OWNER TO postgres;

--
-- TOC entry 1891 (class 0 OID 0)
-- Dependencies: 161
-- Name: entries_entry_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE entries_entry_id_seq OWNED BY entries.entry_id;


--
-- TOC entry 164 (class 1259 OID 16407)
-- Dependencies: 5
-- Name: logins; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE logins (
    login_id integer NOT NULL,
    username text NOT NULL,
    email text NOT NULL,
    pass_hash bit(256) NOT NULL,
    pass_salt bit(256) NOT NULL,
    failed_logins integer,
    lockout_time timestamp with time zone
);


ALTER TABLE public.logins OWNER TO postgres;

--
-- TOC entry 163 (class 1259 OID 16405)
-- Dependencies: 164 5
-- Name: logins_login_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE logins_login_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.logins_login_id_seq OWNER TO postgres;

--
-- TOC entry 1892 (class 0 OID 0)
-- Dependencies: 163
-- Name: logins_login_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE logins_login_id_seq OWNED BY logins.login_id;


--
-- TOC entry 166 (class 1259 OID 16420)
-- Dependencies: 1765 1766 5
-- Name: session_tokens; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE session_tokens (
    session_token_id integer NOT NULL,
    token character(64) NOT NULL,
    login_id integer NOT NULL,
    ip_addr inet NOT NULL,
    time_created timestamp with time zone DEFAULT now() NOT NULL,
    time_used timestamp with time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.session_tokens OWNER TO postgres;

--
-- TOC entry 165 (class 1259 OID 16418)
-- Dependencies: 166 5
-- Name: session_tokens_session_token_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE session_tokens_session_token_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.session_tokens_session_token_id_seq OWNER TO postgres;

--
-- TOC entry 1893 (class 0 OID 0)
-- Dependencies: 165
-- Name: session_tokens_session_token_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE session_tokens_session_token_id_seq OWNED BY session_tokens.session_token_id;


--
-- TOC entry 1762 (class 2604 OID 16399)
-- Dependencies: 162 161 162
-- Name: entry_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY entries ALTER COLUMN entry_id SET DEFAULT nextval('entries_entry_id_seq'::regclass);


--
-- TOC entry 1763 (class 2604 OID 16410)
-- Dependencies: 164 163 164
-- Name: login_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY logins ALTER COLUMN login_id SET DEFAULT nextval('logins_login_id_seq'::regclass);


--
-- TOC entry 1764 (class 2604 OID 16423)
-- Dependencies: 166 165 166
-- Name: session_token_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY session_tokens ALTER COLUMN session_token_id SET DEFAULT nextval('session_tokens_session_token_id_seq'::regclass);


--
-- TOC entry 1768 (class 2606 OID 24593)
-- Dependencies: 162 162 162 1884
-- Name: entries_entry_date_login_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY entries
    ADD CONSTRAINT entries_entry_date_login_id_key UNIQUE (entry_date, login_id);


--
-- TOC entry 1770 (class 2606 OID 16404)
-- Dependencies: 162 162 1884
-- Name: entries_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY entries
    ADD CONSTRAINT entries_pkey PRIMARY KEY (entry_id);


--
-- TOC entry 1773 (class 2606 OID 16415)
-- Dependencies: 164 164 1884
-- Name: logins_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY logins
    ADD CONSTRAINT logins_pkey PRIMARY KEY (login_id);


--
-- TOC entry 1775 (class 2606 OID 16417)
-- Dependencies: 164 164 1884
-- Name: logins_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY logins
    ADD CONSTRAINT logins_username_key UNIQUE (username);


--
-- TOC entry 1777 (class 2606 OID 16428)
-- Dependencies: 166 166 1884
-- Name: session_tokens_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY session_tokens
    ADD CONSTRAINT session_tokens_pkey PRIMARY KEY (session_token_id);


--
-- TOC entry 1779 (class 2606 OID 16477)
-- Dependencies: 166 166 1884
-- Name: session_tokens_token_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY session_tokens
    ADD CONSTRAINT session_tokens_token_key UNIQUE (token);


--
-- TOC entry 1771 (class 1259 OID 16456)
-- Dependencies: 162 1884
-- Name: fki_entries_login_id_fkey; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_entries_login_id_fkey ON entries USING btree (login_id);


--
-- TOC entry 1780 (class 2606 OID 16451)
-- Dependencies: 1772 162 164 1884
-- Name: entries_login_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY entries
    ADD CONSTRAINT entries_login_id_fkey FOREIGN KEY (login_id) REFERENCES logins(login_id);


--
-- TOC entry 1781 (class 2606 OID 16431)
-- Dependencies: 164 166 1772 1884
-- Name: session_tokens_login_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY session_tokens
    ADD CONSTRAINT session_tokens_login_id_fkey FOREIGN KEY (login_id) REFERENCES logins(login_id);


--
-- TOC entry 1889 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2015-10-01 22:45:45

--
-- PostgreSQL database dump complete
--

