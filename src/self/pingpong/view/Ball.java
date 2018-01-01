package self.pingpong.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

public class Ball extends AbstractDrawableElement {

	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap,
				null,
				new Rect((int)getXMinusLength(),
						(int)getYMinusLength(),(int)getXPlusLength(), 
						(int)getYPlusLength()),painter);
	}

	@Override
	protected void initializePainterAndOthers() {
		painter.setColor(Color.GREEN);	
	}

	
}
