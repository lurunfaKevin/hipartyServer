package controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import beans.Chater;
import beans.Room;
import beans.RoomUser;
import beans.User;
import utils.HibernateUtil;

@Controller
@RequestMapping(value="/user")
public class UserController {
	
	private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	
	//用户登录
	@RequestMapping(value="/login") 
	@ResponseBody
 	public Chater UserLogin(String userId,String password){

		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		User user = (User) session.createQuery("from User where userId=:userId")
		.setParameter("userId", userId)
		.uniqueResult();
		Chater chater = new Chater();
		chater.setUserId(userId);
		chater.setOrder("login");
		
		if(user==null){
			chater.setMessage("NO USER");
			return chater;
		}
		if(!user.getPassword().equals(password)){
			chater.setMessage("PASSWORD FAILED");
			return chater;			
		}
		chater.setMessage("SUCCEED");
		
		session.getTransaction().commit();
		
		return chater;
	}	
	//用户注册
	@RequestMapping(value="/regist")
	@ResponseBody
	public Chater UserRegist(String userId,String username,String password){
		
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		User user = (User) session.createQuery("from User where userId=:userId")
		.setParameter("userId", userId)
		.uniqueResult();
		Chater chater = new Chater();
		chater.setUserId(userId);
		chater.setOrder("regist");
		
		if(user!=null){
			chater.setMessage("You have already Registed");
			return chater;
		}
		
		user =new User();
		user.setPassword(password);
		user.setUserId(userId);
		user.setUsername(username);
		session.save(user);
		session.getTransaction().commit();
		
		chater.setMessage("SUCCEED");
		return chater;
	}

	@RequestMapping("/test")
	@ResponseBody
	public Chater Test(String json) {
		Chater chater = new Chater();
		User user = new User();
		user.setPassword("123");
		user.setUsername("wef");
		List<String> list = new ArrayList<String>();
		list.add("123");
		list.add("132");
		Map<String, String> map =new HashMap<>();
		map.put("userId", "ir");
		map.put("roomId", "agrd");
		
		chater.setObject(map);
		
		return chater;
	}
}
