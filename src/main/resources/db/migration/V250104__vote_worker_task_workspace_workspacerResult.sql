alter table vote
    add column automatic boolean not null;

alter table worker drop foreign key worker_ibfk_1;
alter table worker drop column task_id;

drop table task;

alter table workspace
    add column task varchar(255) not null;

create table workspace_task
(
    id               bigint       not null auto_increment primary key,
    workspace_id     bigint       not null,
    winner_id        bigint       not null,
    loser_id         bigint       not null,
    created_at       timestamp(3) not null,
    last_modified_at timestamp(3) not null,
    foreign key (workspace_id) references worker (id),
    foreign key (winner_id) references worker (id),
    foreign key (loser_id) references worker (id)
);
