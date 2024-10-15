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
            String sql = "select all from account;";
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

    /**
     * Insert a new account into Account table.
     * 
     * @param account
     * @return an account object that is inserted.
     */
    public Account insert(Account account)
    {
        Connection connection = ConnectionUtil.getConnection();
        try
        {
            String sql = "insert into account (username, password) values (?,?);";
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

    // public Account update
    //public Account delete
    
    /**
     * Check if username already exists in the database.
     * 
     * @param username
     * @return true if usename already exists, otherwise, false.
     */
    public boolean isUsernameExisted(String username)
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
