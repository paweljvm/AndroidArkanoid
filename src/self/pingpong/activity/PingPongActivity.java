package self.pingpong.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.pcchack.activity.AbstractActivity;
import pl.pcchack.db.AndroidDao;
import self.pingpong.model.GameController;
import self.pingpong.model.GameData;
import self.pingpong.model.HighScoreData;
import self.pingpong.model.PlatformDriver;
import self.pingpong.model.SquareCreator;
import self.pingpong.view.AbstractDrawableElement;
import self.pingpong.view.Ball;
import self.pingpong.view.GameView;
import self.pingpong.view.Platform;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;

public class PingPongActivity extends AbstractActivity {

	public static final int SQUARE_SIDE = 40;
	public static final String CURRENT_LEVEL = "currentLevel",
			DB_HIGH_SCORES = "high_scores.db";

	private AbstractDrawableElement ball, platform;
	private List<AbstractDrawableElement> squares;
	private GameController gameController;
	private GameView gameView;
	private Thread controllerThread;

	private DisplayMetrics metrics;
	private TypedValue typedValue;
	private int startY;
	private volatile GameData gameData;
	private AndroidDao dao;

	@Override
	protected void initFieldsAndServices(Bundle savedState) {
		readGameData(savedState);
		readMetrics();
		createGameElements();
		createGameView();
		createGameModel();

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(CURRENT_LEVEL, gameData);
	}

	private void readGameData(Bundle savedState) {
		gameData = (GameData) (savedState != null ? savedState
				.getSerializable(CURRENT_LEVEL) : null);
		if (gameData == null)
			gameData = (GameData) getIntent().getSerializableExtra(
					CURRENT_LEVEL);
		if (gameData == null)
			gameData = GameData.firstLevel();
	}

	private void readMetrics() {
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		typedValue = new TypedValue();
		getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue,
				true);
		startY = getResources().getDimensionPixelSize(typedValue.resourceId);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(gameView);
		startGame();
	}

	private void startGame() {
		if (controllerThread != null && controllerThread.isAlive())
			controllerThread.interrupt();
		controllerThread = new Thread(gameController);
		controllerThread.start();
	}

	private void createGameElements() {
		ball = new Ball().setBitmap(getBitmap(R.drawable.better_ball))
				.setLength(20).setX(200).setY(200);
		platform = new Platform()
				.setBitmap(getBitmap(R.drawable.better_platform2))
				.setLength(100).setX(100).setY(isPortrait() ? 720 : 400);
		createSquareList();
	}

	private void createGameModel() {
		gameController = new GameController(ball, platform, squares, gameView,
				this);
		gameController.setMaxX(metrics.widthPixels);
		gameController.setMaxY(metrics.heightPixels);
		gameController.setCurrentLevel(gameData);
		gameController.setStartY(startY);
		gameView.setOnTouchListener(new PlatformDriver(platform,
				gameController, (short) gameController.getMaxX()));
		gameView.setGameData(gameData);

	}

	private void createGameView() {
		gameView = new GameView(this, new ArrayList<AbstractDrawableElement>(
				Arrays.asList(ball, platform)), squares);
	}

	private void createSquareList() {
		squares = new ArrayList<AbstractDrawableElement>();
		for (int line = 0; line < gameData.getLinesCount(); line++) {
			for (int i = 0; i < metrics.widthPixels; i += SQUARE_SIDE) {
				squares.add(SquareCreator.createSquare(i, line,
						metrics.widthPixels, metrics.heightPixels,
						SquareCreator.SQUARE_BITMAP_RESOURCES, this));
			}

		}
	}

	public void showHighScoresDialog(final GameData gameData) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				final View view = getLayoutInflater().inflate(
						R.layout.high_scores_dialog, null);
				final EditText nameEdit = (EditText) view.findViewById(R.id.name);
				AlertDialog dialog = new AlertDialog.Builder(PingPongActivity.this,AlertDialog.THEME_HOLO_DARK)
						.setView(view)
						.setCancelable(false)
						.setIcon(R.drawable.ball_icon)
						.setTitle(R.string.high_scores_dialog_title)
						.setPositiveButton(R.string.g_accept,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (dao == null) {
											dao = new AndroidDao(PingPongActivity.this,
													DB_HIGH_SCORES, HighScoreData.class);
										}
										final HighScoreData entity = new HighScoreData();
										entity.setLevel(gameData.getNumber());
										entity.setName(nameEdit.getText().toString());
										entity.setPoints(gameData.getPoints());
										entity.setSpeed(gameData.getSpeed());
										dao.save(entity);
										dialog.cancel();
										PingPongActivity.this.finish();
									}
								})
						.setNegativeButton(R.string.g_cancel,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
										PingPongActivity.this.finish();
									}
								}).create();
				dialog.show();
				
			}
		});
		

	}

	@Override
	protected int getMainViewId() {
		return android.R.layout.list_content;
	}

	@Override
	protected void saveDataInPreferences() {

	}

	@Override
	protected boolean isKeepScreenOn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		killGameControllerThread();
	}

	@Override
	protected void onStop() {
		super.onStop();
		killGameControllerThread();
	}

	private void killGameControllerThread() {
		gameController.setStart(false);
		controllerThread.interrupt();
	}

}
