package nl.codenomads.hackathon.smartcar.drivers;

import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.LedStripType;
import com.github.mbelling.ws281x.Ws281xLedStrip;

import javax.enterprise.context.ApplicationScoped;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static nl.codenomads.hackathon.smartcar.drivers.Utils.delay;


@ApplicationScoped
public class LEDStrip {

    private static final int LED_PIN = 18;
    private static final int LED_DMA_CHANNEL = 10;
    private static final int LED_BRIGHTNESS = 50;
    private static final int LED_PWM_CHANNEL = 0;
    private static final int LED_FREQUENCY = 800_000;
    private static final int LED_COUNT = 8;
    private static final boolean LED_INVERT = false;

    private final Ws281xLedStrip ledStrip;

    public LEDStrip() {
        ledStrip = new Ws281xLedStrip(LED_COUNT,
                LED_PIN,
                LED_FREQUENCY,
                LED_DMA_CHANNEL,
                LED_BRIGHTNESS,
                LED_PWM_CHANNEL,
                LED_INVERT,
                LedStripType.WS2811_STRIP_RGB,
                false);
    }

    public void colorFill(final int red, final int green, final int blue) {
        colorFill(new Color(red, green, blue));
    }

    public void colorFill(final Color color) {
        IntStream.range(0, ledStrip.getLedsCount()).boxed()
                .forEach(i -> ledStrip.setPixel(i, color));
        ledStrip.render();
        delay(MILLISECONDS, 50);
    }

    public Color wheel(final int position) {
        if (position < 0 || 255 < position) {
            return new Color(0, 0, 0);
        } else if (position < 85) {
            return new Color(position * 3, 255 - position * 3, 0);
        } else if (position < 170) {
            return new Color(255 - ((position - 85) * 3), 0, (position - 85) * 3);
        } else {
            return new Color(0, (position - 170) * 3, 255 - ((position - 170) * 3));
        }
    }

    public void chaseRainbow() {
        for (int j = 0; j < 256; j++) {
            for (int q = 0; q < 3; q++) {
                for (int i = 0; i < ledStrip.getLedsCount(); i += 3) {
                    ledStrip.setPixel(i + q, wheel((i + j) % 255));
                }
                ledStrip.render();
                delay(MILLISECONDS, 50);
                for (int i = 0; i < ledStrip.getLedsCount(); i += 3) {
                    ledStrip.setPixel(i + q, new Color(0, 0, 0));
                }
            }
        }
    }

    public void rainbow() {
        for (int j = 0; j < 256 * 5; j++) {
            for (int i = 0; i < ledStrip.getLedsCount(); i++) {
                ledStrip.setPixel(i, wheel((i + j) & 255));
            }
            ledStrip.render();
            delay(MILLISECONDS, 50);
        }
    }

    public void rainbowCycle() {
        for (int j = 0; j < 256 * 5; j++) {
            for (int i = 0; i < ledStrip.getLedsCount(); i++) {
                ledStrip.setPixel(i, wheel(((i * 256 / ledStrip.getLedsCount()) + j) & 255));
            }
            ledStrip.render();
            delay(MILLISECONDS, 50);
        }
    }
}
