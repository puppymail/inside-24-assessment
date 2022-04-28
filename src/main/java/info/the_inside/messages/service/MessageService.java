package info.the_inside.messages.service;

import info.the_inside.messages.model.Message;

import java.util.List;

public interface MessageService {

    List<Message> findMessagesBySender(String name);

    Message createNewMessage(String message, String senderName);

    List<Message> getLatestMessages(int historyLength);

}
