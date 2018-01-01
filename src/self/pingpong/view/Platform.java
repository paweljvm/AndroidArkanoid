package self.pingpong.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

public class Platform extends AbstractDrawableElement{
	private Bitmap platformBitmap;
	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap,null,
			   new Rect((int)getX(), (int)getY(),(int)getXPlusLength(), (int)getY()+30),
				
				painter);
	}

	@Override
	protected void initializePainterAndOthers() {
		painter.setColor(Color.CYAN);

	}

	@Override
	public AbstractDrawableElement setLength(float length) {
		return super.setLength(length < 30 ? 30 : length>100 ? 100 : length);
	}
	
	
}
