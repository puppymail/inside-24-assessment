package info.the_inside.messages.service;

import info.the_inside.messages.model.Sender;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.Optional;

public interface AuthService {

    Sender register(Sender sender) throws AccountException;

    Optional<String> login(Sender sender) throws AccountNotFoundException;

    boolean validateBearerToken(String jwtToken, Sender sender);

}
