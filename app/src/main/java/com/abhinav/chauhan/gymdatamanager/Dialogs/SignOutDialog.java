package com.abhinav.chauhan.gymdatamanager.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.abhinav.chauhan.gymdatamanager.Activities.MainActivity;
import com.abhinav.chauhan.gymdatamanager.R;
import com.firebase.ui.auth.AuthUI;

public class SignOutDialog extends DialogFragment {

    private Context mContext;

    public SignOutDialog(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.sign_out)
                .setMessage(R.string.sure_sign_out)
                .setPositiveButton(R.string.yes, (dialog, which) -> signOut()).create();
    }

    private void signOut() {
        AuthUI.getInstance().signOut(mContext)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(mContext, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else
                        Toast.makeText(getContext(), "Sign out failed, try again", Toast.LENGTH_LONG).show();
                });
    }
}
