package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.Table;

//@Entity
//@Table(name="room")
public class Room {
	
	private String roomname;
	private String roomId;
	private Date starttime;//创建时间
	private int	roomnum;//房间人数
	private String hostId;//主持人的userId
	private List<RoomUser> userlist= new ArrayList<>();
	
//	private String roomstatus;
//	private int id;

	public Room() {
		super();
	}
	public String getHostId() {
		return hostId;
	}
	public void setHostId(String hostId) {
		this.hostId = hostId;
	}
	public List<RoomUser> getUserlist() {
		return userlist;
	}
	public void setUserlist(RoomUser roomuser) {
		this.userlist.add(roomuser);
	}
	public String getRoomname() {
		return roomname;
	}
	public void setRoomname(String roomname) {
		this.roomname = roomname;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	
//	@Id
//	@GeneratedValue
//	public int getId() {
//		return id;
//	}
//	public void setId(int id) {
//		this.id = id;
//	}
	public Date getStarttime() {
		return starttime;
	}
	public void setStarttime(Date starttime) {
		
		this.starttime = starttime;
	}
	public int getRoomnum() {
		return roomnum;
	}
	public void setRoomnum(int roomnum) {
		this.roomnum = roomnum;
	}
//	public String getRoomstatus() {
//		return roomstatus;
//	}
//	public void setRoomstatus(String roomstatus) {
//		this.roomstatus = roomstatus;
//	}
	
}
