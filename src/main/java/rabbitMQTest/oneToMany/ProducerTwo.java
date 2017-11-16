package rabbitMQTest.oneToMany;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/** 
 * @author  阮航  
 * @date 创建时间：2017年11月9日 下午5:40:30 
 * @version 1.0 
*/
public class ProducerTwo {
	
	public static final String QUEUE_NAME = "task_queue";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(QUEUE_NAME,true,false,false,null);
		for(int i = 0; i<1000; i++){
			String message = "hello RabbitMQ" + i;
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
			System.out.println("ProducerTwo Send " + message + "*");
			
		}
		channel.close();
		connection.close();
		
	}

}
