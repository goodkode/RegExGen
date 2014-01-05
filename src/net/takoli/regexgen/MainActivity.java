package net.takoli.regexgen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;  //will host the section contents
	TextView regExText;
	String sw;
	String cnt1, cnt2, cnt3, nCnt;
	String ew1, ew2, ew3, nEw;	
	
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
		sw = ".*";
		cnt1 = cnt2 = cnt3 = nCnt = "";
		ew1 = ew2 = ew3 = nEw = "";
		createRegex();
	}

//	@Override  // Menu settings - optional
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
	
	// this creates the REGEX
	public void createRegex() {
		if(sw.compareTo("") == 0)
			sw = ".*";
		String cnt = andsOrNots(cnt1, cnt2, cnt3, nCnt);
		String ew = andsOrNots(ew1, ew2, ew3, nEw);
		regExText = (TextView) findViewById(R.id.regexText);
		regExText.setText("^"+sw+cnt+ew+"$");
	}
	
	private String andsOrNots(String s1, String s2, String s3, String n) {
		if(s1.compareTo("") == 0 && s2.compareTo("") == 0 
				&& s3.compareTo("") == 0 && n.compareTo("") == 0)
			if(sw.length() >= 2 &&
				sw.charAt(sw.length()-2) == '.' && sw.charAt(sw.length()-1) == '*')
				return "";
		return "(" + s1 + s2 + s3 + n + ")";
	}
	
	private String withEscapes(String orig) {
		String escaped = orig;
		return escaped;
	}
	
	// STARTS WITH FRAGMENT
	public void onSWFCheckboxClicked(View view) {
	    // Is the view now checked?
	    boolean checked = ((CheckBox) view).isChecked();
	    
	    switch(view.getId()) {
	        case R.id.startsAnything:
	            if (checked) {
	            	Log.i("SWFCheckBox", "anything");
	            	((CheckBox) findViewById(R.id.upper_letter)).setChecked(false);
	            	((CheckBox) findViewById(R.id.lower_letter)).setChecked(false);
	            	((CheckBox) findViewById(R.id.number)).setChecked(false);
	            	((CheckBox) findViewById(R.id.symbol)).setChecked(false);
	            	((CheckBox) findViewById(R.id.startsWithCheckBox)).setChecked(false);
		            sw = ".*"; }
	            break;
	        case R.id.upper_letter:
	            if (checked) {
	            	Log.i("SWFCheckBox", "upper_letter");
	            	((CheckBox) findViewById(R.id.startsAnything)).setChecked(false);
	            	((CheckBox) findViewById(R.id.startsWithCheckBox)).setChecked(false); }
	            sw = SWFhelper();
	            break;
	        case R.id.lower_letter:
	            if (checked) {
	            	Log.i("SWFCheckBox", "lower_letter");
	            	((CheckBox) findViewById(R.id.startsAnything)).setChecked(false);
	            	((CheckBox) findViewById(R.id.startsWithCheckBox)).setChecked(false); }
	            sw = SWFhelper();
	            break;
	        case R.id.number:
	            if (checked) {
	            	Log.i("SWFCheckBox", "number");
	            	((CheckBox) findViewById(R.id.startsAnything)).setChecked(false);
	            	((CheckBox) findViewById(R.id.startsWithCheckBox)).setChecked(false); }
	            sw = SWFhelper();
	            break;
	        case R.id.symbol:
	            if (checked) {
	            	Log.i("SWFCheckBox", "symbol");
	            	((CheckBox) findViewById(R.id.startsAnything)).setChecked(false);
	            	((CheckBox) findViewById(R.id.startsWithCheckBox)).setChecked(false); }
	            sw = SWFhelper();
	            break;
	        case R.id.startsWithCheckBox:
	            if (checked) {
	            	Log.i("SWFCheckBox", "startsWithCheckBox");
	            	((CheckBox) findViewById(R.id.startsAnything)).setChecked(false);
	            	((CheckBox) findViewById(R.id.upper_letter)).setChecked(false);
	            	((CheckBox) findViewById(R.id.lower_letter)).setChecked(false);
	            	((CheckBox) findViewById(R.id.number)).setChecked(false);
	            	((CheckBox) findViewById(R.id.symbol)).setChecked(false);
	            	EditText startsWithText = (EditText) findViewById(R.id.startsWithText);
	            	String input = startsWithText.getText().toString();
	            	if(input.compareTo("") == 0)
	            		input = ".*";
	            	sw = input; }
	            else {
	            	((CheckBox) findViewById(R.id.startsAnything)).setChecked(true);
	            	sw = ".*"; }
	            break;
	    }
	    // updates RegEx field
	 	createRegex();
	    // LISTENER: startsWithText changes sent to regex right away
		((EditText) findViewById(R.id.startsWithText)).addTextChangedListener(new TextWatcher() {
	        @Override
	        public void onTextChanged(CharSequence s, int start, int before, int count) {
	        	sw = s.toString();
	        	createRegex();}
	        @Override
	        public void beforeTextChanged(CharSequence s, int start, int count,
	                int after) { }
	        @Override
	        public void afterTextChanged(Editable s) {
	        	sw = s.toString();
	        	createRegex();} });
	}
	private String SWFhelper() {
		String swh = "[";
		if (((CheckBox) findViewById(R.id.lower_letter)).isChecked())
			swh += "a-z";
    	if (((CheckBox) findViewById(R.id.upper_letter)).isChecked())
    		swh += "A-Z";
    	if (((CheckBox) findViewById(R.id.number)).isChecked())
			swh += "0-9";
    	if (((CheckBox) findViewById(R.id.symbol)).isChecked())
			swh += "!-/:-@[-`{-~";
    	swh = swh + "]";
    	if ( 	(((CheckBox) findViewById(R.id.lower_letter)).isChecked()) == false &&
    			(((CheckBox) findViewById(R.id.upper_letter)).isChecked()) == false &&
    			(((CheckBox) findViewById(R.id.number)).isChecked()) == false &&
    			(((CheckBox) findViewById(R.id.symbol)).isChecked()) == false ) {
    		((CheckBox) findViewById(R.id.startsAnything)).setChecked(true);
    		return ".*";
    		}
		return swh;
	}

	// CONTINUES WITH FRAGMENT
	public void onCFCheckboxClicked(View view) {
	    // Is the view now checked?
	    boolean checked = ((CheckBox) view).isChecked();
	    
	    switch(view.getId()) {
	        case R.id.contAnything:
	            if (checked) {
	            	Log.i("CFCheckBox", "contAnything");
	            	((CheckBox) findViewById(R.id.containsText1CheckBox)).setChecked(false);
	            	((CheckBox) findViewById(R.id.containsText2CheckBox)).setChecked(false);
	            	((CheckBox) findViewById(R.id.containsText3CheckBox)).setChecked(false);
	            	((CheckBox) findViewById(R.id.notContainsTextCheckBox)).setChecked(false);
	            	if(sw.compareTo(".*") != 0) {
	            		cnt1 = ".*";
	            		cnt2 = cnt3 = nCnt = ""; } }
	            break;
	        case R.id.containsText1CheckBox:
	            if (checked) {
	            	Log.i("CFCheckBox", "containsText1CheckBox");
	            	((CheckBox) findViewById(R.id.contAnything)).setChecked(false);
	            	EditText contText = (EditText) findViewById(R.id.containsText1);
	            	cnt1 = contText.getText().toString(); }
	            else {
	            	cnt1 = "";
	            	if(	!((CheckBox) findViewById(R.id.containsText2CheckBox)).isChecked() &&
            				!((CheckBox) findViewById(R.id.containsText3CheckBox)).isChecked() &&
            				!((CheckBox) findViewById(R.id.notContainsTextCheckBox)).isChecked()) {
	            	((CheckBox) findViewById(R.id.contAnything)).setChecked(true);
	            	if(sw.compareTo(".*") != 0) {
	            		cnt1 = ".*";
	            		cnt2 = cnt3 = nCnt = ""; } } }
	            break;
	        case R.id.containsText2CheckBox:
	            if (checked) {
	            	Log.i("CFCheckBox", "containsText2CheckBox");
	            	((CheckBox) findViewById(R.id.contAnything)).setChecked(false);
	            	EditText contText = (EditText) findViewById(R.id.containsText2);
	            	cnt2 = contText.getText().toString(); }
	            else {
	            	cnt2 = "";
	            	if(	!((CheckBox) findViewById(R.id.containsText1CheckBox)).isChecked() &&
        					!((CheckBox) findViewById(R.id.containsText3CheckBox)).isChecked() &&
        					!((CheckBox) findViewById(R.id.notContainsTextCheckBox)).isChecked()) {
	            	((CheckBox) findViewById(R.id.contAnything)).setChecked(true);
	            	if(sw.compareTo(".*") != 0) {
	            		cnt1 = ".*";
            			cnt2 = cnt3 = nCnt = ""; } } }
	            break;
	        case R.id.containsText3CheckBox:
	            if (checked) {
	            	Log.i("CFCheckBox", "containsText3CheckBox");
	            	((CheckBox) findViewById(R.id.contAnything)).setChecked(false);
	            	EditText contText = (EditText) findViewById(R.id.containsText3);
	            	cnt3 = contText.getText().toString(); }
	            else {
	            	cnt3 = "";
	            	if(	!((CheckBox) findViewById(R.id.containsText1CheckBox)).isChecked() &&
        					!((CheckBox) findViewById(R.id.containsText2CheckBox)).isChecked() &&
        					!((CheckBox) findViewById(R.id.notContainsTextCheckBox)).isChecked()) {
	            	((CheckBox) findViewById(R.id.contAnything)).setChecked(true);
	            	if(sw.compareTo(".*") != 0) {
	            		cnt1 = ".*";
            			cnt2 = cnt3 = nCnt = ""; } } }
	            break;
	        case R.id.notContainsTextCheckBox:
	            if (checked) {
	            	Log.i("CFCheckBox", "notContainsTextCheckBox");
	            	((CheckBox) findViewById(R.id.contAnything)).setChecked(false);
	            	EditText contText = (EditText) findViewById(R.id.notContainsText);
	            	nCnt = contText.getText().toString(); }
	            else {
	            	nCnt = "";
	            	if(	!((CheckBox) findViewById(R.id.containsText1CheckBox)).isChecked() &&
    					!((CheckBox) findViewById(R.id.containsText2CheckBox)).isChecked() &&
    					!((CheckBox) findViewById(R.id.containsText3CheckBox)).isChecked()) {
            	((CheckBox) findViewById(R.id.contAnything)).setChecked(true);
            	if(sw.compareTo(".*") != 0) {
            		cnt1 = ".*";
        			cnt2 = cnt3 = nCnt = ""; } } }
	            break;
	    }
	    // updates RegEx field
	    createRegex();
	    // LISTENTER: containsText1 changes sent to regex right away
		((EditText) findViewById(R.id.containsText1)).addTextChangedListener(new TextWatcher() {
	        @Override
	        public void onTextChanged(CharSequence s, int start, int before, int count) {
	        	cnt1 = s.toString();
	        	createRegex();}
	        @Override
	        public void beforeTextChanged(CharSequence s, int start, int count,
	                int after) { }
	        @Override
	        public void afterTextChanged(Editable s) {
	        	cnt1 = s.toString();
	        	createRegex();} });
		// LISTENTER: containsText2 changes sent to regex right away
		((EditText) findViewById(R.id.containsText2)).addTextChangedListener(new TextWatcher() {
	        @Override
	        public void onTextChanged(CharSequence s, int start, int before, int count) {
	        	cnt2 = s.toString();
	        	createRegex();}
	        @Override
	        public void beforeTextChanged(CharSequence s, int start, int count,
	                int after) { }
	        @Override
	        public void afterTextChanged(Editable s) {
	        	cnt2 = s.toString();
	        	createRegex();} });
		// LISTENTER: containsText3 changes sent to regex right away
		((EditText) findViewById(R.id.containsText3)).addTextChangedListener(new TextWatcher() {
	        @Override
	        public void onTextChanged(CharSequence s, int start, int before, int count) {
	        	cnt3 = s.toString();
	        	createRegex();}
	        @Override
	        public void beforeTextChanged(CharSequence s, int start, int count,
	                int after) { }
	        @Override
	        public void afterTextChanged(Editable s) {
	        	cnt3 = s.toString();
	        	createRegex();} });
		// LISTENTER: notContainsText changes sent to regex right away
		((EditText) findViewById(R.id.notContainsText)).addTextChangedListener(new TextWatcher() {
	        @Override
	        public void onTextChanged(CharSequence s, int start, int before, int count) {
	        	nCnt = s.toString();
	        	createRegex();}
	        @Override
	        public void beforeTextChanged(CharSequence s, int start, int count,
	                int after) { }
	        @Override
	        public void afterTextChanged(Editable s) {
	        	nCnt = s.toString();
	        	createRegex();} });
	}
	
	// ENDS WITH FRAGMENT
	public void onEWFCheckboxClicked(View view) {
	    // Is the view now checked?
	    boolean checked = ((CheckBox) view).isChecked();
	    
	    switch(view.getId()) {
        case R.id.endsAnything:
            if (checked) {
            	Log.i("EWFCheckbox", "endsAnything");
            	((CheckBox) findViewById(R.id.endsText1CheckBox)).setChecked(false);
            	((CheckBox) findViewById(R.id.endsText2CheckBox)).setChecked(false);
            	((CheckBox) findViewById(R.id.endsText3CheckBox)).setChecked(false);
            	((CheckBox) findViewById(R.id.notEndsTextCheckBox)).setChecked(false);
            	ew1 = "eany";
	            createRegex(); }
            break;
        case R.id.endsText1CheckBox:
            if (checked) {
            	Log.i("EWFCheckbox", "endsText1CheckBox");
            	((CheckBox) findViewById(R.id.endsAnything)).setChecked(false);
            	EditText endText = (EditText) findViewById(R.id.endsText1);
            	ew1 = endText.getText().toString();
	            createRegex(); }
            break;
        case R.id.endsText2CheckBox:
            if (checked) {
            	Log.i("EWFCheckbox", "endsText2CheckBox");
            	((CheckBox) findViewById(R.id.endsAnything)).setChecked(false);
            	EditText endText = (EditText) findViewById(R.id.endsText2);
            	ew2 = endText.getText().toString();
	            createRegex(); }
            break;
        case R.id.endsText3CheckBox:
            if (checked) {
            	Log.i("EWFCheckbox", "endsText3CheckBox");
            	((CheckBox) findViewById(R.id.endsAnything)).setChecked(false);
            	EditText endText = (EditText) findViewById(R.id.endsText3);
            	ew3 = endText.getText().toString();
	            createRegex(); }
            break;
        case R.id.notEndsTextCheckBox:
            if (checked) {
            	Log.i("EWFCheckbox", "notEndsTextCheckBox");
            	((CheckBox) findViewById(R.id.endsAnything)).setChecked(false);
            	EditText endText = (EditText) findViewById(R.id.notEndsWithText);
            	nEw = endText.getText().toString();
	            createRegex(); }
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
			return rootView;
		}
	}

}
