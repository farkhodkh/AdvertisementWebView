package ru.haknazarovfarkhod.advertisementwebview.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

/**
 * Created by USER on 21.02.2018.
 */

public class SimpleDialog extends DialogFragment {
    private String dialogHeader;
    private String dialogMessage;
    private SimpleDialogListener dialogListener;
    public SimpleDialog() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dialogListener = (SimpleDialogListener) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(dialogHeader);
        builder.setMessage(dialogMessage);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                closeDialog();
                //Toast.makeText(getContext(), "Positive button clicked", Toast.LENGTH_SHORT).show();
            }
        });
        return builder.create();
    }

    private void closeDialog() {
        dialogListener.onDialogClosed();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public String getDialogHeader() {
        return dialogHeader;
    }

    public void setDialogHeader(String dialogHeader) {
        this.dialogHeader = dialogHeader;
    }

    public String getDialogMessage() {
        return dialogMessage;
    }

    public void setDialogMessage(String dialogMessage) {
        this.dialogMessage = dialogMessage;
    }

    public interface SimpleDialogListener{
        void onDialogClosed();
    }
}
