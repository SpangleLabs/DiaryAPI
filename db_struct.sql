--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.18
-- Dumped by pg_dump version 9.1.18
-- Started on 2015-12-06 23:46:48

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 184 (class 3079 OID 11639)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2019 (class 0 OID 0)
-- Dependencies: 184
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 166 (class 1259 OID 32778)
-- Dependencies: 1827 5
-- Name: account; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE account (
    account_id integer NOT NULL,
    username text NOT NULL,
    email text NOT NULL,
    pass_hash bit(256) NOT NULL,
    pass_salt bit(256) NOT NULL,
    failed_logins integer DEFAULT 0 NOT NULL,
    lockout_time timestamp with time zone
);


ALTER TABLE public.account OWNER TO postgres;

--
-- TOC entry 165 (class 1259 OID 32776)
-- Dependencies: 166 5
-- Name: accounts_account_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE accounts_account_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.accounts_account_id_seq OWNER TO postgres;

--
-- TOC entry 2020 (class 0 OID 0)
-- Dependencies: 165
-- Name: accounts_account_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE accounts_account_id_seq OWNED BY account.account_id;


--
-- TOC entry 162 (class 1259 OID 16396)
-- Dependencies: 5
-- Name: entry; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE entry (
    entry_id integer NOT NULL,
    entry_date date NOT NULL,
    entry_text text NOT NULL,
    account_id integer NOT NULL
);


ALTER TABLE public.entry OWNER TO postgres;

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
-- TOC entry 2021 (class 0 OID 0)
-- Dependencies: 161
-- Name: entries_entry_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE entries_entry_id_seq OWNED BY entry.entry_id;


--
-- TOC entry 168 (class 1259 OID 32802)
-- Dependencies: 5
-- Name: entry_tag; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE entry_tag (
    entry_tag_id integer NOT NULL,
    entry_id integer NOT NULL,
    tag text NOT NULL
);


ALTER TABLE public.entry_tag OWNER TO postgres;

--
-- TOC entry 167 (class 1259 OID 32800)
-- Dependencies: 168 5
-- Name: entry_tags_entry_tag_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE entry_tags_entry_tag_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.entry_tags_entry_tag_id_seq OWNER TO postgres;

--
-- TOC entry 2022 (class 0 OID 0)
-- Dependencies: 167
-- Name: entry_tags_entry_tag_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE entry_tags_entry_tag_id_seq OWNED BY entry_tag.entry_tag_id;


--
-- TOC entry 170 (class 1259 OID 32818)
-- Dependencies: 1830 5
-- Name: list; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE list (
    list_id integer NOT NULL,
    account_id integer NOT NULL,
    title text NOT NULL,
    description text DEFAULT ''::text NOT NULL
);


ALTER TABLE public.list OWNER TO postgres;

--
-- TOC entry 172 (class 1259 OID 32835)
-- Dependencies: 1832 1833 5
-- Name: list_item; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE list_item (
    list_item_id integer NOT NULL,
    list_id integer NOT NULL,
    name text NOT NULL,
    description text DEFAULT ''::text NOT NULL,
    date_added timestamp with time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.list_item OWNER TO postgres;

--
-- TOC entry 171 (class 1259 OID 32833)
-- Dependencies: 5 172
-- Name: list_items_list_item_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE list_items_list_item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.list_items_list_item_id_seq OWNER TO postgres;

--
-- TOC entry 2023 (class 0 OID 0)
-- Dependencies: 171
-- Name: list_items_list_item_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE list_items_list_item_id_seq OWNED BY list_item.list_item_id;


--
-- TOC entry 169 (class 1259 OID 32816)
-- Dependencies: 170 5
-- Name: lists_list_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE lists_list_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.lists_list_id_seq OWNER TO postgres;

--
-- TOC entry 2024 (class 0 OID 0)
-- Dependencies: 169
-- Name: lists_list_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE lists_list_id_seq OWNED BY list.list_id;


--
-- TOC entry 176 (class 1259 OID 32885)
-- Dependencies: 1836 1837 5
-- Name: note; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE note (
    note_id integer NOT NULL,
    account_id integer NOT NULL,
    title text NOT NULL,
    body text NOT NULL,
    parent_category_id integer,
    time_created timestamp with time zone DEFAULT now() NOT NULL,
    time_edited timestamp with time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.note OWNER TO postgres;

--
-- TOC entry 174 (class 1259 OID 32854)
-- Dependencies: 5
-- Name: note_category; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE note_category (
    note_category_id integer NOT NULL,
    account_id integer NOT NULL,
    name text NOT NULL,
    parent_category_id integer
);


ALTER TABLE public.note_category OWNER TO postgres;

--
-- TOC entry 173 (class 1259 OID 32852)
-- Dependencies: 174 5
-- Name: note_categories_note_category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE note_categories_note_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.note_categories_note_category_id_seq OWNER TO postgres;

--
-- TOC entry 2025 (class 0 OID 0)
-- Dependencies: 173
-- Name: note_categories_note_category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE note_categories_note_category_id_seq OWNED BY note_category.note_category_id;


--
-- TOC entry 175 (class 1259 OID 32883)
-- Dependencies: 176 5
-- Name: notes_note_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE notes_note_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.notes_note_id_seq OWNER TO postgres;

--
-- TOC entry 2026 (class 0 OID 0)
-- Dependencies: 175
-- Name: notes_note_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE notes_note_id_seq OWNED BY note.note_id;


--
-- TOC entry 178 (class 1259 OID 32908)
-- Dependencies: 5
-- Name: routine; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE routine (
    routine_id integer NOT NULL,
    account_id integer NOT NULL,
    name text NOT NULL
);


ALTER TABLE public.routine OWNER TO postgres;

--
-- TOC entry 180 (class 1259 OID 32929)
-- Dependencies: 5
-- Name: routine_item; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE routine_item (
    routine_item_id integer NOT NULL,
    routine_id integer NOT NULL,
    name text NOT NULL,
    description text NOT NULL,
    optional boolean NOT NULL,
    timed boolean NOT NULL
);


ALTER TABLE public.routine_item OWNER TO postgres;

--
-- TOC entry 183 (class 1259 OID 32957)
-- Dependencies: 5
-- Name: routine_item_status; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE routine_item_status (
    routine_run_id integer NOT NULL,
    routine_item_id integer NOT NULL,
    time_started timestamp with time zone,
    duration tinterval,
    notes text
);


ALTER TABLE public.routine_item_status OWNER TO postgres;

--
-- TOC entry 179 (class 1259 OID 32927)
-- Dependencies: 5 180
-- Name: routine_items_routine_item_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE routine_items_routine_item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.routine_items_routine_item_id_seq OWNER TO postgres;

--
-- TOC entry 2027 (class 0 OID 0)
-- Dependencies: 179
-- Name: routine_items_routine_item_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE routine_items_routine_item_id_seq OWNED BY routine_item.routine_item_id;


--
-- TOC entry 177 (class 1259 OID 32906)
-- Dependencies: 178 5
-- Name: routine_routine_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE routine_routine_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.routine_routine_id_seq OWNER TO postgres;

--
-- TOC entry 2028 (class 0 OID 0)
-- Dependencies: 177
-- Name: routine_routine_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE routine_routine_id_seq OWNED BY routine.routine_id;


--
-- TOC entry 182 (class 1259 OID 32945)
-- Dependencies: 1841 5
-- Name: routine_run; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE routine_run (
    routine_run_id integer NOT NULL,
    routine_id integer NOT NULL,
    time_started timestamp with time zone DEFAULT now() NOT NULL,
    duration tinterval
);


ALTER TABLE public.routine_run OWNER TO postgres;

--
-- TOC entry 181 (class 1259 OID 32943)
-- Dependencies: 5 182
-- Name: routine_runs_routine_run_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE routine_runs_routine_run_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.routine_runs_routine_run_id_seq OWNER TO postgres;

--
-- TOC entry 2029 (class 0 OID 0)
-- Dependencies: 181
-- Name: routine_runs_routine_run_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE routine_runs_routine_run_id_seq OWNED BY routine_run.routine_run_id;


--
-- TOC entry 164 (class 1259 OID 16420)
-- Dependencies: 1824 1825 5
-- Name: session_token; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE session_token (
    session_token_id integer NOT NULL,
    token character(64) NOT NULL,
    account_id integer NOT NULL,
    ip_addr inet NOT NULL,
    time_created timestamp with time zone DEFAULT now() NOT NULL,
    time_used timestamp with time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.session_token OWNER TO postgres;

--
-- TOC entry 163 (class 1259 OID 16418)
-- Dependencies: 5 164
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
-- TOC entry 2030 (class 0 OID 0)
-- Dependencies: 163
-- Name: session_tokens_session_token_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE session_tokens_session_token_id_seq OWNED BY session_token.session_token_id;


--
-- TOC entry 1826 (class 2604 OID 32781)
-- Dependencies: 166 165 166
-- Name: account_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY account ALTER COLUMN account_id SET DEFAULT nextval('accounts_account_id_seq'::regclass);


--
-- TOC entry 1822 (class 2604 OID 16399)
-- Dependencies: 162 161 162
-- Name: entry_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY entry ALTER COLUMN entry_id SET DEFAULT nextval('entries_entry_id_seq'::regclass);


--
-- TOC entry 1828 (class 2604 OID 32805)
-- Dependencies: 168 167 168
-- Name: entry_tag_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY entry_tag ALTER COLUMN entry_tag_id SET DEFAULT nextval('entry_tags_entry_tag_id_seq'::regclass);


--
-- TOC entry 1829 (class 2604 OID 32821)
-- Dependencies: 169 170 170
-- Name: list_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY list ALTER COLUMN list_id SET DEFAULT nextval('lists_list_id_seq'::regclass);


--
-- TOC entry 1831 (class 2604 OID 32838)
-- Dependencies: 171 172 172
-- Name: list_item_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY list_item ALTER COLUMN list_item_id SET DEFAULT nextval('list_items_list_item_id_seq'::regclass);


--
-- TOC entry 1835 (class 2604 OID 32888)
-- Dependencies: 176 175 176
-- Name: note_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY note ALTER COLUMN note_id SET DEFAULT nextval('notes_note_id_seq'::regclass);


--
-- TOC entry 1834 (class 2604 OID 32857)
-- Dependencies: 173 174 174
-- Name: note_category_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY note_category ALTER COLUMN note_category_id SET DEFAULT nextval('note_categories_note_category_id_seq'::regclass);


--
-- TOC entry 1838 (class 2604 OID 32911)
-- Dependencies: 177 178 178
-- Name: routine_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY routine ALTER COLUMN routine_id SET DEFAULT nextval('routine_routine_id_seq'::regclass);


--
-- TOC entry 1839 (class 2604 OID 32932)
-- Dependencies: 180 179 180
-- Name: routine_item_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY routine_item ALTER COLUMN routine_item_id SET DEFAULT nextval('routine_items_routine_item_id_seq'::regclass);


--
-- TOC entry 1840 (class 2604 OID 32948)
-- Dependencies: 181 182 182
-- Name: routine_run_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY routine_run ALTER COLUMN routine_run_id SET DEFAULT nextval('routine_runs_routine_run_id_seq'::regclass);


--
-- TOC entry 1823 (class 2604 OID 16423)
-- Dependencies: 164 163 164
-- Name: session_token_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY session_token ALTER COLUMN session_token_id SET DEFAULT nextval('session_tokens_session_token_id_seq'::regclass);


--
-- TOC entry 1994 (class 0 OID 32778)
-- Dependencies: 166 2012
-- Data for Name: account; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY account (account_id, username, email, pass_hash, pass_salt, failed_logins, lockout_time) FROM stdin;
\.


--
-- TOC entry 2031 (class 0 OID 0)
-- Dependencies: 165
-- Name: accounts_account_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('accounts_account_id_seq', 1, false);


--
-- TOC entry 2032 (class 0 OID 0)
-- Dependencies: 161
-- Name: entries_entry_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('entries_entry_id_seq', 1, false);


--
-- TOC entry 1990 (class 0 OID 16396)
-- Dependencies: 162 2012
-- Data for Name: entry; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY entry (entry_id, entry_date, entry_text, account_id) FROM stdin;
\.


--
-- TOC entry 1996 (class 0 OID 32802)
-- Dependencies: 168 2012
-- Data for Name: entry_tag; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY entry_tag (entry_tag_id, entry_id, tag) FROM stdin;
\.


--
-- TOC entry 2033 (class 0 OID 0)
-- Dependencies: 167
-- Name: entry_tags_entry_tag_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('entry_tags_entry_tag_id_seq', 1, false);


--
-- TOC entry 1998 (class 0 OID 32818)
-- Dependencies: 170 2012
-- Data for Name: list; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY list (list_id, account_id, title, description) FROM stdin;
\.


--
-- TOC entry 2000 (class 0 OID 32835)
-- Dependencies: 172 2012
-- Data for Name: list_item; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY list_item (list_item_id, list_id, name, description, date_added) FROM stdin;
\.


--
-- TOC entry 2034 (class 0 OID 0)
-- Dependencies: 171
-- Name: list_items_list_item_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('list_items_list_item_id_seq', 1, false);


--
-- TOC entry 2035 (class 0 OID 0)
-- Dependencies: 169
-- Name: lists_list_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('lists_list_id_seq', 1, false);


--
-- TOC entry 2004 (class 0 OID 32885)
-- Dependencies: 176 2012
-- Data for Name: note; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY note (note_id, account_id, title, body, parent_category_id, time_created, time_edited) FROM stdin;
\.


--
-- TOC entry 2036 (class 0 OID 0)
-- Dependencies: 173
-- Name: note_categories_note_category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('note_categories_note_category_id_seq', 1, false);


--
-- TOC entry 2002 (class 0 OID 32854)
-- Dependencies: 174 2012
-- Data for Name: note_category; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY note_category (note_category_id, account_id, name, parent_category_id) FROM stdin;
\.


--
-- TOC entry 2037 (class 0 OID 0)
-- Dependencies: 175
-- Name: notes_note_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('notes_note_id_seq', 1, false);


--
-- TOC entry 2006 (class 0 OID 32908)
-- Dependencies: 178 2012
-- Data for Name: routine; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY routine (routine_id, account_id, name) FROM stdin;
\.


--
-- TOC entry 2008 (class 0 OID 32929)
-- Dependencies: 180 2012
-- Data for Name: routine_item; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY routine_item (routine_item_id, routine_id, name, description, optional, timed) FROM stdin;
\.


--
-- TOC entry 2011 (class 0 OID 32957)
-- Dependencies: 183 2012
-- Data for Name: routine_item_status; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY routine_item_status (routine_run_id, routine_item_id, time_started, duration, notes) FROM stdin;
\.


--
-- TOC entry 2038 (class 0 OID 0)
-- Dependencies: 179
-- Name: routine_items_routine_item_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('routine_items_routine_item_id_seq', 1, false);


--
-- TOC entry 2039 (class 0 OID 0)
-- Dependencies: 177
-- Name: routine_routine_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('routine_routine_id_seq', 1, false);


--
-- TOC entry 2010 (class 0 OID 32945)
-- Dependencies: 182 2012
-- Data for Name: routine_run; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY routine_run (routine_run_id, routine_id, time_started, duration) FROM stdin;
\.


--
-- TOC entry 2040 (class 0 OID 0)
-- Dependencies: 181
-- Name: routine_runs_routine_run_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('routine_runs_routine_run_id_seq', 1, false);


--
-- TOC entry 1992 (class 0 OID 16420)
-- Dependencies: 164 2012
-- Data for Name: session_token; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY session_token (session_token_id, token, account_id, ip_addr, time_created, time_used) FROM stdin;
\.


--
-- TOC entry 2041 (class 0 OID 0)
-- Dependencies: 163
-- Name: session_tokens_session_token_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('session_tokens_session_token_id_seq', 1, false);


--
-- TOC entry 1854 (class 2606 OID 32787)
-- Dependencies: 166 166 2013
-- Name: accounts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY account
    ADD CONSTRAINT accounts_pkey PRIMARY KEY (account_id);


--
-- TOC entry 1843 (class 2606 OID 24593)
-- Dependencies: 162 162 162 2013
-- Name: entries_entry_date_login_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY entry
    ADD CONSTRAINT entries_entry_date_login_id_key UNIQUE (entry_date, account_id);


--
-- TOC entry 1845 (class 2606 OID 16404)
-- Dependencies: 162 162 2013
-- Name: entries_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY entry
    ADD CONSTRAINT entries_pkey PRIMARY KEY (entry_id);


--
-- TOC entry 1856 (class 2606 OID 32810)
-- Dependencies: 168 168 2013
-- Name: entry_tags_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY entry_tag
    ADD CONSTRAINT entry_tags_pkey PRIMARY KEY (entry_tag_id);


--
-- TOC entry 1860 (class 2606 OID 32844)
-- Dependencies: 172 172 2013
-- Name: list_items_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY list_item
    ADD CONSTRAINT list_items_pkey PRIMARY KEY (list_item_id);


--
-- TOC entry 1858 (class 2606 OID 32827)
-- Dependencies: 170 170 2013
-- Name: lists_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY list
    ADD CONSTRAINT lists_pkey PRIMARY KEY (list_id);


--
-- TOC entry 1862 (class 2606 OID 32862)
-- Dependencies: 174 174 2013
-- Name: note_categories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY note_category
    ADD CONSTRAINT note_categories_pkey PRIMARY KEY (note_category_id);


--
-- TOC entry 1865 (class 2606 OID 32895)
-- Dependencies: 176 176 2013
-- Name: notes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY note
    ADD CONSTRAINT notes_pkey PRIMARY KEY (note_id);


--
-- TOC entry 1873 (class 2606 OID 32964)
-- Dependencies: 183 183 183 2013
-- Name: routine_item_status_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY routine_item_status
    ADD CONSTRAINT routine_item_status_pkey PRIMARY KEY (routine_run_id, routine_item_id);


--
-- TOC entry 1869 (class 2606 OID 32937)
-- Dependencies: 180 180 2013
-- Name: routine_items_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY routine_item
    ADD CONSTRAINT routine_items_pkey PRIMARY KEY (routine_item_id);


--
-- TOC entry 1867 (class 2606 OID 32916)
-- Dependencies: 178 178 2013
-- Name: routine_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY routine
    ADD CONSTRAINT routine_pkey PRIMARY KEY (routine_id);


--
-- TOC entry 1871 (class 2606 OID 32950)
-- Dependencies: 182 182 2013
-- Name: routine_runs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY routine_run
    ADD CONSTRAINT routine_runs_pkey PRIMARY KEY (routine_run_id);


--
-- TOC entry 1850 (class 2606 OID 16428)
-- Dependencies: 164 164 2013
-- Name: session_tokens_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY session_token
    ADD CONSTRAINT session_tokens_pkey PRIMARY KEY (session_token_id);


--
-- TOC entry 1852 (class 2606 OID 16477)
-- Dependencies: 164 164 2013
-- Name: session_tokens_token_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY session_token
    ADD CONSTRAINT session_tokens_token_key UNIQUE (token);


--
-- TOC entry 1846 (class 1259 OID 32799)
-- Dependencies: 162 2013
-- Name: fk_entries_account_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX fk_entries_account_id ON entry USING btree (account_id);


--
-- TOC entry 1848 (class 1259 OID 32793)
-- Dependencies: 164 2013
-- Name: fk_session_token_account_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX fk_session_token_account_id ON session_token USING btree (account_id);


--
-- TOC entry 1847 (class 1259 OID 16456)
-- Dependencies: 162 2013
-- Name: fki_entries_login_id_fkey; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_entries_login_id_fkey ON entry USING btree (account_id);


--
-- TOC entry 1863 (class 1259 OID 32873)
-- Dependencies: 174 2013
-- Name: parent_category_fkey; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX parent_category_fkey ON note_category USING btree (parent_category_id);


--
-- TOC entry 1874 (class 2606 OID 32980)
-- Dependencies: 162 166 1853 2013
-- Name: entries_account_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY entry
    ADD CONSTRAINT entries_account_id_fkey FOREIGN KEY (account_id) REFERENCES account(account_id);


--
-- TOC entry 1876 (class 2606 OID 32985)
-- Dependencies: 1844 168 162 2013
-- Name: entry_tags_entry_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY entry_tag
    ADD CONSTRAINT entry_tags_entry_id_fkey FOREIGN KEY (entry_id) REFERENCES entry(entry_id);


--
-- TOC entry 1878 (class 2606 OID 32990)
-- Dependencies: 170 1857 172 2013
-- Name: list_items_list_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY list_item
    ADD CONSTRAINT list_items_list_id_fkey FOREIGN KEY (list_id) REFERENCES list(list_id);


--
-- TOC entry 1877 (class 2606 OID 32995)
-- Dependencies: 170 166 1853 2013
-- Name: lists_account_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY list
    ADD CONSTRAINT lists_account_id_fkey FOREIGN KEY (account_id) REFERENCES account(account_id);


--
-- TOC entry 1879 (class 2606 OID 33040)
-- Dependencies: 166 1853 174 2013
-- Name: note_categories_account_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY note_category
    ADD CONSTRAINT note_categories_account_id_fkey FOREIGN KEY (account_id) REFERENCES account(account_id);


--
-- TOC entry 1880 (class 2606 OID 33045)
-- Dependencies: 1861 174 174 2013
-- Name: note_category_parent_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY note_category
    ADD CONSTRAINT note_category_parent_fkey FOREIGN KEY (parent_category_id) REFERENCES note_category(note_category_id) DEFERRABLE;


--
-- TOC entry 1881 (class 2606 OID 33005)
-- Dependencies: 1853 176 166 2013
-- Name: notes_account_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY note
    ADD CONSTRAINT notes_account_id_fkey FOREIGN KEY (account_id) REFERENCES account(account_id);


--
-- TOC entry 1882 (class 2606 OID 33010)
-- Dependencies: 1861 174 176 2013
-- Name: notes_parent_category_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY note
    ADD CONSTRAINT notes_parent_category_id_fkey FOREIGN KEY (parent_category_id) REFERENCES note_category(note_category_id) DEFERRABLE;


--
-- TOC entry 1883 (class 2606 OID 33020)
-- Dependencies: 178 166 1853 2013
-- Name: routine_account_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY routine
    ADD CONSTRAINT routine_account_id_fkey FOREIGN KEY (account_id) REFERENCES account(account_id);


--
-- TOC entry 1887 (class 2606 OID 32970)
-- Dependencies: 183 180 1868 2013
-- Name: routine_item_status_routine_item_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY routine_item_status
    ADD CONSTRAINT routine_item_status_routine_item_id_fkey FOREIGN KEY (routine_item_id) REFERENCES routine_item(routine_item_id);


--
-- TOC entry 1886 (class 2606 OID 32965)
-- Dependencies: 183 182 1870 2013
-- Name: routine_item_status_routine_run_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY routine_item_status
    ADD CONSTRAINT routine_item_status_routine_run_id_fkey FOREIGN KEY (routine_run_id) REFERENCES routine_run(routine_run_id);


--
-- TOC entry 1884 (class 2606 OID 32975)
-- Dependencies: 1866 178 180 2013
-- Name: routine_items_routine_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY routine_item
    ADD CONSTRAINT routine_items_routine_id_fkey FOREIGN KEY (routine_id) REFERENCES routine(routine_id);


--
-- TOC entry 1885 (class 2606 OID 33015)
-- Dependencies: 182 178 1866 2013
-- Name: routine_runs_routine_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY routine_run
    ADD CONSTRAINT routine_runs_routine_id_fkey FOREIGN KEY (routine_id) REFERENCES routine(routine_id);


--
-- TOC entry 1875 (class 2606 OID 33025)
-- Dependencies: 164 166 1853 2013
-- Name: session_tokens_account_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY session_token
    ADD CONSTRAINT session_tokens_account_id_fkey FOREIGN KEY (account_id) REFERENCES account(account_id);


--
-- TOC entry 2018 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2015-12-06 23:46:48

--
-- PostgreSQL database dump complete
--

