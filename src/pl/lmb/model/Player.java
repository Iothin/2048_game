package pl.lmb.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Player
{
	private SimpleIntegerProperty score;
	private SimpleStringProperty nickname;
	
	public Player(int score, String nickname)
	{
		this.score = new SimpleIntegerProperty(score);
		this.nickname = new SimpleStringProperty(nickname);
	}
	public int getScore()
	{
		return score.get();
	}
	public void setScore(int score)
	{
		this.score.set(score);
	}
	public String getNickname()
	{
		return nickname.get();
	}
	public void setNickname(String nickname)
	{
		this.nickname.set(nickname);
	}
	
	

}
