package ch.ralena.natibo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ch.ralena.natibo.MainActivity;
import ch.ralena.natibo.R;
import ch.ralena.natibo.adapter.CourseAvailableLanguagesAdapter;
import ch.ralena.natibo.adapter.CourseSelectedLanguagesAdapter;
import ch.ralena.natibo.object.Language;
import io.realm.Realm;
import io.realm.RealmResults;

public class CoursePickLanguageFragment extends Fragment {
	private static final String TAG = CoursePickLanguageFragment.class.getSimpleName();
	public static final String TAG_COURSE_ID = "language_id";

	RealmResults<Language> availableLanguages;
	ArrayList<Language> selectedLanguages;
	Language baseLanguage;
	Language targetLanguage;

	private Realm realm;

	RecyclerView availableLanguagesRecyclerView;
	RecyclerView selectedLanguagesRecyclerView;
	CourseAvailableLanguagesAdapter availableAdapter;
	CourseSelectedLanguagesAdapter selectedAdapter;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_course_pick_language, container, false);

		// switch to back button
		MainActivity activity = (MainActivity) getActivity();
		activity.enableBackButton();
		activity.setTitle(getString(R.string.select_languages));

		realm = Realm.getDefaultInstance();

		availableLanguages = realm.where(Language.class).findAll();
		selectedLanguages = new ArrayList<>();

		// recycler views
		availableLanguagesRecyclerView = view.findViewById(R.id.availableLanguagesRecyclerView);
		selectedLanguagesRecyclerView= view.findViewById(R.id.selectedLanguagesRecyclerView);

		availableAdapter = new CourseAvailableLanguagesAdapter(availableLanguages);
		availableAdapter.asObservable().subscribe(this::availableLanguageClicked);
		availableLanguagesRecyclerView.setAdapter(availableAdapter);
		availableLanguagesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

		selectedAdapter = new CourseSelectedLanguagesAdapter(selectedLanguages);
		selectedLanguagesRecyclerView.setAdapter(selectedAdapter);
		selectedLanguagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

		return view;
	}

	private void availableLanguageClicked(Language language) {
		if (selectedLanguages.contains(language)) {
			selectedLanguages.remove(language);
		} else {
			selectedLanguages.add(language);
		}
		selectedAdapter.notifyDataSetChanged();
	}

	private void loadPrepackagedCourseFragment() {
		CoursePrepackagedCreateFragment fragment = new CoursePrepackagedCreateFragment();
		Bundle bundle = new Bundle();
		bundle.putString(CoursePrepackagedCreateFragment.TAG_BASE_LANGUAGE, baseLanguage.getLanguageId());
		bundle.putString(CoursePrepackagedCreateFragment.TAG_TARGET_LANGUAGE, targetLanguage.getLanguageId());
		fragment.setArguments(bundle);
		getFragmentManager().beginTransaction()
				.replace(R.id.fragmentPlaceHolder, fragment)
				.addToBackStack(null)
				.commit();
	}
}