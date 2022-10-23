package nl.codenomads.hackathon.smartcar.drivers.adc;

import com.pi4j.io.i2c.I2C;

import java.util.stream.IntStream;

public class PCF8591 extends ADC {

    private static final int PCF8591_CMD = 0x40;

    PCF8591(final I2C i2cDevice) {
        super(i2cDevice);
    }

    private int analogRead(final int channel) {
        return IntStream.range(0, 5)
                .boxed()
                .map(ignore -> i2cDevice.readRegister(PCF8591_CMD + channel))
                .sorted()
                .toList()
                .get(4);
    }

    @Override
    public double receive(final int channel) {
        var value1 = 0;
        var value2 = 0;
        do {
            value1 = analogRead(channel);
            value2 = analogRead(channel);
        } while (value1 != value2);
        return Math.round(value1 / 256.0 * 3.3 * 100.0) / 100.0;
    }
}
