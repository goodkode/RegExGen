package net.takoli.regexgen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class AboutDialogFragment extends DialogFragment {
	View v;
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.about_dialog, null));
        AlertDialog ad = builder.create();
        ad.setTitle("About RegEx Generator");
        ad.setButton(AlertDialog.BUTTON_NEGATIVE, "Cool",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        ad.show();
        return ad;
    }
}