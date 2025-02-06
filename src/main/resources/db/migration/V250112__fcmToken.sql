create table fcm_token
(
    id               bigint       not null auto_increment primary key,
    user_id          bigint       not null,
    token            varchar(255) not null,
    last_used_time   timestamp(3) not null,
    created_at       timestamp(3) not null,
    last_modified_at timestamp(3) not null,
    foreign key (user_id) references uuser (id)
);
