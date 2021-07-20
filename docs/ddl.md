```sqlite
CREATE TABLE IF NOT EXISTS `Game`
(
    `game_id`     INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `service_key` TEXT                              NOT NULL,
    `created`     INTEGER                           NOT NULL,
    `pool`        TEXT                              NOT NULL,
    `length`      INTEGER                           NOT NULL,
    `solved`      INTEGER                           NOT NULL,
    `pool_size`   INTEGER                           NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS `index_Game_service_key` 
    ON `Game` (`service_key`);

CREATE INDEX IF NOT EXISTS `index_Game_created` 
    ON `Game` (`created`);

CREATE INDEX IF NOT EXISTS `index_Game_length` 
    ON `Game` (`length`);

CREATE INDEX IF NOT EXISTS `index_Game_pool_size` ON `Game` (`pool_size`);

CREATE TABLE IF NOT EXISTS `Guess`
(
    `guess_id`      INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `service_key`   TEXT                              NOT NULL,
    `game_id`       INTEGER                           NOT NULL,
    `created`       INTEGER                           NOT NULL,
    `guess_text`    TEXT                              NOT NULL,
    `exact_matches` INTEGER                           NOT NULL,
    `near_matches`  INTEGER                           NOT NULL,
    `solution`      INTEGER                           NOT NULL,
    FOREIGN KEY (`game_id`) REFERENCES `Game` (`game_id`) ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE UNIQUE INDEX IF NOT EXISTS `index_Guess_service_key` ON `Guess` (`service_key`);

CREATE INDEX IF NOT EXISTS `index_Guess_game_id` ON `Guess` (`game_id`);

CREATE INDEX IF NOT EXISTS `index_Guess_created` ON `Guess` (`created`);
```

[`ddl.sql`](sql/ddl.sql)