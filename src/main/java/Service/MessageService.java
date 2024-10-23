package Service;

import java.sql.SQLException;
import java.util.List;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;

public class MessageService 
{

    private MessageDAO messageDao;

    public MessageService()
    {
        messageDao = new MessageDAO();
    }

    public MessageService(MessageDAO messageDao)
    {
        this.messageDao = messageDao;
    }

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
            throw new IllegalArgumentException("Message text cannot be empty.");
        }

        // Check if the message is over 255 characters
        if(message.getMessage_text().length() > 255)
        {
            throw new IllegalArgumentException("Message text cannot be over 255 characters");
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
        // Validate the new message
        validateMessage(message);

        // Update the message in the database
        messageDao.updateMessageByMessageId(id, message);
        Message newMessage = messageDao.getMessageByMessageId(id);
        return newMessage;
    }

    public List<Message> getAllMessagesByAccountId(int accountId) throws SQLException 
    {
        List<Message> messages = messageDao.getMessageByAccountId(accountId);
        return messages;
    }
    
}
