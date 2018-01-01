package self.pingpong.model;

import java.util.Random;

import self.pingpong.activity.PingPongActivity;
import self.pingpong.activity.R;
import self.pingpong.view.AbstractDrawableElement;
import self.pingpong.view.BonusSquare;
import self.pingpong.view.Square;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;

public class SquareCreator {
	public static SparseArray<Bitmap> squareBitmaps = new SparseArray< Bitmap>();
	public static Bitmap bonusBitmap ;
	
	public static Random randomizer = new Random();
	public static int[] SQUARE_BITMAP_RESOURCES= {
		
				R.drawable.square_blue,
				R.drawable.square_brown,
				R.drawable.square_green,
				R.drawable.square_purple,
				R.drawable.square_red,
				R.drawable.square_yellow,
//				R.drawable.square_gray
		};
	
	public static  AbstractDrawableElement createSquare(int i,int line,int maxX,int maxY,int[] squareResources,Context context) {
		boolean bonus = i % (randomizer.nextInt(i/2+1)+1) == 0 ;
		int resourceId = squareResources[randomizer.nextInt(SQUARE_BITMAP_RESOURCES.length)];
		Bitmap bitmap = squareBitmaps.get(resourceId) ;
		if(bitmap == null) {
			bitmap = BitmapFactory.decodeResource(context.getResources(),resourceId);
			squareBitmaps.append(resourceId, bitmap);
		}
		if(bonusBitmap == null) {
			bonusBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.bonus);
		}
		return (bonus ?
				new BonusSquare() :
				new Square()
				)
				.setLength(PingPongActivity.SQUARE_SIDE)
				.setBitmap(bitmap)
				.setX(i).setY(line*PingPongActivity.SQUARE_SIDE+50).setMaxX(maxX)
				.setMaxY(maxY)
				.setAbleToRemove(resourceId != R.drawable.square_gray);
	}
}
