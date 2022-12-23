SELECT student.name, student.age, faculty.name FROM student
LEFT JOIN faculty ON student.faculty_id = faculty.id;

SELECT student.name, student.age, faculty.name FROM student
LEFT JOIN faculty ON student.faculty_id = faculty.id
RIGHT JOIN avatar ON student.id = avatar.student_id;