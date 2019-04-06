package com.homeAutomation.Server.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Temperature {
    private String id;
    private double temperature;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public double getTemperature() {
        return temperature;
    }
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "Temperature: " + temperature + ", id: " + id;
    }
}
