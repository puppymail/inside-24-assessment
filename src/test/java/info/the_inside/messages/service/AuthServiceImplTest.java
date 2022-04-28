package info.the_inside.messages.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import info.the_inside.messages.SenderDefaultSupplier;
import info.the_inside.messages.model.Sender;
import info.the_inside.messages.repository.SenderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    SenderRepository senderRepo;

    @Mock
    PasswordEncoder encoder;

    AuthServiceImpl authService;

    SenderDefaultSupplier senderSupplier = SenderDefaultSupplier.getInstance();

    @BeforeEach
    public void setUp() {
        authService = new AuthServiceImpl(senderRepo, encoder);
    }

    @Test
    void register_newSender_senderRegistered() throws AccountException {
        Sender sender = senderSupplier.get();
        when( senderRepo.findById( sender.getName() ) )
                .thenReturn( Optional.empty() );
        when( encoder.encode( sender.getPassword() ) )
                .thenReturn( sender.getPassword() );

        authService.register(sender);

        InOrder order = inOrder(senderRepo, encoder);
        order.verify(senderRepo, times(1)).findById( sender.getName() );
        order.verify(encoder, times(1)).encode( sender.getPassword() );
        order.verify(senderRepo, times(1)).save( any(Sender.class) );
        order.verifyNoMoreInteractions();
    }

    @Test
    void register_existingSender_exceptionThrown() {
        Sender sender = senderSupplier.get();
        when( senderRepo.findById( sender.getName() ) ).thenReturn( Optional.of(sender) );

        assertThrows(AccountException.class,
                () -> authService.register(sender) );
        verify(senderRepo, times(1)).findById( sender.getName() );
        verifyNoMoreInteractions(senderRepo);
    }

    @Test
    void login_validSenderPasswordsMatch_tokenReturned() throws AccountNotFoundException {
        Sender sender = senderSupplier.get();
        when( senderRepo.findById( sender.getName() ) ).thenReturn( Optional.of(sender) );
        when( encoder.matches( anyString(), anyString() ) ).thenReturn(true);

        Optional<String> result = authService.login(sender);

        assertTrue( result.isPresent() );
        InOrder order = inOrder(senderRepo, encoder);
        order.verify(senderRepo, times(1)).findById( sender.getName() );
        order.verify(encoder, times(1)).matches( anyString(), anyString() );
        order.verify(senderRepo, times(1)).save( any(Sender.class) );
        order.verifyNoMoreInteractions();
    }

    @Test
    void login_validSenderPasswordsMismatch_emptyOptReturned() throws AccountNotFoundException {
        Sender sender = senderSupplier.get();
        when( senderRepo.findById( sender.getName() ) ).thenReturn( Optional.of(sender) );
        when( encoder.matches( anyString(), anyString() ) ).thenReturn(false);

        Optional<String> result = authService.login(sender);

        assertTrue( result.isEmpty() );
        InOrder order = inOrder(senderRepo, encoder);
        order.verify(senderRepo, times(1)).findById( sender.getName() );
        order.verify(encoder, times(1)).matches( anyString(), anyString() );
        order.verifyNoMoreInteractions();
    }

    @Test
    void login_invalidSender_exceptionThrown() {
        Sender sender = senderSupplier.get();
        when( senderRepo.findById( sender.getName() ) ).thenReturn( Optional.empty() );

        assertThrows( AccountNotFoundException.class,
                () -> authService.login(sender) );

        verify(senderRepo, times(1)).findById( sender.getName() );
        verifyNoMoreInteractions(senderRepo);
    }

}