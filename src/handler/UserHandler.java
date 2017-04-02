	package handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.google.gson.Gson;

import beans.Chater;
import beans.Lab;
import beans.Punishment;
import beans.Room;
import beans.RoomUser;
import beans.WarmGame;
import utils.HibernateUtil;
import utils.LabUtils;

public class UserHandler implements IoHandler {
	private Lab lab= Lab.getLab();
	
	@Override
	public void exceptionCaught(IoSession arg0, Throwable arg1) throws Exception {
		
	}

	@Override
	public void messageReceived(IoSession iossession, Object arg1) throws Exception {

		System.out.println(arg1);

		Gson gson = new Gson();
		Chater chater = gson.fromJson(arg1.toString(), Chater.class);
		
		switch(chater.getOrder()){
		case "create":handleCreate(iossession, chater);
			break;
		case "in":handleIn(iossession, chater);
			break;
		case "rank":handleRank(iossession, chater);
			break;
		case "nickname":handleNickName(iossession, chater);
			break;
		case "ensure_nickname":handleEnsureNickName(iossession, chater);
			break;
		case "punishment":handlePunishment(iossession, chater);
			break;
		case "ensure_punishment":handleEnsurePunishment(iossession, chater);
			break;
		case "talk":handleTalk(iossession,chater);
			break;
		case "warmgame":handleWarmGame(iossession,chater);
			break;
		case "ensure_warmgame":handleEnsureWarmGame(iossession,chater);
			break;
		default:
			break;
		}
	}
	
	private void handleEnsureWarmGame(IoSession iossession, Chater chater) {
		String warmgameId;
		Map<String, Object> object = new HashMap<>();
		object = (Map<String, Object>) chater.getObject();
		warmgameId=(String) object.get("warmgameId");

		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session=sessionFactory.getCurrentSession();
		session.beginTransaction();
		WarmGame warmgame = (WarmGame) session.createQuery("from WarmGame where WarmGameId=:WarmGameId")
				.setParameter("WarmGameId", warmgameId)
				.uniqueResult();
		session.getTransaction().commit();
		
		Map<String, Object> object2 = new HashMap<>();
		object2.put("warmgame", LabUtils.FindRoomUser(chater.getRoomId(), chater.getUserId()).getNickname()+":"+warmgame.getWarmGame());
		Chater chater2 = new Chater();
		chater2.setObject(object2);
		chater2.setMessage("SUCCEED");
		chater2.setOrder("ensure_warmgame");
		chater2.setRoomId(chater.getRoomId());
		SendAll(chater2, chater.getRoomId());
		
	}

	private void handleWarmGame(IoSession iossession, Chater chater) {
		String roomId = chater.getRoomId();
		Map<String, Object> obj = new HashMap<>();
		obj = (Map<String, Object>) chater.getObject();
		String level = (String) obj.get("warmgameLevel");

		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session=sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<WarmGame> WarmGamelist = session.createQuery("from WarmGame where WarmGameLevel=:WarmGameLevel")
				.setParameter("WarmGameLevel", level).list();
		session.getTransaction().commit();
		// 设定返回值
		Chater chater2 = new Chater();
		chater2.setOrder("warmgame");
		chater2.setRoomId(roomId);
		Map<String, Object> object = new HashMap<>();
		object.put("size", WarmGamelist.size());
		object.put("list", new Gson().toJson(WarmGamelist));
		chater2.setUserId(chater.getUserId());
		chater2.setObject(object);
		chater2.setMessage("SUCCEED");

		SendAll(chater2,chater.getRoomId());

	}

	private void handleTalk(IoSession iossession, Chater chater) {
		
		SendAll(chater, chater.getRoomId());
		
	}

	private void handleEnsurePunishment(IoSession iossession, Chater chater) {
		String punishmentId;
		Map<String, Object> object = new HashMap<>();
		object = (Map<String, Object>) chater.getObject();
		punishmentId=(String) object.get("punishmentId");

		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session=sessionFactory.getCurrentSession();
		session.beginTransaction();
		Punishment punishment = (Punishment) session.createQuery("from Punishment where punishmentId=:punishmentId")
				.setParameter("punishmentId", punishmentId)
				.uniqueResult();
		session.getTransaction().commit();
		System.out.println(punishment.getPunishment());
		Map<String, Object> object2 = new HashMap<>();
		if(LabUtils.FindRoomUser(chater.getRoomId(),chater.getUserId())==null){
			System.out.println(123);	
		}
		String nickname = LabUtils.FindRoomUser(chater.getRoomId(),chater.getUserId()).getNickname();
		System.out.println(nickname);
		if(nickname!=null&&nickname.equals("")){
			object2.put("punishment", nickname+":"+punishment.getPunishment());	
		}
		else{
			object2.put("punishment", chater.getUserId()+":"+punishment.getPunishment());
		}
		
		Chater chater2 = new Chater();
		chater2.setObject(object2);
		chater2.setMessage("SUCCEED");
		chater2.setOrder("ensure_punishment");
		chater2.setRoomId(chater.getRoomId());
		SendAll(chater2, chater.getRoomId());
		
	}

	private void handlePunishment(IoSession iossession, Chater chater) {
		// 房主以广播的方式将惩罚信息发送给全部房员

		// 房间号通过chater.roomId传送
		String roomId = chater.getRoomId();
		Map<String, Object> obj = new HashMap<>();
		obj = (Map<String, Object>) chater.getObject();
		String level = (String) obj.get("punishmentLevel");

		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session=sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<Punishment> punishmentlist = session.createQuery("from Punishment where punishmentLevel=:punishmentLevel")
				.setParameter("punishmentLevel", level).list();
		session.getTransaction().commit();
		// 设定返回值
		Chater chater2 = new Chater();
		chater2.setOrder("punishment");
		chater2.setRoomId(roomId);
		Map<String, Object> object = new HashMap<>();
		object.put("size", String.valueOf(punishmentlist.size()));
		object.put("list", new Gson().toJson(punishmentlist));
		chater2.setObject(object);
		chater2.setMessage("SUCCEED");

		SendSingle(chater2, iossession);
	}

	private void handleRank(IoSession iossession, Chater chater) {
		
		int num=LabUtils.FindRoom(chater.getRoomId()).getRoomnum();
		System.out.println(num+"人数");
		Map<Integer,RoomUser> roomusermap=new HashMap<>();
		List<Integer> ranklist=new ArrayList<>();
		System.out.println("1");
		//绑定
		for(int i=0;i<num;i++){
			int seat=(int) (Math.random()*1000);
			ranklist.add(seat);
			roomusermap.put(seat, LabUtils.FindRoom(chater.getRoomId()).getUserlist().get(i));
		}
		System.out.println("2");
		//排序
		for(int i=0;i<ranklist.size();i++){
			for(int j=0;j<i;j++){
				if(ranklist.get(i)>ranklist.get(j)){
					int temp=ranklist.get(i);
					ranklist.set(i, ranklist.get(j));
					ranklist.set(j, temp);
				}
			}
		}
		System.out.println("3");
		//进行编号
		String rankinformation=1+":"+roomusermap.get(ranklist.get(0)).getNickname()+'\n';
		
		for(int i=1;i<num;i++){
			String nickname = roomusermap.get(ranklist.get(i)).getNickname();
			if(nickname!=null&&!nickname.equals("")){
				rankinformation=rankinformation+(i+1)+":"+roomusermap.get(ranklist.get(i)).getNickname()+'\n';	
			}
			else{
				rankinformation=rankinformation+(i+1)+":"+roomusermap.get(ranklist.get(i)).getUserId()+'\n';
			}
			roomusermap.get(ranklist.get(i)).setSeat(i);
		}
		System.out.println("set");
		Map<String,String> object = new HashMap<>();
		object.put("rank", rankinformation);
		
		Chater chater2 = new Chater();
		chater2.setMessage("SUCCEED");
		chater2.setOrder("rank");
		chater2.setUserId(chater.getUserId());
		chater2.setRoomId(chater.getRoomId());
		chater2.setObject(object);
		
		SendAll(chater2, chater.getRoomId());
	}

	private void handleEnsureNickName(IoSession iossession, Chater chater) {
		//自我介绍内容通过chater.object传送
		Map<String,Object> obj = new HashMap<>();
		obj=(Map<String, Object>) chater.getObject();
		String NickName=(String) obj.get("NickName");
		Chater chater2=new Chater();
		System.out.println("1");
		LabUtils.FindRoomUser(chater.getRoomId(),chater.getUserId()).setNickname(NickName);
		chater2.setMessage("SUCCEED");
		
		//判断是否全部人的昵称都已接收
		long time=System.currentTimeMillis();
		List<RoomUser> list=new ArrayList<>();
		Room room = LabUtils.FindRoom(chater.getRoomId());
		list=room.getUserlist();
		while(list==null||(time-System.currentTimeMillis())>300000){
			int n=list.size();
			for(int i=0;i<n;i++){
				if(list.get(i).getNickname()!=null){
					list.remove(list.get(i));
				}
			}
		}
		String message=null;
		for(int j=0;j<room.getRoomnum();j++){
			message=message+j+1+":"+room.getUserlist().get(j).getNickname()+'\n';	
		}
		Map<String,String> object=new HashMap<>();
		object.put("NickName", message);
		chater2.setObject(object);
		chater2.setOrder("ensure_nickname");
		chater2.setUserId(room.getHostId());
		chater2.setRoomId(room.getRoomId());
		SendAll(chater2, chater.getRoomId());
	}

	private void handleNickName(IoSession iossession, Chater chater) {
		//房主发送自我介绍指令，全部房员进行自我介绍
		
		//房间号通过chater.roomId传送
		String roomId=chater.getRoomId();
		//设定返回值
		Chater chater2= new Chater();
		chater2.setOrder("NickName");
		chater2.setRoomId(roomId);
		chater2.setUserId(chater.getUserId());
		chater2.setMessage("SUCCEED");
		SendAll(chater2,roomId);		
	}
	
	private void handleIn(IoSession iossession, Chater chater) {
		//先设定该房员属性
		RoomUser roomuser = new RoomUser();
		roomuser.setSession(iossession);
		roomuser.setUserId(chater.getUserId());
		//找到此房间，并将此房员加入列表
		//设定返回的chater2		
		Chater chater2= new Chater();
		//此时利用chater中的obj传送roomId
		Map<String,Object> obj = new HashMap<>();
		obj=(Map<String, Object>)chater.getObject();
		String roomId=(String) obj.get("roomId");
		System.out.println(roomId+"房间号");
		Room room =LabUtils.FindRoom(roomId);
//		System.out.println(LabUtils.FindRoom(roomId).getUserlist().size()+"之前");

		Chater chater3=new Chater();
		chater3.setMessage(chater.getUserId()+"已经进入房间");
		chater3.setOrder("talk_in");
		chater3.setUserId(chater.getUserId());
		chater3.setRoomId(roomId);

		if(room==null){
			System.out.println("NO Room");
			chater2.setMessage("No Room");
		}
		else{
			if(LabUtils.FindRoomUser(roomId, chater.getUserId())!=null){
				chater2.setMessage("Already In");
			}
			//无错误
			chater2.setMessage("SUCCEED");
			room.setUserlist(roomuser);
			room.setRoomnum(room.getRoomnum()+1);
			System.out.println(room.getRoomnum());
			
			SendAll(chater3,roomId);
		}
		Map<String,Object> object=new HashMap<>();
		object.put("roomName", room.getRoomname());
		chater2.setObject(object);
		chater2.setOrder("in");
		chater2.setRoomId(roomId);
		chater2.setUserId(chater.getUserId());
		
		SendSingle(chater2,iossession);
		SendSingle(chater3,iossession);
	}

	private void handleCreate(IoSession iossession, Chater chater) {
		//此时房间名通过查Chater的obj传送
		
		//创建房间时是否要检查其在其他房间（多次创建）
		
		Map<String,Object> obj = new HashMap<>();
		obj=(Map<String, Object>)chater.getObject();
		String roomname=(String) obj.get("roomName");
		RoomUser roomuser = new RoomUser();
		Room room = new Room();
		
		String suf= String.valueOf((int)(Math.random()*1000));//通过当前时间产生一个随机数作为后缀
		SessionFactory sessionFactory =  HibernateUtil.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		int pre = (int) session.createQuery("select id from User where userId=:userId")
				.setParameter("userId", chater.getUserId())
				.uniqueResult();
		
		session.getTransaction().commit();
		
		//设定room的属性
		room.setRoomname(roomname);
		System.out.println(roomname+"设置");
		room.setRoomId(pre+suf);
		Date date = new Date();
		room.setStarttime(date);
		room.setRoomnum(1);
		room.setHostId(chater.getUserId());
		
		//设定创建人属性
		roomuser.setSession(iossession);
		roomuser.setUserId(chater.getUserId());
		
		room.setUserlist(roomuser);
		lab.setList(room);
		
		//设定返回的chater2
		Chater chater2= new Chater();
		chater2.setObject(obj);
		chater2.setRoomId(room.getRoomId());
		chater2.setUserId(chater.getUserId());
		chater2.setMessage("SUCCEED");
		chater2.setOrder("create");
		SendSingle(chater2,iossession);
	}

	private void SendSingle(Chater chater,IoSession iossession){
		String msg = new Gson().toJson(chater);
		iossession.write(msg);
	}
	
	private void SendAll(Chater chater,String roomId){
		String msg = new Gson().toJson(chater);
		System.out.println("SendAll");
		Room room=LabUtils.FindRoom(roomId);
		for(int j=0;j<room.getRoomnum();j++){
				System.out.println(j);
				room.getUserlist().get(j).getSession().write(msg);
		}		
	}
	
	private boolean CheckHost(String roomId,String userId){
		if(LabUtils.FindRoom(roomId).getHostId().equals(userId)){
			return true;
		}
		return false;
	}
	
	@Override
	public void messageSent(IoSession arg0, Object arg1) throws Exception {
		System.out.println(arg1+"sent");
	}

	@Override
	public void sessionClosed(IoSession arg0) throws Exception {

	}

	@Override
	public void sessionCreated(IoSession arg0) throws Exception {
		System.out.println("faieo");
	}

	@Override
	public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void sessionOpened(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	
}