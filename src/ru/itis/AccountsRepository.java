package ru.itis;

import ru.itis.models.Account;

import java.util.List;

/**
 * 03.07.2021
 * 01. DB
 *
 * @author Sidikov Marsel (First Software Engineering Platform)
 * @version v1.0
 */
public interface AccountsRepository {
    List<Account> findAll();
    List<Account> findAllByFirstName(String name);

    Account findById(Integer id);
    List<Account> findAllByFirstNameOrLastNameLike(String name);
    void save(Account account);
}
