package com.bignerdranch.practice.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bignerdranch.practice.Fragments.MainScreenViewPagerFragment;
import com.bignerdranch.practice.Fragments.MembersNamesRecyclerViewFragment;
import com.bignerdranch.practice.Model.Member;
import com.bignerdranch.practice.R;
import com.bignerdranch.practice.database.FireBaseHandler;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MembersNamesRecyclerViewFragment.CallBacks {

    private int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(2);
        getSupportActionBar().setIcon(R.drawable.ic_fitness_center_black_24dp);
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            startSignInActivity();
        else
            hostFragment();
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
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                FireBaseHandler.getInstance(this)
                        .setUserReference(FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()));
                List<? extends UserInfo> userInfo = FirebaseAuth.getInstance().getCurrentUser().getProviderData();
                HashMap<String, Object> info = new HashMap<>();
                String name = userInfo.get(1).getDisplayName();
                String phone = userInfo.get(1).getPhoneNumber();
                String email = userInfo.get(1).getEmail();
                Uri photoUrl = userInfo.get(1).getPhotoUrl();
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
            } else
                finish();
        }
    }
}
