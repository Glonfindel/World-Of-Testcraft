package com.worldoftestcraft.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.worldoftestcraft.app.Account;
import com.worldoftestcraft.repository.AccountRepository;

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
        return accountRepository.getById(id);
    }

    @RequestMapping(value = "/worldoftestcraft", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Account> getAccounts(@RequestParam("filter") String f) throws SQLException {
        List<Account> accounts = new LinkedList<Account>();
        for (Account a : accountRepository.getAll()) {
            if (a.getLogin().contains(f))
                accounts.add(a);
        }
        return accounts;
    }

    @RequestMapping(value = "/worldoftestcraft", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Long addAccount(@RequestBody Account a) {
        return new Long(accountRepository.add(a));
    }

    @RequestMapping(value = "/worldoftestcraft/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Long deleteAccount(@PathVariable("id") int id) throws SQLException {
        return new Long(accountRepository.delete(accountRepository.getById(id)));
    }

    @RequestMapping(value = "/worldoftestcraft/", method = RequestMethod.PUT)
    @ResponseBody
    public Long updateAccount(@RequestBody Account a) throws SQLException {
        return new Long(accountRepository.update(1, a));
    }

}