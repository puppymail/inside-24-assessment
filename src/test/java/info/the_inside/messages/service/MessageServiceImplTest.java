package info.the_inside.messages.service;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import info.the_inside.messages.SenderDefaultSupplier;
import info.the_inside.messages.model.Message;
import info.the_inside.messages.model.Sender;
import info.the_inside.messages.repository.MessageRepository;
import info.the_inside.messages.repository.SenderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

    @Mock
    SenderRepository senderRepo;

    @Mock
    MessageRepository messageRepo;

    MessageServiceImpl messageService;

    SenderDefaultSupplier senderSupplier = SenderDefaultSupplier.getInstance();

    @BeforeEach
    void setUp() {
        messageService = new MessageServiceImpl(senderRepo, messageRepo);
    }

    @Test
    void findMessagesBySender_noMessagesForSender_emptyListReturned() {
        Sender sender = senderSupplier.get();
        List<Message> messages = List.of();
        when( messageRepo.getBySender_NameOrderByCreatedAtDesc( sender.getName() ) )
                .thenReturn(messages);

        List<Message> retrievedMessages = messageService.findMessagesBySender( sender.getName() );

        assertEquals(messages, retrievedMessages);
        assertEquals(0, retrievedMessages.size());
        verify(messageRepo, times(1)).getBySender_NameOrderByCreatedAtDesc( sender.getName() );
        verifyNoMoreInteractions(messageRepo);
    }

    @Test
    void findMessagesBySender_someMessagesForSender_messagesReturned() {
        Sender sender = senderSupplier.get();
        List<Message> messages = List.of( new Message( "message 1", now() ),
                                          new Message( "message 2", now() ),
                                          new Message( "message 3", now() ) );
        when( messageRepo.getBySender_NameOrderByCreatedAtDesc( sender.getName() ) )
                .thenReturn(messages);

        List<Message> retrievedMessages = messageService.findMessagesBySender( sender.getName() );

        assertEquals(messages, retrievedMessages);
        assertEquals(3, retrievedMessages.size());
        verify(messageRepo, times(1)).getBySender_NameOrderByCreatedAtDesc( sender.getName() );
        verifyNoMoreInteractions(messageRepo);
    }

    @Test
    void findMessagesBySender_nullOrEmptyNameProvided_exceptionThrown() {
        String emptyString = "";

        assertThrows( IllegalArgumentException.class, () -> messageService.findMessagesBySender(emptyString) );
        assertThrows( IllegalArgumentException.class, () -> messageService.findMessagesBySender(null) );

        verifyNoInteractions(messageRepo);
    }

    @Test
    void createNewMessage_validMessage_messageCreated() {
        Sender sender = senderSupplier.get();
        Message message = new Message( "message", now() );
        message.setSender(sender);
        when( messageRepo.save( any(Message.class) ) )
                .thenReturn(message);

        Message savedMessage = messageService.createNewMessage("message", sender.getName() );

        assertEquals( message.getSender(), savedMessage.getSender() );
        assertEquals( message.getText(), savedMessage.getText() );
        verify(messageRepo, times(1)).save( any(Message.class) );
        verify(senderRepo, times(1)).findById( sender.getName() );
        verifyNoMoreInteractions(messageRepo);
        verifyNoMoreInteractions(senderRepo);
    }

    @Test
    void createNewMessage_nullsProvided_exceptionThrown() {
        assertThrows( NullPointerException.class,
                () -> messageService.createNewMessage(null, null) );
        assertThrows( NullPointerException.class,
                () -> messageService.createNewMessage("message", null) );
        assertThrows( NullPointerException.class,
                () -> messageService.createNewMessage(null, "John") );

        verifyNoInteractions(messageRepo);
        verifyNoInteractions(senderRepo);
    }

    @Test
    void getLatestMessages_0Provided_emptyListReturned() {
        int historySize = 0;
        List<Message> messages = List.of();

        List<Message> retrievedMessages = messageService.getLatestMessages(historySize);

        assertEquals(messages, retrievedMessages);
        assertEquals( 0, retrievedMessages.size() );
        verifyNoInteractions(messageRepo);
    }

    @Test
    void getLatestMessages_3Provided_3messagesReturned() {
        int historySize = 3;
        List<Message> messages = List.of( new Message( "message 1", now() ),
                                          new Message( "message 2", now() ),
                                          new Message( "message 3", now() ) );
        when( messageRepo.getTopMessages(historySize) )
                .thenReturn(messages);

        List<Message> retrievedMessages = messageService.getLatestMessages(historySize);

        assertEquals(messages, retrievedMessages);
        assertEquals( 3, retrievedMessages.size() );
        verify(messageRepo, times(1)).getTopMessages(historySize);
        verifyNoMoreInteractions(messageRepo);
    }

}