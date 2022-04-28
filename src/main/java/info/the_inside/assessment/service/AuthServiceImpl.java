package info.the_inside.assessment.service;

import static info.the_inside.assessment.configuration.AuthConstants.ALGORITHM;
import static info.the_inside.assessment.configuration.AuthConstants.SECRET;
import static info.the_inside.assessment.configuration.AuthConstants.VALIDITY_PERIOD;
import static info.the_inside.assessment.exception.ErrorMessages.SENDER_ALREADY_EXISTS_EX;
import static info.the_inside.assessment.exception.ErrorMessages.SENDER_NOT_FOUND_EX;
import static java.lang.System.currentTimeMillis;
import static java.util.Objects.isNull;

import info.the_inside.assessment.model.Sender;
import info.the_inside.assessment.repository.SenderRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final static String REGEX = "^Bearer_.*";
    private final static Pattern PATTERN = Pattern.compile(REGEX);
    private final static String CLAIM_NAME = "name";

    private final SenderRepository senderRepo;
    private final PasswordEncoder encoder;

    @Autowired
    public AuthServiceImpl(SenderRepository senderRepo,
                           PasswordEncoder encoder) {
        this.senderRepo = senderRepo;
        this.encoder = encoder;
    }

    @Transactional
    @Override
    public Sender register(final Sender sender) throws AccountException {
        if ( senderRepo.findById(sender.getName()).isPresent() )
            throw new AccountException(SENDER_ALREADY_EXISTS_EX);
        sender.setPassword( encoder.encode( sender.getPassword() ) );

        return senderRepo.save(sender);
    }

    @Override
    public Optional<String> login(final Sender sender) throws AccountNotFoundException {
        Optional<Sender> senderOpt = senderRepo.findById( sender.getName() );
        if ( senderOpt.isEmpty() )
            throw new AccountNotFoundException(SENDER_NOT_FOUND_EX);
        Sender existingSender = senderOpt.get();
        if ( encoder.matches( sender.getPassword(), existingSender.getPassword() ) ) {
            String jwtToken = getJwtToken(existingSender);
            existingSender.setToken(jwtToken);
            senderRepo.save(existingSender);

            return Optional.of(jwtToken);
        }

        return Optional.empty();
    }

    @Override
    public boolean validateBearerToken(final String jwtToken, final Sender sender) {
        if ( isNull(jwtToken) || isNull(sender) ) return false;
        if ( PATTERN.matcher(jwtToken).matches() ) {
            Jws<Claims> jws;
            try {
                jws = Jwts.parser()
                        .setSigningKey(SECRET)
                        .parseClaimsJws( jwtToken.substring(7) );
            } catch (Exception e) {
                return false;
            }

            return sender.getName().equals( jws.getBody()
                    .get(CLAIM_NAME, String.class) );
        }

        return false;
    }

    private String getJwtToken(final Sender sender) {
        long now = currentTimeMillis();
        return Jwts.builder()
                .claim( CLAIM_NAME, sender.getName() )
                .setIssuedAt( new Date(now) )
                .setExpiration( new Date(now + VALIDITY_PERIOD) )
                .signWith(ALGORITHM, SECRET)
                .compact();
    }

}