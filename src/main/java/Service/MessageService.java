package Service;

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

    public List<Message> getMessagesByAccountId(int accountId) 
    {
        List<Message> messages = messageDao.getMessageByAccountId(accountId);
        return messages;
    }
    
}
