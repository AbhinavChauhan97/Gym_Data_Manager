package com.abhinav.chauhan.gymdatamanager.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.abhinav.chauhan.gymdatamanager.Activities.MainActivity;
import com.abhinav.chauhan.gymdatamanager.R;
import com.abhinav.chauhan.gymdatamanager.database.FireBaseHandler;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Map;
public class AccountFragment extends Fragment {

    private TextView delAccText;
    private ProgressBar delAccProgress;
    private Button deleteAcc;
    private Button signOut;
    private boolean deletingAcc;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        delAccProgress = view.findViewById(R.id.delete_acc_progress);
        delAccProgress.setVisibility(View.INVISIBLE);
        delAccText = view.findViewById(R.id.delete_acc_text);
        delAccText.setVisibility(View.INVISIBLE);
        setUserDetailsTable();
        setSignOut(view);
        setDeleteAccount(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("ACCOUNT SETTINGS");
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("SETTINGS");
    }

    private void setUserDetailsTable() {
        final GridLayout gridLayout = getView().findViewById(R.id.info_table);
        FireBaseHandler.getInstance(getActivity()).getUserReference()
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> details = documentSnapshot.getData();
                        if (details != null) {
                            for (String key : details.keySet()) {
                                if (!key.equals("PhotoUrl")) {
                                    gridLayout.addView(getTextView(Typeface.BOLD, Color.BLACK, key.concat("  -  ")));
                                    gridLayout.addView(getTextView(Typeface.ITALIC, Color.DKGRAY, details.get(key).toString()));
                                } else {
                                    final ImageView userImage = getView().findViewById(R.id.user_image);
                                    Picasso.with(getActivity())
                                            .load(details.get(key).toString())
                                            .fit()
                                            .into(userImage);
                                }
                            }
                        }
                    }
                });
    }

    private TextView getTextView(int style, int color, String text) {
        TextView textView = new TextView(getActivity());
        textView.setText(text);
        textView.setTypeface(null, style);
        textView.setTextColor(color);
        textView.setTextSize(20.0f);
        return textView;
    }

    private void setDeleteAccount(View view) {
        deleteAcc = view.findViewById(R.id.delete_acc);
        deleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.delete_account)
                        .setMessage(R.string.sure_delete_account)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //                                deleteAccount();
                            }
                        }).create().show();

            }
        });
    }
    private void setSignOut(View view) {
        signOut = view.findViewById(R.id.sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.sign_out)
                        .setMessage(R.string.sure_sign_out)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                signOut();
                            }
                        }).create().show();
            }
        });
    }

    private void signOut() {
        FirebaseFirestore.getInstance().clearPersistence()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AuthUI.getInstance().signOut(getActivity())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            startSignInActivity();
                                        } else
                                            Toast.makeText(getContext(), "Sign out failed, try again", Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                });
    }

    private void deleteAccount() {

        delAccText.setVisibility(View.VISIBLE);
        delAccProgress.setVisibility(View.VISIBLE);
        deleteAcc.setEnabled(false);
        signOut.setEnabled(false);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (account != null && user != null) {
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            user.reauthenticate(credential)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("db", "re-authenticated");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void startSignInActivity() {
        FireBaseHandler.getInstance(getActivity()).nullInstance();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
