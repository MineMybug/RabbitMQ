package rabbitMQTest.publicSubscribe;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/** 
 * @author  阮航  
 * @date 创建时间：2017年11月14日 下午7:31:42 
 * @version 1.0 
*/
public class PublicshSub {
	
	private static final String EXCHANGE_NAME = "logs";
	/**
	 * @param args
	 * @throws IOException
	 * @throws TimeoutException
	 * 发送消息到一个名为“logs”的exchange上，使用“fanout”方式发送，即广播消息，不需要使用queue，发送端不需要关心谁接收。
	 */
	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");//fanout表示分发，所有的消费者得到同样的队列信息
		//分发消息
		for(int i =0 ; i < 100000; i++){
			String message = "hello world" + i;
			channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
			System.out.println("Send message : " + message);
		}
		channel.close();
		connection.close();
	}

}
