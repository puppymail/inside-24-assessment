package info.the_inside.messages.service;

import static info.the_inside.messages.exception.ErrorMessages.SENDER_NOT_FOUND_EX;

import info.the_inside.messages.model.Sender;
import info.the_inside.messages.repository.SenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;

@Service
public class SenderServiceImpl implements SenderService {

    private final SenderRepository senderRepo;

    @Autowired
    public SenderServiceImpl(SenderRepository senderRepo) {
        this.senderRepo = senderRepo;
    }

    @Override
    public Sender getSenderByName(String name) throws AccountNotFoundException {
        return senderRepo.findById(name)
                .orElseThrow( () -> new AccountNotFoundException(SENDER_NOT_FOUND_EX) );
    }

}
