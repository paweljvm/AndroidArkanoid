package self.pingpong.view;

import java.util.Random;

import self.model.datatype.IEnum;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;

public abstract class AbstractDrawableElement implements IDrawableElement {

	protected float x, y, length,incX,incY;
	protected int maxX, maxY;
	protected Paint painter;
	protected volatile boolean ableToRemove, shouldBeDrawn;
	protected Bitmap bitmap;
	protected boolean bonus;
	protected BonusType bonusType;

	public AbstractDrawableElement() {
		painter = new Paint();
		initializePainterAndOthers();
		shouldBeDrawn = true;
		bonus = false;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public AbstractDrawableElement setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
		return this;
	}

	public float getX() {
		return x;
	}

	public AbstractDrawableElement setX(float x) {
		this.x = x;
		return this;
	}

	public float getY() {
		return y;
	}

	public AbstractDrawableElement setY(float y) {
		this.y = y;
		return this;
	}

	public float getLength() {
		return length;
	}

	public float getXPlusLength() {
		return getX() + getLength();
	}

	public float getXMinusLength() {
		return getX() - getLength();
	}

	public float getYPlusLength() {
		return getY() + getLength();
	}

	public float getYMinusLength() {
		return getY() - getLength();
	}

	public int getMaxX() {
		return maxX;
	}

	public AbstractDrawableElement setMaxX(int maxX) {
		this.maxX = maxX;
		return this;
	}

	public int getMaxY() {
		return maxY;
	}

	public AbstractDrawableElement setMaxY(int maxY) {
		this.maxY = maxY;
		return this;
	}

	public AbstractDrawableElement setLength(float length) {
		this.length = length;
		return this;
	}

	protected abstract void initializePainterAndOthers();

	public int getRandomColor() {
		Random random = new Random();
		return Color.rgb(random.nextInt(256), random.nextInt(256),
				random.nextInt(256));
	}

	public boolean isCollision(AbstractDrawableElement element) {
		return element.getYPlusLength() >= this.getY()
				&& ((element.getXMinusLength() >= this.getX() && element
						.getXMinusLength() <= this.getXPlusLength()) || (element
						.getXPlusLength() >= this.getX() && element
						.getXPlusLength() <= this.getXPlusLength()));

	}

	public boolean isAbleToRemove() {
		return ableToRemove;
	}

	public AbstractDrawableElement setAbleToRemove(boolean ableToRemove) {
		this.ableToRemove = ableToRemove;
		return this;
	}

	public boolean isShouldBeDrawn() {
		return shouldBeDrawn;
	}

	public AbstractDrawableElement setShouldBeDrawn(boolean shouldBeDrawn) {
		this.shouldBeDrawn = shouldBeDrawn;
		return this;
	}

	public boolean isBonus() {
		return bonus;
	}

	public AbstractDrawableElement setBonus(boolean bonus) {
		this.bonus = bonus;
		return this;
	}

	public BonusType getBonusType() {
		return bonusType;
	}

	
	
	public float getIncX() {
		return incX;
	}

	public AbstractDrawableElement setIncX(float incX) {
		this.incX = incX;
		return this;
	}

	public float getIncY() {
		return incY;
	}

	public AbstractDrawableElement setIncY(float incY) {
		this.incY = incY;
		return this;
	}

	public AbstractDrawableElement invertIncX() {
		incX*=-1;
		return this;
	}
	public AbstractDrawableElement invertIncY() {
		incY*=-1;
		return this;
	}
	public AbstractDrawableElement updateLocation(float speed) {
		setX(getX()+getIncX()*speed);
		setY(getY()+getIncY()*speed);
		return this;
	}
	public AbstractDrawableElement createCopy() {
		try {
			AbstractDrawableElement element = getClass().newInstance();
			element.setAbleToRemove(isAbleToRemove()).setBitmap(getBitmap())
					.setBonus(isBonus()).setLength(getLength())
					.setMaxX(getMaxX()).setMaxY(getMaxY())
					.setShouldBeDrawn(isShouldBeDrawn()).setX(getX())
					.setY(getY())
					.setIncX(getIncX())
					.setIncY(getIncY());
			return element;
		} catch (Exception e) {
			return null;
		}
	}

	public enum BonusType implements IEnum<Byte> {
		THREE_BALLS((byte) 0), CUT_PLATFORM((byte) 1), EXPAND_PLATFORM((byte) 2),SLOW_DOWN((byte)3),
		SPEED_UP((byte)4),LIVE_ADD((byte)5);
		private byte value;

		private BonusType(byte value) {
			this.value = value;
		}

		@Override
		public Byte getValue() {
			return value;
		}

	}
}
