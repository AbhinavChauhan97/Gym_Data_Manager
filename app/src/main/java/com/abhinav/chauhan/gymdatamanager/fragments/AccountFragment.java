package com.abhinav.chauhan.gymdatamanager.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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

import com.abhinav.chauhan.gymdatamanager.R;
import com.abhinav.chauhan.gymdatamanager.activities.MainActivity;
import com.abhinav.chauhan.gymdatamanager.database.FireBaseHandler;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AccountFragment extends Fragment {

    private TextView delAccText;
    private ProgressBar delAccProgress;
    private Button deleteAcc;
    private Button signOut;

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
        ((AppCompatActivity) getActivity())
                .getSupportActionBar()
                .setTitle(getString(R.string.account));
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity())
                .getSupportActionBar()
                .setTitle(R.string.settings);
    }

    private void setUserDetailsTable() {
        final GridLayout gridLayout = getView().findViewById(R.id.info_table);
        FireBaseHandler.getInstance(getActivity()).getUserReference()
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Map<String, Object> details = documentSnapshot.getData();
                    if (details != null) {
                        for (String key : details.keySet()) {
                            if (!key.equals("PhotoUrl")) {
                                gridLayout.addView(getTextView(Typeface.BOLD, getResources().getColor(R.color.primary_text), key.concat("  -  ")));
                                gridLayout.addView(getTextView(Typeface.ITALIC, getResources().getColor(R.color.secondary_text), details.get(key).toString()));
                            } else {
                                final ImageView userImage = getView().findViewById(R.id.user_image);
                                Picasso.with(getActivity())
                                        .load(details.get(key).toString())
                                        .placeholder(R.drawable.ic_person_black_24dp)
                                        .fit()
                                        .into(userImage);
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
        deleteAcc.setOnClickListener(v -> new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_account)
                .setMessage(R.string.sure_delete_account)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> deleteAccount())
                .create()
                .show());
    }

    private void setSignOut(View view) {
        signOut = view.findViewById(R.id.sign_out);
        signOut.setOnClickListener(v -> new AlertDialog.Builder(getActivity())
                .setTitle(R.string.sign_out)
                .setMessage(R.string.sure_sign_out)
                .setPositiveButton(R.string.yes, (dialog, which) -> signOut()).create().show());
    }

    private void signOut() {
        FirebaseFirestore.getInstance().clearPersistence()
                .addOnCompleteListener(task -> AuthUI.getInstance().signOut(getActivity())
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                startSignInActivity();
                            } else
                                Toast.makeText(getContext(), R.string.sign_out_failed, Toast.LENGTH_LONG).show();
                        }));
    }

    private void deleteAccount() {

        delAccText.setVisibility(View.VISIBLE);
        delAccProgress.setVisibility(View.VISIBLE);
        deleteAcc.setEnabled(false);
        signOut.setEnabled(false);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String providerId = user.getProviderData().get(1).getProviderId();
            if (providerId.equals("google.com")) {
                deleteGoogleClient(user);
            } else if (providerId.equals("phone")) {
                deletePhoneClient(user);
            }
        }
    }

    private void deletePhoneClient(FirebaseUser user) {

        String phoneNumber = user.getProviderData().get(1).getPhoneNumber();
        PhoneAuthProvider.getInstance()
                .verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, getActivity(), new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        user.reauthenticate(phoneAuthCredential)
                                .addOnSuccessListener(aVoid -> user.delete()
                                        .addOnSuccessListener(aVoid1 -> {
                                            Toast.makeText(getActivity(), R.string.account_deleted, Toast.LENGTH_SHORT).show();
                                            startSignInActivity();
                                        })
                                        .addOnFailureListener(e -> e.printStackTrace()))
                                .addOnFailureListener(e -> e.printStackTrace());
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void deleteGoogleClient(FirebaseUser user) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignIn.getClient(getActivity(), gso).silentSignIn()
                .addOnSuccessListener(googleSignInAccount ->
                {
                    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
                    assert account != null;
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    user.reauthenticate(credential)
                            .addOnSuccessListener(
                                    aVoid1 -> user.delete()
                                            .addOnSuccessListener(aVoid2 -> {
                                                Toast.makeText(getActivity(), R.string.account_deleted, Toast.LENGTH_SHORT).show();
                                                startSignInActivity();
                                            })
                                            .addOnFailureListener(e -> e.printStackTrace()))
                            .addOnFailureListener(e -> e.printStackTrace());
                })
                .addOnFailureListener(e -> e.printStackTrace());
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
