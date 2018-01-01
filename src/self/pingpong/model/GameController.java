package self.pingpong.model;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import self.pingpong.activity.PingPongActivity;
import self.pingpong.activity.R;
import self.pingpong.view.AbstractDrawableElement;
import self.pingpong.view.GameView;
import android.app.Activity;

public class GameController implements Runnable,IStartListener {
	private GameView gameView;
	private int maxX, maxY, startY;
	private volatile boolean stop,start;
	private AbstractDrawableElement ball, platform,bonus,ballFirstCopy,ballSecondCopy;
	private List<AbstractDrawableElement> squares;
	private GameData gameData;
	private Activity activity;
	private Random randomizer;
	
	public GameController(AbstractDrawableElement ball,
			AbstractDrawableElement platform,
			List<AbstractDrawableElement> squares, GameView view,Activity context) {
		this.ball = ball;
		this.platform = platform;
		this.gameView = view;
		this.squares = squares;
		this.activity = context;
		this.randomizer = new Random();
		ball.setIncX(.5f).setIncY(1.0f);
		
	}

	@Override
	public void run() {
		while (!stop) {
			CountDownLatch latch = new CountDownLatch(1);
			gameView.setLatch(latch);
			gameView.postInvalidate();
			try {
				latch.await();
			} catch(Exception e) {
				return;
			}
			courseOfGameLoop();
		}

	}

	private void sleep(long timeMilis) {
		try {
			Thread.sleep(timeMilis);
		} catch (Exception e) {
			return;
		}
	}

	private void courseOfGameLoop() {
		if(!start)
			return;
		handleCollisonsAndLocationChanges(ball);
		if(notNullAndShouldBeDrawn(ballFirstCopy))
			handleCollisonsAndLocationChanges(ballFirstCopy);
		if(notNullAndShouldBeDrawn(ballSecondCopy))
			handleCollisonsAndLocationChanges(ballSecondCopy);
		handleBonusActions();
	}
	
	private void handleCollisonsAndLocationChanges(AbstractDrawableElement ball) {
		updateBallLocation(ball);
		handleCollisions(ball);
		handleGameStateChanges(ball);
		correctBallLocationIfNecessary(ball);
	}
	
	private void handleGameStateChanges(AbstractDrawableElement ball) {
		if(isBallLost(ball)) {
			handleBallLost(ball);
		}
		if(isGameOver()) {
			handleGameOver();
		}
		if(isLevelFinished()) {
			handleLevelFinished(ball);
		}
	}
	
	private void handleBallLost(AbstractDrawableElement ball) {
		if(ball == this.ball) {
			if(notNullAndShouldBeDrawn(ballFirstCopy)) {
				ball.setShouldBeDrawn(false);
				this.ball = ballFirstCopy.createCopy();
				gameView.addOtherElements(this.ball);
				ballFirstCopy.setShouldBeDrawn(false);
			} else if(notNullAndShouldBeDrawn(ballSecondCopy)) {
				ball.setShouldBeDrawn(false);
				this.ball = ballSecondCopy.createCopy();
				gameView.addOtherElements(this.ball);
				ballSecondCopy.setShouldBeDrawn(false);
			} else {
				gameData.setLivesCount((byte)(gameData.getLivesCount()-1));
				ball.setY(ball.getY()-100);
				ball.setIncY(-1);
				start =false;
			}
		} else
			ball.setShouldBeDrawn(false);
	}
	
	private void handleCollisions(AbstractDrawableElement ball) {
		if (isWallXCollision(ball)) {
			handleWallXCollision(ball);
		}
		if (isWallYCollision(ball)) {
			handleWallYCollision(ball);
		}
		if(isPlatformCollision(ball)) {
 			handlePlatformCollision(ball);
		}
		if (checkForSquareCollisionAndRemoveIfNecessary(ball)) {
			handleSquareCollision(ball);
		}
	}
	private void handleBonusActions() {
		if(notNullAndShouldBeDrawn(bonus)) {
			bonus.setY(bonus.getY()+gameData.getSpeed()/2);
			if(platform.isCollision(bonus))
				handleBonusCatched();
			if(bonus.getY()> platform.getY())
				handleBonusMissed();
		}
	}
	
	private boolean notNullAndShouldBeDrawn(AbstractDrawableElement element) {
		return element != null && element.isShouldBeDrawn();
	}
	
	
	private void correctBallLocationIfNecessary(AbstractDrawableElement ball) {
		if(ball.getXMinusLength() <0)
			ball.setX(ball.getLength());
		if(ball.getXPlusLength()>maxX)
			ball.setX(maxX-ball.getLength());
	}

	private void handleSquareCollision(AbstractDrawableElement ball) {
		ball.invertIncY();
		gameData.accelerate();
		
	}

	private void handlePlatformCollision(AbstractDrawableElement ball) {
		float quarterOfPlatform =platform.getLength()/4,signX = ball.getX()>= (platform.getX()+ 2* quarterOfPlatform) ? 1 : -1;
		ball.setIncX(Math.min(Math.abs(ball.getX() - (platform.getX()+2*quarterOfPlatform))/(2*quarterOfPlatform),1.0f) * signX);
		ball.setIncY(-1);
		gameData.accelerate();
		
	}

	private void updateBallLocation(AbstractDrawableElement ball) {
		ball.updateLocation(gameData.getSpeed());
	}
	
	private boolean isLevelFinished() {
		for(AbstractDrawableElement square : squares) {
			if(square.isShouldBeDrawn() && square.isAbleToRemove())
				return false;
		}
		return true;
	}

	private boolean isWallXCollision(AbstractDrawableElement ball) {
		return ball.getXPlusLength() >= maxX || ball.getXMinusLength() <= 0;
	}

	private boolean isWallYCollision(AbstractDrawableElement ball) {
		return ball.getYMinusLength()<=startY;
	}

	private boolean isPlatformCollision(AbstractDrawableElement ball) {
		return platform.isCollision(ball);
	}
	private boolean isGameOver() {
		return gameData.getLivesCount() == 0;
	}
	private boolean isBallLost(AbstractDrawableElement ball) {
		return ball.isShouldBeDrawn() && !isPlatformCollision(ball) && 
				ball.getY() > platform.getY();
	}
	private boolean checkForSquareCollisionAndRemoveIfNecessary(AbstractDrawableElement ball) {
		for (int i = ball.getIncX() > 0 ? (squares.size()-1) : 0; ball.getIncX() > 0 ? i >= 0
				: i < squares.size(); i = ball.getIncX() > 0 ? (i - 1) : (i + 1))
			if (checkForSquareCollisionAndRemoveIfNecessary(i,ball)) {
				return true;
			}
		return false;
	}

	private boolean checkForSquareCollisionAndRemoveIfNecessary(int index,AbstractDrawableElement ball) {
		AbstractDrawableElement square = squares.get(index);
		if (square.isShouldBeDrawn() && square.isCollision(ball)) {
			if(isCollisionOnXForSquare(square,ball))
				ball.invertIncX();
			boolean shouldBeDrawn = !square.isAbleToRemove();
			if(!shouldBeDrawn)
				gameData.addPoints();
			square.setShouldBeDrawn(shouldBeDrawn);
			if((bonus == null || !bonus.isShouldBeDrawn()) &&  square.isBonus()) {
				handleBonusAction(square);
			}
			return true;
		}
		return false;
	}
	private void handleWallXCollision(AbstractDrawableElement ball) {
		ball.invertIncX();
		gameData.accelerate();
	}
	private void handleWallYCollision(AbstractDrawableElement ball) {
		ball.invertIncY();
		gameData.accelerate();
	}
	private void handleGameOver() {
		stop = true;
		((PingPongActivity)activity).showHighScoresDialog(gameData);
	}
	private void handleLevelFinished(AbstractDrawableElement ball) {
		gameData = GameData.getNextLevel(gameData);
		for(AbstractDrawableElement square : squares) {
			int squareResource = SquareCreator.SQUARE_BITMAP_RESOURCES[randomizer.nextInt(6)];
			square.setShouldBeDrawn(true);
			square.setAbleToRemove(squareResource != R.drawable.square_gray);
			square.setBitmap(SquareCreator.squareBitmaps.get(squareResource));
		}
		for(int i =0;i<maxX;i+=PingPongActivity.SQUARE_SIDE) {
			squares.add(SquareCreator.createSquare(i, gameData.getLinesCount()-1,maxX,maxY, SquareCreator.SQUARE_BITMAP_RESOURCES,activity));
		}
		setUpBallForNextLevel(this.ball);
		if(notNullAndShouldBeDrawn(ballFirstCopy))
			setUpBallForNextLevel(ballFirstCopy);
		if(notNullAndShouldBeDrawn(ballSecondCopy))
			setUpBallForNextLevel(ballSecondCopy);
		start=false;
	}
	private void setUpBallForNextLevel(AbstractDrawableElement ball) {
		ball.setY(maxY/2).setIncY(-1);
	}
	private void handleBonusAction(AbstractDrawableElement bonus) {
		this.bonus = bonus.createCopy();
		this.bonus.setShouldBeDrawn(true);
		this.bonus.setBitmap(SquareCreator.bonusBitmap);
		gameView.addOtherElements(this.bonus);
	}
	private void handleBonusCatched() {
		switch(bonus.getBonusType()) {
		case CUT_PLATFORM:
			platform.setLength(platform.getLength()/2);
			break;
		case EXPAND_PLATFORM:
			platform.setLength(platform.getLength()*2);
			gameData.addPoints();
			break;
		case THREE_BALLS:
			gameData.addPoints();
			if(!notNullAndShouldBeDrawn(ballFirstCopy)) {
				ballFirstCopy = ball.createCopy();
				ballFirstCopy.invertIncY();
			}
			if(!notNullAndShouldBeDrawn(ballSecondCopy)) {
				ballSecondCopy = ball.createCopy();
				ballSecondCopy.invertIncX();
			}
			gameView.addOtherElements(ballFirstCopy,ballSecondCopy);
			break;
		case SLOW_DOWN:
			gameData.slowDownVeryMuch();
			break;
		case SPEED_UP:
			gameData.accelerateVeryMuch();
			break;
		case LIVE_ADD:
			gameData.setLivesCount((byte) (gameData.getLivesCount()+1));
		default:
			break;
		}
		bonus.setShouldBeDrawn(false);
	}
	private void handleBonusMissed() {
		bonus.setShouldBeDrawn(false);
	}
	private boolean isCollisionOnXForSquare(AbstractDrawableElement square,AbstractDrawableElement ball) {
		return ball.getXPlusLength()>=square.getX() || ball.getXMinusLength()<=square.getXPlusLength();
	}

	@Override
	public void onStart() {
		start= true;		
	}
	

	public int getMaxX() {
		return maxX;
	}

	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartY(int startY) {
		this.startY = startY;
	}

	
	
	public void setStart(boolean start) {
		this.start = start;
	}

	public GameData getCurrentLevel() {
		return gameData;
	}

	public void setCurrentLevel(GameData currentLevel) {
		this.gameData = currentLevel;
	}
}
