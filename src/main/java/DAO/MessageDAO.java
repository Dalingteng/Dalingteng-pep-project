package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.*;


public class MessageDAO 
{
//     public Message getMessageById(){}
    
//     public Message createMessage(){}
//     public Message updateMessage(){}
//     public Message deleteMessage(){}

    

    public List<Message> getMessageByAccountId(int accountId) 
    {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try
        {
            String sql = "SELECT * FROM account WHERE posted_by=?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, accountId);

            ResultSet rs = ps.executeQuery();
            
            while(rs.next())
            {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return messages;
    }
    

}
