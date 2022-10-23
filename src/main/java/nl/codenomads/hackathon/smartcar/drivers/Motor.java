package nl.codenomads.hackathon.smartcar.drivers;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Motor {

    private final PCA9685 motorDriver;

    public Motor(final PCA9685 motorDriver) {
        this.motorDriver = motorDriver;
        motorDriver.setPwmFrequency(50);
    }

    public void leftFrontWheel(final int duty) {
        if (duty < 0) {
            motorDriver.setMotorPwm(0, 0);
            motorDriver.setMotorPwm(1, duty);
        } else if (0 < duty) {
            motorDriver.setMotorPwm(0, duty);
            motorDriver.setMotorPwm(1, 0);
        } else {
            motorDriver.setMotorPwm(0, 4095);
            motorDriver.setMotorPwm(1, 4095);
        }
    }

    public void rightFrontWheel(final int duty) {
        if (duty < 0) {
            motorDriver.setMotorPwm(6, 0);
            motorDriver.setMotorPwm(7, duty);
        } else if (0 < duty) {
            motorDriver.setMotorPwm(6, duty);
            motorDriver.setMotorPwm(7, 0);
        } else {
            motorDriver.setMotorPwm(6, 4095);
            motorDriver.setMotorPwm(7, 4095);
        }
    }

    public void rightRearWheel(final int duty) {
        if (duty < 0) {
            motorDriver.setMotorPwm(4, 0);
            motorDriver.setMotorPwm(5, duty);
        } else if (0 < duty) {
            motorDriver.setMotorPwm(4, duty);
            motorDriver.setMotorPwm(5, 0);
        } else {
            motorDriver.setMotorPwm(4, 4095);
            motorDriver.setMotorPwm(5, 4095);
        }
    }

    // somehow channel order is mixed for this one
    public void leftRearWheel(final int duty) {
        if (duty < 0) {
            motorDriver.setMotorPwm(3, 0);
            motorDriver.setMotorPwm(2, duty);
        } else if (0 < duty) {
            motorDriver.setMotorPwm(3, duty);
            motorDriver.setMotorPwm(2, 0);
        } else {
            motorDriver.setMotorPwm(2, 4095);
            motorDriver.setMotorPwm(3, 4095);
        }
    }

}
