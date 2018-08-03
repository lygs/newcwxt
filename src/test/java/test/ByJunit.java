package test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ep.entity.Sysuser;
import com.ep.service.UserInfoService;

/**
* @author  Zhaoxh
* @version 创建时间：2018年8月2日 下午3:15:20
* 
*/
public class ByJunit {

	ClassPathXmlApplicationContext ctx;
	@Before
	public void init(){
		ctx = new ClassPathXmlApplicationContext(
			"applicationContext.xml",
			"spring-mvc.xml");
	}
	
	@Test
	public void test1() {
		UserInfoService userDao= ctx.getBean("userInfoService",UserInfoService.class);
		
		List<Sysuser> users = userDao.findAll();
		for(Sysuser user : users) {
			System.out.println(user.getUserName());
		}
	}
	
}
