package ru.itis;

import ru.itis.models.Account;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 03.07.2021
 * 01. DB
 *
 * @author Sidikov Marsel (First Software Engineering Platform)
 * @version v1.0
 */
public class AccountsRepositoryJdbcImpl implements AccountsRepository {

    //language=SQL
    private static final String SQL_SELECT_ALL = "select * from account order by id";

    //language=SQL
    private static final String SQL_SELECT_ALL_BY_FIRST_NAME = "select * from account where first_name = ?";

    //language=SQL
    private static final String SQL_SELECT_BY_ID = "select  * from account where id = ?";

    //language=SQL
    private static final String SQL_INSERT = "INSERT INTO account(first_name,last_name,age) VALUES (?,?,?)";

    //language=SQL
    private static final String SQL_SELECT_ALL_BY_LIKE = "select * from account where first_name like ? or last_name like ?";

    private final DataSource dataSource;

    public AccountsRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Function<ResultSet, Account> map = row -> {
        try {
            int id = row.getInt("id");
            String firstName = row.getString("first_name");
            String lastName = row.getString("last_name");
            int age = row.getInt("age");

            return new Account(id, firstName, lastName, age);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    };

    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();

            Statement statement = connection.createStatement();

            ResultSet rows = statement.executeQuery(SQL_SELECT_ALL);

            while (rows.next()) {
                accounts.add(map.apply(rows));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        return accounts;
    }

    @Override
    public List<Account> findAllByFirstName(String searchFirstName) {
        List<Account> accounts = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL_BY_FIRST_NAME);
            statement.setString(1, searchFirstName);

            ResultSet rows = statement.executeQuery();

            while (rows.next()) {
                accounts.add(map.apply(rows));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        return accounts;
    }

    @Override
    public Account findById(Integer id) {
        Account account = null;
        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID);
            statement.setInt(1, id);

            ResultSet rows = statement.executeQuery();

            if (rows.next()) {
                account = (map.apply(rows));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        return account;
    }

    @Override
    public List<Account> findAllByFirstNameOrLastNameLike(String name) {
        List<Account> accounts = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL_BY_LIKE);
            statement.setString(1, name);
            statement.setString(2, name);

            ResultSet rows = statement.executeQuery();

            while (rows.next()) {
                accounts.add(map.apply(rows));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        return accounts;
    }

    @Override
    public void save(Account a) {
        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement statement = connection.prepareStatement(SQL_INSERT);

            statement.setString(1, a.getFirstName());
            statement.setString(2, a.getLastName());
            statement.setInt(3, a.getAge());

            statement.execute();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
