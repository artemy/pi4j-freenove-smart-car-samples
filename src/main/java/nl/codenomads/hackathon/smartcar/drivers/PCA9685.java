package nl.codenomads.hackathon.smartcar.drivers;

import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;

import javax.enterprise.context.ApplicationScoped;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static nl.codenomads.hackathon.smartcar.drivers.Utils.delay;

@ApplicationScoped
public class PCA9685 {

    private static final int PCA9685_REG_MODE1 = 0x00;
    private static final int PCA9685_REG_PRESCALE = 0xFE;
    private static final int PCA_9685_REG_LED0_ON_L = 0x06;
    private static final int PCA_9685_REG_LED0_ON_H = 0x07;
    private static final int PCA_9685_REG_LED0_OFF_L = 0x08;
    private static final int PCA_9685_REG_LED0_OFF_H = 0x09;

    private final I2C i2cDevice;

    public PCA9685(final Context pi4j) {
        final I2CProvider i2CProvider = pi4j.provider("pigpio-i2c");
        final I2CConfig i2cConfig = I2C.newConfigBuilder(pi4j)
                .id("pca9685")
                .name("PCA9685")
                .bus(1)
                .device(0x40)
                .build();
        this.i2cDevice = i2CProvider.create(i2cConfig);

        i2cDevice.writeRegister(PCA9685_REG_MODE1, 0x00);
    }

    public void setPwmFrequency(final int frequency) {
        final var prescalerValue = 25000000.0 / 4096.0 / frequency - 1.0;
        final var prescaler = Math.floor(prescalerValue + 0.5);

        final var oldMode = i2cDevice.readRegister(PCA9685_REG_MODE1);
        final var newMode = (oldMode & 0x7F) | 0x10;
        i2cDevice.writeRegister(PCA9685_REG_MODE1, newMode);
        i2cDevice.writeRegister(PCA9685_REG_PRESCALE, (int) prescaler);
        i2cDevice.writeRegister(PCA9685_REG_MODE1, oldMode);
        delay(MILLISECONDS, 1);
        i2cDevice.writeRegister(PCA9685_REG_MODE1, oldMode | 0x80);
    }

    private void setPwm(final int channel, final int on, final int off) {
        i2cDevice.writeRegister(PCA_9685_REG_LED0_ON_L + 4 * channel, on & 0xFF);
        i2cDevice.writeRegister(PCA_9685_REG_LED0_ON_H + 4 * channel, on >> 8);
        i2cDevice.writeRegister(PCA_9685_REG_LED0_OFF_L + 4 * channel, off & 0xFF);
        i2cDevice.writeRegister(PCA_9685_REG_LED0_OFF_H + 4 * channel, off >> 8);
    }

    public void setMotorPwm(final int channel, final int duty) {
        setPwm(channel, 0, Math.abs(duty));
    }

    public void setServoPwm(final int channel, final int pulse) {
        final var off = pulse * 4096 / 20000;
        setPwm(channel, 0, off);
    }
}
