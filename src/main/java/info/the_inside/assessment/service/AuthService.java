package info.the_inside.assessment.service;

import info.the_inside.assessment.model.Sender;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.Optional;

public interface AuthService {

    Sender register(Sender sender) throws AccountException;

    Optional<String> login(Sender sender) throws AccountNotFoundException;

    boolean validateBearerToken(String jwtToken, Sender sender);

}
