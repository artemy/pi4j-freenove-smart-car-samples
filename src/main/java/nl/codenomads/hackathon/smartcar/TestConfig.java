package nl.codenomads.hackathon.smartcar;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "testing")
public interface TestConfig {
    @WithDefault("false")
    boolean adc();

    @WithDefault("false")
    boolean buzzer();

    @WithDefault("false")
    boolean infrared();

    @WithDefault("false")
    boolean leds();

    @WithDefault("false")
    boolean motor();

    @WithDefault("false")
    boolean photoresistor();

    @WithDefault("false")
    boolean servo();

    @WithDefault("false")
    boolean ultrasonic();
}
