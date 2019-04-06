package com.homeAutomation.Server.Communication;

import com.homeAutomation.Server.Logger.Logger;
import com.homeAutomation.Server.Model.TempInput;
import com.homeAutomation.Server.Model.TempInputs;
import com.homeAutomation.Server.Model.Temperature;
import com.homeAutomation.TestObj;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.AbstractCollection;
import java.util.List;

public class TempThread implements Runnable {
    public TempThread(TempInputs tempInputs){
        this.tempInputs = tempInputs;
    }

    public void setTempInputs(TempInputs tempInputs) {
        this.tempInputs = tempInputs;
    }

    private TempInputs tempInputs;

    @Override
    public void run() {
        //TODO temp out of control
        String url = "http://192.168.1.4:8082";
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.setRequestFactory(new SimpleClientHttpRequestFactory());
        SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) restTemplate
                .getRequestFactory();
        rf.setReadTimeout(3000);
        rf.setConnectTimeout(3000);

        while(true){
            long startTime = System.currentTimeMillis();

            try {
                ResponseEntity<List<Temperature>> response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Temperature>>() {
                        });
                List<Temperature> temperatures = response.getBody();

                for (Temperature t : temperatures) {
                    for (TempInput in : tempInputs) {
                        if (t.getId().equals(in.getId())) {
                            in.setValue((int) (t.getTemperature() * 100));
                            in.setOutOfControl(false);
                            in.setLastActivity(System.currentTimeMillis());
                            System.out.println(t.toString());
                            break;
                        }
                    }
                }
            }catch(Exception e){
                Logger.getInstance().log(e.toString());
            }

            for (TempInput in : tempInputs){
                if(in.getLastActivity() + 2000 < System.currentTimeMillis()){
                    in.setOutOfControl(true);
                }
            }

            try {
                long sleepValue = 2000 - (System.currentTimeMillis() - startTime);
                if(sleepValue > 0){
                    Thread.sleep(sleepValue);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
