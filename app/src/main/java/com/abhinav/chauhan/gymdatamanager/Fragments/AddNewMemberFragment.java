package com.abhinav.chauhan.gymdatamanager.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.abhinav.chauhan.gymdatamanager.Dialogs.DatePickerDialog;
import com.abhinav.chauhan.gymdatamanager.Model.Member;
import com.abhinav.chauhan.gymdatamanager.R;
import com.abhinav.chauhan.gymdatamanager.database.FireBaseHandler;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

public class AddNewMemberFragment extends Fragment {

    private TextInputEditText mNewMemberName;
    private TextInputEditText mNewMemberPhone;
    private TextInputEditText mNewMemberAddress;
    private TextInputEditText mNewMemberJoiningDate;
    private ImageView mNewMemberPhoto;
    private int REQ_TAKE_PHOTO = 123;
    private Uri imageFileUri;
    private Member mNewMember;

    public static AddNewMemberFragment newInstance() {
        AddNewMemberFragment fragment = new AddNewMemberFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mNewMember = new Member();
        return inflater.inflate(R.layout.add_new_member, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNewMemberName = view.findViewById(R.id.member_name_textinput_edittext);
        mNewMemberPhone = view.findViewById(R.id.member_phone_textinput_edittext);
        mNewMemberAddress = view.findViewById(R.id.member_address_textinput_edittext);
        mNewMemberPhoto = view.findViewById(R.id.new_member_circularimageview);
        setAddThisMemberListener(view);
        setDontAddThisMember(view);
        setClickImageImageButton(view);
        setJoiningDateEditText();
    }

    private void setJoiningDateEditText() {
        mNewMemberJoiningDate = getView().findViewById(R.id.member_joiningdate_textinput_edittext);
        mNewMemberJoiningDate.setText(DateFormat.getInstance().format(new Date()));
        mNewMemberJoiningDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog();
                dialog.setTargetFragment(AddNewMemberFragment.this, 1);
                dialog.show(getFragmentManager(), "datePicker");
            }
        });

    }

    private void setClickImageImageButton(View view) {
        ImageButton mClickImage = view.findViewById(R.id.click_image);
        mClickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File file = new File(getActivity().getExternalFilesDir(null), mNewMember.getMemberId() + ".jpg");
                imageFileUri = FileProvider.getUriForFile(getActivity(), "com.abhinav.chauhan.gymdatamanager.provider", file);
                Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
                startActivityForResult(captureImage, REQ_TAKE_PHOTO);
            }
        });
    }

    private void setAddThisMemberListener(View view) {

        ImageButton mAddThisMember = view.findViewById(R.id.add_this_member);
        mAddThisMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    if (isInputValid()) {
                        if (mNewMember.hasImage()) {
                            byte[] thumbImageData = createImage(300, 300);
                            byte[] fullImageData = createImage(800, 800);
                            if (thumbImageData != null && fullImageData != null) {
                                upLoadImage(mNewMember.getMemberId() + "t.jpg", thumbImageData);
                                upLoadImage(mNewMember.getMemberId() + "t.jpg", fullImageData);
                                addMemberToDataBase();
                                AddNewMemberFragment.this.getActivity().finish();
                            } else
                                Toast.makeText(getActivity(), "image currupted, try again", Toast.LENGTH_LONG).show();
                        } else addMemberToDataBase();

                    } else Toast.makeText(getActivity(), "invalid input", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isInputValid() {
        String name = mNewMemberName.getText().toString().trim();
        String phone = mNewMemberPhone.getText().toString().trim();
        String address = mNewMemberAddress.getText().toString().trim();
        if (name.length() > 0 && phone.matches("(0/91)?[7-9][0-9]{9}") && address.length() > 0) {
            mNewMember.setMemberName(name.toUpperCase());
            mNewMember.setMemberPhone(phone);
            mNewMember.setMemberAddress(address);
            return true;
        }
        return false;
    }

    private void addMemberToDataBase() {
        FireBaseHandler.getInstance(getActivity()).addMember(mNewMember);
    }

    private void setDontAddThisMember(View view) {
        ImageButton mDontAddThisMember = view.findViewById(R.id.cancel_adding_this_member);
        mDontAddThisMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewMemberFragment.this.getActivity().finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQ_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Picasso.with(getActivity()).load(imageFileUri).fit().centerInside().into(mNewMemberPhoto);
            mNewMember.setHasImage(true);
          /*  try {
                Bitmap fullImageBitmap = android.provider.MediaStore.Images.Media
                        .getBitmap(getActivity().getContentResolver(), imageFileUri);
                upLoadImage(mNewMember.getMemberId() + "t.jpg", getImageData(300, 300, fullImageBitmap));
                upLoadImage(mNewMember.getMemberId() + "f.jpg", getImageData(800, 800, fullImageBitmap));
                mNewMember.setHasImage(true);
            } catch (IOException e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }*/
        } else if (requestCode == 231 && resultCode == Activity.RESULT_OK) {
            int[] extra = data.getExtras().getIntArray("date");
            String date = "" + extra[2] + "/" + extra[1] + "/" + extra[0];
            mNewMemberJoiningDate.setText(date);
        }
    }

    private byte[] createImage(int height, int width) {
        byte[] imageData = null;
        try {
            Bitmap fullImageBitmap = android.provider.MediaStore.Images.Media
                    .getBitmap(getActivity().getContentResolver(), imageFileUri);
            imageData = getImageData(height, width, fullImageBitmap);
        } catch (IOException e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
        }
        return imageData;
    }
    private void upLoadImage(String imageName, byte[] imageData) {
        FireBaseHandler.getInstance(getActivity())
                .getMemberImagesReference()
                .child(imageName).putBytes(imageData);
    }

    private byte[] getImageData(int height, int width, Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap thumbnail = Bitmap.createScaledBitmap(bitmap, height, width, false);
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 30, stream);
        return stream.toByteArray();
    }
}

