package com.abhinav.chauhan.practice.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.abhinav.chauhan.practice.R;

public class MemberNotFoundDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        return new AlertDialog.Builder(getActivity()).setTitle(R.string.member_not_found)
                .setPositiveButton(android.R.string.ok, null)
                .setMessage(R.string.enter_correct_name)
                .setIcon(R.drawable.ic_warning_black_24dp).create();
    }
}
