ALTER TABLE workspace_result DROP FOREIGN KEY workspace_result_ibfk_1;

alter table workspace_result
    add foreign key (workspace_id) references workspace (id);

