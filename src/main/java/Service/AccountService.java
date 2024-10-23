package Service;

import Model.Account;
import DAO.AccountDAO;

import java.sql.SQLException;
import java.util.*;

public class AccountService
{
    private AccountDAO accountDao;
    public AccountService()
    {
        accountDao = new AccountDAO();
    }
    public AccountService(AccountDAO accountDao)
    {
        this.accountDao = accountDao;
    }
    

    public Account registerAccount(Account account) throws SQLException
    {
        // Validate the account
        validateAccount(account); 

        // Check if account already exists by username
        Account searchedAccount = getAccountByUsername(account.getUsername());
        if(searchedAccount != null)
        {
            throw new RuntimeException("Account already exists.");
        }

        // Insert new account into the database
        Account newAccount = accountDao.insertAccount(account);
        return newAccount;
    }
    
    // private void loginAccount(Context ctx)
    // {

    // }

    private Account getAccountByUsername(String username) throws SQLException
    {
        Account account = accountDao.getAccountByUsername(username);
        return account;    
    }
   
    private void validateAccount(Account account) throws SQLException 
    {
        String username = account.getUsername();
        String password = account.getPassword();
        boolean usernameExisted = false;

        // Check if username is empty
        if(username.isEmpty())
        {
            throw new IllegalArgumentException("Username cannot be empty.");
        }

        // Check if password is empty
        if(password.isEmpty())    
        {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        // Check if password is at least 4 characters
        if(password.length() < 4)
        {
            throw new IllegalArgumentException("Password must be at least 4 characters.");
        }
        
        // Check if username already exists
        usernameExisted = accountDao.alreadyExist(account.getUsername());
        if(usernameExisted)
        {
            throw new RuntimeException("Username is already existed.");
        }
    }

    
    
    public Account getAccountByAccountId(int posted_by) throws SQLException
    {
        Account account = accountDao.getAccountByAccountId(posted_by);    
        return account;
    }

    
}