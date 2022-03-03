package shai.websocket.server;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.TextMessage;
import shai.websocket.server.handler.SocketTextHandler;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
public class ServerApplication {

	@Autowired
	RabbitTemplate rabbitTemplate;

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@Bean
	public Queue myQueue() {
		return new Queue("myQueue", false);
	}

	@RabbitListener(queues = "myQueue")
	public void listen(String in) {
		System.out.println("Message read from myQueue : " + in);
		SocketTextHandler.sendMessage(in);
	}

	@Scheduled(fixedDelay = 1000)
	public void sendMessage() {
		String message = RandomStringUtils.randomAlphanumeric(17);
		System.out.println("Published to queue myQueue : " + message);
		rabbitTemplate.convertAndSend("myQueue", message);
	}

}
