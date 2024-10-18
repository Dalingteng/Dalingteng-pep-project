package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.*;


public class MessageDAO 
{
    // private Message createMessage(Message message, Account account)
    // {

    // }
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

    public Message deleteMessageByMessageId(int id) throws SQLException
    {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "DELETE * FROM message WHERE message_id=?;";
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
    // private Message updateMessageByMessageId(int id){}

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
