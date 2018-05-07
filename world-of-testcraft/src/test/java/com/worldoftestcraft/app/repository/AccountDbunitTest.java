package com.worldoftestcraft.app.repository;

import static org.junit.Assert.*;

import java.net.URL;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.worldoftestcraft.app.Account;
import com.worldoftestcraft.repository.AccountRepository;
import com.worldoftestcraft.repository.AccountRepositoryImpl;

import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AccountDbunitTest extends DBTestCase {
    public static String url = "jdbc:hsqldb:hsql://localhost/workdb";

    AccountRepository accountRepository;

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        accountRepository = new AccountRepositoryImpl(DriverManager.getConnection(url));
    }

    @Test
    public void doNothing() {
        assertEquals(4, accountRepository.getAll().size());
    }

    @Test
    public void checkAdding() throws Exception {
        Account account = new Account();
        account.setLogin("Glonfindel");
        account.setPassword("aaa");

        assertEquals(1, accountRepository.add(account));

        IDataSet dbDataSet = this.getConnection().createDataSet();
        ITable actualTable = dbDataSet.getTable("ACCOUNT");
        ITable filteredTable = DefaultColumnFilter.excludedColumnsTable(actualTable, new String[] { "ID" });
        IDataSet expectedDataSet = getDataSet("ds-2.xml");
        ITable expectedTable = expectedDataSet.getTable("ACCOUNT");
        Assertion.assertEquals(expectedTable, filteredTable);
        accountRepository.delete(account);
    }

    @Override
    protected DatabaseOperation getSetUpOperation() throws Exception {
        return DatabaseOperation.INSERT;
    }

    @Override
    protected DatabaseOperation getTearDownOperation() throws Exception {
        return DatabaseOperation.DELETE;
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return this.getDataSet("ds-1.xml");
    }

    protected IDataSet getDataSet(String datasetName) throws Exception {
        URL url = getClass().getClassLoader().getResource(datasetName);
        FlatXmlDataSet ret = new FlatXmlDataSetBuilder().build(url.openStream());
        return ret;
    }

}