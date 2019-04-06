package com.homeAutomation.Server.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static Logger logger = null;
    private FileWriter fileWriter;
    private PrintWriter printWriter;
    private static final String CONFIG_PATH = "logs/";
    private static final Object lock = new Object();

    private Logger(String path){
        path += ".txt";
        try {
            fileWriter = new FileWriter(path);
            printWriter = new PrintWriter(fileWriter);
        }catch(IOException e){
            System.out.println(e.getStackTrace());
        }
        //TODO: Prepare implementation with storig of log in database, use hibernate
    }

    public static Logger getInstance(){
        if(logger == null){
            synchronized (lock){
                if(logger == null) {
                    SimpleDateFormat ft = new SimpleDateFormat("yy_MM_dd'_'hh_mm");
                    Date date = new Date();

                    logger = new Logger(CONFIG_PATH + ft.format(date).toString());
                    logger.log("Logger file created");
                }
            }
        }
        return logger;
    }

    public void log(String entry){
        Date date = new Date();
        String str = new String(date + ": " + entry);
        printWriter.println(str);
        System.out.println(str);
        try {
            printWriter.flush();
            fileWriter.flush();
        }catch (IOException e){

        }
    }

    @Override
    public void finalize(){
        try {
            printWriter.flush();
            fileWriter.flush();
            fileWriter.close();
        }catch (IOException e){
            System.out.println(e.toString());
        }
        System.out.println("Closing ");
    }
}