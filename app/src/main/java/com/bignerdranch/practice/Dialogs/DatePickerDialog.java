package com.bignerdranch.practice.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DatePickerDialog extends DialogFragment {

    private DatePicker mDatePicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        mDatePicker = new DatePicker(getActivity());
        return new AlertDialog.Builder(getActivity())
                .setView(mDatePicker)
                .setTitle("choose Date")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra("date", new int[]{mDatePicker.getYear()
                                , mDatePicker.getMonth()
                                , mDatePicker.getDayOfMonth()});
                        getTargetFragment().
                                onActivityResult(231, Activity.RESULT_OK, intent);
                    }
                }).create();
    }
}
