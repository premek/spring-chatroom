CREATE TABLE room (
  `id` int PRIMARY KEY,
  `name` VARCHAR(50) NOT NULL,
  `label` VARCHAR(50) NOT NULL
);
CREATE INDEX i_room_name ON room(name);

INSERT INTO room (id, name, label) VALUES (1, 'music', 'Music');
INSERT INTO room (id, name, label) VALUES (2, 'movies', 'Movies');
INSERT INTO room (id, name, label) VALUES (3, 'gaming', 'Gaming');

CREATE TABLE message (
  `id` BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  `room_id` VARCHAR(50) NOT NULL,
  `posted` TIMESTAMP NOT NULL,
  `username` VARCHAR(50) NOT NULL,
  `text` text NOT NULL,
  CONSTRAINT fk_room_id FOREIGN KEY (room_id) REFERENCES room(id)
);

CREATE INDEX i_message_room_posted ON message(room_id, posted)

