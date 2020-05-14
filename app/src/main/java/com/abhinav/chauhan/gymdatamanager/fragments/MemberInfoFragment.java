package com.abhinav.chauhan.gymdatamanager.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abhinav.chauhan.gymdatamanager.R;
import com.abhinav.chauhan.gymdatamanager.database.FireBaseHandler;
import com.abhinav.chauhan.gymdatamanager.dialogs.DeleteConformationDialog;
import com.abhinav.chauhan.gymdatamanager.dialogs.FeeSubmitDialog;
import com.abhinav.chauhan.gymdatamanager.model.FeeRecord;
import com.abhinav.chauhan.gymdatamanager.model.Member;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

public final class MemberInfoFragment extends Fragment {

    private TextInputLayout mPhone;
    private Member mMember;
    private String FEES_DIALOG = "Fees Dialog";
    private String DELETE_MEMBER_DIALOG = "Delete Member";
    private ProgressBar mProgressBar;

    public MemberInfoFragment() {
    }
    private MemberInfoFragment(Member member) {
        this.setRetainInstance(true);
        mMember = member;
    }


    public static MemberInfoFragment newInstance(Member member) {
        return new MemberInfoFragment(member);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.member_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressBar = view.findViewById(R.id.progressbar);
        setSubmitFeesFab(view);
        setMessageMemberButton(view);
        setCallMemberButton(view);
        setDeleteMemberButton(view);
        setMemberNameTextInputLayout(view);
        setMemberAddressTextInputLayout(view);
        setMemberPhoneTextInputLayout(view);
        setMemberRecordsRecyclerView(view);
        setMemberJoiningDateTextInputlayout(view);
        setMemberImageImageView(view);
    }

    private void setSubmitFeesFab(View view) {
        FloatingActionButton submitFeesFab = view.findViewById(R.id.submit_fee_fab);
        submitFeesFab.setOnClickListener(v -> FeeSubmitDialog.newInstance(mMember)
                .show(getFragmentManager(), FEES_DIALOG));
    }

    private void setCallMemberButton(View view) {
        ImageButton callMember = view.findViewById(R.id.call_member);
        callMember.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPhone.getEditText().getText()))));
    }

    private void setMessageMemberButton(View view) {
        ImageButton messageMember = view.findViewById(R.id.message_member);
        messageMember.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + mPhone.getEditText().getText()))));
    }

    private void setDeleteMemberButton(View view) {
        ImageButton deleteMember = view.findViewById(R.id.delete_member);
        deleteMember.setOnClickListener(v -> DeleteConformationDialog.newInstance(mMember).show(getParentFragmentManager(), DELETE_MEMBER_DIALOG));
    }

    private void setMemberNameTextInputLayout(View view) {
        TextInputLayout name = view.findViewById(R.id.member_name_textinputlayout);
        name.getEditText().setText(mMember.getMemberName());
        name.getEditText().setEnabled(false);
        name.setEndIconOnClickListener(new MemberEditor(name));
    }

    private void setMemberPhoneTextInputLayout(View view) {
        mPhone = view.findViewById(R.id.member_phone_textinputlayout);
        mPhone.getEditText().setText(mMember.getMemberPhone());
        mPhone.getEditText().setEnabled(false);
        mPhone.setEndIconOnClickListener(new MemberEditor(mPhone));
    }

    private void setMemberAddressTextInputLayout(View view) {
        final TextInputLayout address = view.findViewById(R.id.member_address_textinputlayout);
        address.getEditText().setText(mMember.getMemberAddress());
        address.getEditText().setEnabled(false);
        address.setEndIconOnClickListener(new MemberEditor(address));
    }

    private void setMemberJoiningDateTextInputlayout(View view) {
        TextInputLayout date = view.findViewById(R.id.member_joiningdate_textinputlayout);
        date.getEditText().setText(mMember.getMemberJoiningDate());
        date.getEditText().setEnabled(false);
        date.setEndIconDrawable(null);
    }

    private void setMemberRecordsRecyclerView(View view) {
        RecyclerView mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Query baseQuery = FireBaseHandler.getInstance(getActivity()).getFeeReference().whereEqualTo("id", mMember.getMemberId());
        FirestoreRecyclerOptions<FeeRecord> options = new FirestoreRecyclerOptions.Builder<FeeRecord>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery, FeeRecord.class)
                .build();
        FirestoreRecyclerAdapter<FeeRecord, Holder> mAdapter = new FirestoreRecyclerAdapter<FeeRecord, Holder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Holder holder, int i, @NonNull FeeRecord feeRecord) {
                holder.bind(feeRecord);
            }
            @NonNull
            @Override
            public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new Holder(LayoutInflater.from(getActivity()), parent);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                mProgressBar.setVisibility(View.INVISIBLE);
                if (getItemCount() == 0)
                    getView().findViewById(R.id.empty_message).setVisibility(View.VISIBLE);
                else
                    getView().findViewById(R.id.empty_message).setVisibility(View.INVISIBLE);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setMemberImageImageView(View view) {
        final ImageView imageView = view.findViewById(R.id.circular_imageview);
        imageView.setOnClickListener(v -> {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(getView().getId(), new MemberFullSizeImage(mMember.getMemberId(), imageView.getDrawable()))
                    .addToBackStack(null)
                    .commit();
        });
        if (mMember.isHasImage()) {
            File fullSizeImageDir = new File(getActivity().getExternalCacheDir().getAbsolutePath()
                    + "/full_images");
            if (!fullSizeImageDir.exists()) {
                fullSizeImageDir.mkdir();
            }
            File[] fullSizeImages = fullSizeImageDir.listFiles();
            for (File image : fullSizeImages) {
                if (image.getName().equals(mMember.getMemberId() + ".jpg")) {
                    Picasso.with(getActivity())
                            .load(image)
                            .into(imageView);
                    Log.d("db", "here");
                    return;
                }
            }
            FireBaseHandler.getInstance(getActivity())
                    .getMemberImagesReference()
                    .child(mMember.getMemberId() + ".jpg").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        Picasso.with(getActivity())
                            .load(uri.toString())
                            .fit()
                            .centerInside()
                            .placeholder(R.drawable.ic_person_black_24dp)
                                .into(imageView);
                        Thread thread = new Thread() {
                            public void run() {
                                File file = new File(fullSizeImageDir.getAbsolutePath() + "/" + mMember.getMemberId() + ".jpg");
                                try {
                                    Bitmap bitmap = Picasso.with(getActivity())
                                            .load(uri).get();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        thread.start();

                    })
                    .addOnFailureListener(e -> Picasso.with(getActivity())
                            .load(AddNewMemberFragment.file)
                            .fit()
                            .centerInside()
                            .placeholder(R.drawable.ic_person_black_24dp)
                            .into(imageView));
        }
    }

    private static class Holder extends RecyclerView.ViewHolder {

        private TextView mAmount;
        private TextView mDate;

        Holder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.member_past_records, parent, false));
            this.mAmount = itemView.findViewById(R.id.member_record_amount_textview);
            this.mDate = itemView.findViewById(R.id.member_record_date_textview);
        }

        void bind(FeeRecord record) {
            this.mAmount.setText(String.valueOf(record.getAmount()));
            this.mDate.setText(record.getDate());
        }
    }

    private final class MemberEditor implements View.OnClickListener {
        private TextInputLayout mTextInputLayout;
        private String text;
        private String upDateValue;

        MemberEditor(TextInputLayout textInputLayout) {
            mTextInputLayout = textInputLayout;
        }

        @Override
        public void onClick(View v) {
            text = mTextInputLayout.getEditText().getText().toString().trim();
            if (mTextInputLayout.getEditText().isEnabled()) {
                if (canUpdate()) {
                    upDateDatabase(mTextInputLayout.getTag().toString());
                    mTextInputLayout.getEditText().setEnabled(false);
                    mTextInputLayout.setEndIconDrawable(R.drawable.ic_mode_edit_black_24dp);
                    Toast.makeText(getActivity(), R.string.saved_changes, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getActivity(), R.string.invalid_input, Toast.LENGTH_SHORT).show();
            } else {
                mTextInputLayout.getEditText().setEnabled(true);
                mTextInputLayout.setEndIconDrawable(R.drawable.ic_check_black_24dp);
            }
        }

        private boolean canUpdate() {
            switch (mTextInputLayout.getId()) {
                case R.id.member_name_textinputlayout:
                    text = text.replaceAll("\\s{2,}", "");
                    if (text.matches("[a-zA-Z ]+")) {
                        upDateValue = text;
                        mMember.setMemberName(upDateValue);
                        return true;
                    }
                    return false;
                case R.id.member_address_textinputlayout:
                    text = text.replaceAll("\\s{2,}", "");
                    if (text.matches("[a-zA-Z0-9 ]+")) {
                        upDateValue = text;
                        mMember.setMemberAddress(upDateValue);
                        return true;
                    }
                    return false;
                case R.id.member_phone_textinputlayout:
                    text = text.replaceAll("\\s+", "");
                    if (text.matches("(?:\\+9[1-9])?[7-9][0-9]{9}")) {
                        upDateValue = text;
                        mMember.setMemberPhone(upDateValue);
                        return true;
                    }
                    return false;
            }
            return false;
        }

        private void upDateDatabase(String column) {
            FireBaseHandler.getInstance(getActivity())
                    .updateMember(mMember.getMemberId(), upDateValue, column);
        }
    }
}