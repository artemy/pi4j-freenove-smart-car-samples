package nl.codenomads.hackathon.smartcar.drivers;

import java.util.concurrent.TimeUnit;

public enum Utils {
    ;

    public static void delay(final TimeUnit timeunit, final int amount) {
        try {
            timeunit.sleep(amount);
        } catch (final InterruptedException ignore) {
            Thread.currentThread().interrupt();
        }
    }
}
