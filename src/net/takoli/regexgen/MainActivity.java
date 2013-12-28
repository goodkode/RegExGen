package net.takoli.regexgen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;  //will host the section contents
	TextView regExText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for the sections
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
	}

	@Override  // Menu settings - optional
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	// this creates the REGEX - just test now
	public void onStartsWithButton(View view) {
		// Do something in response to button
		EditText startsWithText = (EditText) findViewById(R.id.startsWithText);
		String startsWith = startsWithText.getText().toString();
		regExText = (TextView) findViewById(R.id.regexText);
		regExText.setText(startsWith);
	}
	
	public void onSWFCheckboxClicked(View view) {
	    // Is the view now checked?
	    boolean checked = ((CheckBox) view).isChecked();
	    
	    switch(view.getId()) {
	        case R.id.anything:
	            if (checked) {
	            	Log.i("SWFCheckBox", "anything");
	            	((CheckBox) findViewById(R.id.upper_letter)).setChecked(false);
	            	((CheckBox) findViewById(R.id.lower_letter)).setChecked(false);
	            	((CheckBox) findViewById(R.id.number)).setChecked(false);
	            	((CheckBox) findViewById(R.id.symbol)).setChecked(false);
	            	((CheckBox) findViewById(R.id.startsWithCheckBox)).setChecked(false);
		            regExText = (TextView) findViewById(R.id.regexText);
		    		regExText.setText("anything"); }
	            break;
	        case R.id.upper_letter:
	            if (checked) {
	            	Log.i("SWFCheckBox", "upper_letter");
	            	((CheckBox) findViewById(R.id.anything)).setChecked(false);
	            	((CheckBox) findViewById(R.id.startsWithCheckBox)).setChecked(false);
		            regExText = (TextView) findViewById(R.id.regexText);
		    		regExText.setText("upper_letter"); }
	            break;
	        case R.id.lower_letter:
	            if (checked) {
	            	Log.i("SWFCheckBox", "lower_letter");
	            	((CheckBox) findViewById(R.id.anything)).setChecked(false);
	            	((CheckBox) findViewById(R.id.startsWithCheckBox)).setChecked(false);
		            regExText = (TextView) findViewById(R.id.regexText);
		    		regExText.setText("lower_letter"); }
	            break;
	        case R.id.number:
	            if (checked) {
	            	Log.i("SWFCheckBox", "number");
	            	((CheckBox) findViewById(R.id.anything)).setChecked(false);
	            	((CheckBox) findViewById(R.id.startsWithCheckBox)).setChecked(false);
		            regExText = (TextView) findViewById(R.id.regexText);
		    		regExText.setText("number"); }
	            break;
	        case R.id.symbol:
	            if (checked) {
	            	Log.i("SWFCheckBox", "symbol");
	            	((CheckBox) findViewById(R.id.anything)).setChecked(false);
	            	((CheckBox) findViewById(R.id.startsWithCheckBox)).setChecked(false);
		            regExText = (TextView) findViewById(R.id.regexText);
		    		regExText.setText("symbol"); }
	            break;
	        case R.id.startsWithCheckBox:
	            if (checked) {
	            	Log.i("SWFCheckBox", "startsWithCheckBox");
	            	((CheckBox) findViewById(R.id.anything)).setChecked(false);
	            	((CheckBox) findViewById(R.id.upper_letter)).setChecked(false);
	            	((CheckBox) findViewById(R.id.lower_letter)).setChecked(false);
	            	((CheckBox) findViewById(R.id.number)).setChecked(false);
	            	((CheckBox) findViewById(R.id.symbol)).setChecked(false);
	            	EditText startsWithText = (EditText) findViewById(R.id.startsWithText);
	        		String startsWith = startsWithText.getText().toString();
		            regExText = (TextView) findViewById(R.id.regexText);
		    		regExText.setText(startsWith); }
	            break;
	    }
	}

	//this returns a fragment corresponding to one of the sections/tabs/pages.
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_section1);
			case 1:
				return getString(R.string.title_section2);
			case 2:
				return getString(R.string.title_section3);
			}
			return null;
		}
	}

	// dummy fragment representing a section of the app, but that simply displays dummy text.
	public static class DummySectionFragment extends Fragment {
		
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			int currentPage = getArguments().getInt(ARG_SECTION_NUMBER);
			Log.i("onCreateView", "current page: " + currentPage);
			View rootView = null;
			switch (currentPage) {
				case 1:
					rootView = inflater.inflate(R.layout.starts_with_fragment,
							container, false); break;
				case 2:
					rootView = inflater.inflate(R.layout.contains_fragment,
							container, false); break;
				case 3:
					rootView = inflater.inflate(R.layout.ends_with_fragment,
							container, false); break;
			}
//			TextView dummyTextView = (TextView) rootView
//					.findViewById(R.id.section_label);
//			dummyTextView.setText(Integer.toString(currentPage));
			return rootView;
		}
	}

}
