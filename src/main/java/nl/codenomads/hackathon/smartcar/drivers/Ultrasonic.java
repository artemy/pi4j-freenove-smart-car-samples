package nl.codenomads.hackathon.smartcar.drivers;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.PullResistance;

import javax.enterprise.context.ApplicationScoped;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static nl.codenomads.hackathon.smartcar.drivers.Utils.delay;

@ApplicationScoped
public class Ultrasonic {

    private static final int TRIGGER_PIN = 27;
    private static final int ECHO_PIN = 22;

    private final DigitalOutput trigger;
    private final DigitalInput echo;

    public Ultrasonic(final Context pi4j) {
        final var triggerConfig = DigitalOutput.newConfigBuilder(pi4j)
                .id("ultrasonic_trigger_pin")
                .name("Ultrasonic trigger pin")
                .address(TRIGGER_PIN)
                .provider("pigpio-digital-output")
                .build();
        trigger = pi4j.create(triggerConfig);
        final var echoConfig = DigitalInput.newConfigBuilder(pi4j)
                .id("ultrasonic_echo_pin")
                .name("Ultrasonic echo pin")
                .address(ECHO_PIN)
                .pull(PullResistance.PULL_DOWN)
                .provider("pigpio-digital-input")
                .build();
        echo = pi4j.create(echoConfig);
    }

    public void sendTriggerPulse() {
        trigger.high();
        delay(MICROSECONDS, 100);
        trigger.low();
    }

    public void waitForEcho(final DigitalState state) {
        //noinspection StatementWithEmptyBody
        while (!echo.equals(state)) {
            // noop
        }
    }

    public int getDistance() {
        final var integers = IntStream.range(0, 3)
                .boxed()
                .map(ignore -> {
                    sendTriggerPulse();
                    waitForEcho(DigitalState.HIGH);
                    final var startTime = System.nanoTime();
                    waitForEcho(DigitalState.LOW);
                    final var endTime = System.nanoTime();
                    return ((((endTime - startTime) / 1e3) / 2) / 29.1);
                })
                .map(Double::intValue)
                .sorted()
                .toList();
        return integers
                .get(0);
    }
}
