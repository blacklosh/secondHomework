package ru.itis;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.itis.models.Account;
import ru.itis.models.Course;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    public static void main(String[] args)  {

        Properties properties = new Properties();

        try {
            properties.load(new FileReader("resources\\application.properties"));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        HikariConfig config = new HikariConfig();
        config.setDriverClassName(properties.getProperty("db.driver"));
        config.setJdbcUrl(properties.getProperty("db.url"));
        config.setUsername(properties.getProperty("db.user"));
        config.setPassword(properties.getProperty("db.password"));
        config.setMaximumPoolSize(Integer.parseInt(properties.getProperty("db.hikari.pool-size")));

        DataSource dataSource = new HikariDataSource(config);


        CoursesRepository coursesRepository = new CoursesRepositoryJdbcImpl(dataSource);

        Scanner scanner = new Scanner(System.in);

        // Can test all features!
        System.out.println(coursesRepository.findAll());
        System.out.println(coursesRepository.findAllByName("Java"));
        System.out.println(coursesRepository.findById(3));
        System.out.println(coursesRepository.findById(4));
        Course newest = new Course("Python","--/--/--",2,null);
        coursesRepository.save(newest);
        System.out.println(coursesRepository.findAllByName("Python"));
        newest.setDate("xx/xx/xx");
        coursesRepository.update(newest);
        System.out.println("-------------");
        System.out.println(coursesRepository.findAll());
        System.out.println(coursesRepository.delete(newest));
        System.out.println(coursesRepository.findAll());
    }
}
