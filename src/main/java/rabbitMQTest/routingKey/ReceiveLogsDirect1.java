package rabbitMQTest.routingKey;

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
 * @date 创建时间：2017年11月15日 下午2:37:46 
 * @version 1.0 
*/
public class ReceiveLogsDirect1 {
	
	//交换机名称
	public static final String EXCHANGE_NAME = "direct_log";
	//路由名称
	public static final String routingKey = "error";
	
	public static final String QUEUE_NAME = "logs_forError";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		//声明交换机
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");
		
		//获取匿名队列名称
//		String queueName = channel.queueDeclare().getQueue();
		
		channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		
		//根据路由关键字进行绑定
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, routingKey);
		System.out.println("ReceiveLogsDirect1 exchange:"+EXCHANGE_NAME+"," +
                " queue:"+QUEUE_NAME+", BindRoutingKey:" + routingKey);
		
		System.out.println("ReceiveLogsDirect1 waitting for message!");
		
		Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" ReceviceMessage2 '" + message + "'");
            }
        };
	
        channel.basicConsume(QUEUE_NAME, true, consumer);//队列会自动删除
	}

}
