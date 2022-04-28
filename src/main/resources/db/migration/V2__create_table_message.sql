CREATE TABLE IF NOT EXISTS message
(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    text VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    sender_name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_message PRIMARY KEY (id),
    CONSTRAINT fk_message_sender FOREIGN KEY (sender_name) REFERENCES sender(name)
);