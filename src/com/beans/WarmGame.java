package com.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="WarmGame")
public class WarmGame {
	private String warmGameName;
	private String warmGameLevel;
	private String warmGameId;
	private String warmGame;
	private String warmGameUrl;
	private int id;
	
	public String getWarmGameName() {
		return warmGameName;
	}
	public void setWarmGameName(String warmGameName) {
		warmGameName = warmGameName;
	}
	public String getWarmGameLevel() {
		return warmGameLevel;
	}
	public void setWarmGameLevel(String warmGameLevel) {
		warmGameLevel = warmGameLevel;
	}
	public String getWarmGameId() {
		return warmGameId;
	}
	public void setWarmGameId(String warmGameId) {
		warmGameId = warmGameId;
	}
	public String getWarmGame() {
		return warmGame;
	}
	public void setWarmGame(String warmGame) {
		warmGame = warmGame;
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
