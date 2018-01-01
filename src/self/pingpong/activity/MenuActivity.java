package self.pingpong.activity;

import pl.pcchack.activity.AbstractActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AbstractActivity {

	@Override
	protected void initFieldsAndServices(Bundle savedState) {
		
	}

	@Override
	protected int getMainViewId() {
		return R.layout.menu_layout;
	}

	@Override
	protected void saveDataInPreferences() {
		
	}

	@Override
	protected boolean isKeepScreenOn() {
		return false;
	}
	public void handleAction(View view) {
		switch(view.getId()) {
		case R.id.new_game_button:
			startActivity(new Intent(this,PingPongActivity.class));
			break;
		case R.id.high_scores_button:
			startActivity(new Intent(this,HighScoresActivity.class));
			break;
		case R.id.exit_button:
			showConfirm(R.string.exit_confirm,new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();					
				}
			});
			break;
		}
	}
}
