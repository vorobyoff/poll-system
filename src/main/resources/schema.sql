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
	poll_id bigint REFERENCES poll(id) NOT NULL,
	user_id text REFERENCES users(id) NOT NULL,
	answer jsonb NOT NULL
);