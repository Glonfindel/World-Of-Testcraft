package com.worldoftestcraft.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.worldoftestcraft.repository.*;

import org.junit.After;
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
    private PreparedStatement dropTableStmt;
    @Mock
    ResultSet resultSet;
    
    @Before
    public void initRepository() throws SQLException {
        when(connectionMock.prepareStatement("INSERT INTO Account (login, password) VALUES (?, ?)")).thenReturn(addAccountStmt);
        when(connectionMock.prepareStatement("SELECT id, login, password FROM Account")).thenReturn(getAllAccountsStmt);
        when(connectionMock.prepareStatement("SELECT id, login, password FROM Account WHERE id = ?")).thenReturn(getAccountByIdStmt);
        when(connectionMock.prepareStatement("UPDATE Account SET login = ?, password = ? WHERE id = ?")).thenReturn(updateAccountStmt);
        when(connectionMock.prepareStatement("DELETE FROM Account WHERE id = ?")).thenReturn(deleteAccountStmt);
        when(connectionMock.prepareStatement("DROP TABLE Account")).thenReturn(dropTableStmt);
        accountRepository = new AccountRepositoryImpl();
        accountRepository.setConnection(connectionMock);
        verify(connectionMock).prepareStatement("INSERT INTO Account (login, password) VALUES (?, ?)");
        verify(connectionMock).prepareStatement("SELECT id, login, password FROM Account");
        verify(connectionMock).prepareStatement("SELECT id, login, password FROM Account WHERE id = ?");
        verify(connectionMock).prepareStatement("UPDATE Account SET login = ?, password = ? WHERE id = ?");
        verify(connectionMock).prepareStatement("DELETE FROM Account WHERE id = ?");
        verify(connectionMock).prepareStatement("DROP TABLE Account");
    }

    @Test
    public void checkAdding() throws Exception {
        when(addAccountStmt.executeUpdate()).thenReturn(1);
     
        Account account = new Account();
        account.setLogin("Glonfindel");
        account.setPassword("aaa");
        assertEquals(1, accountRepository.add(account));
        verify(addAccountStmt, times(1)).setString(1, "Glonfindel");
        verify(addAccountStmt, times(1)).setString(2, "aaa");
        verify(addAccountStmt).executeUpdate();
    }

    @Test
    public void checkAddingInOrder() throws Exception {
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
    public void checkExceptionWhenAddingNullAdding() throws Exception {
        when(addAccountStmt.executeUpdate()).thenThrow(new SQLException());
        Account account = new Account();
        account.setLogin(null);
        account.setPassword("aaa");
        assertEquals(1, accountRepository.add(account));
    }

    @Test
    public void checkDeleting() throws Exception {
        when(deleteAccountStmt.executeUpdate()).thenReturn(1);
        Account account = new Account();
        account.setId(1);
        account.setLogin("Glonfindel");
        account.setPassword("aaa");
        assertEquals(1, accountRepository.delete(account));
        verify(deleteAccountStmt, times(1)).setInt(1, account.getId());
        verify(deleteAccountStmt).executeUpdate();
    }
}
