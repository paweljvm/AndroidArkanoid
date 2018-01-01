package self.pingpong.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;

public class Square extends AbstractDrawableElement{
	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, null,
				new RectF(getX(),getY(),getXPlusLength(),getYPlusLength()),
				painter);
	}

	@Override
	protected void initializePainterAndOthers() {
		painter.setColor(getRandomColor());
	}
	
	
	

	@Override
	public AbstractDrawableElement setAbleToRemove(boolean ableToRemove) {
		if(!ableToRemove)
			painter.setColor(Color.GRAY);
		return super.setAbleToRemove(ableToRemove);
	}

	@Override
	public boolean isCollision(AbstractDrawableElement element) {
		return element.getYMinusLength() <= getYPlusLength() 
				&&
				(	
				( getX()<=element.getXMinusLength() && getXPlusLength() >=element.getXPlusLength())
				||
				(this.getXPlusLength() >= element.getXMinusLength() && 	this.getX() <= element.getXPlusLength())
				||
				(this.getX()> element.getXMinusLength() && this.getXPlusLength()<element.getXPlusLength()));
	}
	
}
