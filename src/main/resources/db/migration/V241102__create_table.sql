drop table workout_record;
drop table workoutHistory;

create table workout_history
(
    id               bigint       not null auto_increment primary key,
    last_modified_at timestamp(3) not null,
    is_approved      boolean      not null,
    total_score      integer      not null,
    worker_id        bigint       not null,
    created_at       timestamp(3) not null,
    foreign key (worker_id) references worker (id)
) engine=InnoDB;

create table workout_record
(
    count            integer      not null,
    id               bigint       not null auto_increment primary key,
    mission_id       bigint       not null,
    workout_history_id       bigint       not null,
    created_at       timestamp(3) not null,
    last_modified_at timestamp(3) not null,
    foreign key (mission_id) references mission (id),
    foreign key (workout_history_id) references workout_history (id)
) engine=InnoDB;

create table favorite_mission
(
    id               bigint       not null auto_increment primary key,
    mission_id       bigint       not null,
    worker_id        bigint       not null,
    created_at       timestamp(3) not null,
    last_modified_at timestamp(3) not null,
    foreign key (mission_id) references mission (id),
    foreign key (worker_id) references worker (id)
) engine=InnoDB;



