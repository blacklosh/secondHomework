create table teacher
(
    id serial primary key,
    first_name varchar(20) not null,
    last_name varchar(20) not null,
    exp integer
);

create table course
(
    id serial primary key,
    course_name varchar(20) not null,
    date varchar(20) not null,
    teacher_id serial,
    foreign key (teacher_id) references teacher(id)
);

create table lesson
(
    id serial primary key,
    lesson_name varchar(20) not null,
    date varchar(20) not null,
    course_id serial,
    foreign key (course_id) references course(id)
);


create table student
(
    id serial primary key,
    first_name varchar(20) not null,
    last_name varchar(20) not null,
    group_id integer
);

create table studcourse
(
    student_id serial,
    course_id serial,
    foreign key (student_id) references student(id),
    foreign key (course_id) references course(id)
);