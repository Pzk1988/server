package com.homeAutomation.Server.Communication;

import com.homeAutomation.Server.Logger.Logger;
import com.homeAutomation.Server.Model.Configuration;
import com.homeAutomation.Server.Model.Input;
import com.homeAutomation.Server.Model.Output;
import de.re.easymodbus.modbusclient.ModbusClient;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class RemoteIoThread implements Runnable {
    private ModbusClient modbusClient;
    private Configuration config;
    private final int READ_DELAY = 5;
    private short[] prevInputState;
    private List<Input> inputs;
    private List<Output> outputs;
    private boolean connected;
    private ReentrantLock lock;
    private boolean firstRun;

    public RemoteIoThread(Configuration config, List<Input> inputs, List<Output> outputs) {
        this.config = config;
        this.inputs = inputs;
        this.outputs = outputs;

        prevInputState = new short[config.getDigitalInputSlots()];
        connected = false;
        lock = new ReentrantLock();
        setOutOfControl(true);
        firstRun = true;
        modbusClient = new ModbusClient(config.getRemoteInOutIp(), config.getRemoteInOutPort());
    }
    public void run() {
        while(true){
            if(connected == false){
                connected = connect();
            }else{
                connected = transmission();
            }
        }
    }

    private boolean connect() {
        boolean retVal = false;
        try{
            modbusClient.Connect();
            Logger.getInstance().log("Connected to wago server");
            retVal = true;
            firstRun = true;
        }catch (IOException e){
            Logger.getInstance().log("Unable to connect to wago server: " + e.getMessage());
        }finally {
            return retVal;
        }
    }

    private boolean transmission(){
        try {
            processInputs();
            processOutputs();
            Thread.sleep(READ_DELAY);
        }catch(Exception e){
            setOutOfControl(true);
            return false;
        }
        return true;
    }

    private void processOutputs() throws Exception {
        int[] data = new int[config.getDigitalOutputSlots()];

        lock.lock();
        for(int i = 0; i < config.getDigitalOutputSlots(); i++){
            for(int j = 0; j < config.getChannelsPerSlot(); j++){
                if(outputs.get((i * config.getChannelsPerSlot()) + j).getValue() == 1){
                    data[i] |= 0x0001 << j;
                }
            }
        }
        lock.unlock();

        try{
            modbusClient.WriteMultipleRegisters(0, data);
        }
        catch(Exception e){
            Logger.getInstance().log(e.toString());
            throw new Exception("");
        }
    }

    private void processInputs() throws Exception {
        int[] result;

        try{
            result = modbusClient.ReadInputRegisters(0, config.getDigitalInputSlots());
        }
        catch(Exception e){
            Logger.getInstance().log(e.toString());
            throw new Exception("cos");
        }

        //TODO: Write efficent processInputs method
        lock.lock();
            if (firstRun == false) {
                for (int i = 0; i < config.getDigitalInputSlots(); i++) {
                    short state = (short) result[i];
                    if (prevInputState[i] != state) {
                        for (int j = 0; j < 16; j++) {
                            if ((state & (1 << j)) != (prevInputState[i] & (1 << j))) {
                                inputs.get((i * 16) + j).setValue(state & (1 << j));
                                Logger.getInstance().log(String.format("Slot %d, channel %d value %d", i, j, inputs.get((i * 16) + j).getValue()));
                            }
                        }
                        prevInputState[i] = (short) result[i];
                    }
                }
            } else {
                for (int i = 0; i < config.getDigitalInputSlots(); i++) {
                    short state = (short) result[i];
                    Logger.getInstance().log(String.format("Slot %d, value 0x%04x", i, state));
                    for (int j = 0; j < 16; j++) {
                        inputs.get((i * 16) + j).setValue(state & (1 << j));
                    }
                    prevInputState[i] = (short) result[i];
                }
                setOutOfControl(false);
                firstRun = false;
            }
        lock.unlock();
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public void setOutOfControl(boolean outOfControl) {
        lock.lock();
        for(Input in : inputs){
            in.setOutOfControl(outOfControl);
        }
        lock.unlock();
    }
}