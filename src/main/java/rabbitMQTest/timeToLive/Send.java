package rabbitMQTest.timeToLive;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException; 
import com.rabbitmq.client.AMQP.BasicProperties;  
import com.rabbitmq.client.AMQP.BasicProperties.Builder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import rabbitMQTest.common.SerializeUtils;
import rabbitMQTest.common.User;

/** 
 * @author  阮航  
 * @date 创建时间：2017年11月17日 下午3:10:38 
 * @version 1.0 
*/
public class Send {
	
	
	/**
	 * 在topic转发器的基础上练习延时转发，发送消息时指定消息过期时间 
     * 消息已发送到queue上，但未有consumer进行消费 
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public static void SendAtoB(Serializable object) throws IOException, TimeoutException{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		//声明转发的交换机
		channel.exchangeDeclare("header_exechange", "topic");
		
		//定义headers存储的键值对
		Map<String, Object> headers = new HashMap<String,Object>();
		headers.put("key", "123456");
		headers.put("token", "654321");
		
		//把键值对放在properties
		 Builder properties=new BasicProperties.Builder();  
         properties.headers(headers);  
         properties.deliveryMode(2);//持久化  
//	     //指定消息过期时间为12秒,队列上也可以指定消息的过期时间，两者以较小时间为准  
//   	 properties.expiration("12000");//延时12秒，不会及时删除(在consuemr消费时判定是否过期，因为每条消息的过期时间不一致，删除过期消息就需要扫描整个队列)  
	     channel.basicPublish("header_exechange", "" ,properties.build(), SerializeUtils.serialize(object));  
	     System.out.println("Send '"+object+"'");  
	     channel.close();  
	     connection.close();  
	}
	
	/**
	 * @param args
	 * @throws TimeoutException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, TimeoutException {
		User user = new User();
		user.setAge(12);
		user.setName("阮航");
		user.setPassword("123456");
		SendAtoB(user);
	}

}
