package ua.hneu.languagetrainer.masterdetailflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ua.hneu.edu.languagetrainer.R;
import ua.hneu.languagetrainer.App;
import ua.hneu.languagetrainer.CounterWordsListViewAdapter;
import ua.hneu.languagetrainer.model.other.CounterWord;
import ua.hneu.languagetrainer.model.other.Giongo;
import ua.hneu.languagetrainer.pages.counterwords.CounterWordsIntroductionActivity;
import ua.hneu.languagetrainer.service.CounterWordsService;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import  ua.hneu.languagetrainer.pages.counterwords.CounterWordsIntroductionActivity;

public class CounterWordsActivityFragment extends Fragment {
	TextView infoTextView;
	ListView sectionsListView;
    TextView wordTextView;
    TextView transcriptionTextView;
    TextView translationTextView;
    Button showTranslationButton;
    CounterWord random;

	ArrayList<String> sectionNames = new ArrayList<String>();
	ArrayList<String> infoList = new ArrayList<String>();
	CounterWordsListViewAdapter adapter;
	public static final String ARG_ITEM_ID = "item_id";
	CounterWordsService cvs = new CounterWordsService();
	public static String selectedSection;

	public CounterWordsActivityFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.counterwords_fragment,
				container, false);

		HashMap<String, int[]> info = cvs.getAllSectionsWithProgress(App.cr);
		sectionNames = new ArrayList<String>();
		infoList = new ArrayList<String>();
		sectionsListView = (ListView) rootView
				.findViewById(R.id.sectionsListView);

        wordTextView = (TextView) rootView.findViewById(R.id.wordTextView);
        transcriptionTextView = (TextView) rootView.findViewById(R.id.transcriptionCWTextView);
        translationTextView = (TextView) rootView.findViewById(R.id.translationCWTextView);
        showTranslationButton = (Button) rootView.findViewById(R.id.showTranslationCWButton);

        while(random==null) {
            try {
                random = App.allCounterWordsDictionary.fetchRandom();
            } catch (Exception e) {
            }
        }
        wordTextView.setText(random.getWord());
            translationTextView.setText(random.getTranslation());
        transcriptionTextView.setText("["+random.getTranscription()+"]");

        translationTextView.setAlpha(0);
        wordTextView.setTypeface(App.kanjiFont, Typeface.NORMAL);
        transcriptionTextView.setTypeface(App.kanjiFont, Typeface.NORMAL);

        Set<Entry<String, int[]>> set = info.entrySet();
		Iterator<Entry<String, int[]>> it = set.iterator();
		while (it.hasNext()) {
			Map.Entry<String, int[]> entry = (Map.Entry<String, int[]>) it
					.next();
			sectionNames.add(entry.getKey() + "");
			infoList.add(entry.getValue()[1] + "/" + entry.getValue()[0]);
		}

		adapter = new CounterWordsListViewAdapter(getActivity(), sectionNames,
				infoList);
		sectionsListView.setAdapter(adapter);
		sectionsListView.setOnItemClickListener(sectionsListViewClickListener);

        //TODO: from start is user wants
       /* if(App.isCounterWordsLearned()) rootView.findViewById(R.id.practiceCounters).setEnabled(false);

        sectionsListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                view.setSelected(true);
            }
        });*/

        return rootView;
	}

	final private transient OnItemClickListener sectionsListViewClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(final AdapterView<?> parent, final View view,
				final int position, final long itemID) {
			// TODO: replace this
			String[] r = infoList.get(position).split("/");
			if (Integer.parseInt(r[0]) != Integer.parseInt(r[1]))
				selectedSection = sectionNames.get(position);

            CounterWordsService cws = new CounterWordsService();
            App.counterWordsDictionary = cws.createCurrentDictionary(
                    selectedSection, App.numberOfEntriesInCurrentDict, App.cr);

//			adapter.setTextColorOfListViewRow((ListView) parent, position,
//					Color.parseColor("#ffbb33"));
			
			App.sectionName=selectedSection;

            App.counterWordsDictionary = CounterWordsService.createCurrentDictionary(
                    App.sectionName, App.numberOfEntriesInCurrentDict, App.cr);
            Intent intent = new Intent(getActivity(), CounterWordsIntroductionActivity.class);
            startActivity(intent);
		}
	};

}
