package nl.codenomads.hackathon.smartcar;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import nl.codenomads.hackathon.smartcar.drivers.*;
import nl.codenomads.hackathon.smartcar.drivers.adc.ADC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static nl.codenomads.hackathon.smartcar.drivers.Utils.delay;

@QuarkusMain
public class ApplicationMain implements QuarkusApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationMain.class);
    private final ADC adc;
    private final Buzzer buzzer;
    private final Infrared infrared;
    private final LEDStrip ledStrip;

    private final Motor motor;
    private final PhotoResistor photoResistor;
    private final Servo servo;
    private final Ultrasonic ultrasonic;

    private final TestConfig testConfig;

    public ApplicationMain(final ADC adc,
                           final Buzzer buzzer,
                           final Infrared infrared,
                           final LEDStrip ledStrip,
                           final Motor motor,
                           final PhotoResistor photoResistor,
                           final Servo servo,
                           final Ultrasonic ultrasonic,
                           final TestConfig testConfig) {
        this.adc = adc;
        this.buzzer = buzzer;
        this.infrared = infrared;
        this.ledStrip = ledStrip;
        this.motor = motor;
        this.photoResistor = photoResistor;
        this.servo = servo;
        this.ultrasonic = ultrasonic;
        this.testConfig = testConfig;
    }

    @Override
    public int run(final String... args) {
        if (testConfig.adc()) {
            testAdc();
            delay(MILLISECONDS, 1000);
        }
        if (testConfig.buzzer()) {
            testBuzzer();
            delay(MILLISECONDS, 1000);
        }
        if (testConfig.infrared()) {
            testInfrared();
            delay(MILLISECONDS, 1000);
        }
        if (testConfig.leds()) {
            testLeds();
            delay(MILLISECONDS, 1000);
        }
        if (testConfig.motor()) {
            testMotors();
            delay(MILLISECONDS, 1000);
        }
        if (testConfig.photoresistor()) {
            testPhotoResistor();
            delay(MILLISECONDS, 1000);
        }
        if (testConfig.servo()) {
            testServos();
            delay(MILLISECONDS, 1000);
        }
        if (testConfig.ultrasonic()) {
            testUltrasonicDistance();
            delay(MILLISECONDS, 1000);
        }

        return 0;
    }


    private void testAdc() {
        LOGGER.info("Testing ADC");
        for (int i = 0; i < 10; i++) {
            LOGGER.info("Left sensor: {}", adc.receive(0));
            LOGGER.info("Right sensor: {}", adc.receive(1));
            LOGGER.info("Battery level: {}", adc.receive(2) * 3);
            delay(MILLISECONDS, 1000);
        }
    }

    private void testBuzzer() {
        LOGGER.info("Testing buzzer");
        buzzer.on();
        delay(MILLISECONDS, 1000);
        buzzer.off();
    }

    private void testInfrared() {
        LOGGER.info("Testing Infrared");
        for (int i = 0; i < 10; i++) {
            LOGGER.info("Sensors: L{}M{}R{}",
                    infrared.getLeft() ? "1" : "0",
                    infrared.getMiddle() ? "1" : "0",
                    infrared.getRight() ? "1" : "0");
            delay(MILLISECONDS, 1000);
        }
    }

    private void testLeds() {
        LOGGER.info("Testing Color LEDs");
        ledStrip.colorFill(255, 0, 0);
        ledStrip.colorFill(0, 255, 0);
        ledStrip.colorFill(0, 0, 255);
        LOGGER.info("LED Chaser animation");
        ledStrip.chaseRainbow();
        LOGGER.info("Rainbow animation");
        ledStrip.rainbow();
        LOGGER.info("Rainbow cycle");
        ledStrip.rainbowCycle();
        ledStrip.colorFill(0, 0, 0);
    }

    private void testMotors() {
        LOGGER.info("Testing motors");
        LOGGER.info("Right front wheel spinning FORWARDS");
        motor.rightFrontWheel(1000);
        delay(MILLISECONDS, 2000);
        LOGGER.info("Right front wheel spinning BACKWARD");
        motor.rightFrontWheel(-1000);
        delay(MILLISECONDS, 2000);
        motor.rightFrontWheel(0);
        delay(MILLISECONDS, 1000);

        LOGGER.info("Left front wheel spinning FORWARDS");
        motor.leftFrontWheel(1000);
        delay(MILLISECONDS, 2000);
        LOGGER.info("Left front wheel spinning BACKWARDS");
        motor.leftFrontWheel(-1000);
        delay(MILLISECONDS, 2000);
        motor.leftFrontWheel(0);
        delay(MILLISECONDS, 1000);

        LOGGER.info("Right rear wheel spinning FORWARDS");
        motor.rightRearWheel(1000);
        delay(MILLISECONDS, 2000);
        LOGGER.info("Right rear wheel spinning BACKWARDS");
        motor.rightRearWheel(-1000);
        delay(MILLISECONDS, 2000);
        motor.rightRearWheel(0);
        delay(MILLISECONDS, 1000);

        LOGGER.info("Left rear wheel spinning FORWARDS");
        motor.leftRearWheel(1000);
        delay(MILLISECONDS, 2000);
        LOGGER.info("Left rear wheel spinning BACKWARDS");
        motor.leftRearWheel(-1000);
        delay(MILLISECONDS, 2000);
        motor.leftRearWheel(0);
    }

    public void testPhotoResistor() {
        LOGGER.info("Testing photoresistor");
        for (int i = 0; i < 10; i++) {
            LOGGER.info("Photoresistor values: L{} R{}", photoResistor.getLeft(), photoResistor.getRight());
            delay(MILLISECONDS, 1000);
        }
    }

    private void testServos() {
        LOGGER.info("Testing servo 0, setting angle 50");
        servo.setServoAngle(Servo.SERVO_CHANNEL_0, 50);
        delay(MILLISECONDS, 1000);
        LOGGER.info("Testing servo 0, setting angle 90");
        servo.setServoAngle(Servo.SERVO_CHANNEL_0, 90);
        delay(MILLISECONDS, 1000);
        LOGGER.info("Testing servo 0, setting angle 130");
        servo.setServoAngle(Servo.SERVO_CHANNEL_0, 130);
        delay(MILLISECONDS, 1000);
        LOGGER.info("Testing servo 0, setting angle 90");
        servo.setServoAngle(Servo.SERVO_CHANNEL_0, 90);
        delay(MILLISECONDS, 1000);

        LOGGER.info("Testing servo 1, setting angle 70");
        servo.setServoAngle(Servo.SERVO_CHANNEL_1, 70);
        delay(MILLISECONDS, 1000);
        LOGGER.info("Testing servo 1, setting angle 90");
        servo.setServoAngle(Servo.SERVO_CHANNEL_1, 90);
        delay(MILLISECONDS, 1000);
        LOGGER.info("Testing servo 1, setting angle 150");
        servo.setServoAngle(Servo.SERVO_CHANNEL_1, 150);
        delay(MILLISECONDS, 1000);
        LOGGER.info("Testing servo 1, setting angle 90");
        servo.setServoAngle(Servo.SERVO_CHANNEL_1, 90);
    }

    private void testUltrasonicDistance() {
        LOGGER.info("Testing ultrasonic distance sensor");
        for (int i = 0; i < 10; i++) {
            LOGGER.info("Distance: {}", ultrasonic.getDistance());
            delay(MILLISECONDS, 1000);
        }
    }
}
