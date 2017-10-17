SET SYNCHRONOUS_COMMIT = 'off';

BEGIN TRANSACTION;

DROP TRIGGER IF EXISTS on_thread_inserted ON thread;
DROP TRIGGER IF EXISTS on_post_inserted ON post;
DROP TRIGGER IF EXISTS on_post_message_edited ON post;

DROP FUNCTION IF EXISTS update_forum_n_threads() CASCADE;
DROP FUNCTION IF EXISTS update_forum_n_posts() CASCADE;
DROP FUNCTION IF EXISTS update_post_is_edited() CASCADE;

DROP TABLE IF EXISTS "user" CASCADE;
DROP TABLE IF EXISTS forum CASCADE;
DROP TABLE IF EXISTS thread CASCADE;
DROP TABLE IF EXISTS vote CASCADE;
DROP TABLE IF EXISTS post CASCADE;

DROP INDEX IF EXISTS user_nickname_lower_unique;
DROP INDEX IF EXISTS user_email_lower_unique;
DROP INDEX IF EXISTS forum_slug_lower_unique;

CREATE TABLE "user" (
  nickname VARCHAR(50) NOT NULL,
  fullname VARCHAR(100) NOT NULL,
  email VARCHAR(50) NOT NULL,
  description TEXT
);

CREATE UNIQUE INDEX user_nickname_lower_unique
  ON "user" (lower("user".nickname));

CREATE UNIQUE INDEX user_email_lower_unique
  ON "user" (lower("user".email));

CREATE TABLE forum (
  title VARCHAR(100) NOT NULL,
  slug VARCHAR(100) NOT NULL,
  moderator VARCHAR(50) NOT NULL,
  n_threads BIGINT DEFAULT 0,
  n_posts BIGINT DEFAULT 0
);


CREATE UNIQUE INDEX forum_slug_lower_unique
  ON forum (lower(slug));

CREATE TABLE thread (
  id BIGSERIAL PRIMARY KEY,
  title VARCHAR(50) NOT NULL,
  slug VARCHAR(50),
  description TEXT NOT NULL,
  creator VARCHAR(50) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
  forum VARCHAR(50) NOT NULL,
  rating INTEGER DEFAULT 0 NOT NULL
);

CREATE TABLE post (
  id BIGSERIAL PRIMARY KEY,
  message TEXT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  is_edited BOOLEAN DEFAULT FALSE NOT NULL,
  author VARCHAR(50) NOT NULL,
  parent BIGINT REFERENCES post,
  thread VARCHAR(50),
  forum VARCHAR(50) NOT NULL
);

CREATE TABLE vote (
  "user" VARCHAR(50) NOT NULL,
  vote INT NOT NULL
    CONSTRAINT plus_or_minus CHECK (vote = -1 OR vote = 1)
);


CREATE OR REPLACE FUNCTION update_forum_n_threads()
  RETURNS TRIGGER AS $$
    BEGIN
      UPDATE forum SET forum.n_threads = forum.n_threads + 1;
      RETURN NULL;
    END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER on_thread_inserted
AFTER INSERT ON thread
FOR EACH ROW EXECUTE PROCEDURE update_forum_n_threads();


CREATE OR REPLACE FUNCTION update_forum_n_posts()
  RETURNS TRIGGER AS $$
    BEGIN
      UPDATE forum SET forum.n_posts = forum.n_posts + 1;
      RETURN NULL;
    END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER on_post_inserted
AFTER INSERT ON post
FOR EACH ROW EXECUTE PROCEDURE update_forum_n_posts();


CREATE OR REPLACE FUNCTION update_post_is_edited()
  RETURNS TRIGGER AS $$
    BEGIN
      IF old.message != new.message THEN
        UPDATE post SET post.is_edited = TRUE;
      END IF;
      RETURN NULL;
    END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER on_post_message_edited
AFTER UPDATE ON post
FOR EACH ROW EXECUTE PROCEDURE update_post_is_edited();

END TRANSACTION;