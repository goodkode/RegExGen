package net.takoli.regexgen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class AboutDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.about_message)
               .setPositiveButton(R.string.got_it, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // do nothing, just exit 
                   }
               });
        return builder.create();
    }
}