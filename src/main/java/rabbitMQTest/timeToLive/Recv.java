package rabbitMQTest.timeToLive;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
 * @date 创建时间：2017年11月17日 下午4:01:22 
 * @version 1.0 
*/
public class Recv {
	
	 /** 
     * 在topic转发器的基础上练习延时转发，设置队列过期时间(过期后自动删除)，过期消息处理策略(转发给相匹配的queue) 
     * 实验时启动接收类创建队列后，关闭该线程，使其进入未使用状态 
     * @throws Exception 
     */  
    public static void recvAToB() throws Exception{  
    	ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
        channel.exchangeDeclare("header_exechange", "topic");  
        
        //设置队列过期时间为30秒，消息过期转发给指定转发器、匹配的routingkey(可不指定)  
        Map<String, Object> args=new HashMap<String, Object>();  
        args.put("x-expires", 30000l);//队列过期时间  
        args.put("x-message-ttl", 12000l);//队列上消息过期时间  
        args.put("x-dead-letter-exchange", "exchange-direct");//过期消息转向路由  
        args.put("x-dead-letter-routing-key", "routing-delay");//过期消息转向路由相匹配routingkey  
        
        //创建一个临时队列  
        String queueName=channel.queueDeclare("tmp01",true,false,false,args).getQueue();  
       
        //指定headers的匹配类型(all、any)、键值对  
        Map<String, Object> headers=new HashMap<String, Object>();  
        headers.put("x-match", "all");//all any(只要有一个键值对匹配即可)  
        headers.put("key", "123456");  
//      headers.put("token", "6543211");  
        
        //绑定临时队列和转发器header_exchange  
        channel.queueBind(queueName, "header_exechange", "", headers);  
        System.out.println("Received ...");  
//        Consumer consumer=new DefaultConsumer(channel){  
//            @Override  
//            public void handleDelivery(String consumerTag,Envelope envelope,AMQP.BasicProperties properties,byte[] body) throws IOException{  
//                User user = (User)SerializeUtils.deSerialize(body);  
//                System.out.println(envelope.getRoutingKey()+":Received :'"+user.toString()+"' done");  
//                channel.basicAck(envelope.getDeliveryTag(), false);  
//            }  
//        };  
       
//        //关闭自动应答机制，默认开启；这时候需要手动进行
//        channel.basicConsume(queueName, true, consumer);  
    }  
      
    public static void main(String[] args) throws Exception {  
        recvAToB();  
    }  

}
