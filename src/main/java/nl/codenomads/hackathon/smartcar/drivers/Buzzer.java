package nl.codenomads.hackathon.smartcar.drivers;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Buzzer {

    private static final int BUZZER_PIN = 17;

    private final DigitalOutput buzzerOutput;

    public Buzzer(final Context pi4j) {
        final var buzzerConfig = DigitalOutput.newConfigBuilder(pi4j)
                .id("buzzer")
                .name("Buzzer")
                .address(BUZZER_PIN)
                .provider("pigpio-digital-output")
                .build();
        buzzerOutput = pi4j.create(buzzerConfig);
    }

    public void on() {
        buzzerOutput.on();
    }

    public void off() {
        buzzerOutput.off();
    }

}
