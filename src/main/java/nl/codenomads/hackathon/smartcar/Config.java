package nl.codenomads.hackathon.smartcar;


import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import nl.codenomads.hackathon.smartcar.drivers.adc.ADC;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

@Dependent
public class Config {

    @Produces
    public Context pi4jContext() {
        return Pi4J.newAutoContext();
    }

    @Produces
    public ADC adc(final Context pi4j) {
        return ADC.getInstance(pi4j);
    }
}
