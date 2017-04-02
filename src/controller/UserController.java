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

//	//用户退出房间
//	@RequestMapping(value="/out")
//	@ResponseBody
//	public Chater UserOut(String userId,String roomId){
//		
//		Session session = sessionFactory.getCurrentSession();
//		session.beginTransaction();
//		Chater chater = new Chater();
//		chater.setOrder("out");
//		
//		RoomUser roomuser = (RoomUser)session.createQuery("from RoomUser where roomId=:roomId and useId=:useId")
//		.setParameter("userId", userId)
//		.setParameter("useId", userId)
//		.uniqueResult();
//		if(roomuser==null){
//			chater.setMessage("你已退出房间");
//			return chater;
//		}
//		session.delete(roomuser);
//		
//		Room room =(Room)session.createQuery("from Room where roomId=:roomId")
//				.setParameter("roomId", roomId)
//				.uniqueResult();
//		room.setRoomnum(room.getRoomnum()-1);
//		if(room.getRoomnum()==0){
//			session.delete(room);
//		}
//		session.getTransaction().commit();
//		chater.setMessage("退出成功");
//		return chater;
//	}
//	//自我介绍(实际上确认发送，即ensureintroduce之后才执行此方法)
//	@RequestMapping(value="/introduce")
//	@ResponseBody
//	public Chater UserIntroduce(String userId,String roomId,String introduce){
//		
//		Session session=sessionFactory.getCurrentSession();
//		session.beginTransaction();
//		Chater chater= new Chater();
//		chater.setOrder("introduce");
//		
//		RoomUser roomuser=(RoomUser)session.createQuery("from RoomUser where roomId=:roomId and userId=:userId")
//				.setParameter("roomId", roomId)
//				.setParameter("useId", userId)
//				.uniqueResult();
//		roomuser.setStatus(roomuser.getRoomusername()+":"+introduce);
//		//将单个成员的自我介绍拼接
//		Room room = new Room();
//		room.setRoomstatus(room.getRoomstatus()+roomuser.getStatus()+'\n');
//		
//		session.save(roomuser);
//		session.save(room);
//		session.getTransaction().commit();
//		
//		//检查全部成员的自我介绍是否都已接收
//		RoomUser roomuser1=(RoomUser) session.createQuery("select status from RoomUser where roomId=:roomId and status=:status")
//				.setParameter("roomId", roomId)
//				.setParameter("status", null)
//				.uniqueResult();
//				
//		if(roomuser1==null){
//			chater.setObj(room.getRoomstatus());
//			chater.setMessage("全部成员的自我介绍");
//			return chater;
//		}
//		chater.setMessage("自我介绍已提交");
//		return chater;
//	}	
//	//排位
//	public Chater Rank(String roomId){
//		Session session=sessionFactory.getCurrentSession();
//		session.beginTransaction();
//		Chater chater = new Chater();
//		chater.setOrder("Rank");
//		
//		Room room = (Room)session.createQuery("from Room where roomId=:roomId")
//				.setParameter("roomId", roomId)
//				.uniqueResult();
//		
//		List<RoomUser> ranklist=(List<RoomUser>) session.createSQLQuery("from RoomUser where roomId=:roomId")
//				.setParameter("roomId", roomId);
//		for(int i=0;i<room.getRoomnum();i++){
//			int seat=(int) (Math.random()*room.getRoomnum());
//			for(int j=0;j<i;j++){
//				int seated=ranklist.get(j).getSeat();
//						if(seat==seated){
//							seat=(seat+1)%room.getRoomnum();
//						}
//			}
//			ranklist.get(i).setSeat(seat);
//			chater.setObj(chater.getObj()+(seat+":"+ranklist.get(i).getRoomusername()+'\n'));
//			session.save(ranklist.get(i));
//		}
//		chater.setMessage("rank successed");
//		session.getTransaction().commit();
//		return chater;
//	}

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
		map.put("userId", "agr");
		map.put("roomId", "agrd");
		
		chater.setObject(map);
		
		return chater;
	}
}
