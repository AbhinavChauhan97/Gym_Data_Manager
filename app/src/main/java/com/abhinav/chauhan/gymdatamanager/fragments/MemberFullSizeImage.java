package com.abhinav.chauhan.gymdatamanager.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.abhinav.chauhan.gymdatamanager.R;

public class MemberFullSizeImage extends Fragment {

    private Drawable mFullImage;
    private String mImageName;
    public MemberFullSizeImage() {
    }

    MemberFullSizeImage(String memberId, Drawable fullImage) {
        mFullImage = fullImage;
        mImageName = memberId + ".jpg";
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.member_full_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(v -> {
        });
        ImageView imageView = view.findViewById(R.id.full_image);
        ProgressBar progressBar = view.findViewById(R.id.progressbar);
        imageView.setImageDrawable(mFullImage);
        /*FireBaseHandler.getInstance(getActivity())
                .getMemberImagesReference()
                .child(mImageName)
                .getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    Picasso.with(getActivity())
                            .load(uri)
                            .placeholder(mPlaceHolder)
                            .into(imageView);
                    progressBar.setVisibility(View.INVISIBLE);
                })
                .addOnFailureListener(e -> {
                    Picasso.with(getActivity())
                            .load(AddNewMemberFragment.file)
                            .into(imageView);
                    progressBar.setVisibility(View.INVISIBLE);
                });*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}
