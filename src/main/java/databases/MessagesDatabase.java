package databases;

import objects.Message;
import objects.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert Ćwiertnia on 2017-08-05.
 */
public class MessagesDatabase {

    private static MessagesDatabase instance = null;
    protected MessagesDatabase() {
        messageList = new ArrayList<>();
    }
    public static MessagesDatabase getInstance() {
        if(instance == null) {
            instance = new MessagesDatabase();
        }
        return instance;
    }

    List<Message> messageList;

    public MessagesDatabase(List<Message> messageList) {
        this.messageList = messageList;
    }

    public List<Message> getMessageList() {
        return messageList;
    }
}
