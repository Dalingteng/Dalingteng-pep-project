package DAO;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.*;


public class MessageDAO 
{
    /**
     * This method inserts a message into the database.
     * 
     * @param message The message to be inserted
     * @param account The account creating the message
     * @return The inserted message
     * @throws SQLException if an error occurs during accessing database
     */
    public Message createMessage(Message message, Account account) throws SQLException
    {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, message.getPosted_by());
        ps.setString(2, message.getMessage_text());
        ps.setLong(3, message.getTime_posted_epoch());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if(rs.next())
        {
            int generatedMessageId = rs.getInt(1);
            return new Message(generatedMessageId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
        }
        return null;
    }

    /**
     * This method retrieves all messages from the database.
     * 
     * @return A list of all messages in the database
     * @throws SQLException if an error occurs during accessing database
     */
    public List<Message> getAllMessages() throws SQLException
    {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message;";
        PreparedStatement ps = connection.prepareStatement(sql);
        List<Message> messages = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
            Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            messages.add(message);
        }
        return messages;
    }
    
    /**
     * This method retrieves a message by a message ID.
     * 
     * @param id The message ID of the message to be retrieved
     * @return The retrieved message
     * @throws SQLException if an error occurs during accessing database
     */
    public Message getMessageByMessageId(int id) throws SQLException
    {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE message_id=?;";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
            Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            return message;
        }
        return null;
    }

    /**
     * This method deletes a message identified by a message ID from the database.
     * @param id The message ID of the message to be deleted
     * @return True if deleted, otherwise False
     * @throws SQLException if an error occurs during accessing database
     */
    public boolean deleteMessageByMessageId(int id) throws SQLException
    {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "DELETE FROM message WHERE message_id=?;";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    }

    /**
     * This method updates a message identified by a message ID in the database.
     * 
     * @param id The message ID of the message to be updated
     * @param message The new message to update
     * @throws SQLException if an error occurs during accessing database
     */
    public void updateMessageByMessageId(int id, Message message) throws SQLException
    {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "UPDATE message SET message_text=? WHERE message_id=?;";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, message.getMessage_text());
        ps.setInt(2, id);
        ps.executeUpdate();
    }

    /**
     * This method retrieves all messages posted by a specific account from the database.
     * 
     * @param accountId The account ID of the account whose messages to be retrieved
     * @return A list of all messages posted by the specific account
     * @throws SQLException if an error occurs during accessing database
     */
    public List<Message> getMessageByAccountId(int accountId) throws SQLException
    {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE posted_by=?;";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, accountId);
        List<Message> messages = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
            Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            messages.add(message);
        }
        return messages;
    } 
}
