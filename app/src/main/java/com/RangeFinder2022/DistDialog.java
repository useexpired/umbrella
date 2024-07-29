package com.rangefinder2022;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.slider.RangeSlider;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class DistDialog {

    private Context ctx;
    private RangeSlider distSlider;
    private TextView txtDistMax;

    private FragmentManager fm;

    public DistDialog() {}
    public DistDialog(AppCompatActivity acti, RangeSlider distSlider, TextView txtDistMax) {
        this.ctx = acti.getApplicationContext();
        this.distSlider = distSlider;
        this.txtDistMax = txtDistMax;
    }

    public void fireDialog(FragmentManager fm2, boolean status, int wrongMax) {
        SettinDialogFrag dialog = new SettinDialogFrag();
        List<Float> val = distSlider.getValues();
        Bundle bundle = new Bundle();
        bundle.putInt("min", 1 + Math.round(val.get(0)));
        bundle.putInt("max", Math.round(val.get(1)));
        bundle.putInt("status", status ? 1 : 0);
        bundle.putInt("prev", wrongMax);
        dialog.setArguments(bundle);
        dialog.show(fm2, "SettinDialogFrag");
    }

    public void sleepyPositiveClick(DialogFragment dialog, int freqMax, int distMax) {
        Toast.makeText(ctx, " new freq max=" + freqMax + " dist=" + distMax, Toast.LENGTH_SHORT).show();
        Log.i("help", Math.round(distSlider.getValues().get(0)) + " - " + distMax);
        final int x2 = Math.round(distSlider.getValues().get(1));
        if (distMax <= x2) {
            Log.i("help", x2 + " is smaller than " + distMax);
            distSlider.setValues(distSlider.getValues().get(0), (float) distMax);
            txtDistMax.setText(Math.round(distSlider.getValues().get(1)) + "m");
        }
        distSlider.setValueTo((float) distMax);
    }

    public void sleepyNegativeClick(DialogFragment dialog) {}
}