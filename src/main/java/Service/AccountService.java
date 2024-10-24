package Service;

import Model.Account;
import DAO.AccountDAO;
import java.sql.SQLException;

public class AccountService
{
    private AccountDAO accountDao;

    /**
     * Default constructor
     */
    public AccountService()
    {
        accountDao = new AccountDAO();
    }

    /**
     * Constructor to use external AccountDAO
     * @param accountDao
     */
    public AccountService(AccountDAO accountDao)
    {
        this.accountDao = accountDao;
    }
    
    /**
     * This method inserts a new account into the database.
     * 
     * @param account The account to be inserted
     * @return The inserted account
     * @throws SQLException if an error occurs during accessing database
     */
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
    
    /**
     * This method authenticates an account credential with username and password.
     * 
     * @param account The account to be authenticated
     * @return The authenticated account, or null if not authenticated
     * @throws SQLException if an error occurs during accessing database
     */
    public Account authenticate(Account account) throws SQLException
    {
        String username = account.getUsername().trim();
        String password = account.getPassword().trim();

        Account retrievedAccount = getAccountByUsername(username);
        if(retrievedAccount != null && retrievedAccount.getPassword().equals(password))
        {
            return retrievedAccount;
        }
        return null;
    }
    
    /**
     * This method retrieves an account by the username
     * @param username The username of the account to be retrieved
     * @return The retrieved account
     * @throws SQLException if an error occurs during accessing database
     */
    private Account getAccountByUsername(String username) throws SQLException
    {
        Account account = accountDao.getAccountByUsername(username);
        return account;    
    }
   
    /**
     * This method validates if the account meet all requirements.
     * 
     * @param account The account to be validated
     * @throws SQLException if an error occurs during accessing database
     */
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
  
    /**
     * This method retrieves an account by an account ID.
     * 
     * @param posted_by The account ID in the message used to retrieve the account
     * @return The retrieved account
     * @throws SQLException if an error occurs during accessing database
     */
    public Account getAccountByAccountId(int posted_by) throws SQLException
    {
        Account account = accountDao.getAccountByAccountId(posted_by);    
        return account;
    }
}