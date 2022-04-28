package info.the_inside.assessment;

import info.the_inside.assessment.model.Sender;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class SenderDefaultSupplier implements Supplier<Sender> {

    private final static SenderDefaultSupplier instance = new SenderDefaultSupplier();

    private final static String DEFAULT_SENDER_NAME = "John";
    private final static String DEFAULT_SENDER_PASSWORD = "passlen=10";

    @Override
    public Sender get() {
        return new Sender(DEFAULT_SENDER_NAME, DEFAULT_SENDER_PASSWORD);
    }

    public static SenderDefaultSupplier getInstance() {
        return instance;
    }

}
