package com.homeAutomation;

import com.homeAutomation.Server.ServerMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Application {
	private static Thread serverThread;
	@Autowired
	static ServerMain serverMain;// = new ServerMain();
	public static void main(String[] args) {

		ApplicationContext ctx = SpringApplication.run(Application.class, args);
		Thread subscriber = new Thread(ctx.getBean(ServerMain.class));
		subscriber.start();

//		serverMain.run();
//		serverThread = new Thread(serverMain);
//		serverThread.start();

//		SpringApplication.run(Application.class, args);
	}

}
