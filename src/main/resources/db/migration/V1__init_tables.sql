CREATE TABLE user_ (
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(255) NOT NULL
);

CREATE TABLE category (
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE event (
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name       VARCHAR(255) NOT NULL UNIQUE,
    started_at TIMESTAMPTZ  NOT NULL,
    ended_at   TIMESTAMPTZ  NOT NULL,

    CONSTRAINT check_event_dates CHECK (
                started_at < ended_at AND
                started_at >= NOW() AND
                ended_at > NOW()
        )
);

CREATE TABLE team (
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE team_registration (
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    team_result VARCHAR(255) NOT NULL,
    team_id     BIGINT       NOT NULL,
    event_id    BIGINT       NOT NULL,

    UNIQUE (team_id, event_id),

    FOREIGN KEY (team_id)
        REFERENCES team (id)
        ON DELETE CASCADE,

    FOREIGN KEY (event_id)
        REFERENCES event (id)
        ON DELETE CASCADE
);

CREATE TABLE task (
    id               BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    question         TEXT         NOT NULL,
    description      TEXT         NOT NULL,
    number_of_points INT          NOT NULL CHECK (number_of_points > 0),
    event_id         BIGINT       NOT NULL,
    category_id      BIGINT       NOT NULL,

    UNIQUE (event_id, name),

    FOREIGN KEY (event_id)
        REFERENCES event (id)
        ON DELETE CASCADE,

    FOREIGN KEY (category_id)
        REFERENCES category (id)
        ON DELETE SET NULL
);

CREATE TABLE file (
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    path    VARCHAR(255) NOT NULL UNIQUE,
    task_id BIGINT       NOT NULL,

    FOREIGN KEY (task_id)
        REFERENCES task (id)
        ON DELETE CASCADE
);

CREATE TABLE task_dependence (
    id                BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    dependent_task_id BIGINT NOT NULL,
    parent_task_id    BIGINT NOT NULL,

    UNIQUE (dependent_task_id, parent_task_id),

    FOREIGN KEY (dependent_task_id)
        REFERENCES task (id)
        ON DELETE CASCADE,

    FOREIGN KEY (parent_task_id)
        REFERENCES task (id)
        ON DELETE CASCADE
);

CREATE TABLE student (
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    user_id    BIGINT       NOT NULL UNIQUE,

    FOREIGN KEY (user_id)
        REFERENCES user_ (id)
        ON DELETE CASCADE
);

CREATE TABLE team_member (
    id                   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    team_role            VARCHAR(255) NOT NULL,
    team_registration_id BIGINT NOT NULL,
    student_id           BIGINT NOT NULL,

    UNIQUE (team_registration_id, student_id),

    FOREIGN KEY (team_registration_id)
        REFERENCES team_registration (id)
        ON DELETE CASCADE,

    FOREIGN KEY (student_id)
        REFERENCES student (id)
        ON DELETE CASCADE
);

CREATE TABLE team_task_assignment (
    id                   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    correct_answer       VARCHAR(255) NOT NULL,
    completed_at         TIMESTAMPTZ,
    team_registration_id BIGINT       NOT NULL,
    task_id              BIGINT       NOT NULL,

    UNIQUE (team_registration_id, task_id),

    FOREIGN KEY (team_registration_id)
        REFERENCES team_registration (id)
        ON DELETE CASCADE,

    FOREIGN KEY (task_id)
        REFERENCES task (id)
        ON DELETE CASCADE
);
