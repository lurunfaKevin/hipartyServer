package beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="WarmGame")
public class WarmGame {
	private String WarmGameName;
	private String WarmGameLevel;
	private String WarmGameId;
	private String WarmGame;
	private int id;
	
	public String getWarmGameName() {
		return WarmGameName;
	}
	public void setWarmGameName(String warmGameName) {
		WarmGameName = warmGameName;
	}
	public String getWarmGameLevel() {
		return WarmGameLevel;
	}
	public void setWarmGameLevel(String warmGameLevel) {
		WarmGameLevel = warmGameLevel;
	}
	public String getWarmGameId() {
		return WarmGameId;
	}
	public void setWarmGameId(String warmGameId) {
		WarmGameId = warmGameId;
	}
	public String getWarmGame() {
		return WarmGame;
	}
	public void setWarmGame(String warmGame) {
		WarmGame = warmGame;
	}
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
