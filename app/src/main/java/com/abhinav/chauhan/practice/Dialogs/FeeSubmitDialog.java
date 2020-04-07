package com.abhinav.chauhan.practice.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.abhinav.chauhan.practice.Model.FeeRecord;
import com.abhinav.chauhan.practice.Model.Member;
import com.abhinav.chauhan.practice.Preferences.EditPreferences;
import com.abhinav.chauhan.practice.R;
import com.abhinav.chauhan.practice.database.FireBaseHandler;

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
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.fees_dialog, null);
        final EditText amountEditText = view.findViewById(R.id.enter_amount);
        amountEditText.setText(EditPreferences.getInstance().getUserPreference(getContext()).getString(FEES, "500"));
        return new AlertDialog.Builder(getActivity()).setView(view).setTitle(R.string.submit_fees_dialog_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final EditText monthEditText = view.findViewById(R.id.month);
                        int totalFeeSubmittedMonths = Integer.parseInt(monthEditText.getText().toString()) + mMember.getNoOfFeesSubmittedMonths();
                        mMember.setNoOfFeesSubmittedMonths(totalFeeSubmittedMonths);
                        FeeRecord feeRecord = new FeeRecord(Integer.parseInt(amountEditText.getText().toString()));
                        feeRecord.setName(mMember.getMemberName());
                        feeRecord.setId(mMember.getMemberId());
                        upDateDatabase(feeRecord, totalFeeSubmittedMonths);
                        dismiss();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null).create();
    }

    private void upDateDatabase(FeeRecord feeRecord, int totalFeesSubmittedMonths) {


        FireBaseHandler.getInstance(getActivity()).addMemberFee(mMember, feeRecord);

    }
}
