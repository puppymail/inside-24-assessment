package info.the_inside.assessment.configuration;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.Base64Codec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;

@Configuration
public class AuthConfiguration {

    private static final Integer BYTE_ARRAY_SIZE = 256;
    private static final Long RND_SEED = 1L;

    // Default token lifetime
    public static final Long VALIDITY_PERIOD = 3_600_000L;

    public static final String SECRET;
    public static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS256;

    // Name and password for default user that is added at Context load
    public static final String DEFAULT_SENDER_NAME = "root";
    public static final String DEFAULT_SENDER_PASSWORD = "root";

    // Generating and encoding secret for HS256 algorithm.
    static {
        Random rnd = new Random(RND_SEED);
        byte[] bytes = new byte[BYTE_ARRAY_SIZE];
        rnd.nextBytes(bytes);
        SECRET = new Base64Codec().encode(bytes);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
