package com.abhinav.chauhan.gymdatamanager.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.abhinav.chauhan.gymdatamanager.Model.FeeRecord;
import com.abhinav.chauhan.gymdatamanager.Model.Member;
import com.abhinav.chauhan.gymdatamanager.Preferences.EditPreferences;
import com.abhinav.chauhan.gymdatamanager.R;
import com.abhinav.chauhan.gymdatamanager.database.FireBaseHandler;

public class FeeSubmitDialog extends DialogFragment {

    private String FEES = "Fees Per Month";
    private Member mMember;

    private FeeSubmitDialog(Member member) {
        mMember = member;
    }

    public static FeeSubmitDialog newInstance(Member member) {
        return new FeeSubmitDialog(member);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        final String[] noOfMonths = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.fees_dialog, null);
        final Spinner monthSpinner = view.findViewById(R.id.month);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, noOfMonths);
        monthSpinner.setAdapter(adapter);
        final EditText amountEditText = view.findViewById(R.id.enter_amount);
        amountEditText.setText(EditPreferences.getInstance().getUserPreference(getContext()).getString(FEES, "500"));
        return new AlertDialog.Builder(getActivity()).setView(view).setTitle(R.string.submit_fees_dialog_title)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    int totalFeeSubmittedMonths = Integer.parseInt(noOfMonths[monthSpinner.getSelectedItemPosition()]) + mMember.getNoOfFeesSubmittedMonths();
                    mMember.setNoOfFeesSubmittedMonths(totalFeeSubmittedMonths);
                    FeeRecord feeRecord = new FeeRecord(Integer.parseInt(amountEditText.getText().toString()));
                    feeRecord.setName(mMember.getMemberName());
                    feeRecord.setId(mMember.getMemberId());
                    upDateDatabase(feeRecord);
                    dismiss();
                })
                .setNegativeButton(android.R.string.cancel, null).create();
    }

    private void upDateDatabase(FeeRecord feeRecord) {

        FireBaseHandler.getInstance(getActivity()).addMemberFee(mMember, feeRecord);

    }
}
