package com.hostelhub.dao;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.hostelhub.config.FirebaseConfig;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class MessMenuDao {

    private final Firestore db;

    public MessMenuDao() {
        db = FirebaseConfig.getFireStore();
    }

    public boolean saveTodayMenu(
            String breakfast,
            String lunch,
            String snacks,
            String dinner,
            String wardenUid) {
        try {
            Map<String, Object> data = new HashMap<>();

            data.put("breakfast", breakfast);
            data.put("lunch", lunch);
            data.put("snacks", snacks);
            data.put("dinner", dinner);
            data.put("updatedBy", wardenUid);
            data.put(
                    "updatedAt",
                    FieldValue.serverTimestamp());

            db.collection("messMenu")
                    .document(LocalDate.now().toString())
                    .set(data)
                    .get();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Object> getTodayMenu() {
        try {
            DocumentSnapshot document = db.collection("messMenu")
                    .document(
                            LocalDate.now().toString())
                    .get()
                    .get();

            if (document.exists()) {
                return document.getData();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}