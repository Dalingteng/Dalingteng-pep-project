package Service;

import java.sql.SQLException;
import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {

    private MessageDAO messageDao;

    public MessageService()
    {
        messageDao = new MessageDAO();
    }
    public MessageService(MessageDAO messageDao)
    {
        this.messageDao = messageDao;
    }

    // private void createAccount(Context ctx)
    // {

    // }
    // private void loginAccount(Context ctx)
    // {

    // }
    // private Message createMessage(Message message, Account account)
    // {

    // }
    public List<Message> getAllMessages() throws SQLException
    {
        List<Message> messages = messageDao.getAllMessages();
        return messages;
    }

    public Message getMessageByMessageId(int id) throws SQLException
    {
        Message message = messageDao.getMessageByMessageId(id);
        return message;
    }
    
    public Message deleteMessageByMessageId(int id) throws SQLException
    {
        Message deletedMessage = messageDao.deleteMessageByMessageId(id);
        return deletedMessage;
    }
    // private Message updateMessageByMessageId(int id){}

    public List<Message> getAllMessagesByAccountId(int accountId) throws SQLException 
    {
        List<Message> messages = messageDao.getMessageByAccountId(accountId);
        return messages;
    }
    
}
