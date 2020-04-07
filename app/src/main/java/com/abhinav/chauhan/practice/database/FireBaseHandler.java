package com.abhinav.chauhan.practice.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.abhinav.chauhan.practice.Model.FeeRecord;
import com.abhinav.chauhan.practice.Model.Member;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FireBaseHandler {

    private static FireBaseHandler sfirebaseHandler;
    private static FirebaseFirestore sfirestore;
    private static DocumentReference suserReference;
    private Context mApplicationContext;

    private FireBaseHandler(Context context) {
        mApplicationContext = context.getApplicationContext();
        sfirestore = FirebaseFirestore.getInstance();
        suserReference = sfirestore.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public static FireBaseHandler getInstance(Context context) {
        if (sfirebaseHandler == null) {
            sfirebaseHandler = new FireBaseHandler(context);
        }
        return sfirebaseHandler;
    }

    public DocumentReference getUserReference() {
        return suserReference;
    }

    public void setUserReference(DocumentReference userReference) {
        suserReference = userReference;
    }

    public CollectionReference getMemberReference() {
        return suserReference.collection("members");
    }

    public CollectionReference getFeeReference() {
        return suserReference.collection("fees");
    }

    public StorageReference getMemberImagesReference() {
        return FirebaseStorage.getInstance().getReference().child("member_images");
    }

    public boolean addMember(Member member) {

        getMemberReference().document(member.getMemberId()).set(member);
        return true;
    }

    public boolean addMemberFee(Member member, FeeRecord feeRecord) {
        WriteBatch batch = FirebaseFirestore.getInstance().batch();
        DocumentReference memberDoc = getMemberReference().document(member.getMemberId());
        DocumentReference memberFeeDoc = getFeeReference().document();
        batch.update(memberDoc, "noOfFeesSubmittedMonths", member.getNoOfFeesSubmittedMonths());
        batch.set(memberFeeDoc, feeRecord);
        batch.commit().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("db", "falied");
            }
        });
        return true;
    }

    public boolean deleteMember(Member member) {

        getMemberImagesReference().child(member.getMemberId() + "t.jpg").delete();
        getMemberImagesReference().child(member.getMemberId() + "f.jpg").delete();
        return true;
    }

    public boolean updateMember(final Member member, String column) {

        return true;
    }

    public boolean deleteMemberWithFeeRecords(Member member) {

        return true;
    }
}
