-- liquibase formatted sql

-- changeset dKarpov:1
CREATE INDEX student_name_index ON student (name);

-- changeset dKarpov:2
CREATE INDEX faculty_name_color_index ON faculty (name, color);