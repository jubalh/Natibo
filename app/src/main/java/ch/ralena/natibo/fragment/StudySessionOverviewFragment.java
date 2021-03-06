package ch.ralena.natibo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.ralena.natibo.MainActivity;
import ch.ralena.natibo.R;
import ch.ralena.natibo.adapter.StudySessionAdapter;
import ch.ralena.natibo.object.Course;
import ch.ralena.natibo.object.Day;
import ch.ralena.natibo.object.SentenceSet;
import io.realm.Realm;

public class StudySessionOverviewFragment extends Fragment {
	private static final String TAG = StudySessionOverviewFragment.class.getSimpleName();
	public static final String KEY_COURSE_ID = "language_id";

	Course course;

	private Realm realm;

	private MainActivity activity;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_study_session_overview, container, false);

		activity = (MainActivity) getActivity();
		activity.setTitle(getString(R.string.session_overview));

		// load schedules from database
		String id = getArguments().getString(KEY_COURSE_ID);
		realm = Realm.getDefaultInstance();
		course = realm.where(Course.class).equalTo("id", id).findFirst();

		// load language name
		TextView courseTitleLabel = view.findViewById(R.id.courseTitleText);
		courseTitleLabel.setText(course.getTitle());

		Day day = course.getCurrentDay();
		SentenceSet sentenceSet = day.getSentenceSets().get(0);

		// set up recyclerlist and adapter
		RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
		StudySessionAdapter adapter = new StudySessionAdapter(course.getBaseLanguage().getLanguageId(), course.getTargetLanguage().getLanguageId(), sentenceSet.getSentencePairs());
		recyclerView.setAdapter(adapter);
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(layoutManager);

//		adapter.asObservable().subscribe(this::loadSentenceListFragment);

		return view;
	}


}
