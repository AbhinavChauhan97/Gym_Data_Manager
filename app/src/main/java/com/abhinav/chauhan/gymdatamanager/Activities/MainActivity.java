package com.abhinav.chauhan.gymdatamanager.Activities;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.abhinav.chauhan.gymdatamanager.Fragments.MainScreenViewPagerFragment;
import com.abhinav.chauhan.gymdatamanager.Fragments.MembersNamesRecyclerViewFragment;
import com.abhinav.chauhan.gymdatamanager.Model.Member;
import com.abhinav.chauhan.gymdatamanager.MyApplication;
import com.abhinav.chauhan.gymdatamanager.Preferences.EditPreferences;
import com.abhinav.chauhan.gymdatamanager.R;
import com.abhinav.chauhan.gymdatamanager.database.FireBaseHandler;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;


public final class MainActivity extends AppCompatActivity implements MembersNamesRecyclerViewFragment.CallBacks {

    private int RC_SIGN_IN = 1;
    private int RC_PATTERN = 2;

    private void showBiometricPrompt() {
        getWindow().getDecorView().getRootView().setAlpha(0.2f);
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(MainActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                hostFragment();
                getWindow().getDecorView().getRootView().setAlpha(1.0f);
            }

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                switch (errorCode) {
                    case BiometricPrompt.ERROR_NEGATIVE_BUTTON:
                    case BiometricPrompt.ERROR_LOCKOUT:
                    case BiometricPrompt.ERROR_CANCELED:
                        finish();
                }
            }
        });
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(R.string.finger_prompt_title))
                .setSubtitle(getString(R.string.finger_prompt_subtitle))
                .setNegativeButtonText(getString(R.string.finger_prompt_exitbutton))
                .build();
        biometricPrompt.authenticate(promptInfo);
    }

    private void showPatternPrompt() {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        Intent intent = keyguardManager
                .createConfirmDeviceCredentialIntent(getString(R.string.enter_pattern), getString(R.string.pattern_required));
        startActivityForResult(intent, RC_PATTERN);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // getSupportActionBar().setIcon(R.drawable.ic_fitness_center_black_24dp);
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startSignInActivity();
            return;
        }
        SharedPreferences userPreference = EditPreferences.getInstance().getUserPreference(this);
        if (userPreference.getBoolean("security", false)) {
            if (userPreference.getString("auth", "").equals("finger")) {
                if (BiometricManager.from(this).canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
                    showBiometricPrompt();
                } else hostFragment();
            } else {
                if (((KeyguardManager) this.getSystemService(KEYGUARD_SERVICE)).isKeyguardSecure())
                    showPatternPrompt();
                else hostFragment();
            }
        } else hostFragment();




    }

    private void hostFragment() {
        setContentView(R.layout.fragment_container);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new MainScreenViewPagerFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    @Override
    public void onMemberNamesPressed(Member member) {
        Intent intent = new Intent(this, MemberInfoActivity.class);
        intent.putExtra("Member", member);
        startActivity(intent);
    }

    private void startSignInActivity() {

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .setLogo(R.drawable.ic_gym)
                .build(), RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            HashMap<String, Object> info = new HashMap<>();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            assert user != null;
            String name = user.getDisplayName();
            String phone = user.getPhoneNumber();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            if (name != null)
                info.put("NAME", name);
            if (phone != null)
                info.put("PHONE", phone);
            if (email != null)
                info.put("E-MAIL", email);
            if (photoUrl != null)
                info.put("PhotoUrl", photoUrl.toString());
            FireBaseHandler.getInstance(this)
                    .getUserReference().set(info);
            hostFragment();
        } else if (requestCode == RC_PATTERN) {
            if (resultCode == RESULT_CANCELED) finish();
            else if (resultCode == RESULT_OK) hostFragment();
        } else
            finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MyApplication.getInstance()
                .setLocale("hi", newBase);
    }
}
