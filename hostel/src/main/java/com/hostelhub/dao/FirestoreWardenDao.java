package com.hostelhub.dao;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.hostelhub.config.FirebaseConfig;

import com.hostelhub.model.Warden;

public class FirestoreWardenDao {

    private final Firestore db;

    public FirestoreWardenDao() {
        db = FirebaseConfig.getFireStore();
    }

    // Existing method
    public boolean saveWarden(Warden warden) {

        try {

            db.collection("wardens")
                    .document(warden.getUid())
                    .set(warden)
                    .get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // This method is used by WardenSignup
    public boolean saveWarden(
            String uid,
            String name,
            String email,
            String phone) {

        try {

            Warden warden = new Warden(
                    uid,
                    name,
                    email,
                    phone,
                    "warden");

            db.collection("wardens")
                    .document(uid)
                    .set(warden)
                    .get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    public Warden getWardenByUid(String uid) {

        try {

            DocumentSnapshot document = db.collection("wardens")
                    .document(uid)
                    .get()
                    .get();

            if (!document.exists()) {

                return null;
            }

            return document.toObject(
                    Warden.class);

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }
}