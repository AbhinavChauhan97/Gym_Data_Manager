package com.abhinav.chauhan.gymdatamanager.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.abhinav.chauhan.gymdatamanager.Fragments.MainScreenViewPagerFragment;
import com.abhinav.chauhan.gymdatamanager.Fragments.MembersNamesRecyclerViewFragment;
import com.abhinav.chauhan.gymdatamanager.Model.Member;
import com.abhinav.chauhan.gymdatamanager.R;
import com.abhinav.chauhan.gymdatamanager.database.FireBaseHandler;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public final class MainActivity extends AppCompatActivity implements MembersNamesRecyclerViewFragment.CallBacks {

    private int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getSupportActionBar().setElevation(2);
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
            } else
                finish();
        }
    }
}
