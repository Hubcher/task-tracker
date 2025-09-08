alter table task
    add column created timestamp default CURRENT_TIMESTAMP;