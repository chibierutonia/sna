package services;

import databases.MessagesDatabase;
import databases.UserDatabase;
import objects.Message;
import objects.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Robert Ćwiertnia on 2017-08-05.
 */
@Path("/user")
public class UserService {

    UserDatabase userDatabase;
    MessagesDatabase messagesDatabase;

    public UserService() {
        userDatabase = UserDatabase.getInstance();
        messagesDatabase = MessagesDatabase.getInstance();
    }

    @GET
    @Path("/")
    public Response getWelcome() {

        String output = "Welcome to SNA";

        return Response.status(200).entity(output).build();

    }

    @GET
    @Path("/login/{username}")
    public Response login(@PathParam("username") String userName) {
        String responseMessage = "";

        User currentUser = null;

        for (User user : userDatabase.getUserDatabase()) {
            if (user.getUserName().equals(userName)) {
                currentUser = user;
            }
        }

        if (currentUser != null) {
            userDatabase.setCurrentUser(currentUser);
            responseMessage = "Logged in: " + currentUser.getUserName();
        } else {
            currentUser = new User();
            currentUser.setUserId(String.valueOf(new Date().getTime()));
            currentUser.setUserName(userName);
            currentUser.setRegisterDate(new Date());
            currentUser.setFolloweesNamesList(new ArrayList<>());
            userDatabase.setCurrentUser(currentUser);
            userDatabase.getUserDatabase().add(currentUser);
            responseMessage = "User created: " + currentUser.getUserName() + "\n" + "Logged in: " + currentUser.getUserName();
        }

        return Response.status(200).entity(responseMessage).build();
    }

    @POST
    @Path("/write")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response writeOnWall(String messageBody) {
        String responseMessage = "";

        if (messageBody == null || messageBody.isEmpty()) {
            responseMessage = "You cannot post an empty message";
            return Response.status(200).entity(responseMessage).build();
        }
        else if (messageBody.length() <= 140) {
            Message message = new Message();
            message.setMessageId(String.valueOf(new Date().getTime()));
            message.setUserName(userDatabase.getCurrentUser().getUserName());
            message.setCreateDate(new Date());
            message.setMessageBody(messageBody);
            messagesDatabase.getMessageList().add(message);

            responseMessage = "Message posted";
            return Response.status(200).entity(responseMessage).build();
        } else {
            responseMessage = "Your message if too long, 140 characters max";
            return Response.status(200).entity(responseMessage).build();
        }

    }

    @GET
    @Path("/read_own")
    public Response getOwnMessages() {
        String responseMessage = "";

        List<Message> ownMessagesList = new ArrayList<>();

        if (messagesDatabase.getMessageList() != null && !messagesDatabase.getMessageList().isEmpty()) {
            for (Message message: messagesDatabase.getMessageList()) {
                if (message.getUserName().equals(userDatabase.getCurrentUser().getUserName())) {
                    ownMessagesList.add(message);
                }
            }

            Collections.reverse(ownMessagesList);

            StringBuilder sb = new StringBuilder();

            for (Message message : ownMessagesList) {
                sb.append(outputMessage(message));
            }

            responseMessage = sb.toString();
            return Response.status(200).entity(responseMessage).build();
        } else {
            responseMessage = "There are no messages on your wall";
            return Response.status(200).entity(responseMessage).build();
        }
    }

    @GET
    @Path("/read_followees")
    public Response getFolloweesMessages() {
        String responseMessage = "";
        List<Message> followeesMessagesList = new ArrayList<>();

        if (messagesDatabase.getMessageList() != null && !messagesDatabase.getMessageList().isEmpty()) {
            for (Message message: messagesDatabase.getMessageList()) {
                if (userDatabase.getCurrentUser().getFolloweesNamesList() != null && !userDatabase.getCurrentUser().getFolloweesNamesList().isEmpty()) {
                    for (String followeeName : userDatabase.getCurrentUser().getFolloweesNamesList()) {
                        if (message.getUserName().equals(followeeName)) {
                            followeesMessagesList.add(message);
                        }
                    }

                    Collections.reverse(followeesMessagesList);

                    StringBuilder sb = new StringBuilder();

                    for (Message messageFromList : followeesMessagesList) {
                        sb.append(outputMessage(messageFromList));
                    }

                    responseMessage = sb.toString();
                    return Response.status(200).entity(responseMessage).build();
                } else {
                    responseMessage = "You don't follow anyone";
                    return Response.status(200).entity(responseMessage).build();
                }
            }
        } else {
            responseMessage = "There are no messages on your followees walls";
            return Response.status(200).entity(responseMessage).build();
        }
        return Response.status(200).entity(responseMessage).build();
    }

    @GET
    @Path("/follow/{username}")
    public Response followUser(@PathParam("username") String userName) {
        String responseMessage = "";
        String followedUserName = "";

        if (userName != null && !userName.equals("")) {
            Boolean userFound = false;
            for (User user : userDatabase.getUserDatabase()) {
                if (user.getUserName().equals(userName)) {
                    userDatabase.getCurrentUser().getFolloweesNamesList().add(user.getUserName());
                    followedUserName = user.getUserName();
                    userFound = true;
                }
            }
            if (!userFound) {
                responseMessage = "User not found";
                return Response.status(200).entity(responseMessage).build();
            } else {
                responseMessage = "You are now following user " + followedUserName;
                return Response.status(200).entity(responseMessage).build();
            }
        } else {
            responseMessage = "You have not entered any username";
            return Response.status(200).entity(responseMessage).build();
        }
    }

    @GET
    @Path("/unfollow/{username}")
    public Response unfollowUser(@PathParam("username") String userName) {
        String responseMessage = "";
        String unfollowedUserName = "";

        if (userName != null && !userName.equals("")) {
            Boolean userFound = false;
            for (User user : userDatabase.getUserDatabase()) {
                if (user.getUserName().equals(userName)) {
                    userDatabase.getCurrentUser().getFolloweesNamesList().remove(user.getUserName());
                    unfollowedUserName = user.getUserName();
                    userFound = true;
                }
            }
            if (!userFound) {
                responseMessage = "User not found";
                return Response.status(200).entity(responseMessage).build();
            } else {
                responseMessage = "You are no longer following user " + unfollowedUserName;
                return Response.status(200).entity(responseMessage).build();
            }
        } else {
            responseMessage = "You have not entered any username";
            return Response.status(200).entity(responseMessage).build();
        }
    }

    private String outputMessage(Message message) {
        StringBuilder sb = new StringBuilder();
        sb.append("Message by: " + message.getUserName() + "\t\t posted on: " + message.getCreateDate());
        sb.append("\n");
        sb.append(message.getMessageBody());
        sb.append("\n");

        System.out.println(sb.toString());

        return sb.toString();
    }
}
