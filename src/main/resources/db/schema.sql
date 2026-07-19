CREATE TABLE IF NOT EXISTS public.tool_types
(
    type character varying(20) COLLATE pg_catalog."default" NOT NULL,
    description text COLLATE pg_catalog."default",
    CONSTRAINT type_pkey PRIMARY KEY (type)
);

CREATE TABLE IF NOT EXISTS public.tools
(
    id character varying(50) COLLATE pg_catalog."default" NOT NULL,
    type character varying(20) COLLATE pg_catalog."default" NOT NULL,
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    diameter numeric(6,2) NOT NULL,
    quantity integer NOT NULL,
    flutes integer,
    inserts integer,
    pitch numeric(6,2),
    CONSTRAINT tools_pkey PRIMARY KEY (id),
    CONSTRAINT fk_tool_type FOREIGN KEY (type)
    REFERENCES public.tool_types (type) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT tools_quantity_check CHECK (quantity >= 0)
);

INSERT INTO tool_types (type, description) VALUES
        ('DRILL', 'Drill bit'),
        ('END_MILL', 'End mill cutter'),
        ('FACE_MILL', 'Face mill cutter'),
        ('TAP', 'Thread tap')
    ON CONFLICT (type) DO NOTHING;