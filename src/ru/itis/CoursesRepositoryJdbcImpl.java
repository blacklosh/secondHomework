package ru.itis;

import ru.itis.models.Course;
import ru.itis.models.Student;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.function.Function;

public class CoursesRepositoryJdbcImpl implements CoursesRepository{


    //language=SQL
    private static final String SQL_SELECT_ALL = "select * from course order by id";

    //language=SQL
    private static final String SQL_SELECT_ALL_BY_NAME = "select * from course where course_name = ?";

    //language=SQL
    private static final String SQL_SELECT_BY_ID = "select  * from course where id = ?";

    //language=SQL
    private static final String SQL_INSERT = "INSERT INTO course(course_name,date,teacher_id) VALUES (?,?,?)";

    //language=SQL
    private static final String SQL_UPDATE_BY_ID = "update course set course_name = ?, date = ?, teacher_id = ? where id = ?";

    //language=SQL
    private static final String SQL_DELETE = "DELETE FROM course WHERE id = ?";

    private final DataSource dataSource;

    public CoursesRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    private Function<ResultSet, Course> map = row -> {
        try {
            int id = row.getInt("id");
            String name = row.getString("course_name");
            String date = row.getString("date");
            int teacherId = row.getInt("teacher_id");

            return new Course(id, name, date, teacherId, null);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    };

    @Override
    public List<Course> findAll() {
        List<Course> courses = new ArrayList<>();
        ResultSet rows = null;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()){

            rows = statement.executeQuery(SQL_SELECT_ALL);

            while (rows.next()) {
                Course tmp = map.apply(rows);
                tmp.setStudents(getStudentsByCourse(tmp.getId()));
                courses.add(tmp);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            try {
                rows.close();
            } catch (Exception throwables){}
        }
        return courses;
    }

    @Override
    public List<Course> findAllByName(String name) {
        List<Course> courses = new ArrayList<>();
        ResultSet rows = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL_BY_NAME)){
            statement.setString(1,name);
            rows = statement.executeQuery();

            while (rows.next()) {
                Course tmp = map.apply(rows);
                tmp.setStudents(getStudentsByCourse(tmp.getId()));
                courses.add(tmp);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            try {
                rows.close();
            } catch (SQLException throwables){}
        }
        return courses;
    }

    @Override
    public Optional<Course> findById(Integer id) {
        Course result = null;
        ResultSet rows = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)){
            statement.setInt(1,id);
            rows = statement.executeQuery();

            if (rows.next()) {
                result = map.apply(rows);
                result.setStudents(getStudentsByCourse(result.getId()));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            try {
                rows.close();
            } catch (SQLException throwables){}
        }
        return (result==null?Optional.empty():Optional.of(result));
    }

    @Override
    public void save(Course course) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(SQL_INSERT);

            List<Course> tmp = new ArrayList<>();
            tmp.addAll(findAll());

            statement.setString(1, course.getName());
            statement.setString(2, course.getDate());
            statement.setInt(3, course.getTeacherId());

            connection = dataSource.getConnection();
            int affectedRows = statement.executeUpdate();
            connection = dataSource.getConnection();
            addStudentsToCourse(course.getId(), course.getStudents());

            if (affectedRows != 1) {
                throw new SQLException("Exception in <Insert> (1)");
            }

            List<Course> result = new ArrayList<>();
            connection = dataSource.getConnection();
            result.addAll(findAll());

            Set<Integer> t1 = new HashSet<>();
            for(Course c : result){t1.add(c.getId());}
            for(Course c : tmp){t1.remove(c.getId());}

            if(t1.size()!=1){
                throw new SQLException("Exception in <Insert> (2)");
            }

            course.setId(t1.toArray(new Integer[t1.size()])[0]);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            try{
                connection.close();
            }catch(Exception e){}
            try{
                statement.close();
            }catch(Exception e){}
        }
    }

    @Override
    public void update(Course course) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_BY_ID)) {

            statement.setString(1, course.getName());
            statement.setString(2, course.getDate());
            statement.setInt(3, course.getTeacherId());
            statement.setInt(4, course.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Exception in <Update>");
            }

            removeStudentsFromCourse(course.getId());
            addStudentsToCourse(course.getId(), course.getStudents());

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }

    }

    @Override
    public boolean delete(Course course) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {

            statement.setInt(1, course.getId());

            int affectedRows = statement.executeUpdate();

            removeStudentsFromCourse(course.getId());

            return affectedRows>0;

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Function<ResultSet, Student> map2 = row -> {
        try {
            int id = row.getInt("id");
            String firstName = row.getString("first_name");
            String lastName = row.getString("last_name");
            int groupId = row.getInt("group_id");

            return new Student(id, firstName, lastName, groupId, null);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    };

    private Student[] getStudentsByCourse(Integer id){
        ArrayList<Student> result = new ArrayList<>();
        String SQL_SELECT_STUDENTS_BY_COURSE = "SELECT * FROM student WHERE id IN (SELECT distinct student_id FROM studcourse WHERE course_id = ?)";

        ResultSet rows = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_STUDENTS_BY_COURSE)){

            statement.setInt(1,id);
            rows = statement.executeQuery();

            while (rows.next()) {
                result.add(map2.apply(rows));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            try {
                rows.close();
            } catch (Exception throwables){}
        }

        return result.toArray(new Student[result.size()]);
    }

    private void removeStudentsFromCourse(Integer courseId){
        String SQL_DELETE_STUDENT_FROM_COURSE = "DELETE FROM studcourse WHERE course_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE_STUDENT_FROM_COURSE)) {

            statement.setInt(1, courseId);

            int affectedRows = statement.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void addStudentsToCourse(Integer courseId, Student[] students){
        String SQL_INSERT_STUDENT_TO_COURSE = "INSERT INTO studcourse VAlUES (?,?)";
        //Set<Student> alreadyOnCouse = new HashSet<>();
        if(students!=null)
        for(Student student : students){
            if(!isStudentOnCourse(student.getId(),courseId)){
                try (Connection connection = dataSource.getConnection();
                     PreparedStatement statement = connection.prepareStatement(SQL_INSERT_STUDENT_TO_COURSE)) {

                    statement.setInt(1, student.getId());
                    statement.setInt(2, courseId);

                    int affectedRows = statement.executeUpdate();

                    if (affectedRows != 1) {
                        throw new SQLException("Exception in <Insert> (3)");
                    }
                } catch (SQLException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }
    }

    private boolean isStudentOnCourse(Integer studentId, Integer courseId){
        Student[] students = getStudentsByCourse(courseId);
        for(Student s : students){
            if(s.getId()==studentId) return true;
        }
        return false;
    }
}
