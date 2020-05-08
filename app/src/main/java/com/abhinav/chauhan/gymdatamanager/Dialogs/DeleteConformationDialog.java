package com.abhinav.chauhan.gymdatamanager.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.abhinav.chauhan.gymdatamanager.Model.Member;
import com.abhinav.chauhan.gymdatamanager.R;
import com.abhinav.chauhan.gymdatamanager.database.FireBaseHandler;

public class DeleteConformationDialog extends DialogFragment {

    private Member mMember;

    private DeleteConformationDialog(Member member) {
        mMember = member;
    }

    public static DeleteConformationDialog newInstance(Member member) {
        return new DeleteConformationDialog(member);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        return new AlertDialog.Builder(getActivity()).setMessage(getString(R.string.sure_delete, mMember.getMemberName()))
                .setIcon(R.drawable.baseline_delete_24)
                .setTitle(R.string.confirm_delete).setPositiveButton(R.string.yes, (dialog, which) -> {
                    upDateDataBase();
                    DeleteConformationDialog.this.dismiss();
                    requireActivity().finish();
                }).setNegativeButton(R.string.no, null).create();
    }

    private void upDateDataBase() {
        FireBaseHandler.getInstance(getActivity()).deleteMember(mMember);
    }
}
