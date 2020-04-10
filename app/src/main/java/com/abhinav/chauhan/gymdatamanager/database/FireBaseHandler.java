package com.abhinav.chauhan.gymdatamanager.database;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.abhinav.chauhan.gymdatamanager.Model.FeeRecord;
import com.abhinav.chauhan.gymdatamanager.Model.Member;
import com.abhinav.chauhan.gymdatamanager.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
        Log.d("db", "instance created");
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
        return suserReference;
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

    public void addMember(Member member) {
        getMemberReference().document(member.getMemberId())
                .set(member)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(mApplicationContext, R.string.member_added, Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mApplicationContext, R.string.couldnt_add_member, Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void addMemberFee(Member member, FeeRecord feeRecord) {
        WriteBatch batch = FirebaseFirestore.getInstance().batch();
        DocumentReference memberDoc = getMemberReference().document(member.getMemberId());
        DocumentReference memberFeeDoc = getFeeReference().document();
        batch.update(memberDoc, "noOfFeesSubmittedMonths", member.getNoOfFeesSubmittedMonths());
        batch.set(memberFeeDoc, feeRecord);
        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(mApplicationContext, R.string.fees_submitted, Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mApplicationContext, R.string.couldnt_submit_fee, Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void deleteMember(Member member) {

        final WriteBatch batch = FirebaseFirestore.getInstance().batch();
        DocumentReference memberDoc = getMemberReference().document(member.getMemberId());
        batch.delete(memberDoc);
        getFeeReference().whereEqualTo("id", member.getMemberId()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            batch.delete(doc.getReference());
                        }
                    }
                });

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(mApplicationContext, R.string.member_deleted, Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mApplicationContext, R.string.couldnt_delete_member, Toast.LENGTH_LONG).show();
            }
        });
        getMemberImagesReference().child(member.getMemberId() + "t.jpg").delete();
        getMemberImagesReference().child(member.getMemberId() + "f.jpg").delete();
    }

    public void updateMember(final Member member, String column) {
        getMemberReference().document(member.getMemberId())
                .update(column, member)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(mApplicationContext, R.string.data_updated, Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mApplicationContext, R.string.couldnt_update, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
