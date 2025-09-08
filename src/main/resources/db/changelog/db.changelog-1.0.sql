-- Таблица проектов
-- TODO: Проверить  таблицы, а то наворотил
create table if not exists project (
    id serial primary key ,
    name varchar(64) not null
);

-- Таблица состояний задач
create table if not exists task_state (

    id bigserial primary key ,
    name varchar(64) not null ,
    created_at timestamp default CURRENT_TIMESTAMP,
    project_id int not null ,
    constraint fk_taskstate_project foreign key (project_id)
        references project(id) on delete cascade
);

-- Таблица задач
create table if not exists task (
    id bigserial primary key,
    name varchar(64) not null,
    description varchar(255),
    teg varchar(32),
    priority varchar(50),
    task_state_id bigint not null ,
    constraint fk_task_taskstate foreign key (task_state_id)
        references task_state(id) on delete cascade
);
