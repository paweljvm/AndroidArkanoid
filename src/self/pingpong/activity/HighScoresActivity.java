package self.pingpong.activity;

import pl.pcchack.activity.AbstractActivity;
import pl.pcchack.activity.ListViewAdapter;
import pl.pcchack.db.AndroidDao;
import self.pingpong.model.HighScoreData;
import android.os.Bundle;
import android.widget.ListView;

public class HighScoresActivity extends AbstractActivity{
	private ListViewAdapter<HighScoreData> adapter;
	private ListView listView;
	private AndroidDao dao = AndroidDao.getLastInstance();
	@Override
	protected void initFieldsAndServices(Bundle savedInstanceState) {
		if(dao == null) {
			dao = new AndroidDao(this,PingPongActivity.DB_HIGH_SCORES,HighScoreData.class);
		}
		try {
			adapter = new ListViewAdapter<HighScoreData>(this,dao.getAll(HighScoreData.class,HighScoreData.P_POINTS,false));	
		} catch(Exception e) {
			
		}
		listView = new ListView(this);
		listView.setAdapter(adapter);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(listView);
	}

	@Override
	protected int getMainViewId() {
		return R.layout.g_list_layout;
	}

	@Override
	protected void saveDataInPreferences() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isKeepScreenOn() {
		// TODO Auto-generated method stub
		return false;
	}

	
	
}
