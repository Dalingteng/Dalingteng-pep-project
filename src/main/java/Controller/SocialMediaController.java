package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Service.AccountService;
import Service.MessageService;
import Model.Account;
import Model.Message;

import java.sql.SQLException;
import java.util.*;

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
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::createAccount);
        app.post("/login", this::loginAccount);
        app.post("messages", this::createMessage);
        app.get("messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageByMessageId);
        app.delete("/messages/{message_id}", this::deleteMessageByMessageId);
        app.patch("/messages/{message_id}", this::updateMessageByMessageId);
        app.get("/accounts/{account_id}/messages", this:: getAllMessagesByAccountId);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) 
    {
        context.json("sample text");
    }

    private void createAccount(Context ctx)
    {

    }
    private void loginAccount(Context ctx)
    {

    }
    private void createMessage(Context ctx)
    {

    }
    private void getAllMessages(Context ctx) throws SQLException
    {
        List<Message> messages = messageService.getAllMessages();
        if(messages.isEmpty())
        {
            ctx.status(200);
        }
        ctx.json(messages);
    }
    
    private void getMessageByMessageId(Context ctx) throws SQLException
    {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageByMessageId(messageId);
        if(message == null) 
        { 
            ctx.status(200);
        }
        else
        {
            ctx.json(message);
        }
    }
    
    private void deleteMessageByMessageId(Context ctx) throws SQLException
    {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.deleteMessageByMessageId(messageId);
        if(message == null) 
        { 
            ctx.status(200);    
        }
        ctx.json(message);
    }
    
    private void updateMessageByMessageId(Context ctx){}

    private void getAllMessagesByAccountId(Context ctx) throws SQLException
    {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesByAccountId(accountId);
        if(messages.isEmpty())
        {
            ctx.status(200);
        }
        ctx.json(messages);
    }

}