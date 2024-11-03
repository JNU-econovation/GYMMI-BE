create table uuser
(
    id               bigint                  not null auto_increment primary key,
    is_resigned      boolean                 not null,
    created_at       timestamp(3)            not null,
    last_modified_at timestamp(3)            not null,
    email            varchar(255) default '' not null,
    login_id         varchar(255)            not null,
    nickname         varchar(255)            not null,
    password         varchar(255)            not null
) engine=InnoDB;

create table feedback
(
    id               bigint       not null auto_increment primary key,
    user_id          bigint       not null,
    content          tinytext     not null,
    created_at       timestamp(3) not null,
    last_modified_at timestamp(3) not null,
    foreign key (user_id) references uuser (id)
) engine=InnoDB;

create table workspace
(
    goal_score       integer                 not null,
    head_count       integer                 not null,
    created_at       timestamp(3)            not null,
    creator          bigint                  not null,
    status           varchar(255)            not null,
    id               bigint                  not null auto_increment primary key,
    last_modified_at timestamp(3)            not null,
    description      varchar(255) default '' not null,
    name             varchar(255)            not null,
    password         varchar(255)            not null,
    tag              varchar(255) default '' not null,
    foreign key (creator) references uuser (id)
) engine=InnoDB;

create table logined
(
    id               bigint       not null auto_increment primary key,
    user_id          bigint       not null unique,
    refresh_token    varchar(255),
    created_at       timestamp(3) not null,
    last_modified_at timestamp(3) not null,
    foreign key (user_id) references uuser (id)
) engine=InnoDB;

create table mission
(
    id           bigint       not null auto_increment primary key,
    score        integer,
    workspace_id bigint       not null,
    name         varchar(255) not null,
    foreign key (workspace_id) references workspace (id)
) engine=InnoDB;

create table profile_image
(
    id               bigint       not null auto_increment primary key,
    user_id          bigint       not null unique,
    origin_name      varchar(255) not null,
    stored_name      varchar(255) not null,
    created_at       timestamp(3) not null,
    last_modified_at timestamp(3) not null,
    foreign key (user_id) references uuser (id)
) engine=InnoDB;

create table task
(
    id        bigint       not null auto_increment primary key,
    is_picked bit          not null,
    name      varchar(255) not null
) engine=InnoDB;

create table worker
(
    contributed_score integer      not null,
    id                bigint       not null auto_increment primary key,
    task_id           bigint unique,
    user_id           bigint       not null,
    workspace_id      bigint       not null,
    created_at        timestamp(3) not null,
    last_modified_at  timestamp(3) not null,
    unique (user_id, workspace_id),
    foreign key (task_id) references task (id),
    foreign key (user_id) references uuser (id),
    foreign key (workspace_id) references workspace (id)
) engine=InnoDB;

create table workoutHistory
(
    id               bigint       not null auto_increment primary key,
    last_modified_at timestamp(3) not null,
    worker_id        bigint       not null,
    created_at       timestamp(3) not null,
    foreign key (worker_id) references worker (id)
) engine=InnoDB;

create table workout_record
(
    count            integer      not null,
    id               bigint       not null auto_increment primary key,
    mission_id       bigint       not null,
    working_id       bigint       not null,
    created_at       timestamp(3) not null,
    last_modified_at timestamp(3) not null,
    foreign key (mission_id) references mission (id),
    foreign key (working_id) references workoutHistory (id)
) engine=InnoDB;
