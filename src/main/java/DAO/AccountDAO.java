package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.*;

public class AccountDAO 
{
    /**
     * Retrieve all accounts from Account table.
     * 
     * @return a list of accounts. If there is no account, return null.
     */
    public List<Account> getAllAccounts()
    {
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        try
        {
            String sql = "SELECT * FROM account;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                accounts.add(account);
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    public Account getAccountByAccountId(int id)
    {
        Connection connection = ConnectionUtil.getConnection();
        try
        {
            String sql = "SELECT * FROM account WHERE account_id=?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                return account;
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Insert a new account into Account table.
     * 
     * @param account
     * @return an account object that is inserted.
     */
    public Account insertAccount(Account account)
    {
        Connection connection = ConnectionUtil.getConnection();
        try
        {
            String sql = "INSERT INTO account (username, password) VALUES (?,?);";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next())
            {
                int generated_account_id = (int) rs.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Update account in Account table by account id.
     * 
     * @param account
     * @return
     */
    public void updateAccount(int id, Account account)
    {
        Connection connection = ConnectionUtil.getConnection();
        try
        {
            String sql = "UPDATE account SET username=?, password=? WHERE account_id=?;";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.setInt(3, id);

            ps.executeUpdate();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    public void deleteAccount(int id, Account account)
    {
        Connection connection = ConnectionUtil.getConnection();
        try
        {
            String sql = "DELETE * from account WHERE accont_id=?;";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.setInt(3, id);

            ps.executeUpdate();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        } 
    }
    
    /**
     * Check if username already exists in the database.
     * 
     * @param username
     * @return true if usename already exists, otherwise, false.
     */
    public boolean alreadyExist(String username)
    {
        Connection connection = ConnectionUtil.getConnection();
        try
        {
            String sql = "select count(*) from account where username=?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            
            
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                return true;
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
