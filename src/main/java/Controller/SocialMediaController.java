package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Service.AccountService;
import Service.MessageService;
import Model.Account;
import Model.Message;

import static org.mockito.ArgumentMatchers.nullable;

import java.sql.SQLException;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController()
    {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerAccount);
        app.post("/login", this::loginAccount);
        app.post("messages", this::createMessage);
        app.get("messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageByMessageId);
        app.delete("/messages/{message_id}", this::deleteMessageByMessageId);
        app.patch("/messages/{message_id}", this::updateMessageByMessageId);
        app.get("/accounts/{account_id}/messages", this:: getAllMessagesByAccountId);

        return app;
    }
    // ## 1: Our API should be able to process new User registrations.
    private void registerAccount(Context ctx) throws JsonProcessingException, SQLException
    {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        try 
        {
            Account registeredAccount = accountService.registerAccount(account);
            ctx.json(mapper.writeValueAsString(registeredAccount));
        }
        catch(Exception e)
        {
            // Set reponse status to 400 if the registration is not successfully 
            ctx.status(400);
        }
    }


    private void loginAccount(Context ctx)
    {

    }

    // ## 3: Our API should be able to process the creation of new messages.
    private void createMessage(Context ctx) throws JsonProcessingException, SQLException
    {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        try 
        {
            Account account = accountService.getAccountByAccountId(message.getPosted_by());
            Message createdMessage = messageService.createMessage(message, account);
            ctx.json(mapper.writeValueAsString(createdMessage));
        }
        catch(Exception e)
        {
            // Set reponse status to 400 if the creation of message is not successfully 
            ctx.status(400);
        }
    }

    // ## 4: Our API should be able to retrieve all messages.
    private void getAllMessages(Context ctx) throws SQLException
    {
        List<Message> messages = messageService.getAllMessages();
        if(!messages.isEmpty())
        {
            ctx.json(messages);
        }
        else
        {
            ctx.status(200);
            ctx.json(messages);
        }
    }
    
    // ## 5: Our API should be able to retrieve a message by its ID.
    private void getMessageByMessageId(Context ctx) throws SQLException
    {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageByMessageId(messageId);
        if(message != null) 
        { 
            ctx.json(message);
        }
        else
        {
            ctx.status(200);
        }
    }
    
    // ## 6: Our API should be able to delete a message identified by a message ID.
    private void deleteMessageByMessageId(Context ctx) throws SQLException
    {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageByMessageId(messageId);
        if(message != null)
        {
            messageService.deleteMessageByMessageId(messageId);
            ctx.json(message);
        }
        else
        {
            ctx.status(200);
        }
    }

    // ## 7: Our API should be able to update a message text identified by a message ID.
    private void updateMessageByMessageId(Context ctx) throws JsonProcessingException, SQLException
    {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);  
        int messageId = Integer.parseInt(ctx.pathParam("message_id")); 
        try
        {
            Message updatedMessage = messageService.updateMessageByMessageId(messageId, message);
            if(updatedMessage != null)
            {
                ctx.json(mapper.writeValueAsString(updatedMessage));
            }
        }
        catch (Exception e)
        {
            // Set reponse status to 400 if the update of message is not successfully 
            ctx.status(400);
        }
    }

    // ## 8: Our API should be able to retrieve all messages written by a particular user.
    private void getAllMessagesByAccountId(Context ctx) throws SQLException
    {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesByAccountId(accountId);
        if(!messages.isEmpty())
        {
            ctx.json(messages);
        }
        else
        {
            ctx.status(200);
            ctx.json(messages);
        }
    }
}