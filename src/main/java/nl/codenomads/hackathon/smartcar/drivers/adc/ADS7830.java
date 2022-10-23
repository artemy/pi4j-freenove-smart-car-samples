package nl.codenomads.hackathon.smartcar.drivers.adc;

import com.pi4j.io.i2c.I2C;

public class ADS7830 extends ADC {

    private static final int ADS7830_CMD = 0x84;

    ADS7830(final I2C i2cDevice) {
        super(i2cDevice);
    }

    @Override
    public double receive(final int channel) {
        final var command_set = ADS7830_CMD | ((((channel << 2) | (channel >> 1)) & 0x07) << 4);
        i2cDevice.write(command_set);
        var value1 = 0;
        var value2 = 0;
        do {
            value1 = i2cDevice.read();
            value2 = i2cDevice.read();
        } while (value1 != value2);
        return Math.round(value1 / 256.0 * 3.3 * 100.0) / 100.0;
    }
}
