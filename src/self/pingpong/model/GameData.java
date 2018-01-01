package self.pingpong.model;

import java.io.Serializable;

public class GameData implements Serializable {
	private static final long serialVersionUID = -6397575479480857126L;
	private float speed,accelerate,maxSpeed;
	private byte number,bonusCount,linesCount,livesCount,pointRatio;
	private int points;
	public float getSpeed() {
		return speed;
	}
	public GameData setSpeed(float speed) {
		
		if(speed > maxSpeed)
			speed = maxSpeed;
		if(speed < 0)
			speed = 1;
		this.speed = speed;
		return this;
	}
	public float getAccelerate() {
		return accelerate;
	}
	public GameData setAccelerate(float accelerate) {
		this.accelerate = accelerate;
		return this;
	}
	public float getMaxSpeed() {
		return maxSpeed;
	}
	public GameData setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
		return this;
	}
	public byte getNumber() {
		return number;
	}
	public GameData setNumber(byte number) {
		this.number = number;
		return this;
	}
	public byte getBonusCount() {
		return bonusCount;
	}
	public GameData setBonusCount(byte bonusCount) {
		this.bonusCount = bonusCount;
		return this;
	}
	public byte getLinesCount() {
		return linesCount;
	}
	public GameData setLinesCount(byte linesCount) {
		this.linesCount = linesCount;
		return this;
	}
	public byte getLivesCount() {
		return livesCount;
	}
	public GameData setLivesCount(byte livesCount) {
		this.livesCount = livesCount;
		if(this.livesCount < 0)
			this.livesCount=0;
		return this;
	}
	public static GameData firstLevel() {
		return new GameData()
		.setLivesCount((byte)3)
		.setAccelerate(0.01f)
		.setMaxSpeed(3.0f)
		.setNumber((byte)1)
		.setSpeed(2.0f)
		.setPointRatio((byte)1)
		.setLinesCount((byte)1);
	
	}
	public static GameData getNextLevel(GameData currentGameData) {
		return currentGameData.setAccelerate(currentGameData.getAccelerate()*2)
			   .setMaxSpeed(currentGameData.getMaxSpeed()+.2f)
			   .setNumber((byte)  (currentGameData.getNumber()+1))
			   .setSpeed(currentGameData.getNumber())
			   .setPointRatio((byte) (currentGameData.getPointRatio()*2))
			   .setLinesCount((byte) ( currentGameData.getLinesCount()+1));
		
		
	}
	public void accelerateVeryMuch() {
		setSpeed(speed*1.2f);
	}
	
	public void accelerate() {
		setSpeed(speed+accelerate);
	}
	public void slowDownVeryMuch() {
		setSpeed(speed/1.2f);
	}
	public byte getPointRatio() {
		return pointRatio;
	}
	public GameData setPointRatio(byte pointRatio) {
		this.pointRatio = pointRatio;
		return this;
	}
	public Integer getPoints() {
		return points;
	}
	public GameData setPoints(Integer points) {
		this.points = points;
		return this;
	}
	public void addPoints() {
		points+=pointRatio;
	}
	
}
