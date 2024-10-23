package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.*;

public class AccountDAO 
{
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
     * Check if username already exists in the database.
     * 
     * @param username
     * @return true if username already exists, otherwise, false.
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
}
