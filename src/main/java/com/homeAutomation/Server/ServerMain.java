package com.homeAutomation.Server;
import com.homeAutomation.Server.Communication.RemoteIoThread;
import com.homeAutomation.Server.Communication.TempThread;
import com.homeAutomation.Server.Interface.ILogicExpResult;
import com.homeAutomation.Server.Logger.Logger;
import com.homeAutomation.Server.Model.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class ServerMain implements Runnable {

    private final String CONFIG_PATH = "configuration/";
    private Outputs outputs;
    private Inputs inputs;
    private TempInputs tempInputs;
    private Configuration config;
    private Coils coils;
    private LogicExpEvaluator logixExpEvaluator;

    private AbstractCollection<ILogicExpResult> logicExpressionResult;
    private AbstractCollection<Token> operandsList;

    private RemoteIoThread remoteIoThread;
    private AbstractCollection<Thread> threads;
    private TempThread tempThread;
    private ReentrantLock lock;

    @Override
    public void run(){
        // Set up configuration

        // Deserialize
        config = deserialize(Configuration.class,CONFIG_PATH + "Configuration.xml");
        tempInputs = deserialize(TempInputs.class, CONFIG_PATH + "TempInputList.xml");
        inputs = deserialize(Inputs.class, CONFIG_PATH + "InputList.xml");
        for (TempInput t: tempInputs) {
            inputs.add(t);
        }
        outputs = deserialize(Outputs.class, CONFIG_PATH + "OutputList.xml");
        coils = deserialize(Coils.class, CONFIG_PATH + "Coils.xml");
        InfixExpressions infixExpressions = deserialize(InfixExpressions.class, CONFIG_PATH + "InfixExpressions.xml");

        // Create logic exp result list
        logicExpressionResult = new ArrayList<ILogicExpResult>();
        logicExpressionResult.addAll(outputs);
        logicExpressionResult.addAll(coils);

        // Create operand list
        operandsList = new ArrayList<Token>();
        operandsList.addAll(inputs);
        operandsList.addAll(outputs);
        operandsList.addAll(coils);

        // Pars postfix
        logixExpEvaluator = new LogicExpEvaluator();
        logixExpEvaluator.setInputs(operandsList);
        logixExpEvaluator.setLogicExpResults(logicExpressionResult);
        logixExpEvaluator.parsInfixToPostfix(infixExpressions);

        // Threads
        threads = new ArrayList();

        remoteIoThread = new RemoteIoThread(config, inputs.getList(1), outputs.getList(1));
        lock = remoteIoThread.getLock();
        threads.add(new Thread(remoteIoThread));

        tempThread = new TempThread(tempInputs);
        threads.add(new Thread(tempThread));

        for (Thread thread: threads) {
            thread.start();
        }

        // Clean
        infixExpressions = null;

        runtime();
    }

    private void runtime(){
        Logger.getInstance().log("Entering main loop");

        while( true ){
            if(lock.tryLock()) {
                logixExpEvaluator.evaluate();
                inputs.updatePrevValue();
                outputs.updatePrevValue();
                coils.updatePrevValue();
                lock.unlock();
            }
        }
    }
    public <T> void serialize(T object, String name) {
        try {
            File file = new File(name);
            JAXBContext context = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.marshal(object, file);

        } catch (JAXBException e) {
            System.err.println(String.format("Exception while marshalling: %s", e.getMessage()));
        }
    }
    public <T> T deserialize(Class<T> clazz, String name) {
        try {
            File file = new File(name);
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            T obj = (T) unmarshaller.unmarshal(file);
            return obj;

        } catch (JAXBException e) {
            System.err.println(String.format("Exception while marshalling: %s", e.getMessage()));
            return null;
        }
    }

    public void setCoil(String name, int value) {
        for (Coil coil: coils) {
            if(coil.getName().equals(name)){
                coil.setValue(value);
                coil.setOutOfControl(false);
            }
        }
    }
}
