package ru.itis.models;

import java.util.Arrays;

public class Student {
    private Integer id;
    private String firstName;
    private String lastName;
    private Integer group;
    private Course[] courses;

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", group=" + group +
                ", courses=" + Arrays.toString(courses) +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    public Course[] getCourses() {
        return courses;
    }

    public void setCourses(Course[] courses) {
        this.courses = courses;
    }

    public Student(String firstName, String lastName, Integer group, Course[] courses) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.group = group;
        this.courses = courses;
    }

    public Student(Integer id, String firstName, String lastName, Integer group, Course[] courses) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.group = group;
        this.courses = courses;
    }
}
