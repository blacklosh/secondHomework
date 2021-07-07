package ru.itis;

import ru.itis.models.Course;

import java.util.List;
import java.util.Optional;

public interface CoursesRepository {
    List<Course> findAll();
    List<Course> findAllByName(String name);
    Optional<Course> findById(Integer id);
    void save(Course course);
    void update(Course course);

    boolean delete(Course course);
}
