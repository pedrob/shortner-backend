CREATE DATABASE shortner;

DROP TABLE IF EXISTS public.application_user CASCADE;
CREATE TABLE public.application_user (
    id bigint NOT NULL,
    password character varying(255),
    username character varying(20),

    PRIMARY KEY (id)
);


ALTER TABLE public.application_user OWNER TO postgres;

CREATE SEQUENCE public.application_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.application_user_id_seq OWNER TO postgres;


ALTER SEQUENCE public.application_user_id_seq OWNED BY public.application_user.id;


DROP TABLE IF EXISTS public.url CASCADE;
CREATE TABLE public.url (
    hash character varying(255) NOT NULL,
    created_at timestamp without time zone,
    originalurl character varying(255),
    username character varying(255),

    PRIMARY KEY (hash)
);


ALTER TABLE public.url OWNER TO postgres;

ALTER TABLE ONLY public.application_user ALTER COLUMN id SET DEFAULT nextval('public.application_user_id_seq'::regclass);



