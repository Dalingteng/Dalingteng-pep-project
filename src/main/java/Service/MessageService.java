package Service;

import static org.mockito.ArgumentMatchers.nullable;

import java.sql.SQLException;
import java.util.List;

import javax.management.RuntimeErrorException;

import DAO.MessageDAO;
import Model.Account;
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
    public Message createMessage(Message message, Account account) throws SQLException
    {
        // Check if the account exists
        if(account == null)
        {
            throw new RuntimeException("Account must exist before posting a message.");
        }

        // Validate the message
        validateMessage(message);

        // Insert the message into the database
        Message createdMessage = messageDao.createMessage(message, account);
        return createdMessage;
    }
    private void validateMessage(Message message) 
    {
        // Check if the message is empty
        if(message.getMessage_text().trim().isEmpty())
        {
            throw new RuntimeException("Message text cannot be empty.");
        }

        // Check if the message is over 255 characters
        if(message.getMessage_text().length() > 255)
        {
            throw new RuntimeException("Message text cannot be over 255 characters");
        }
    }
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
    
    public boolean deleteMessageByMessageId(int id) throws SQLException
    {
        boolean isDeleted = messageDao.deleteMessageByMessageId(id);
        return isDeleted;
    }

    public Message updateMessageByMessageId(int id, Message message) throws SQLException
    {
        Message retrievedMessage = messageDao.getMessageByMessageId(id);
        
        // Check if the message exists
        if(retrievedMessage == null)
        {
            throw new RuntimeException("Message not found.");
        }

        // Update the message text 
        retrievedMessage.setMessage_text(message.getMessage_text());

        // Validate the new message
        validateMessage(retrievedMessage);

        // Update the message in the database
        messageDao.updateMessageByMessageId(retrievedMessage.getMessage_id(), message);
        return retrievedMessage;
    }

    public List<Message> getAllMessagesByAccountId(int accountId) throws SQLException 
    {
        List<Message> messages = messageDao.getMessageByAccountId(accountId);
        return messages;
    }
    
}
