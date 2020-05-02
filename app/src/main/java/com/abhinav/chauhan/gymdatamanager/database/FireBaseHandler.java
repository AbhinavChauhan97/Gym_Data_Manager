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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

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

    public CollectionReference getFirestoreImagesReference() {
        return suserReference.collection("images");
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
                .child(member.getMemberId() + "t.jpg").putBytes(thumbImageData);
        getMemberImagesReference()
                .child(member.getMemberId() + "f.jpg").putBytes(fullImageData);
        HashMap<String, Object> imageMap = new HashMap<>();
        imageMap.put("t", member.getMemberId() + "t.jpg");
        imageMap.put("f", member.getMemberId() + "f.jpg");
        getFirestoreImagesReference().add(imageMap);
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
                    getFirestoreImagesReference().document(member.getMemberId() + "t.jpg")
                            .addSnapshotListener((documentSnapshot, e) -> {
                                if (e != null) {
                                    batch.delete(documentSnapshot.getReference());
                                    batch.delete(getFirestoreImagesReference().document(member.getMemberId() + "f.jpg"));
                                }
                            });

                    batch.delete(getMemberReference().document(member.getMemberId()));
                    batch.commit();
                });
        getMemberImagesReference().child(member.getMemberId() + "t.jpg").delete();
        getMemberImagesReference().child(member.getMemberId() + "f.jpg").delete();
    }

    public void updateMember(final String memberId, final String updateValue, final String column) {
        final WriteBatch batch = sfirestore.batch();
        getFeeReference().whereEqualTo("id", memberId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        batch.update(snapshot.getReference(), column, updateValue);
                    }
                    if (column.equals("memberName")) {
                        final String name = updateValue.toUpperCase();
                        batch.update(getMemberReference().document(memberId), column, name);
                        getFeeReference().whereEqualTo("id", memberId)
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots1) {
                                        batch.update(snapshot.getReference(), "name", name);
                                    }
                                    batch.commit();
                                });
                    } else
                        batch.commit();
                });

    }

    public Task<Void> deleteUserData() {

        // DocumentReference root = getUserReference();
        // CollectionReference members = getUserReference().collection("members");
        return null;

    }

    public Task<QuerySnapshot> deleteUserImages() {

        return null;
       /* return getFirestoreImagesReference()
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        getMemberImagesReference().child(snapshot.get("t").toString()).delete();
                        getMemberImagesReference().child(snapshot.get("f").toString()).delete();
                    }
                });*/

    }
}
