package ua.hneu.languagetrainer.masterdetailflow;

import ua.hneu.edu.languagetrainer.R;
import ua.hneu.languagetrainer.App;
import ua.hneu.languagetrainer.pages.giongo.AllGiongo;
import ua.hneu.languagetrainer.pages.giongo.GiongoIntroductionActivity;
import ua.hneu.languagetrainer.service.GiongoService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

public class GiongoActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_detail);
		Log.i("ItemDetailActivity", "ItemDetailActivity.onCreate()");
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			arguments.putString(GiongoActivityFragment.ARG_ITEM_ID, getIntent()
					.getStringExtra(GiongoActivityFragment.ARG_ITEM_ID));
			GiongoActivityFragment fragment = new GiongoActivityFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.item_detail_container, fragment).commit();
		}
        //TODO: from start is user wants
        if(App.isGiongoLearned()) findViewById(R.id.practiceGiongo).setEnabled(false);
	}

	public void onClickPracticeGiongo(View v) {
		// load giongo
		GiongoService gs = new GiongoService();
		App.giongoWordsDictionary = gs.createCurrentDictionary(
				App.numberOfEntriesInCurrentDict, App.cr);
		Intent intent = new Intent(this, GiongoIntroductionActivity.class);
		startActivity(intent);
	}
	
	public void onClickAllGiongo(View v) {
		// load giongo
		GiongoService gs = new GiongoService();
		App.giongoWordsDictionary = gs.createCurrentDictionary(
				App.numberOfEntriesInCurrentDict, App.cr);
		Intent intent = new Intent(this, AllGiongo.class);
		startActivity(intent);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpTo(this,
					new Intent(this, MenuListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
