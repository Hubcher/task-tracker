-- ===== ПРОЕКТЫ =====
INSERT INTO project (name) VALUES ('CRM Системы');
INSERT INTO project (name) VALUES ('E-Commerce Платформа');

-- ===== СОСТОЯНИЯ ЗАДАЧ (TASK STATES) =====
-- для проекта 1 (CRM System)
INSERT INTO task_state (name, created_at, project_id) VALUES ('Database Design', CURRENT_TIMESTAMP, 1);
INSERT INTO task_state (name, created_at, project_id) VALUES ('Authentication Module', CURRENT_TIMESTAMP, 1);

-- для проекта 2 (E-Commerce Platform)
INSERT INTO task_state (name, created_at, project_id) VALUES ('Deployment Setup', CURRENT_TIMESTAMP, 2);

-- ===== ЗАДАЧИ (TASKS) =====
-- у первого состояния (id=1)
INSERT INTO task (name, description, teg, priority, task_state_id)
VALUES ('Design database schema', 'Создать ERD и таблицы для CRM', 'backend', 'HIGH',  1);

INSERT INTO task (name, description, teg, priority, task_state_id)
VALUES ('Implement authentication', 'Добавить систему JWT-авторизации', 'security', 'MEDIUM',  1);

-- у второго состояния (id=2)
INSERT INTO task (name, description, teg, priority, task_state_id)
VALUES ('Set up CI/CD', 'Настроить GitHub Actions и Docker', 'devops', 'LOW', 2);

-- у третьего состояния (id=3)
INSERT INTO task (name, description, teg, priority,  task_state_id)
VALUES ('Product catalog', 'Разработать каталог товаров с фильтрами', 'frontend', 'HIGH',  3);

INSERT INTO task (name, description, teg, priority, task_state_id)
VALUES ('Shopping cart', 'Реализовать корзину с хранением в сессии', 'frontend', 'MEDIUM',  3);
