package com.abhinav.chauhan.gymdatamanager.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.abhinav.chauhan.gymdatamanager.R;
import com.abhinav.chauhan.gymdatamanager.database.FireBaseHandler;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class AboutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDeveloperImageView(view);
        setAboutTextView(view);
        setLinks(view);
    }

    private void setDeveloperImageView(View view) {
        final ImageView developerImage = view.findViewById(R.id.developer_img);
        FireBaseHandler.getInstance(getActivity())
                .getMemberImagesReference()
                .child("developer pic.jpg")
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("db", uri.toString());
                        Picasso.with(getActivity())
                                .load(uri)
                                .fit()
                                //.centerCrop()
                                .placeholder(R.drawable.ic_person_black_24dp)
                                .into(developerImage);
                    }
                });
    }

    private void setAboutTextView(View view) {
        final TextView about = view.findViewById(R.id.about_developer);
        FireBaseHandler.getInstance(getActivity())
                .getAboutDeveloper()
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        String s = documentSnapshot.get("about", String.class);
                        if (s != null) {
                            s = s.replace("|", "\n");
                            s = s.replace("$", "\n\n");
                            about.setText(s);
                        }

                    }
                });
    }

    private void setLinks(View view) {
        TextView email = view.findViewById(R.id.email);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "abhinavchauhna@gmail.com", null));
                startActivity(intent);
            }
        });
        TextView github = view.findViewById(R.id.github_repo);
        Spanned html = Html.fromHtml(
                "<a href='https://www.github.com/AbhinavChauhan97/Gym_Data_Manager/'>Github</a>");
        github.setMovementMethod(LinkMovementMethod.getInstance());
        github.setText(html);
    }
}
