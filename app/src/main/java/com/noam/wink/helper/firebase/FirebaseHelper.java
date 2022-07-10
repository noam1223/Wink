package com.noam.wink.helper.firebase;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.noam.wink.helper.singleton.SingletonUser;
import com.noam.wink.model.User;

import java.util.HashMap;
import java.util.Map;

public class FirebaseHelper {

    public static void isUserSigned(FirebaseListener firebaseListener, String userID) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {

                    User user = task.getResult().toObject(User.class);


                    if (user != null) {
                        SingletonUser.getInstance().setUser(user);
                    } else SingletonUser.getInstance().setUser(new User());

                    firebaseListener.retrieveUserData(null);

                } else if (task.getException() != null) {
                    firebaseListener.retrieveUserData(task.getException().getMessage());
                } else {
                    SingletonUser.getInstance().setUser(new User());
                    firebaseListener.retrieveUserData(null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                firebaseListener.retrieveUserData(e.getLocalizedMessage());
            }
        });
    }


    public static void writeUserDB() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userID = FirebaseAuth.getInstance().getUid();
        User user = SingletonUser.getInstance().getUser();

        db.collection("users").document(userID).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });


    }


}
