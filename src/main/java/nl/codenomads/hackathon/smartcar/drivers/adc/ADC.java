package nl.codenomads.hackathon.smartcar.drivers.adc;

import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;

public abstract class ADC {
    private static final int ADC_ADDRESS = 0x48;
    protected final I2C i2cDevice;

    protected ADC(final I2C i2cDevice) {
        this.i2cDevice = i2cDevice;
    }

    public abstract double receive(final int channel);

    public static ADC getInstance(final Context pi4j) {
        final I2CProvider i2CProvider = pi4j.provider("pigpio-i2c");
        final I2CConfig i2cConfig = I2C.newConfigBuilder(pi4j)
                .id("adc")
                .name("ADC")
                .bus(1)
                .device(ADC_ADDRESS)
                .build();
        final var i2cDevice = i2CProvider.create(i2cConfig);

        final var aa = i2cDevice.readRegister(0xF4);
        if (aa < 150) {
            return new PCF8591(i2cDevice);
        } else {
            return new ADS7830(i2cDevice);
        }
    }

}
