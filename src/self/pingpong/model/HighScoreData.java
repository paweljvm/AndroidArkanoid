package self.pingpong.model;

import pl.pcchack.activity.IDescriptable;
import pl.pcchack.db.IEntity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="high_score")
public class HighScoreData implements IEntity ,IDescriptable{
	private static final long serialVersionUID = 1L;
	public static final String P_POINTS="points";
	@DatabaseField(columnName="id",generatedId=true)
	private Long id;
	@DatabaseField(columnName="name")
	private String name;
	@DatabaseField(columnName="points")
	private Integer points;
	@DatabaseField(columnName="speed")
	private Float speed;
	@DatabaseField(columnName="level")
	private Byte level;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getPoints() {
		return points;
	}
	public void setPoints(Integer points) {
		this.points = points;
	}
	public Float getSpeed() {
		return speed;
	}
	public void setSpeed(Float speed) {
		this.speed = speed;
	}
	public Byte getLevel() {
		return level;
	}
	public void setLevel(Byte level) {
		this.level = level;
	}
	@Override
	public String getDescription() {
		return String.format("Points: %d\t   Speed: %.2f\t    Level:%d ", points,speed,level);
	}
	@Override
	public Integer getSrc() {
		return android.R.drawable.star_on;
	}
	
	
}
