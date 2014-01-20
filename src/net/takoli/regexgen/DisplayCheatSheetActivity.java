package net.takoli.regexgen;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;

public class DisplayCheatSheetActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cheat_sheet);
	}
	
	@Override
	public void onBackPressed() {
		Log.i("cheatSheet", "onBackPressed");
		NavUtils.navigateUpFromSameTask(this);
		overridePendingTransition(R.anim.main_return, R.anim.cheat_exit);
	}
	
	public void backToMain(View view) {
		NavUtils.navigateUpFromSameTask(this);
		overridePendingTransition(R.anim.main_return, R.anim.cheat_exit);
	}
}
