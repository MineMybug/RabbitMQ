package rabbitMQTest.common;

import java.io.Serializable;

/** 
 * @author  阮航  
 * @date 创建时间：2017年11月16日 上午11:23:27 
 * @version 1.0 
*/
public class User implements Serializable{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 3100981671781117409L;

	private String name;
	
	private int age;
	
	private String password;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", age=" + age + ", password=" + password + "]";
	}
	
	

}
