alter table photo_feed_image change column photo_feed_id photo_feed_id bigint not null unique;

alter table thumps_up ADD CONSTRAINT UNIQUE (user_id, photo_feed_id);


