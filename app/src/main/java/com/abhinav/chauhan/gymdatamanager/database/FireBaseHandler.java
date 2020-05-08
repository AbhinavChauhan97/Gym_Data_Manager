package com.abhinav.chauhan.gymdatamanager.database;

import android.content.Context;
import android.util.Log;

import com.abhinav.chauhan.gymdatamanager.Model.FeeRecord;
import com.abhinav.chauhan.gymdatamanager.Model.Member;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public final class FireBaseHandler {

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

    public DocumentReference getAboutDeveloper() {
        return sfirestore.collection("users").document("about developer");
    }

    public void nullInstance() {
        sfirebaseHandler = null;
    }

    public DocumentReference getUserReference() {


        Log.d("db", suserReference.getId());
        return suserReference;
    }

    public CollectionReference getMemberReference() {
        return suserReference.collection("members");
    }

    public CollectionReference getFeeReference() {
        return suserReference.collection("fees");
    }

    public StorageReference getMemberImagesReference() {
        return FirebaseStorage.getInstance()
                .getReference().child("member_images")
                .child(suserReference.getId());
    }

    public void addMember(Member member, byte[] thumbImageData, byte[] fullImageData) {
        addMember(member);
        getMemberImagesReference()
                .child(member.getMemberId() + ".jpg").putBytes(thumbImageData);
    }

    public Task addMember(Member member) {
        return getMemberReference().document(member.getMemberId())
                .set(member);
    }

    public Task addMemberFee(Member member, FeeRecord feeRecord) {
        WriteBatch batch = FirebaseFirestore.getInstance().batch();
        DocumentReference memberDoc = getMemberReference().document(member.getMemberId());
        DocumentReference memberFeeDoc = getFeeReference().document();
        batch.update(memberDoc, "noOfFeesSubmittedMonths", member.getNoOfFeesSubmittedMonths());
        batch.set(memberFeeDoc, feeRecord);
        return batch.commit();
    }

    public void deleteMember(final Member member) {
        final WriteBatch batch = sfirestore.batch();
        getFeeReference().whereEqualTo("id", member.getMemberId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        batch.delete(snapshot.getReference());
                    }
                    batch.delete(getMemberReference().document(member.getMemberId()));
                    batch.commit();
                });
        getMemberImagesReference().child(member.getMemberId() + "_200x200.jpg").delete();
        getMemberImagesReference().child(member.getMemberId() + "_700x700.jpg").delete();
        getMemberImagesReference().child(member.getMemberId() + ".jpg").delete();
    }

    public void updateMember(final String memberId, final String updateValue, final String column) {
        final WriteBatch batch = sfirestore.batch();
        batch.update(getMemberReference().document(memberId), column, updateValue);
        if (column.equals("memberName")) {
            getFeeReference()
                    .whereEqualTo("id", memberId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            Log.d("db", snapshot.getId());
                            batch.update(snapshot.getReference(), "name", updateValue);
                        }
                        batch.commit();
                    });
        } else {
            batch.commit();
        }
    }
}
