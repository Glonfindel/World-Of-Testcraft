package com.worldoftestcraft.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.worldoftestcraft.repository.*;

import org.junit.Before;

@RunWith(MockitoJUnitRunner.class)
public class AccountMockedTest {
    AccountRepository accountRepository;

    @Mock
    private Connection connectionMock;
    @Mock
    private PreparedStatement addAccountStmt;
    @Mock
    private PreparedStatement updateAccountStmt;
    @Mock
    private PreparedStatement deleteAccountStmt;
    @Mock
    private PreparedStatement getAllAccountsStmt;
    @Mock
    private PreparedStatement getAccountByIdStmt;
    @Mock
    AccountRepository accountRepositoryMock;

    @Before
    public void initRepository() throws SQLException {
        when(connectionMock.prepareStatement("INSERT INTO Account (login, password) VALUES (?, ?)"))
                .thenReturn(addAccountStmt);
        when(connectionMock.prepareStatement("SELECT id, login, password FROM Account")).thenReturn(getAllAccountsStmt);
        when(connectionMock.prepareStatement("SELECT id, login, password FROM Account WHERE id = ?"))
                .thenReturn(getAccountByIdStmt);
        when(connectionMock.prepareStatement("UPDATE Account SET login = ?, password = ? WHERE id = ?"))
                .thenReturn(updateAccountStmt);
        when(connectionMock.prepareStatement("DELETE FROM Account WHERE id = ?")).thenReturn(deleteAccountStmt);
        accountRepository = new AccountRepositoryImpl();
        accountRepository.setConnection(connectionMock);
        accountRepositoryMock = mock(AccountRepositoryImpl.class);
        verify(connectionMock).prepareStatement("INSERT INTO Account (login, password) VALUES (?, ?)");
        verify(connectionMock).prepareStatement("SELECT id, login, password FROM Account");
        verify(connectionMock).prepareStatement("SELECT id, login, password FROM Account WHERE id = ?");
        verify(connectionMock).prepareStatement("UPDATE Account SET login = ?, password = ? WHERE id = ?");
        verify(connectionMock).prepareStatement("DELETE FROM Account WHERE id = ?");
    }

    @Test
    public void checkAdd() throws Exception {
        when(addAccountStmt.executeUpdate()).thenReturn(1);

        Account account = new Account();
        account.setId(1);
        account.setLogin("Glonfindel");
        account.setPassword("aaa");
        assertEquals(1, accountRepository.add(account));
        verify(addAccountStmt, times(1)).setString(1, "Glonfindel");
        verify(addAccountStmt, times(1)).setString(2, "aaa");
        verify(addAccountStmt).executeUpdate();
    }

    @Test
    public void checkAddInOrder() throws Exception {
        InOrder inorder = inOrder(addAccountStmt);
        when(addAccountStmt.executeUpdate()).thenReturn(1);

        Account account = new Account();
        account.setLogin("Glonfindel");
        account.setPassword("aaa");
        assertEquals(1, accountRepository.add(account));

        inorder.verify(addAccountStmt, times(1)).setString(1, "Glonfindel");
        inorder.verify(addAccountStmt, times(1)).setString(2, "aaa");
        inorder.verify(addAccountStmt).executeUpdate();
    }

    @Test(expected = IllegalStateException.class)
    public void checkExceptionWhenAddNullAdding() throws Exception {
        when(addAccountStmt.executeUpdate()).thenThrow(new SQLException());
        Account account = new Account();
        account.setLogin(null);
        account.setPassword("aaa");
        assertEquals(1, accountRepository.add(account));
    }

    @Test
    public void checkDelete() throws Exception {
        when(deleteAccountStmt.executeUpdate()).thenReturn(1);
        Account account = new Account();
        account.setId(1);
        account.setLogin("Glonfindel");
        account.setPassword("aaa");
        assertEquals(1, accountRepository.delete(account));
        verify(deleteAccountStmt, times(1)).setInt(1, account.getId());
        verify(deleteAccountStmt).executeUpdate();
    }

    @Test
    public void checkGetAll() throws Exception {
        AbstractResultSet mockedResultSet = mock(AbstractResultSet.class);
        when(mockedResultSet.next()).thenCallRealMethod();
        when(mockedResultSet.getInt("id")).thenCallRealMethod();
        when(mockedResultSet.getString("login")).thenCallRealMethod();
        when(mockedResultSet.getString("password")).thenCallRealMethod();
        when(getAllAccountsStmt.executeQuery()).thenReturn(mockedResultSet);
        assertEquals(1, accountRepository.getAll().size());
        verify(getAllAccountsStmt, times(1)).executeQuery();
        verify(mockedResultSet, times(1)).getInt("id");
        verify(mockedResultSet, times(1)).getString("login");
        verify(mockedResultSet, times(1)).getString("password");
        verify(mockedResultSet, times(2)).next();
    }

    @Test
    public void checkGetById() throws Exception {
        AbstractResultSet mockedResultSet = mock(AbstractResultSet.class);
        when(mockedResultSet.next()).thenCallRealMethod();
        when(mockedResultSet.getInt("id")).thenCallRealMethod();
        when(mockedResultSet.getString("login")).thenCallRealMethod();
        when(mockedResultSet.getString("password")).thenCallRealMethod();
        when(getAccountByIdStmt.executeQuery()).thenReturn(mockedResultSet);
        assertNotNull(accountRepository.getById(1));
        verify(getAccountByIdStmt, times(1)).executeQuery();
        verify(mockedResultSet, times(1)).getInt("id");
        verify(mockedResultSet, times(1)).getString("login");
        verify(mockedResultSet, times(1)).getString("password");
        verify(mockedResultSet, times(2)).next();
    }

    @Test
    public void checkUpdate() throws Exception {

        Account account = new Account();
        account.setId(1);
        account.setLogin("Glonfindel");
        account.setPassword("aaa");
        doReturn(account).when(accountRepositoryMock).getById(isA(int.class));
        int idToUpdate = 1;
        accountRepository.update(idToUpdate , account);
        assertEquals(accountRepositoryMock.getById(1).getLogin(), account.getLogin());
        verify(updateAccountStmt, times(1)).setString(1, "Glonfindel");
        verify(updateAccountStmt, times(1)).setString(2, "aaa");
        verify(updateAccountStmt).executeUpdate();
    }

    abstract class AbstractResultSet implements ResultSet {
        int i = 0;

        @Override
        public boolean next() throws SQLException {
            if (i == 1)
                return false;
            i++;
            return true;
        }

        @Override
        public int getInt(String s) throws SQLException{
            return 1;
        }

        @Override
        public String getString(String columnLabel) throws SQLException{
            switch (columnLabel) {
                case "login":
                    return "Glonfindel";
                case "password":
                    return "aaa";
                default:
                    return "";
            }
        }
    }

}
