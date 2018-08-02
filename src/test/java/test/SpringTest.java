package test;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.ep.entity.Sysuser;
import com.ep.service.UserInfoService;

/**
* @author  Zhaoxh
* @version 创建时间：2018年8月2日 下午3:31:14
* 测试类
*/
public class SpringTest extends BaseSpringTest {
	
	@Autowired
    public UserInfoService userInfoService;

	@Test
	public void selectAllUser() {
		List<Sysuser> users = userInfoService.findAll();
		for(Sysuser user : users) {
			System.out.println("UserName:"+user.getUserName() + "\n" + "UID:" + user.getUserId());
		}
	}
	
	@Transactional
	@Rollback(true)
	@Test
	public void insert() {
		String i = userInfoService.addUser("userName5", "123456", "", "1");
		System.out.println(i);
		
	}
	
	@Test
	public void del() {
		String i = userInfoService.deleteUser("7");
		System.out.println(i);
		
	}
}
