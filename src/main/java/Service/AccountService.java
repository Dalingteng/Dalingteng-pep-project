package Service;

import Model.Account;
import DAO.AccountDAO;
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
    public Account getAccountById(int posted_by) 
    {
        Account account = accountDao.getAccountByAccountId(posted_by);    
        return account;
    }

    
}