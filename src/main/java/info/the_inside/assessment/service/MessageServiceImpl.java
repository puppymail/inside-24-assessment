package info.the_inside.assessment.service;

import static info.the_inside.assessment.exception.ErrorMessages.INVALID_ARGUMENT_EX;
import static java.time.LocalDateTime.now;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import info.the_inside.assessment.model.Message;
import info.the_inside.assessment.repository.MessageRepository;
import info.the_inside.assessment.repository.SenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final SenderRepository senderRepo;
    private final MessageRepository messageRepo;

    @Autowired
    public MessageServiceImpl(SenderRepository senderRepo, MessageRepository messageRepo) {
        this.senderRepo = senderRepo;
        this.messageRepo = messageRepo;
    }

    @Override
    public List<Message> findMessagesBySender(String name) {
        if ( isNull(name) || name.isBlank() )
            throw new IllegalArgumentException(INVALID_ARGUMENT_EX);

        return messageRepo.getBySender_NameOrderByCreatedAtDesc(name);
    }

    @Transactional
    @Override
    public Message createNewMessage(String text, String senderName) {
        requireNonNull(text);
        requireNonNull(senderName);
        Message message = new Message( text, now() );
        // Search for sender by name, if no matching sender found, set the sender to null
        message.setSender( senderRepo.findById(senderName).orElse(null) );

        return messageRepo.save(message);
    }

    @Override
    public List<Message> getLatestMessages(int historySize) {
        if (historySize <= 0) {
            return List.of();
        }
        return messageRepo.getTopMessages(historySize);
    }

}
