package ru.itis.models;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

public class Course {
    private Integer id;
    private String name;
    private String date;
    private Integer teacherId;
    private Student[] students;

    public Course(String name, String date, Integer teacherId, Student[] students) {
        this.name = name;
        this.date = date;
        this.teacherId = teacherId;
        this.students = students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(getId(), course.getId()) &&
                Objects.equals(getName(), course.getName()) &&
                Objects.equals(getDate(), course.getDate()) &&
                Objects.equals(getTeacherId(), course.getTeacherId()) &&
                Arrays.equals(getStudents(), course.getStudents());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getId(), getName(), getDate(), getTeacherId());
        result = 31 * result + Arrays.hashCode(getStudents());
        return result;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", teacherId=" + teacherId +
                ", students=" + Arrays.toString(students) +
                '}'+"\r\n";
    }

    public Course(Integer id, String name, String date, Integer teacherId, Student[] students) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.teacherId = teacherId;
        this.students = students;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Student[] getStudents() {
        return students;
    }

    public void setStudents(Student[] students) {
        this.students = students;
    }
}
