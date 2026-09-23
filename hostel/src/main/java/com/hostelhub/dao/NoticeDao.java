package com.hostelhub.dao;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.hostelhub.config.FirebaseConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoticeDao {

    private final Firestore db;

    public NoticeDao() {
        db = FirebaseConfig.getFireStore();
    }

    public boolean addNotice(
            String title,
            String message,
            String wardenUid) {

        try {

            Map<String, Object> data =
                    new HashMap<>();

            data.put(
                    "title",
                    title);

            data.put(
                    "message",
                    message);

            data.put(
                    "createdBy",
                    wardenUid);

            data.put(
                    "audience",
                    "ALL");

            data.put(
                    "targetRole",
                    "ALL");

            data.put(
                    "createdAt",
                    FieldValue.serverTimestamp());

            db.collection("notices")
                    .add(data)
                    .get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    public List<Map<String, Object>> getNotices() {

        List<Map<String, Object>> notices =
                new ArrayList<>();

        try {

            QuerySnapshot snapshot =
                    db.collection("notices")
                            .get()
                            .get();

            for (QueryDocumentSnapshot doc :
                    snapshot.getDocuments()) {

                Map<String, Object> data =
                        new HashMap<>(
                                doc.getData());

                data.put(
                        "noticeId",
                        doc.getId());

                data.put(
                        "id",
                        doc.getId());

                notices.add(data);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return notices;
    }

    public boolean updateNotice(
            String noticeId,
            String title,
            String message) {

        try {

            Map<String, Object> update =
                    new HashMap<>();

            update.put(
                    "title",
                    title);

            update.put(
                    "message",
                    message);

            update.put(
                    "audience",
                    "ALL");

            update.put(
                    "targetRole",
                    "ALL");

            update.put(
                    "updatedAt",
                    FieldValue.serverTimestamp());

            db.collection("notices")
                    .document(noticeId)
                    .update(update)
                    .get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    public boolean deleteNotice(
            String noticeId) {

        try {

            DocumentReference ref =
                    db.collection("notices")
                            .document(noticeId);

            ref.delete().get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
}