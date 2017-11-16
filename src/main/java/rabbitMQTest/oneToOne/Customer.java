package rabbitMQTest.oneToOne;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import rabbitMQTest.common.SerializeUtils;
import rabbitMQTest.common.User;

/**
 * @author 阮航
 * @date 创建时间：2017年11月9日 下午3:20:17
 * @version 1.0
 */
public class Customer {

	public static final String QUEUE_NAME = "hello";

	public static void Recv() throws IOException, TimeoutException {
		// 创建连接工厂
		ConnectionFactory factory = new ConnectionFactory();

		// 设置Rabbitmq的地址
		factory.setHost("localhost");

		// 创建一个新的连接
		Connection connection = factory.newConnection();

		// 创建一个通道
		Channel channel = connection.createChannel();

		// 声明要关注的队列
		channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		System.out.println("Customer Waitting Receved message!");

		// DefaultConsumer类实现了Consumer接口，通过传入一个频道，
		// 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
//				String message = new String(body, "UTF-8");
				User user = (User) SerializeUtils.deSerialize(body);
				System.out.println("Customer Received " + user.toString() + "");
			}
		};
		//自动回复队列应答 -- RabbitMQ中的消息确认机制
        channel.basicConsume(QUEUE_NAME, false, consumer);
	}
	
	public static void main(String[] args) throws IOException, TimeoutException {

		Recv();
	}


}
