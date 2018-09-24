package net.takoli.regexgen;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private EditText regExText;
    private String sw;
    private String cnt1, cnt2, cnt3, nCnt;
    private String ew1, ew2, ew3, nEw;
    private String startDelimiter, endDelimiter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics());
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_activity);

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                hideKeyboard();
            }
        });
        ((RadioButton)findViewById(R.id.matchOnWordsRadio)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    startDelimiter = "\\b";
                    endDelimiter = "\\b";
                } else {
                    startDelimiter = "^";
                    endDelimiter = "$";
                }
                createRegex();
            }
        });
        regExText = findViewById(R.id.regexText);

        sw = "";
        cnt1 = cnt2 = cnt3 = nCnt = "";
        ew1 = ew2 = ew3 = nEw = "";
        startDelimiter = "\\b";
        endDelimiter = "\\b";

        createRegex();
    }

    private void createRegex() {
        String S, C, Cn, E, En;
        S = C = Cn = E = En = "";

        if (sw.compareTo("") != 0 && sw.charAt(0) != '[' && sw.charAt(sw.length()-1) != ']')
            S = withEscapes(sw);
        else S = sw;
        S = startDelimiter + S;

        C = concatParts(withEscapes(cnt1), withEscapes(cnt2), withEscapes(cnt3));
        if (C.compareTo("") != 0)
            C = ".*" + C + ".*";
        else
            C = ".*";
        if (nCnt.compareTo("") != 0)	// implement negative lookahead
            Cn = "(?!.*" + withEscapes(nCnt) + ")";
        C = Cn + C;

        E = concatParts(withEscapes(ew1), withEscapes(ew2), withEscapes(ew3));
        if (nEw.compareTo("") != 0)		// implement negative lookbehind
            En = "(?<!" + withEscapes(nEw) + ")";
        E = E + En;
        if (E.compareTo("") != 0)
            E += endDelimiter;

        regExText.setText(S + C + E);
    }

    private String concatParts(String s1, String s2, String s3) {
        String output = "";
        int count = 0;
        if (s1.compareTo("") != 0) {
            count++;
            output = output + s1; }
        if (s2.compareTo("") != 0) {
            count++;
            if (count == 1)
                output = output + s2;
            else
                output = output + "|" + s2; }
        if (s3.compareTo("") != 0) {
            count++;
            if (count == 1)
                output = output + s3;
            else
                output = output + "|" + s3; }
        if (count <= 1)
            return output;
        else
            return "(" + output + ")";
    }

    private String withEscapes(String orig) {
        StringBuilder escaped = new StringBuilder();
        char c;
        for (int i = 0; i < orig.length(); i++) {
            c = orig.charAt(i);
            if (c == '.' || c == '?' || c == '*' || c == '+'
                    || c == '&' || c == ':' || c == '{' || c == '}'
                    || c == '[' || c == ']' || c == '(' || c == ')'
                    || c == '^' || c == '$')
                escaped.append("\\" + c);
            else if (c == '\\')
                escaped.append("\\\\");	// backslash char
            else if (c == ' ')
                escaped.append("\\s");	// space char
            else
                escaped.append(c);
        }
        return escaped.toString();
    }

    public void onCopyClick(View view) {
        String regex = regExText.getText().toString();
        regExText.requestFocus();
        regExText.setSelection(0, regex.length());
        ClipData clip = ClipData.newPlainText("regex", regex);
        ClipboardManager clipboard = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, R.string.copied, Toast.LENGTH_LONG).show();
    }

    public void onShareClick(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, regExText.getText());
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Created with RegExGen app");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share_regex_to)));
    }

    public void showCheatSheet(View view) {
        Intent intent = new Intent(this, DisplayCheatSheetActivity.class);
        startActivityForResult(intent, RESULT_OK);
        overridePendingTransition(R.anim.cheat_enter, R.anim.main_exit);
    }

    // STARTS WITH FRAGMENT
    public void onStartsWithCheckboxListener(View view) {
        if (view.getTag() == null) hideKeyboard();
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.startsAnything:
                if (checked) {
                    ((CheckBox) findViewById(R.id.upper_letter)).setChecked(false);
                    ((CheckBox) findViewById(R.id.lower_letter)).setChecked(false);
                    ((CheckBox) findViewById(R.id.number)).setChecked(false);
                    ((CheckBox) findViewById(R.id.symbol)).setChecked(false);
                    ((CheckBox) findViewById(R.id.startsWithCheckBox))
                            .setChecked(false);
                    sw = "";
                } else if (!((CheckBox) findViewById(R.id.upper_letter))
                        .isChecked()
                        && !((CheckBox) findViewById(R.id.lower_letter))
                        .isChecked()
                        && !((CheckBox) findViewById(R.id.number)).isChecked()
                        && !((CheckBox) findViewById(R.id.symbol)).isChecked()
                        && !((CheckBox) findViewById(R.id.startsWithCheckBox))
                        .isChecked()) {
                    ((CheckBox) findViewById(R.id.startsAnything)).setChecked(true);
                    sw = "";
                }
                break;
            case R.id.upper_letter:
                if (checked) {
                    ((CheckBox) findViewById(R.id.startsAnything))
                            .setChecked(false);
                    ((CheckBox) findViewById(R.id.startsWithCheckBox))
                            .setChecked(false);
                }
                sw = SWFhelper();
                break;
            case R.id.lower_letter:
                if (checked) {
                    ((CheckBox) findViewById(R.id.startsAnything))
                            .setChecked(false);
                    ((CheckBox) findViewById(R.id.startsWithCheckBox))
                            .setChecked(false);
                }
                sw = SWFhelper();
                break;
            case R.id.number:
                if (checked) {
                    ((CheckBox) findViewById(R.id.startsAnything))
                            .setChecked(false);
                    ((CheckBox) findViewById(R.id.startsWithCheckBox))
                            .setChecked(false);
                }
                sw = SWFhelper();
                break;
            case R.id.symbol:
                if (checked) {
                    ((CheckBox) findViewById(R.id.startsAnything))
                            .setChecked(false);
                    ((CheckBox) findViewById(R.id.startsWithCheckBox))
                            .setChecked(false);
                }
                sw = SWFhelper();
                break;
            case R.id.startsWithCheckBox:
                if (checked) {
                    ((CheckBox) findViewById(R.id.startsAnything))
                            .setChecked(false);
                    ((CheckBox) findViewById(R.id.upper_letter)).setChecked(false);
                    ((CheckBox) findViewById(R.id.lower_letter)).setChecked(false);
                    ((CheckBox) findViewById(R.id.number)).setChecked(false);
                    ((CheckBox) findViewById(R.id.symbol)).setChecked(false);
                    EditText startsWithText = (EditText) findViewById(R.id.startsWithText);
                    startsWithText.requestFocus();
                    sw = withEscapes(startsWithText.getText().toString());
                } else {
                    ((CheckBox) findViewById(R.id.startsAnything)).setChecked(true);
                    sw = "";
                }
                break;
        }
        createRegex();
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
            swh += "!-/:-@\\[-`\\{-~";
        swh = swh + "]";
        if ((((CheckBox) findViewById(R.id.lower_letter)).isChecked()) == false
                && (((CheckBox) findViewById(R.id.upper_letter)).isChecked()) == false
                && (((CheckBox) findViewById(R.id.number)).isChecked()) == false
                && (((CheckBox) findViewById(R.id.symbol)).isChecked()) == false) {
            ((CheckBox) findViewById(R.id.startsAnything)).setChecked(true);
            return "";
        }
        return swh;
    }

    public void regSWFlisteners(View view) {
        // LISTENER: startsWithText changes sent to regex right away
        ((EditText) findViewById(R.id.startsWithText))
                .addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        ((CheckBox) findViewById(R.id.startsWithCheckBox))
                                .setChecked(true);
                        ((CheckBox) findViewById(R.id.upper_letter))
                                .setChecked(false);
                        ((CheckBox) findViewById(R.id.lower_letter))
                                .setChecked(false);
                        ((CheckBox) findViewById(R.id.number))
                                .setChecked(false);
                        ((CheckBox) findViewById(R.id.symbol))
                                .setChecked(false);
                        ((CheckBox) findViewById(R.id.startsAnything))
                                .setChecked(false);
                        sw = s.toString();
                        createRegex();
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        ((CheckBox) findViewById(R.id.startsWithCheckBox))
                                .setChecked(true);
                        ((CheckBox) findViewById(R.id.upper_letter))
                                .setChecked(false);
                        ((CheckBox) findViewById(R.id.lower_letter))
                                .setChecked(false);
                        ((CheckBox) findViewById(R.id.number))
                                .setChecked(false);
                        ((CheckBox) findViewById(R.id.symbol))
                                .setChecked(false);
                        ((CheckBox) findViewById(R.id.startsAnything))
                                .setChecked(false);
                        sw = s.toString();
                        createRegex();
                    }
                });
    }

    // CONTINUES WITH FRAGMENT
    public void onContinueWithCheckboxListener(View view) {
        if (view.getTag() == null) hideKeyboard();
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.contAnything:
                if (checked) {
                    ((CheckBox) findViewById(R.id.containsText1CheckBox))
                            .setChecked(false);
                    ((CheckBox) findViewById(R.id.containsText2CheckBox))
                            .setChecked(false);
                    ((CheckBox) findViewById(R.id.containsText3CheckBox))
                            .setChecked(false);
                    ((CheckBox) findViewById(R.id.notContainsTextCheckBox))
                            .setChecked(false);
                    cnt1 = cnt2 = cnt3 = nCnt = "";
                } else if (!((CheckBox) findViewById(R.id.containsText1CheckBox))
                        .isChecked()
                        && !((CheckBox) findViewById(R.id.containsText2CheckBox))
                        .isChecked()
                        && !((CheckBox) findViewById(R.id.containsText3CheckBox))
                        .isChecked()
                        && !((CheckBox) findViewById(R.id.notContainsTextCheckBox))
                        .isChecked()) {
                    ((CheckBox) findViewById(R.id.contAnything)).setChecked(true);
                    cnt1 = cnt2 = cnt3 = nCnt = "";
                }
                break;
            case R.id.containsText1CheckBox:
                if (checked) {
                    ((CheckBox) findViewById(R.id.contAnything)).setChecked(false);
                    EditText contText = (EditText) findViewById(R.id.containsText1);
                    contText.requestFocus();
                    cnt1 = contText.getText().toString();
                } else {
                    cnt1 = "";
                    if (!((CheckBox) findViewById(R.id.containsText2CheckBox))
                            .isChecked()
                            && !((CheckBox) findViewById(R.id.containsText3CheckBox))
                            .isChecked()
                            && !((CheckBox) findViewById(R.id.notContainsTextCheckBox))
                            .isChecked()) {
                        ((CheckBox) findViewById(R.id.contAnything)).setChecked(true);
                        cnt2 = cnt3 = nCnt = ""; }
                }
                break;
            case R.id.containsText2CheckBox:
                if (checked) {
                    ((CheckBox) findViewById(R.id.contAnything)).setChecked(false);
                    EditText contText = (EditText) findViewById(R.id.containsText2);
                    contText.requestFocus();
                    cnt2 = contText.getText().toString();
                } else {
                    cnt2 = "";
                    if (!((CheckBox) findViewById(R.id.containsText1CheckBox))
                            .isChecked()
                            && !((CheckBox) findViewById(R.id.containsText3CheckBox))
                            .isChecked()
                            && !((CheckBox) findViewById(R.id.notContainsTextCheckBox))
                            .isChecked()) {
                        ((CheckBox) findViewById(R.id.contAnything)).setChecked(true);
                        cnt1 = cnt3 = nCnt = ""; }
                }
                break;
            case R.id.containsText3CheckBox:
                if (checked) {
                    ((CheckBox) findViewById(R.id.contAnything)).setChecked(false);
                    EditText contText = (EditText) findViewById(R.id.containsText3);
                    contText.requestFocus();
                    cnt3 = contText.getText().toString();
                } else {
                    cnt3 = "";
                    if (!((CheckBox) findViewById(R.id.containsText1CheckBox))
                            .isChecked()
                            && !((CheckBox) findViewById(R.id.containsText2CheckBox))
                            .isChecked()
                            && !((CheckBox) findViewById(R.id.notContainsTextCheckBox))
                            .isChecked()) {
                        ((CheckBox) findViewById(R.id.contAnything)).setChecked(true);
                        cnt1 = cnt2 = nCnt = ""; }
                }
                break;
            case R.id.notContainsTextCheckBox:
                if (checked) {
                    ((CheckBox) findViewById(R.id.contAnything)).setChecked(false);
                    EditText contText = (EditText) findViewById(R.id.notContainsText);
                    contText.requestFocus();
                    nCnt = contText.getText().toString();
                } else {
                    nCnt = "";
                    if (!((CheckBox) findViewById(R.id.containsText1CheckBox))
                            .isChecked()
                            && !((CheckBox) findViewById(R.id.containsText2CheckBox))
                            .isChecked()
                            && !((CheckBox) findViewById(R.id.containsText3CheckBox))
                            .isChecked()) {
                        ((CheckBox) findViewById(R.id.contAnything)).setChecked(true);
                        cnt1 = cnt2 = cnt3; }
                }
                break;
        }
        // updates RegEx field
        createRegex();
    }

    public void regCFlisteners(View view) {
        // LISTENER: containsText1 changes sent to regex right away
        ((EditText) findViewById(R.id.containsText1))
                .addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        ((CheckBox) findViewById(R.id.containsText1CheckBox))
                                .setChecked(true);
                        ((CheckBox) findViewById(R.id.contAnything))
                                .setChecked(false);
                        cnt1 = s.toString();
                        createRegex();
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        ((CheckBox) findViewById(R.id.containsText1CheckBox))
                                .setChecked(true);
                        ((CheckBox) findViewById(R.id.contAnything))
                                .setChecked(false);
                        cnt1 = s.toString();
                        createRegex();
                    }
                });
        // LISTENER: containsText2 changes sent to regex right away
        ((EditText) findViewById(R.id.containsText2))
                .addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        ((CheckBox) findViewById(R.id.containsText2CheckBox))
                                .setChecked(true);
                        ((CheckBox) findViewById(R.id.contAnything))
                                .setChecked(false);
                        cnt2 = s.toString();
                        createRegex();
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        ((CheckBox) findViewById(R.id.containsText2CheckBox))
                                .setChecked(true);
                        ((CheckBox) findViewById(R.id.contAnything))
                                .setChecked(false);
                        cnt2 = s.toString();
                        createRegex();
                    }
                });
        // LISTENER: containsText3 changes sent to regex right away
        ((EditText) findViewById(R.id.containsText3))
                .addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        ((CheckBox) findViewById(R.id.containsText3CheckBox))
                                .setChecked(true);
                        ((CheckBox) findViewById(R.id.contAnything))
                                .setChecked(false);
                        cnt3 = s.toString();
                        createRegex();
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        ((CheckBox) findViewById(R.id.containsText3CheckBox))
                                .setChecked(true);
                        ((CheckBox) findViewById(R.id.contAnything))
                                .setChecked(false);
                        cnt3 = s.toString();
                        createRegex();
                    }
                });
        // LISTENER: notContainsText changes sent to regex right away
        ((EditText) findViewById(R.id.notContainsText))
                .addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        ((CheckBox) findViewById(R.id.notContainsTextCheckBox))
                                .setChecked(true);
                        ((CheckBox) findViewById(R.id.contAnything))
                                .setChecked(false);
                        nCnt = s.toString();
                        createRegex();
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        ((CheckBox) findViewById(R.id.notContainsTextCheckBox))
                                .setChecked(true);
                        ((CheckBox) findViewById(R.id.contAnything))
                                .setChecked(false);
                        nCnt = s.toString();
                        createRegex();
                    }
                });
    }

    // ENDS WITH FRAGMENT
    public void onEndsWithCheckboxListener(View view) {
        if (view.getTag() == null) hideKeyboard();
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.endsAnything:
                if (checked) {
                    ((CheckBox) findViewById(R.id.endsText1CheckBox))
                            .setChecked(false);
                    ((CheckBox) findViewById(R.id.endsText2CheckBox))
                            .setChecked(false);
                    ((CheckBox) findViewById(R.id.endsText3CheckBox))
                            .setChecked(false);
                    ((CheckBox) findViewById(R.id.notEndsTextCheckBox))
                            .setChecked(false);
                    ew1 = ew2 = ew3 = nEw = "";
                } else if (!((CheckBox) findViewById(R.id.endsText1CheckBox))
                        .isChecked()
                        && !((CheckBox) findViewById(R.id.endsText2CheckBox))
                        .isChecked()
                        && !((CheckBox) findViewById(R.id.endsText3CheckBox))
                        .isChecked()
                        && !((CheckBox) findViewById(R.id.notEndsTextCheckBox))
                        .isChecked()) {
                    ((CheckBox) findViewById(R.id.endsAnything)).setChecked(true);
                    ew1 = ew2 = ew3 = nEw = "";
                }
                break;
            case R.id.endsText1CheckBox:
                if (checked) {
                    ((CheckBox) findViewById(R.id.endsAnything)).setChecked(false);
                    EditText contText = (EditText) findViewById(R.id.endsText1);
                    contText.requestFocus();
                    ew1 = contText.getText().toString();
                } else {
                    ew1 = "";
                    if (!((CheckBox) findViewById(R.id.endsText2CheckBox))
                            .isChecked()
                            && !((CheckBox) findViewById(R.id.endsText3CheckBox))
                            .isChecked()
                            && !((CheckBox) findViewById(R.id.notEndsTextCheckBox))
                            .isChecked()) {
                        ((CheckBox) findViewById(R.id.endsAnything)).setChecked(true);
                        ew1 = ew2 = ew3 = nEw = ""; }
                }
                break;
            case R.id.endsText2CheckBox:
                if (checked) {
                    ((CheckBox) findViewById(R.id.endsAnything)).setChecked(false);
                    EditText contText = (EditText) findViewById(R.id.endsText2);
                    contText.requestFocus();
                    ew2 = contText.getText().toString();
                } else {
                    ew2 = "";
                    if (!((CheckBox) findViewById(R.id.endsText1CheckBox))
                            .isChecked()
                            && !((CheckBox) findViewById(R.id.endsText3CheckBox))
                            .isChecked()
                            && !((CheckBox) findViewById(R.id.notEndsTextCheckBox))
                            .isChecked()) {
                        ((CheckBox) findViewById(R.id.endsAnything)).setChecked(true);
                        ew1 = ew2 = ew3 = nEw = ""; }
                }
                break;
            case R.id.endsText3CheckBox:
                if (checked) {
                    ((CheckBox) findViewById(R.id.endsAnything)).setChecked(false);
                    EditText contText = (EditText) findViewById(R.id.endsText3);
                    contText.requestFocus();
                    ew3 = contText.getText().toString();
                } else {
                    ew3 = "";
                    if (!((CheckBox) findViewById(R.id.endsText1CheckBox))
                            .isChecked()
                            && !((CheckBox) findViewById(R.id.endsText2CheckBox))
                            .isChecked()
                            && !((CheckBox) findViewById(R.id.notEndsTextCheckBox))
                            .isChecked()) {
                        ((CheckBox) findViewById(R.id.endsAnything)).setChecked(true);
                        ew1 = ew2 = ew3 = nEw = ""; }
                }
                break;
            case R.id.notEndsTextCheckBox:
                if (checked) {
                    ((CheckBox) findViewById(R.id.endsAnything)).setChecked(false);
                    EditText contText = (EditText) findViewById(R.id.notEndsText);
                    contText.requestFocus();
                    nEw = contText.getText().toString();
                } else {
                    nEw = "";
                    if (!((CheckBox) findViewById(R.id.endsText1CheckBox))
                            .isChecked()
                            && !((CheckBox) findViewById(R.id.endsText2CheckBox))
                            .isChecked()
                            && !((CheckBox) findViewById(R.id.endsText3CheckBox))
                            .isChecked()) {
                        ((CheckBox) findViewById(R.id.endsAnything)).setChecked(true);
                        ew1 = ew2 = ew3 = nEw = ""; }
                }
                break;
        }
        // updates RegEx field
        createRegex();
    }

    public void regEWFlisteners(View view) {
        //Log.i("takoli", "regEWFlisteners");
        // LISTENER: endsText1 changes sent to regex right away
        ((EditText) findViewById(R.id.endsText1))
                .addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        ((CheckBox) findViewById(R.id.endsText1CheckBox))
                                .setChecked(true);
                        ((CheckBox) findViewById(R.id.endsAnything))
                                .setChecked(false);
                        ew1 = s.toString();
                        createRegex();
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        ((CheckBox) findViewById(R.id.endsText1CheckBox))
                                .setChecked(true);
                        ((CheckBox) findViewById(R.id.endsAnything))
                                .setChecked(false);
                        ew1 = s.toString();
                        createRegex();
                    }
                });
        // LISTENER: endsText2 changes sent to regex right away
        ((EditText) findViewById(R.id.endsText2))
                .addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        ((CheckBox) findViewById(R.id.endsText2CheckBox))
                                .setChecked(true);
                        ((CheckBox) findViewById(R.id.endsAnything))
                                .setChecked(false);
                        ew2 = s.toString();
                        createRegex();
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        ((CheckBox) findViewById(R.id.endsText2CheckBox))
                                .setChecked(true);
                        ((CheckBox) findViewById(R.id.endsAnything))
                                .setChecked(false);
                        ew2 = s.toString();
                        createRegex();
                    }
                });
        // LISTENER: endsText3 changes sent to regex right away
        ((EditText) findViewById(R.id.endsText3))
                .addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        ((CheckBox) findViewById(R.id.endsText3CheckBox))
                                .setChecked(true);
                        ((CheckBox) findViewById(R.id.endsAnything))
                                .setChecked(false);
                        ew3 = s.toString();
                        createRegex();
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        ((CheckBox) findViewById(R.id.endsText3CheckBox))
                                .setChecked(true);
                        ((CheckBox) findViewById(R.id.endsAnything))
                                .setChecked(false);
                        ew3 = s.toString();
                        createRegex();
                    }
                });
        // LISTENER: notendsText changes sent to regex right away
        ((EditText) findViewById(R.id.notEndsText))
                .addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        ((CheckBox) findViewById(R.id.notEndsTextCheckBox))
                                .setChecked(true);
                        ((CheckBox) findViewById(R.id.endsAnything))
                                .setChecked(false);
                        nEw = s.toString();
                        createRegex();
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        ((CheckBox) findViewById(R.id.notEndsTextCheckBox))
                                .setChecked(true);
                        ((CheckBox) findViewById(R.id.endsAnything))
                                .setChecked(false);
                        nEw = s.toString();
                        createRegex();
                    }
                });
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new SectionFragment();
            Bundle args = new Bundle();
            args.putInt(SectionFragment.ARG_SECTION_NUMBER, position + 1);
            fragment.setArguments(args);
            fragment.setUserVisibleHint(true);
            return fragment;
        }

        @Override
        public int getCount() {
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

    public static class SectionFragment extends Fragment {
        public static final String ARG_SECTION_NUMBER = "section_number";
        public int currentPage;
        public View thisView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            currentPage = getArguments().getInt(ARG_SECTION_NUMBER);
            //Log.i("onCreateView", "page: "+currentPage);
            thisView = null;
            switch (currentPage) {
                case 1:
                    thisView = inflater.inflate(R.layout.starts_with_fragment,
                            container, false);
                    break;
                case 2: {
                    thisView = inflater.inflate(R.layout.contains_fragment,
                            container, false);
                }
                break;
                case 3:
                    thisView = inflater.inflate(R.layout.ends_with_fragment,
                            container, false);
                    break;
            }
            return thisView;
        }

        @Override
        public void setUserVisibleHint(final boolean visible) {
            super.setUserVisibleHint(visible);
            if (visible && currentPage == 1) {
                thisView.findViewById(R.id.sideText1).performClick();
            }
            if (visible && currentPage == 2) {
                thisView.findViewById(R.id.sideText2).performClick();
            }
            if (visible && currentPage == 3) {
                thisView.findViewById(R.id.sideText3).performClick();
            }
        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
