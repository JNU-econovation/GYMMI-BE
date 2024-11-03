create table workout_proof
(
    id        bigint       not null auto_increment primary key,
    image_url varchar(500) not null,
    comment   varchar(500) not null
) engine=InnoDB;

alter table workout_history
    add column workout_proof_id bigint not null;

alter table workout_history
    add foreign key (workout_proof_id) references workout_proof (id);








