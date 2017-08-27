import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import objects.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Robert Ćwiertnia on 2017-08-05.
 */
public class SimpleNetworkingApplication {

    Map<String, String> commandMap = new HashMap<>();

    public static void main(String[] args) {

        SimpleNetworkingApplication sna = new SimpleNetworkingApplication();
        sna.populateCommandList();

        Boolean mainLoop = true;
        Boolean logged = false;

        InputStreamReader cin = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String userName = "";

        System.out.println("Welcome to SNA");
        System.out.println("After logging in, type 'help' for list of commands");
        System.out.println();

        while (mainLoop) {
            try {
                if (!logged) {
                    System.out.println();
                    System.out.println("Input your username: ");
                    userName = br.readLine();
                    accessWebService("login", userName, true);
                    logged = true;
                }

                System.out.println();
                System.out.println("Hello " + userName);
                System.out.println("Awaiting order: " + "\t\t\t" + "(for command list write 'help')");
                String order = br.readLine();

                switch (order) {
                    case "write":
                        System.out.println("Enter your message: ");
                        String messageBody = br.readLine();
                        accessWebService("write", messageBody, false);
                        break;

                    case "read own":
                        accessWebService("read_own", "", true);
                        break;

                    case "read fol":
                        accessWebService("read_followees", "", true);
                        break;

                    case "follow":
                        System.out.println("Enter the username of a user you want to follow: ");
                        String userNameToFollow = br.readLine();
                        accessWebService("follow", userNameToFollow, true);
                        break;

                    case "unfollow":
                        System.out.println("Enter the username of a user you want to unfollow: ");
                        String userNameToUnfollow = br.readLine();
                        accessWebService("unfollow", userNameToUnfollow, true);
                        break;

                    case "logout":
                        logged = false;
                        System.out.println("You have been logged out");
                        break;

                    case "help":
                        System.out.println("Command list: ");
                        for (String command : sna.commandMap.keySet()) {
                            System.out.println(command + "\t\t\t\t\t" + sna.commandMap.get(command));
                        }
                        break;

                    case "quit":
                        mainLoop = false;
                        break;

                    case "exit":
                        mainLoop = false;
                        break;

                    default:
                        System.out.println("Unrecognizable command");

                }
            }
            catch (IOException e) {
                System.out.println(e);
                System.out.println("Error!");
            }
        }

        try {
            cin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String accessWebService(String url, String param, boolean isGetParam) {
        try {
            Client client = Client.create();

            WebResource webResource = client
                    .resource("http://localhost:4444/rest/user/" + url + "/" + (isGetParam ? param : ""));

            System.out.println("Connected to: " + webResource.getURI().toString());
            ClientResponse response;

            if (isGetParam) {
                response = webResource.accept("application/json").get(ClientResponse.class);
            } else {
                response = webResource.type("application/json").post(ClientResponse.class, param);
            }

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            String output = response.getEntity(String.class);
            System.out.println(output);
            return output;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void populateCommandList() {
        commandMap.put("write", "Write a post on your personal wall");
        commandMap.put("read own", "Read all your posts in reverse chronological order");
        commandMap.put("read fol", "Read all of your followees posts in reverse chronological order");
        commandMap.put("follow", "Follow a specific user by their username");
        commandMap.put("unfollow", "Stop following a specific user by their username");
        commandMap.put("logout", "Logs you out and lets you login on a different username");
        commandMap.put("help", "Lists all of available commands");
        commandMap.put("quit", "Stops the application");
        commandMap.put("exit", "Stops the application");

    }

    private void outputMessage(Message message) {
        System.out.println("Message by: " + message.getUserName() + "\t\t posted on: " + message.getCreateDate());
        System.out.println();
        System.out.println(message.getMessageBody());
        System.out.println();
    }
}
