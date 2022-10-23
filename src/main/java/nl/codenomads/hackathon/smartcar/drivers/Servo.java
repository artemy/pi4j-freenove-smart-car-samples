package nl.codenomads.hackathon.smartcar.drivers;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Servo {

    public static final int SERVO_CHANNEL_0 = 8;
    public static final int SERVO_CHANNEL_1 = 9;

    private final PCA9685 servoDriver;

    public Servo(final PCA9685 servoDriver) {
        this.servoDriver = servoDriver;
        servoDriver.setPwmFrequency(50);
    }

    public void setServoAngle(final int channel, final int angle) {
        servoDriver.setServoPwm(channel, angleToDuty(channel, angle));
    }

    private int angleToDuty(final int channel, final int angle) {
        if (channel == SERVO_CHANNEL_0) {
            return (int) (2500 - ((angle + 10) / 0.09));
        }
        return (int) (500 + ((angle + 10) / 0.09));
    }

}
