package self.pingpong.view;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import self.pingpong.activity.R;
import self.pingpong.model.GameData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class GameView extends View{
	private List<AbstractDrawableElement> basicElements;
	private List<AbstractDrawableElement> otherElements;
	private GameData gameData;
	private Paint painter;
	private CountDownLatch latch;
	
	public GameView(Context context,List<AbstractDrawableElement> elements, 
			List<AbstractDrawableElement> otherElements) {
		super(context);
		this.basicElements = elements;
		this.otherElements = otherElements;
		painter = new Paint();
		painter.setColor(Color.GREEN);
		painter.setTextSize(15);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		drawInfoBar(canvas);
		drawElements(canvas, basicElements);
		drawElements(canvas, otherElements);
		latch.countDown();
	}
	private void drawInfoBar(Canvas canvas) {
		Context context = getContext();
		canvas.drawText(context.getString(R.string.level,gameData.getNumber()), 5,30,painter);
		canvas.drawText(context.getString(R.string.points,gameData.getPoints()), 160, 30, painter);
		canvas.drawText(context.getString(R.string.speed,gameData.getSpeed()), 250, 30, painter);
		canvas.drawText(context.getString(R.string.lives, gameData.getLivesCount()), 350,30, painter);
	}
	private void drawElements(Canvas canvas,Iterable<AbstractDrawableElement> elements) {
		for(AbstractDrawableElement element : elements)
			if(element != null && element.isShouldBeDrawn())
				element.draw(canvas);
	}
	public void addOtherElements(AbstractDrawableElement...otherElements) {
		if(basicElements != null) {
			for(AbstractDrawableElement otherElement : otherElements)
				if(otherElement != null)
					basicElements.add(otherElement);
			
		}
	}
	public void setGameData(GameData gameData) {
		this.gameData = gameData;
	}
	public void setBasicElements(List<AbstractDrawableElement> basicElements) {
		this.basicElements = basicElements;
	}
	public void setOtherElements(List<AbstractDrawableElement> otherElements) {
		this.otherElements = otherElements;
	}
	public void setLatch(CountDownLatch latch) {
		this.latch = latch;
	}
	
}
