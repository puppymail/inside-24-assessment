package info.the_inside.assessment.configuration;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.Base64Codec;

import java.util.Random;

public class AuthConstants {

    private static final Integer BYTE_ARRAY_SIZE = 256;
    private static final Long RND_SEED = 1L;

    public static final Long VALIDITY_PERIOD = 3_600_000L;

    public static final String SECRET;
    public static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS256;

    public static final String DEFAULT_SENDER_NAME = "root";
    public static final String DEFAULT_SENDER_PASSWORD = "root";

    static {
        Random rnd = new Random(RND_SEED);
        byte[] bytes = new byte[BYTE_ARRAY_SIZE];
        rnd.nextBytes(bytes);
        SECRET = new Base64Codec().encode(bytes);
    }

}
