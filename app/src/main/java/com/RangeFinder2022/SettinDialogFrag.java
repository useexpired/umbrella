package com.rangefinder2022;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class SettinDialogFrag extends DialogFragment {

    public interface SettinDialogListener {
        void onDialogPos(DialogFragment dialog, int freqMax, int DistanceMax, boolean status);
    }
    SettinDialogListener listener;

    @Override
    public void onAttach(Context ctx) {
        super.onAttach(ctx);
        try {
            listener = (SettinDialogListener) ctx;
        } catch (ClassCastException e) {
            throw new ClassCastException(" must implement SettinDialogListener");
        }
    }

    private EditText editMax;
    private int min=1, max=60, prevVal;
    private Boolean isValidValue;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            min= getArguments().getInt("min");
            max = getArguments().getInt("max");
            final int s=getArguments().getInt("status");
            isValidValue = s == 1;
            prevVal=getArguments().getInt("prev");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.RangeDialogTheme);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View vDialog = inflater.inflate(R.layout.dialog_setting, null);
        builder.setView(vDialog)
                .setMessage("Maximum Distance : "+ min +" - 60(m)")
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        EditText editMax = vDialog.findViewById(R.id.editDistance);
                        final int editVal = Integer.parseInt(editMax.getText().toString());
                        int freqMax = 200;

                        listener.onDialogPos(com.rangefinder2022.SettinDialogFrag.this,
                                freqMax, editVal,
                                (editVal >= min && editVal <= 60)
                        );

                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        editMax = vDialog.findViewById(R.id.editDistance);
        if( max !=0){
            editMax.setHint(Integer.toString(max));
        }
        if(!isValidValue){
            editMax.setTextColor( getResources().getColor(R.color.colorDialogDistTxtWrong));
            editMax.setText(Integer.toString(prevVal));
        }else{
            editMax.setTextColor( getResources().getColor(R.color.colorDialogDistTxt));
        }
        return builder.create();
    }
}
