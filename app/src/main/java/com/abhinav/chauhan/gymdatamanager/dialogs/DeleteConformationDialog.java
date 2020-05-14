package com.abhinav.chauhan.gymdatamanager.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.abhinav.chauhan.gymdatamanager.R;
import com.abhinav.chauhan.gymdatamanager.database.FireBaseHandler;
import com.abhinav.chauhan.gymdatamanager.model.Member;

import java.io.File;

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
                    eraseImages();
                    upDateDataBase();
                    DeleteConformationDialog.this.dismiss();
                    requireActivity().finish();
                }).setNegativeButton(R.string.no, null).create();
    }

    private void eraseImages() {
        new File(getActivity().getExternalCacheDir() + "/thumbnails/" + mMember.getMemberId() + "_180x180.jpg").delete();
        new File(getActivity().getExternalCacheDir() + "/full_images/" + mMember.getMemberId() + ".jpg").delete();
    }
    private void upDateDataBase() {
        FireBaseHandler.getInstance(getActivity()).deleteMember(mMember);
    }
}
