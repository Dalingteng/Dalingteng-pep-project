package Service;

import java.sql.SQLException;
import java.util.List;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;

public class MessageService 
{

    private MessageDAO messageDao;

    /**
     * Default constructor
     */
    public MessageService()
    {
        messageDao = new MessageDAO();
    }

    /**
     * Constructor to use external MessageDAO
     * @param messageDao
     */
    public MessageService(MessageDAO messageDao)
    {
        this.messageDao = messageDao;
    }

    /**
     * This method inserts a new message into the database.
     * 
     * @param message The message to be inserted
     * @param account The account creating the message
     * @return The inserted message 
     * @throws SQLException if an error occurs during accessing database
     */
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

    /**
     * This method validates if the message meets all requirements.
     * 
     * @param message The message to be validated
     */
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

    /**
     * This method retrieves all messages in the database.
     * 
     * @return A list of all messages in the database
     * @throws SQLException if an error occurs during accessing database
     */
    public List<Message> getAllMessages() throws SQLException
    {
        List<Message> messages = messageDao.getAllMessages();
        return messages;
    }

    /**
     * This method retrieves a message by a message ID.
     * 
     * @param id The message ID used to retrieve the message
     * @return The retrieved message
     * @throws SQLException if an error occurs during accessing database
     */
    public Message getMessageByMessageId(int id) throws SQLException
    {
        Message message = messageDao.getMessageByMessageId(id);
        return message;
    }
    
    /**
     * This method deletes a message by its message ID from the database.
     * 
     * @param id The message ID of the message to be deleted
     * @return True if deleted, otherwise False
     * @throws SQLException if an error occurs during accessing database
     */
    public boolean deleteMessageByMessageId(int id) throws SQLException
    {
        boolean isDeleted = messageDao.deleteMessageByMessageId(id);
        return isDeleted;
    }

    /**
     * This method updates a message by its message ID.
     * 
     * @param id The message ID of the message to be updated
     * @param message The new message to be updated
     * @return The updated message
     * @throws SQLException if an error occurs during accessing database
     */
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

    /**
     * This method retrieves all messages by an account ID.
     * 
     * @param accountId The account ID used to retrieve all messages
     * @return A list of all messages posted by the account ID
     * @throws SQLException if an error occurs during accessing database
     */
    public List<Message> getAllMessagesByAccountId(int accountId) throws SQLException 
    {
        List<Message> messages = messageDao.getMessageByAccountId(accountId);
        return messages;
    }
    
}
