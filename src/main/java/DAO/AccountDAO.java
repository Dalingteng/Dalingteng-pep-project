package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.*;

public class AccountDAO 
{
    /**
     * This method retrieves all accounts from the database.
     * 
     * @return A list of all accounts in the database
     * @throws SQLException if an error occurs during accessing database
     */
    public List<Account> getAllAccounts() throws SQLException
    {
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account;";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
            Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            accounts.add(account);
        }
        return accounts;
    }

    /**
     * This method retrieves an account identified by an account ID
     * 
     * @param id The account ID of the account to be retrieved
     * @return The retrieved account, or null if account did not exist
     * @throws SQLException if an error occurs during accessing database
     */
    public Account getAccountByAccountId(int id) throws SQLException
    {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE account_id=?;";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
            Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            return account;
        }
        return null;
    }
    
    /**
     * This method checks if the username already existed in the database.
     * 
     * @param username The username to be checked
     * @return True if existed, otherwise False
     */
    public boolean alreadyExist(String username) throws SQLException
    {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT COUNT(*) FROM account WHERE username=?;";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
            if(rs.getInt(1) > 0) 
                return true;
        }
        return false;
    }

    /**
     * This method retrieves an account by a username.
     * 
     * @param username The username of the account to be retrieved
     * @return The retrieved account by the username
     * @throws SQLException if an error occurs during accessing database
     */
    public Account getAccountByUsername(String username) throws SQLException
    {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE username=?;";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
            Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            return account;
        }
        return null;
    }

    /**
     * This method inserts an account into the database.
     * 
     * @param account The account to be inserted
     * @return The inserted account
     * @throws SQLException if an error occurs during accessing database
     */
    public Account insertAccount(Account account) throws SQLException
    {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "INSERT INTO account (username, password) VALUES (?,?);";
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, account.getUsername());
        ps.setString(2, account.getPassword());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if(rs.next())
        {
            int generated_account_id = rs.getInt(1);
            return new Account(generated_account_id, account.getUsername(), account.getPassword());
        }
        return null;
    }

    /**
     * This method updates an account identified by an account ID in the database.
     * 
     * @param id The account id of the account to be updated
     * @param account The new account to update
     * @throws SQLException if an error occurs during accessing database
     */
    public void updateAccount(int id, Account account) throws SQLException
    {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "UPDATE account SET username=?, password=? WHERE account_id=?;";
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, account.getUsername());
        ps.setString(2, account.getPassword());
        ps.setInt(3, id);
        ps.executeUpdate();
    }
    
    /**
     * This method deletes an account identified by an account ID from the database.
     * 
     * @param id The account ID of the account to be deleted
     * @param account The deleted account
     * @return True if deleted, otherwise False
     * @throws SQLException if an error occurs during accessing database
     */
    public boolean deleteAccount(int id) throws SQLException
    {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "DELETE FROM account WHERE account_id=?;";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    }
}
