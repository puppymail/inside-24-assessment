package info.the_inside.assessment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import info.the_inside.assessment.SenderDefaultSupplier;
import info.the_inside.assessment.model.Sender;
import info.the_inside.assessment.repository.SenderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SenderServiceImplTest {

    @Mock
    SenderRepository senderRepo;

    SenderServiceImpl senderService;

    SenderDefaultSupplier senderSupplier = SenderDefaultSupplier.getInstance();

    @BeforeEach
    public void setUp() {
        senderService = new SenderServiceImpl(senderRepo);
    }

    @Test
    void getSenderByName_getExistingSender_senderRetrieved() throws AccountNotFoundException {
        Sender sender = senderSupplier.get();
        when( senderRepo.findById( sender.getName() ) )
                .thenReturn( Optional.of(sender) );

        Sender retrievedSender = senderService.getSenderByName( sender.getName() );

        assertEquals( sender.getName(), retrievedSender.getName() );
        verify(senderRepo, times(1)).findById(sender.getName());
        verifyNoMoreInteractions(senderRepo);
    }

    @Test
    void getSenderByName_getNonExistingSender_exceptionThrown() {
        Sender sender = senderSupplier.get();
        when( senderRepo.findById( sender.getName() ) ).thenReturn( Optional.empty() );

        assertThrows( AccountNotFoundException.class,
                () -> senderService.getSenderByName( sender.getName() ) );
        verify(senderRepo, times(1)).findById( sender.getName() );
        verifyNoMoreInteractions(senderRepo);
    }

}