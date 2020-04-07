package com.abhinav.chauhan.practice.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abhinav.chauhan.practice.Model.FeeRecord;
import com.abhinav.chauhan.practice.R;
import com.abhinav.chauhan.practice.database.FireBaseHandler;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class AllMembersRecordsFragment extends Fragment {

    private static RecyclerView mRecyclerView;
    private static FirestoreRecyclerAdapter<FeeRecord, Holder> mAdapter;
    private ProgressBar mProgressBar;

    public static AllMembersRecordsFragment newInstance() {
        AllMembersRecordsFragment fragment = new AllMembersRecordsFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFirebaseRecyclerAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.recycler_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressBar = view.findViewById(R.id.progressbar);
        setRecyclerView(view);
    }

    private void setRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setupFirebaseRecyclerAdapter() {
        Query query = FireBaseHandler.getInstance(getActivity()).getFeeReference().orderBy("date", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<FeeRecord> options = new FirestoreRecyclerOptions.Builder<FeeRecord>()
                .setLifecycleOwner(this)
                .setQuery(query, FeeRecord.class).build();
        mAdapter = new FirestoreRecyclerAdapter<FeeRecord, Holder>(options) {
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
            }
        };

    }


    private static class Holder extends RecyclerView.ViewHolder {

        TextView mName;
        TextView mDate;
        TextView mAmount;
        String mId;

        Holder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fee_records_row, parent, false));
            mName = itemView.findViewById(R.id.name_in_allMembersRecords);
            mDate = itemView.findViewById(R.id.date_in_allMembersRecords);
            mAmount = itemView.findViewById(R.id.amount_in_allMembersRecords);
        }

        void bind(FeeRecord record) {
            mName.setText(record.getName());
            mDate.setText(record.getDate());
            mAmount.setText(String.valueOf(record.getAmount()));
            mId = record.getId();
        }
    }
}
