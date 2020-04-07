package com.bignerdranch.practice.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bignerdranch.practice.Model.Member;
import com.bignerdranch.practice.R;
import com.bignerdranch.practice.database.FireBaseHandler;

public class DeleteConformationDialog extends DialogFragment {

    private Member mMember;

    private DeleteConformationDialog(Member member) {
        mMember = member;
    }

    public static DeleteConformationDialog newInstance(Member member) {
        DeleteConformationDialog fragment = new DeleteConformationDialog(member);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        return new AlertDialog.Builder(getActivity()).setMessage(getString(R.string.sure_delete, mMember.getMemberName()))
                .setIcon(R.drawable.baseline_delete_black_18)
                .setTitle(R.string.confirm_delete).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        upDateDataBase();
                        DeleteConformationDialog.this.dismiss();
                        getActivity().finish();
                    }
                }).setNegativeButton(R.string.no, null).create();
    }

    private void upDateDataBase() {
        FireBaseHandler.getInstance(getActivity()).deleteMemberWithFeeRecords(mMember);
    }
}
