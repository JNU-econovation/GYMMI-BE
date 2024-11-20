create table photo_feed
(
    id               bigint       not null auto_increment primary key,
    user_id          bigint       not null,
    comment          varchar(255) not null,
    thumps_up_count  integer      not null,
    created_at       timestamp(3) not null,
    last_modified_at timestamp(3) not null,
    foreign key (user_id) references uuser (id)
) engine=InnoDB;

create table thumps_up
(
    id               bigint       not null auto_increment primary key,
    user_id          bigint       not null,
    photo_feed_id    bigint       not null,
    created_at       timestamp(3) not null,
    last_modified_at timestamp(3) not null,
    foreign key (user_id) references uuser (id),
    foreign key (photo_feed_id) references photo_feed (id)
) engine=InnoDB;

create table photo_feed_image
(
    id            bigint       not null auto_increment primary key,
    photo_feed_id bigint       not null,
    filename      varchar(255) not null,
    foreign key (photo_feed_id) references photo_feed (id)
) engine=InnoDB;
