package com.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.beans.*;
import com.google.gson.Gson;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.utils.HibernateUtil;

import javax.servlet.http.HttpServletResponse;

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
	//暖场游戏
	@RequestMapping(value="/warmgame")
	@ResponseBody
	public Chater WarmGame(String level){
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session=sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<WarmGame> WarmGamelist = session.createQuery("from WarmGame where WarmGameLevel=:WarmGameLevel")
				.setParameter("WarmGameLevel", level).list();
		session.getTransaction().commit();
		// 设定返回值
		Chater chater = new Chater();
		chater.setOrder("warmgame");
		Map<String, Object> object = new HashMap<>();
		object.put("size", WarmGamelist.size());
		object.put("list", new Gson().toJson(WarmGamelist));
		chater.setObject(object);
		chater.setMessage("SUCCEED");
		return chater;
	}
	//惩罚

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

	@RequestMapping("/download")

	public void TestdownloadFile(String url,HttpServletResponse response){
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName="+url);
		try {
			File file=new File(url);
			System.out.println(file.getAbsolutePath());
			InputStream inputStream= null;
			try {
				inputStream = new FileInputStream("file/"+file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println("11111111111"+file);
			OutputStream os=response.getOutputStream();
			byte[] b=new byte[1024];
			int length;
			while((length=inputStream.read(b))>0){
				os.write(b,0,length);
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
