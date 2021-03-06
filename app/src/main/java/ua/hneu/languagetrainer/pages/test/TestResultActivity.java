package ua.hneu.languagetrainer.pages.test;

import ua.hneu.edu.languagetrainer.R;
import ua.hneu.languagetrainer.App;
import ua.hneu.languagetrainer.LearningStatistics;
import ua.hneu.languagetrainer.masterdetailflow.MenuListActivity;
import ua.hneu.languagetrainer.model.tests.Test;
import ua.hneu.languagetrainer.pages.MainActivity;
import ua.hneu.languagetrainer.pages.SettingsActivity;
import ua.hneu.languagetrainer.passing.TestPassing;
import ua.hneu.languagetrainer.service.TestService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TestResultActivity extends Activity {
	TextView vocTextView;
	TextView readingTextView;
	TextView vocResultTextView;
	TextView readingResultTextView;
	TextView listeningResultTextView;
	TextView totalResultTextView;
	ImageView vocIcon;
	ImageView readingIcon;
	ImageView listeningIcon;
	ImageView totalIcon;

	TestPassing tp = App.tp;
	TestService ts = new TestService();
	Test t;

	String testName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_result);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			testName = extras.getString("testName");
		}
		vocTextView = (TextView) findViewById(R.id.vocTextView);
		readingTextView = (TextView) findViewById(R.id.readingTextView);
		vocResultTextView = (TextView) findViewById(R.id.vocResultsTextView);
		readingResultTextView = (TextView) findViewById(R.id.readingResultsTextView);
		listeningResultTextView = (TextView) findViewById(R.id.listeningResultsTextView);
		totalResultTextView = (TextView) findViewById(R.id.totalResultsTextView);
		vocIcon = (ImageView) findViewById(R.id.vocIcon);
		readingIcon = (ImageView) findViewById(R.id.readingIcon);
		listeningIcon = (ImageView) findViewById(R.id.listeningIcon);
		totalIcon = (ImageView) findViewById(R.id.totalIcon);

		int scoreVoc = (int) tp.getScoreInVocGr();
		int scoreReading = (int) tp.getScoreInReading();
		int scoreListening = (int) tp.getScoreInListening();
		int total = scoreVoc + scoreReading + scoreListening;

		t = ts.getTestByName(testName, getContentResolver());

		if (App.userInfo.getLevel() == 4 || App.userInfo.getLevel() == 5) {
			readingTextView.setText(getString(R.string.voc_gr) + ", "
					+ getString(R.string.reading));
			int score = scoreVoc + scoreReading;
			readingResultTextView.setText(score + " / 120");
			listeningResultTextView.setText(scoreListening + " / 60");
			totalResultTextView.setText(total + " / 180");
			vocTextView.setVisibility(View.INVISIBLE);
			vocResultTextView.setVisibility(View.INVISIBLE);
			vocIcon.setVisibility(View.INVISIBLE);

			if (scoreVoc + scoreReading < 38)
				readingIcon.setImageResource(R.drawable.no);
			if (scoreListening < 19)
				listeningIcon.setImageResource(R.drawable.no);
			if (!t.isPassed())
				totalIcon.setImageResource(R.drawable.no);
		} else {
			vocResultTextView.setText(scoreVoc + " / 60");
			readingResultTextView.setText(scoreReading + " / 60");
			listeningResultTextView.setText(scoreListening + " / 60");
			totalResultTextView.setText(total + " / 180");

			if (scoreVoc < 19)
				vocIcon.setImageResource(R.drawable.no);
			if (scoreReading < 19)
				readingIcon.setImageResource(R.drawable.no);
			if (scoreListening < 19)
				listeningIcon.setImageResource(R.drawable.no);
			if (!t.isPassed())
				totalIcon.setImageResource(R.drawable.no);
		}
		t.setPointsPart1(scoreVoc);
		t.setPointsPart2(scoreReading);
		t.setPointsPart3(scoreListening);
		ts.update(t, getContentResolver());
	}

	public void buttonReturnOnClick(View v) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_statistics:
                intent = new Intent(this, LearningStatistics.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

}
