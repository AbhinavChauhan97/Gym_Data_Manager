package com.abhinav.chauhan.practice.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abhinav.chauhan.practice.Activities.PreferenceActivity;
import com.abhinav.chauhan.practice.Dialogs.MemberNotFoundDialog;
import com.abhinav.chauhan.practice.Model.Member;
import com.abhinav.chauhan.practice.Preferences.EditPreferences;
import com.abhinav.chauhan.practice.R;
import com.abhinav.chauhan.practice.database.FireBaseHandler;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.Query;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import static com.google.firebase.firestore.Query.Direction.ASCENDING;
import static com.google.firebase.firestore.Query.Direction.DESCENDING;


public class MembersNamesRecyclerViewFragment extends Fragment {

    private final String MEMBER_NOT_FOUND_DIALOG = "Member Not Found";
    private CallBacks mCallBacks;
    private FirestoreRecyclerAdapter<Member, Holder> mAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private int mSelectedSorting;

    public static MembersNamesRecyclerViewFragment newInstance() {
        return new MembersNamesRecyclerViewFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallBacks = (CallBacks) context;
        Log.d("db", "onAttach");
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
        Log.d("db", "onstart");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mSelectedSorting = EditPreferences
                .getInstance()
                .getUserPreference(getActivity())
                .getInt(getString(R.string.selected_sorting), 0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.d("db", "oncreateview");
        return inflater.inflate(R.layout.recycler_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("db", "onviewCreated");
        progressBar = view.findViewById(R.id.progressbar);
        setMembersNameRecyclerView(view);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.d("db", "oncreateOptionsMenu");
        inflater.inflate(R.menu.entry_screen_menu, menu);
        setSearchView(menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Log.d("db", "onprepareOptionmenu");
        menu.getItem(0).getSubMenu()
                .getItem(mSelectedSorting / 2)
                .getSubMenu()
                .getItem(mSelectedSorting % 2)
                .setChecked(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("db", "onOptionsItemSelected");
        class SortingSelector implements MenuItem.OnMenuItemClickListener {
            private int selectedSorting;

            private SortingSelector(int selectedSorting) {
                this.selectedSorting = selectedSorting;
            }

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                item.setChecked(true);
                mSelectedSorting = this.selectedSorting;
                mRecyclerView.setAdapter(setupFirestoreRecyclerAdapter(null));
                EditPreferences.getInstance()
                        .getUserPreference(getActivity())
                        .edit()
                        .putInt(getString(R.string.selected_sorting), this.selectedSorting)
                        .apply();

                return true;
            }
        }
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(getActivity(), PreferenceActivity.class));
                return true;
            case R.id.by_name:
                item.getSubMenu().getItem(0).setOnMenuItemClickListener(new SortingSelector(0));
                item.getSubMenu().getItem(1).setOnMenuItemClickListener(new SortingSelector(1));
                return true;
            case R.id.by_fees:
                item.getSubMenu().getItem(0).setOnMenuItemClickListener(new SortingSelector(2));
                item.getSubMenu().getItem(1).setOnMenuItemClickListener(new SortingSelector(3));
                return true;
            case R.id.by_joining_date:
                item.getSubMenu().getItem(0).setOnMenuItemClickListener(new SortingSelector(4));
                item.getSubMenu().getItem(1).setOnMenuItemClickListener(new SortingSelector(5));

        }
        return true;
    }

    private FirestoreRecyclerOptions<Member> getFirebaseRecyclerOptions(Query baseQuery) {
        FirestoreRecyclerOptions<Member> options = new FirestoreRecyclerOptions.Builder<Member>()
                .setQuery(baseQuery, Member.class)
                .build();
        return options;
    }

    private FirestoreRecyclerAdapter<Member, Holder> getFireBaseRecyclerAdapter(FirestoreRecyclerOptions<Member> options) {
        return new FirebaseAdapter(options, false);
    }

    private FirestoreRecyclerAdapter<Member, Holder> setupFirestoreRecyclerAdapter(Query query) {
        FirestoreRecyclerOptions<Member> options;
        if (query == null) {
            query = FireBaseHandler.getInstance(getActivity())
                    .getMemberReference();
            Log.d("db", String.valueOf(mSelectedSorting).concat("selected sorting"));
            switch (mSelectedSorting) {
                case 1:
                    options = getFirebaseRecyclerOptions(query.orderBy("memberName", DESCENDING));
                    break;
                case 2:
                    options = getFirebaseRecyclerOptions(query.orderBy("noOfFeesSubmittedMonths", ASCENDING)
                            .orderBy("memberJoiningDate", ASCENDING));
                    break;
                case 3:
                    options = getFirebaseRecyclerOptions(query.orderBy("noOfFeesSubmittedMonths", DESCENDING)
                            .orderBy("memberJoiningDate"));
                    break;
                case 4:
                    options = getFirebaseRecyclerOptions(query.orderBy("memberJoiningDate", ASCENDING));
                    break;
                case 5:
                    options = getFirebaseRecyclerOptions(query.orderBy("memberJoiningDate", DESCENDING));
                    break;
                default:
                    options = getFirebaseRecyclerOptions(query.orderBy("memberName", ASCENDING));
            }
        } else {
            options = getFirebaseRecyclerOptions(query);
        }

        mAdapter = getFireBaseRecyclerAdapter(options);
        mAdapter.startListening();
        return mAdapter;
    }

    private void setMembersNameRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(setupFirestoreRecyclerAdapter(null));
    }

    private void setSearchView(Menu menu) {

        MenuItem searchMenuItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mRecyclerView.setAdapter(setupFirestoreRecyclerAdapter(FireBaseHandler.getInstance(getActivity())
                        .getMemberReference()
                        .whereEqualTo("memberName", query)));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mRecyclerView.setAdapter(setupFirestoreRecyclerAdapter(FireBaseHandler.getInstance(getActivity())
                        .getMemberReference()
                        .orderBy("memberName")
                        .startAt(newText)));
                return true;
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
        Log.d("db", "onstop");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBacks = null;
    }

    public interface CallBacks {
        void onMemberNamesPressed(Member member);
    }

    private class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private String mUri;
        private Member mMember;
        private TextView mName;
        private TextView mDaysLeft;
        private ImageView mArrow;
        private CircularImageView mThumbnailImage;

        Holder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.member_name, parent, false));
            mName = itemView.findViewById(R.id.member_name_textview);
            mDaysLeft = itemView.findViewById(R.id.days);
            mArrow = itemView.findViewById(R.id.arrow);
            mThumbnailImage = itemView.findViewById(R.id.thumbnail_pic);
            itemView.setOnClickListener(this);
        }

        void bind(Member member, long daysLeft) {
            mMember = member;
            if (daysLeft > 0)
                mArrow.setBackground(getResources().getDrawable(R.drawable.ic_arrow_upward_black_24dp));
            else
                mArrow.setBackground(getResources().getDrawable(R.drawable.ic_arrow_downward_black_24dp));
            mName.setText(member.getMemberName());
            mDaysLeft.setText(String.valueOf(daysLeft));
            if (member.isHasImage()) {
                FireBaseHandler.getInstance(getActivity()).getMemberImagesReference().child(member.getMemberId() + "t.jpg")
                        .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        mUri = uri.toString();
                        Picasso.with(getActivity())
                                .load(mUri)
                                .placeholder(R.drawable.ic_person_black_24dp)
                                .fit().centerCrop().into(mThumbnailImage);
                    }
                });
            }
        }

        @Override
        public void onClick(View v) {
            mCallBacks.onMemberNamesPressed(mMember);
        }
    }

    private class FirebaseAdapter extends FirestoreRecyclerAdapter<Member, Holder> {
        boolean mIsSubmitAdapter;

        public FirebaseAdapter(@NonNull FirestoreRecyclerOptions<Member> options, boolean isSubmitAdapter) {
            super(options);
            progressBar.setVisibility(View.VISIBLE);
            this.mIsSubmitAdapter = isSubmitAdapter;
        }

        @Override
        protected void onBindViewHolder(@NonNull Holder holder, int i, @NonNull Member member) {
            holder.bind(member, member.getDaysLeftForFeeSubmission());
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new Holder(inflater, parent);
        }

        @Override
        public void onDataChanged() {
            super.onDataChanged();
            progressBar.setVisibility(View.INVISIBLE);
            if (mIsSubmitAdapter && getItemCount() == 0)
                new MemberNotFoundDialog().show(getFragmentManager(), MEMBER_NOT_FOUND_DIALOG);
        }
    }
}
