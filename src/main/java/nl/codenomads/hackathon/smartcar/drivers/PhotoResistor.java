package nl.codenomads.hackathon.smartcar.drivers;

import nl.codenomads.hackathon.smartcar.drivers.adc.ADC;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PhotoResistor {

    private final ADC adc;

    public PhotoResistor(final ADC adc) {
        this.adc = adc;
    }

    public double getLeft() {
        return adc.receive(0);
    }

    public double getRight() {
        return adc.receive(1);
    }
}
