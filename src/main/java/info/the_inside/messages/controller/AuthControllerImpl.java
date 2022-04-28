package info.the_inside.messages.controller;

import static info.the_inside.messages.controller.ControllerConstants.ROOT_URL;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import info.the_inside.messages.dto.AuthRequest;
import info.the_inside.messages.dto.JwtTokenResponse;
import info.the_inside.messages.model.Sender;
import info.the_inside.messages.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import java.util.Optional;

@RestController
@RequestMapping(ROOT_URL)
public class AuthControllerImpl implements AuthController {

    private static final String LOGIN_URL = "/login";
    private static final String REGISTER_URL = "/register";

    private final AuthService service;

    @Autowired
    public AuthControllerImpl(AuthService service) {
        this.service = service;
    }

    @PostMapping(value = LOGIN_URL, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    // AuthRequest consists of name and password fields
    public JwtTokenResponse login(@RequestBody AuthRequest request)
            throws FailedLoginException, AccountNotFoundException {
        Optional<String> jwtTokenOpt;
        jwtTokenOpt = service.login( new Sender( request.getName(), request.getPassword() ) );

        // If no token was provided by service.login, then throw ex
        return new JwtTokenResponse( jwtTokenOpt.orElseThrow(FailedLoginException::new) );
    }

    @PostMapping(value = REGISTER_URL, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    // AuthRequest consists of name and password fields
    public void register(@RequestBody AuthRequest request) throws AccountException {
        // Create new Sender. Json structure is the same with login
        service.register( new Sender( request.getName(), request.getPassword() ) );
    }

}
