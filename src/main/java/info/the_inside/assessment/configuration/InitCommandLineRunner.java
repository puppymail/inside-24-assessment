package info.the_inside.assessment.configuration;

import static info.the_inside.assessment.configuration.AuthConfiguration.DEFAULT_SENDER_NAME;
import static info.the_inside.assessment.configuration.AuthConfiguration.DEFAULT_SENDER_PASSWORD;

import info.the_inside.assessment.model.Sender;
import info.the_inside.assessment.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order
public class InitCommandLineRunner implements CommandLineRunner {

    private final AuthService authService;

    @Autowired
    public InitCommandLineRunner(AuthService authService) {
        this.authService = authService;
    }

    // Adding default user after Context load
    @Override
    public void run(String... args) throws Exception {
        Sender sender = new Sender(DEFAULT_SENDER_NAME, DEFAULT_SENDER_PASSWORD);
        authService.register(sender);
    }

}
