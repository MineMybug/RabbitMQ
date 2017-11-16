package rabbitMQTest.oneToOne;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import rabbitMQTest.common.SerializeUtils;
import rabbitMQTest.common.User;

/** 
 * @author  阮航  
 * @date 创建时间：2017年11月9日 下午2:49:52 
 * @version 1.0
 * 消息生产者 
*/
public class Producer {
	
	/**
	 * 单发送、单接收场景，无特别处理，用于发送消息 
	 * @param queue 队列
	 * @param object 实体
	 * @throws IOException
	 * @throws TimeoutException
	 * @throws UnsupportedEncodingException
	 */
	public static void Send(String queue,Serializable object) throws IOException, TimeoutException, UnsupportedEncodingException {
		//创建连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		
		//设置RabbitMQ的相关属性
		factory.setHost("localhost");
//		factory.setUsername("rh");
//		factory.setPassword("123456");
//		factory.setPort(12567);
		
		//创建一个新的连接
		Connection connection = factory.newConnection();
		
		//创建一个通道
		Channel channel = connection.createChannel();
		
		/*
		 * 注：queueDeclare第一个参数表示队列名称、
		 * 第二个参数为是否持久化（true表示是，队列将在服务器重启时生存）
		 * 第三个参数为是否是独占队列（创建者可以使用的私有队列，断开后自动删除）
		 * 第四个参数为当所有消费者客户端连接断开时是否自动删除队列
		 * 第五个参数为队列的其他参数
		 */
		//申明一个队列
		channel.queueDeclare(queue, true, false, false, null);
				
//		String messge = "Hello RabbitMQ!";
		
		/*
		 * 注：basicPublish第一个参数为交换机名称
		 * 第二个参数为队列映射的路由key
		 * 第三个参数为消息的其他属性
		 * 第四个参数为发送信息的主体
		 */		
		//发送到队列中
		channel.basicPublish("", queue, null, SerializeUtils.serialize(object));
		
		System.out.println("Producer Send " + " " + SerializeUtils.serialize(object) );
		
		//关闭通道和连接
		channel.close();
		connection.close();
	}
	
	public static void main(String[] args) throws IOException, TimeoutException {
		//消息实体
		User user = new User();
		user.setName("阮航");
		user.setAge(17);
		user.setPassword("123456");

		//队列
		String queue = "hello";
		Send(queue,user);
		
		
	}


}
