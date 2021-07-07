package ru.itis;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * 03.07.2021
 * 01. DB
 *
 * @author Sidikov Marsel (First Software Engineering Platform)
 * @version v1.0
 */
public class SimpleDataSource implements DataSource {

    private Connection connection;

    private final Properties properties;

    public SimpleDataSource(Properties properties) {
        this.properties = properties;
        // данная инструкция позволит загрузить драйвер в виртуальную машину
        loadDriver();
    }

    private void loadDriver() {
        try {
            Class.forName(this.properties.getProperty("db.driver"));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            openConnection();
        }
        return connection;
    }

    private void openConnection() {
        try {
            connection = DriverManager.getConnection(
                    properties.getProperty("db.url"),
                    properties.getProperty("db.user"),
                    properties.getProperty("db.password"));
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new NotImplementedException();
    }
}
