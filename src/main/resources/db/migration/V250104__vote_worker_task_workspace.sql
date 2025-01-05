alter table vote add column automatic boolean not null;

alter table worker drop foreign key worker_ibfk_1;
alter table worker drop column task_id;

drop table task;

alter table workspace add column task varchar(255) not null;


