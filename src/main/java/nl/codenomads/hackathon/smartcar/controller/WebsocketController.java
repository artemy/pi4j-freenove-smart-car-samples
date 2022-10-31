package nl.codenomads.hackathon.smartcar.controller;

import io.quarkus.scheduler.Scheduled;
import nl.codenomads.hackathon.smartcar.Commands;
import nl.codenomads.hackathon.smartcar.drivers.Buzzer;
import nl.codenomads.hackathon.smartcar.drivers.Motor;
import nl.codenomads.hackathon.smartcar.drivers.Servo;
import nl.codenomads.hackathon.smartcar.drivers.adc.ADC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static nl.codenomads.hackathon.smartcar.Commands.*;

@ServerEndpoint("/rc-car")
@ApplicationScoped
public class WebsocketController {

    private static final String MESSAGE_SEPARATOR = "#";
    private static final Logger LOGGER = LoggerFactory
            .getLogger(WebsocketController.class);
    private final List<Session> sessions = Collections.synchronizedList(new ArrayList<>());

    private final ADC adc;
    private final Motor motor;

    private final Servo servo;
    private final Buzzer buzzer;

    public WebsocketController(final ADC adc, final Motor motor, final Servo servo, final Buzzer buzzer) {
        this.adc = adc;
        this.motor = motor;
        this.servo = servo;
        this.buzzer = buzzer;
    }

    @OnOpen
    public void onOpen(final Session session) {
        LOGGER.info("onOpen> ");
        sessions.add(session);
        sendPower();
    }

    @OnClose
    public void onClose(final Session session) {
        LOGGER.info("onClose> ");
        sessions.remove(session);
    }

    @OnError
    public void onError(final Session session, final Throwable throwable) {
        LOGGER.info("onError> " + throwable);
        sessions.remove(session);
    }

    @OnMessage
    public void onMessage(final String message) {
        LOGGER.info("onMessage> " + message);
        final var commands = List.of(message.split(MESSAGE_SEPARATOR));
        if (commands.isEmpty())
            return;
        switch (commands.get(0)) {
            case Commands.CMD_MOTOR -> {
                final var speeds = commands.subList(1, commands.size()).stream()
                        .map(Integer::parseInt)
                        .toList();
                motor.setAllWheels(speeds.get(0), speeds.get(1), speeds.get(2), speeds.get(3));
            }
            case CMD_SERVO -> servo.setServoAngle(Integer.parseInt(commands.get(1)), Integer.parseInt(commands.get(2)));
            case CMD_BUZZER -> {
                if ("ON".equals(commands.get(1))) {
                    buzzer.on();
                } else {
                    buzzer.off();
                }
            }
        }
    }

    // every 30 seconds
    @Scheduled(every = "PT30s")
    void sendPower() {
        final DecimalFormat df = new DecimalFormat("#.##");

        final var powerLevel = df.format(adc.receive(2) * 3);
        broadcast(CMD_BATTERY + MESSAGE_SEPARATOR + powerLevel);
    }

    private void broadcast(final String message) {
        sessions.forEach(s -> s.getAsyncRemote()
                .sendObject(message, result -> {
                    if (result.getException() != null) {
                        LOGGER.error("Unable to send message: " + result.getException());
                    }
                }));
    }
}
