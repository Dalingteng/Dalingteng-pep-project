package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Service.AccountService;
import Service.MessageService;
import Model.Account;
import Model.Message;

import java.sql.SQLException;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class SocialMediaController 
{
    private AccountService accountService;
    private MessageService messageService;

    /**
     * Constructor to initialize accountService and messageService objects
     */
    public SocialMediaController()
    {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * This method creates the endpoints of the application by initializing Javalin objects 
     * to handle the Javalin controllers.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerAccount);
        app.post("/login", this::loginAccount);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageByMessageId);
        app.delete("/messages/{message_id}", this::deleteMessageByMessageId);
        app.patch("/messages/{message_id}", this::updateMessageByMessageId);
        app.get("/accounts/{account_id}/messages", this:: getAllMessagesByAccountId);

        return app;
    }
    
    /**
     * This method handles a registration process of a new Account for users.
     * It makes a POST request to "/register" containing a JSON of the Account in the request body.
     * If the registration is successful, the response body contains a JSON of the Account and the response status is 200 by default.
     * If the registration is not successful, the response status is 400. (Client error)
     * 
     * @param ctx The context object for handling HTTP request and response
     * @throws JsonProcessingException if an error occurs during parsing or generating JSON content
     * @throws SQLException if an error occurs during accessing database
     */
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
            // Set reponse status to 400 if the registration is not successful
            ctx.status(400); 
        }
    }

    /**
     * This method handles a login process for users.
     * It makes a POST request to "/login" containing a JSON of the Account in the request body.
     * If the login is successful, the response body contains a JSON of the Account and the response status is 200 by default.
     * If the login is not successful, the response status is 401. (Unauthorized)
     * 
     * @param ctx The context object for handling HTTP request and response
     * @throws JsonProcessingException if an error occurs during parsing or generating JSON content
     */
    private void loginAccount(Context ctx) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        try 
        {
            Account loggedInAccount = accountService.authenticate(account);
            if(loggedInAccount != null)
            {
                ctx.json(mapper.writeValueAsString(loggedInAccount));
            }
            else
            {
                // Set reponse status to 401 if the login is not successful
                ctx.status(401);
            }
        }
        catch(Exception e)
        {
            // Set reponse status to 401 if the login is not successful
            ctx.status(400);
        }
    }

    /**
     * This method handles a creation of a new Message process.
     * It makes a POST request to "/messages" containing a JSON of the Message in the request body.
     * If the creation of message is successful, the response body contains a JSON of the Message and the response status is 200 by default.
     * If the creation of message is not successful, the response status is 400. (Client error)
     * 
     * @param ctx The context object for handling HTTP request and response
     * @throws JsonProcessingException if an error occurs during parsing or generating JSON content
     * @throws SQLException if an error occurs during accessing database
     */
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
            // Set reponse status to 400 if the creation of message is not successful
            ctx.status(400);
        }
    }

    /**
     * This method retrieves all messages in the database
     * It makes a GET request to "/messages".
     * The response body contains a JSON of a list containing all messages in the database.
     * If there is no message, the response body is expected to be empty.
     * The response status is 200 by default.
     * 
     * @param ctx The context object for handling HTTP request and response
     * @throws SQLException if an error occurs during accessing database
     */
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
    
    /**
     * This method retrieves a message by its ID.
     * It makes a GET request to "/messages/{message_id}".
     * The response body contains a JSON of the Message identified by a message ID.
     * If there is no message, the response body is expected to be empty.
     * The response status is 200 by default.
     * 
     * @param ctx The context object for handling HTTP request and response
     * @throws SQLException if an error occurs during accessing database
     */
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
    
    /**
     * This method deletes a message identified by a message ID.
     * It makes a DELETE request to "/messages/{message_id}".
     * The deletion removes an existing message from the database.
     * If the message existed, the response body contains the now-deleted message.
     * If the message did not exist, the response body is empty.
     * The reponse status is 200 by default.
     * 
     * @param ctx The context object for handling HTTP request and response
     * @throws SQLException if an error occurs during accessing database
     */
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

    /**
     * This method updates a message identified by a message ID.
     * It makes a PATCH request to "/messages/{message_id}".
     * The request body contains a new message text values to replace the message identified by a message ID.
     * If the update is successful, the response body contains the full-updated message and the response status is 200 by default.
     * If the update is not sucessful, the response status is 400. (Client error)
     * 
     * @param ctx The context object for handling HTTP request and response
     * @throws JsonProcessingException if an error occurs during parsing or generating JSON content
     * @throws SQLException if an error occurs during accessing database
     */
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
            // Set reponse status to 400 if the update of message is not successful
            ctx.status(400);
        }
    }

    /**
     * This method retrieves all messages by a particular user.
     * It makes a GET request to "/accounts/{account_id}/messages".
     * The response body contains a JSON of a list containing all messages posted by a particular user.
     * If there is no message, the response body is expected to be empty.
     * The response status is 200 by default.
     * 
     * @param ctx The context object for handling HTTP request and response
     * @throws SQLException if an error occurs during accessing database
     */
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