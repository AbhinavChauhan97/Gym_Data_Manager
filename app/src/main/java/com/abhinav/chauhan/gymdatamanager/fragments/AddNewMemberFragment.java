package com.abhinav.chauhan.gymdatamanager.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.abhinav.chauhan.gymdatamanager.R;
import com.abhinav.chauhan.gymdatamanager.database.FireBaseHandler;
import com.abhinav.chauhan.gymdatamanager.dialogs.DatePickerDialog;
import com.abhinav.chauhan.gymdatamanager.model.Member;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

public final class AddNewMemberFragment extends Fragment {

    private TextInputEditText mNewMemberName;
    private TextInputEditText mNewMemberPhone;
    private TextInputEditText mNewMemberAddress;
    private TextInputEditText mNewMemberJoiningDate;
    private ImageView mNewMemberPhoto;
    private int REQ_TAKE_PHOTO = 123;
    static File file = null;
    private int DATE_PICKER = 321;
    private int CONTACT_PICKER = 312;
    private Member mNewMember;

    public static AddNewMemberFragment newInstance() {
        return new AddNewMemberFragment();
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
        setContactPickerButton(view);
        setClickImageImageButton(view);
        setJoiningDateEditText(view);
    }

    private void setJoiningDateEditText(View view) {
        mNewMemberJoiningDate = view.findViewById(R.id.member_joiningdate_textinput_edittext);
        mNewMemberJoiningDate.setText(DateFormat.getInstance().format(new Date()));
        mNewMemberJoiningDate.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog();
            dialog.setTargetFragment(AddNewMemberFragment.this, DATE_PICKER);
            assert getFragmentManager() != null;
            dialog.show(getFragmentManager(), "datePicker");
        });

    }

    private void setClickImageImageButton(View view) {
        ImageButton mClickImage = view.findViewById(R.id.click_image);
        mClickImage.setOnClickListener(v -> {

            file = new File(getActivity().getExternalFilesDir(null), mNewMember.getMemberId() + ".jpg");
            Uri imageFileUri = FileProvider.getUriForFile(getActivity(), "com.abhinav.chauhan.gymdatamanager.provider", file);
            Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
            captureImage.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(captureImage, REQ_TAKE_PHOTO);
        });
    }

    private void setAddThisMemberListener(View view) {

        Button mAddThisMember = view.findViewById(R.id.add_member);
        mAddThisMember.setOnClickListener(v -> {
            {
                if (isInputValid()) {
                    if (mNewMember.isHasImage()) {
                        Bitmap image = BitmapFactory.decodeFile(file.getAbsolutePath());
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.JPEG, 20, stream);
                        FireBaseHandler.getInstance(getActivity()).addMember(mNewMember, stream.toByteArray(), stream.toByteArray());
                    } else
                        FireBaseHandler.getInstance(getActivity()).addMember(mNewMember);
                    AddNewMemberFragment.this.requireActivity().finish();
                } else
                    Toast.makeText(getActivity(), R.string.invalid_input, Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isInputValid() {
        String name = Objects.requireNonNull(mNewMemberName.getText()).toString().replaceAll("\\s{2,}", " ");
        String phone = Objects.requireNonNull(mNewMemberPhone.getText()).toString().replaceAll("\\s+", "");
        String address = Objects.requireNonNull(mNewMemberAddress.getText()).toString().replaceAll("\\s{2,}", "");
        if (address.matches("[a-zA-Z0-9 ]+") && phone.matches("(?:\\+9[1-9])?[7-9][0-9]{9}") && name.matches("[a-zA-Z ]+")) {
            mNewMember.setMemberName(name.toUpperCase());
            mNewMember.setMemberPhone(phone);
            mNewMember.setMemberAddress(address);
            return true;
        }
        return false;
    }

    private void setContactPickerButton(View view) {
        Button button = view.findViewById(R.id.add_from_contacts);
        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        button.setOnClickListener(v -> startActivityForResult(pickContact, CONTACT_PICKER));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Picasso.with(getActivity()).load(file).fit().centerInside().into(mNewMemberPhoto);
            mNewMember.setHasImage(true);
        } else if (requestCode == DATE_PICKER && resultCode == Activity.RESULT_OK && data != null) {
            int[] extra = data.getExtras().getIntArray("date");
            assert extra != null;
            String date = "" + extra[2] + "/" + extra[1] + "/" + extra[0];
            mNewMemberJoiningDate.setText(date);
        } else if (requestCode == CONTACT_PICKER && data != null) {

            @SuppressLint("Recycle") Cursor c1 = requireActivity()
                    .getContentResolver()
                    .query(data.getData(), new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                            ContactsContract.CommonDataKinds.Photo.DISPLAY_NAME}, null, null, null);
            assert c1 != null;
            c1.moveToFirst();
            mNewMemberName.setText(c1.getString(1));
            mNewMemberPhone.setText(c1.getString(0));
        }
    }
}

