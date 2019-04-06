package com.homeAutomation.Server.Model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Configuration {
    private String remoteInOutIp;
    private int remoteInOutPort;
    private int channelsPerSlot;
    private int digitalInputSlots;
    private int digitalOutputSlots;
    private int tempInputs;

    public String getRemoteInOutIp() {
        return remoteInOutIp;
    }
    public void setRemoteInOutIp(String remoteInOutIp) {
        this.remoteInOutIp = remoteInOutIp;
    }

    public int getRemoteInOutPort() {
        return remoteInOutPort;
    }
    public void setRemoteInOutPort(int remoteInOutPort) {
        this.remoteInOutPort = remoteInOutPort;
    }

    public int getChannelsPerSlot() {
        return channelsPerSlot;
    }
    public void setChannelsPerSlot(int channelsPerSlot) {
        this.channelsPerSlot = channelsPerSlot;
    }

    public int getDigitalInputSlots() {
        return digitalInputSlots;
    }
    public void setDigitalInputSlots(int digitalInputSlots) {
        this.digitalInputSlots = digitalInputSlots;
    }

    public int getDigitalOutputSlots() {
        return digitalOutputSlots;
    }
    public void setDigitalOutputSlots(int digitalOutputSlots) {
        this.digitalOutputSlots = digitalOutputSlots;
    }

    public int getTempInputs() {
        return tempInputs;
    }
    public void setTempInputs(int tempInputs) {
        this.tempInputs = tempInputs;
    }

    @Override
    public String toString(){
        String res = "Config: " + remoteInOutIp + ":" + remoteInOutPort + "\r\n";
        return res;
    }
}
