alter table workout_proof rename workout_confirmation;

alter table workout_history drop foreign key workout_history_ibfk_2;
alter table workout_history change column workout_proof_id workout_confirmation_id bigint not null unique;
alter table workout_history
    add foreign key (workout_confirmation_id) references workout_confirmation (id);

create table objection
(
    id                      bigint       not null auto_increment primary key,
    worker_id               bigint       not null,
    workout_confirmation_id bigint       not null unique,
    reason                  varchar(255) not null,
    is_in_progress          boolean      not null,
    created_at              timestamp(3) not null,
    last_modified_at        timestamp(3) not null,
    foreign key (worker_id) references worker (id),
    foreign key (workout_confirmation_id) references workout_confirmation (id)
) engine=InnoDB;

create table vote
(
    id               bigint       not null auto_increment primary key,
    worker_id        bigint       not null,
    objection_id     bigint       not null,
    is_approved      boolean      not null,
    created_at       timestamp(3) not null,
    last_modified_at timestamp(3) not null,
    foreign key (worker_id) references worker (id),
    foreign key (objection_id) references objection (id),
    unique (worker_id, objection_id)
) engine=InnoDB;
