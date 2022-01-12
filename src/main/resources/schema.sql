CREATE TABLE IF NOT EXISTS poll (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title varchar(256) NOT NULL,
    start_at timestamp NOT NULL,
    end_at timestamp,
    description text,
    questions jsonb NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
	id text PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS answer (
	id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	poll_id bigint NOT NULL,
	user_id text NOT NULL,
	answer jsonb NOT NULL,

	CONSTRAINT poll_id_fk
	    FOREIGN KEY(poll_id)
        REFERENCES poll(id)
    	ON DELETE CASCADE,

    CONSTRAINT user_id_fk
        FOREIGN KEY(user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);