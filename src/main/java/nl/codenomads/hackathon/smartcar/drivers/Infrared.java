package nl.codenomads.hackathon.smartcar.drivers;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Infrared {
    private static final int IR1_PIN = 14;
    private static final int IR2_PIN = 15;
    private static final int IR3_PIN = 23;

    private final DigitalInput ir1Input;
    private final DigitalInput ir2Input;
    private final DigitalInput ir3Input;

    public Infrared(final Context pi4j) {
        final var ir1Config = DigitalInput.newConfigBuilder(pi4j)
                .id("infrared-ir1")
                .name("Infrared Left")
                .address(IR1_PIN)
                .provider("pigpio-digital-input")
                .build();
        ir1Input = pi4j.create(ir1Config);
        final var ir2Config = DigitalInput.newConfigBuilder(pi4j)
                .id("infrared-ir2")
                .name("Infrared Middle")
                .address(IR2_PIN)
                .provider("pigpio-digital-input")
                .build();
        ir2Input = pi4j.create(ir2Config);
        final var ir3Config = DigitalInput.newConfigBuilder(pi4j)
                .id("infrared-ir3")
                .name("Infrared Right")
                .address(IR3_PIN)
                .provider("pigpio-digital-input")
                .build();
        ir3Input = pi4j.create(ir3Config);
    }

    public boolean getLeft() {
        return ir1Input.isHigh();
    }

    public boolean getMiddle() {
        return ir2Input.isHigh();
    }

    public boolean getRight() {
        return ir3Input.isHigh();
    }
}
