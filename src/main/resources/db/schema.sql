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

CREATE TABLE IF NOT EXISTS public.tool_issues
(
    id BIGSERIAL PRIMARY KEY,
    tool_id character varying(50) NOT NULL,
    amount integer NOT NULL,
    operation_type character varying(20) NOT NULL,
    issued_at TIMESTAMP NOT NULL DEFAULT now(),
        CONSTRAINT fk_tool_id FOREIGN KEY (tool_id)
        REFERENCES public.tools (id)
        ON DELETE RESTRICT,
    CONSTRAINT tool_issues_amount_check CHECK (amount > 0),
    CONSTRAINT tool_issues_operation_type_check CHECK (operation_type IN ('PICKUP', 'RETURN'))
);

INSERT INTO tool_types (type, description) VALUES
        ('DRILL', 'Drill bit'),
        ('END_MILL', 'End mill cutter'),
        ('FACE_MILL', 'Face mill cutter'),
        ('TAP', 'Thread tap')
    ON CONFLICT (type) DO NOTHING;

CREATE INDEX idx_tool_issues_tool_id ON tool_issues(tool_id);
CREATE INDEX idx_tool_issues_issued_at ON tool_issues(issued_at DESC);