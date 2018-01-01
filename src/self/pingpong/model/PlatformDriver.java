package self.pingpong.model;

import self.pingpong.view.AbstractDrawableElement;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class PlatformDriver implements OnTouchListener {
	
	private AbstractDrawableElement platform;
	private int activePointer;
	private IStartListener listener;
	private short maxX;
	public PlatformDriver(AbstractDrawableElement platform,IStartListener listener,short maxX) {
		super();
		this.platform = platform;
		this.listener = listener;
		this.maxX = maxX;
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = MotionEventCompat.getActionMasked(event);
		
		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			final int pointerIndex = MotionEventCompat.getActionIndex(event);
			activePointer = MotionEventCompat.getPointerId(event, pointerIndex);
			if(event.getDownTime() >=3000)
				listener.onStart();
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			final int pointerIndex = (int) MotionEventCompat.findPointerIndex(event,
					activePointer);
			int x = (int) MotionEventCompat.getX(event, pointerIndex);
			if(x < 0 )
				x = 10;
			if(x > maxX)
				x=(int) (maxX-platform.getLength());
			platform.setX(x);
			break;
		}
		
		}
		return true;
	}

}
