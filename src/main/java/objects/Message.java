package objects;

import java.util.Date;

/**
 * Created by Robert Ćwiertnia on 2017-08-05.
 */
public class Message {

    String messageId;
    String userName;
    String messageBody;
    Date createDate;

    public Message() {
    }

    public Message(String messageId, String userName, String messageBody, Date createDate) {
        this.messageId = messageId;
        this.userName = userName;
        this.messageBody = messageBody;
        this.createDate = createDate;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
