package rabbitMQTest.publicSubscribe;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/** 
 * @author  阮航  
 * @date 创建时间：2017年11月14日 下午7:42:14 
 * @version 1.0 
*/
public class ReceviceMessage1 {
	
	private static final String EXCHANGE_NAME = "logs";
	
	/**
	 * @param args
	 * @throws IOException
	 * @throws TimeoutException
	 * 
	 * 1、声明名为“logs”的exchange的，方式为"fanout"，和发送端一样。
	 * 2、channel.queueDeclare().getQueue();该语句得到一个随机名称的Queue，该queue的类型为non-durable、exclusive、auto-delete的，将该queue绑定到上面的exchange上接收消息。
     * 3、注意binding queue的时候，channel.queueBind()的第三个参数Routing key为空，即所有的消息都接收。如果这个值不为空，在exchange type为“fanout”方式下该值被忽略！ 
	 */
	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		
		//申明一个队列
		String queueName = channel.queueDeclare().getQueue();
		//绑定交换机
		channel.queueBind(queueName, EXCHANGE_NAME, "");

		System.out.println("ReceviceMessage1 waitting for messages!");

		Consumer consumer = new DefaultConsumer(channel) {
	            @Override
	            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
	                String message = new String(body, "UTF-8");
	                System.out.println(" ReceviceMessage1 '" + message + "'");
	            }
	        };
		
        channel.basicConsume(queueName, true, consumer);//队列会自动删除
	}
	
}
