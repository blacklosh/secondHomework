insert into teacher(first_name, last_name, exp)
VALUES ('Иван','Иванов',1);
insert into teacher(first_name, last_name, exp)
VALUES ('Пётр','Петров',2);
insert into teacher(first_name, last_name, exp)
VALUES ('Василий','Сидоров',3);

insert into course(course_name, date, teacher_id)
values ('1C','01.02.23',1);
insert into course(course_name, date, teacher_id)
values ('Java','04.05.26',1);
insert into course(course_name, date, teacher_id)
values ('PHP','09.08.27',2);

insert into lesson(lesson_name, date,course_id)
values ('1c-1','x',1);
insert into lesson(lesson_name, date,course_id)
values ('1c-2','y',1);
insert into lesson(lesson_name, date,course_id)
values ('1c-3','z',1);
insert into lesson(lesson_name, date,course_id)
values ('j1','w',2);
insert into lesson(lesson_name, date,course_id)
values ('j2','v',2);
insert into lesson(lesson_name, date,course_id)
values ('php1','g',3);

insert into student(first_name, last_name, group_id)
values ('Имя1','Ф1',001);
insert into student(first_name, last_name, group_id)
values ('Имя2','Ф2',001);
insert into student(first_name, last_name, group_id)
values ('Имя3','Ф3',002);

insert into studcourse(student_id,course_id)
values (1,1);
insert into studcourse(student_id,course_id)
values (1,2);
insert into studcourse(student_id,course_id)
values (2,2);
insert into studcourse(student_id,course_id)
values (2,3);
insert into studcourse(student_id,course_id)
values (3,1);