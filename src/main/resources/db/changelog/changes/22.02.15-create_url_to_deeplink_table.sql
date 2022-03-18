--liquibase formatted sql

--changeset vyanchenko:1
CREATE TABLE IF NOT EXISTS url_to_deeplink (
    id bigserial PRIMARY KEY,
    url text NOT NULL,
    deeplink text NOT NULL
);

ALTER TABLE ONLY url_to_deeplink
    ADD CONSTRAINT uq_url_deeplink UNIQUE (url, deeplink);