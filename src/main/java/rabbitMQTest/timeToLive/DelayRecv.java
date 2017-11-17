package rabbitMQTest.timeToLive;

import java.io.IOException;

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
 * @author  阮航  
 * @date 创建时间：2017年11月17日 下午4:15:24 
 * @version 1.0 
 * 延时消息处理类
*/
public class DelayRecv {

	/** 
     * 创建队列并声明consumer用于处理转发过来的延时消息 
     * @throws Exception 
     */  
    public static void delayRecv() throws Exception{  
    	ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
        channel.exchangeDeclare("exchange-direct", "direct");  
        String queueName=channel.queueDeclare().getQueue();  
        channel.queueBind(queueName, "exchange-direct", "routing-delay");
        
        System.out.println("DelayRecv waitting for ...");
        Consumer consumer=new DefaultConsumer(channel){  
            @Override  
            public void handleDelivery(String consumerTag,Envelope envelope,AMQP.BasicProperties properties,byte[] body) throws IOException{  
            	User user = (User)SerializeUtils.deSerialize(body);  
                System.out.println(envelope.getRoutingKey()+":Received :'"+user.toString()+"' done");  
//              channel.basicAck(envelope.getDeliveryTag(), false);  
            }  
        };  
        //关闭自动应答机制，默认开启；这时候需要手动进行 
        channel.basicConsume(queueName, true, consumer);  
    }  
      
    public static void main(String[] args) throws Exception {  
        delayRecv();  
    }  
}
