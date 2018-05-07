package com.worldoftestcraft.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.worldoftestcraft.app.Account;
import com.worldoftestcraft.repository.AccountRepository;
import com.worldoftestcraft.repository.AccountRepositoryFactory;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@RestController
public class AccountApi {

    @Autowired
    AccountRepository accountRepository;

    @RequestMapping("/")
    public String index() {
        return "This is non rest, just checking if everything works.";
    }

    @RequestMapping(value = "/worldoftestcraft/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Account getAccount(@PathVariable("id") int id) throws SQLException {
        accountRepository = AccountRepositoryFactory.getInstance();
        return accountRepository.getById(id);
    }

    @RequestMapping(value = "/worldoftestcraft", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Account> getAccounts() throws SQLException {
        accountRepository = AccountRepositoryFactory.getInstance();
        List<Account> accounts = new LinkedList<Account>();
        for (Account a : accountRepository.getAll()) {
                accounts.add(a);
        }
        return accounts;
    }

    @RequestMapping(value = "/worldoftestcraft", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Long addAccount(@RequestBody Account a) {
        accountRepository = AccountRepositoryFactory.getInstance();
        return new Long(accountRepository.add(a));
    }

    @RequestMapping(value = "/worldoftestcraft/{id}", method = RequestMethod.DELETE)
    public Long deleteAccount(@PathVariable("id") int id) throws SQLException {
        accountRepository = AccountRepositoryFactory.getInstance();
        return new Long(accountRepository.delete(accountRepository.getById(id)));
    }

    @RequestMapping(value = "/worldoftestcraft/{id}", method = RequestMethod.PUT)
    public Long updateAccount(@PathVariable("id") int id, @RequestBody Account a) throws SQLException {
        accountRepository = AccountRepositoryFactory.getInstance();
        return new Long(accountRepository.update(id, a));
    }

}