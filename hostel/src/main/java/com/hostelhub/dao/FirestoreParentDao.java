
package com.hostelhub.dao;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.hostelhub.config.FirebaseConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreParentDao {

    public List<Map<String, Object>> getAllParents() {

        List<Map<String, Object>> parents = new ArrayList<>();

        try {

            QuerySnapshot snapshot = FirebaseConfig
                    .getFireStore()
                    .collection("parents")
                    .get()
                    .get();

            for (QueryDocumentSnapshot doc : snapshot.getDocuments()) {

                Map<String, Object> data = new HashMap<>(doc.getData());

                data.put("documentId", doc.getId());

                parents.add(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return parents;
    }

    public Map<String, Object> getParentByUid(
            String uid) {

        try {

            DocumentSnapshot doc = FirebaseConfig
                    .getFireStore()
                    .collection("parents")
                    .document(uid)
                    .get()
                    .get();

            if (doc.exists()) {

                Map<String, Object> data = new HashMap<>(doc.getData());

                data.put("documentId", doc.getId());

                return data;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateParent(
            String uid,
            String fullName,
            String email,
            String phone,
            String studentName,
            String studentId) {

        try {

            Map<String, Object> data = new HashMap<>();

            data.put("fullName", fullName);
            data.put("email", email);
            data.put("phone", phone);
            data.put("studentName", studentName);
            data.put("studentId", studentId);
            data.put(
                    "updatedAt",
                    FieldValue.serverTimestamp());

            FirebaseConfig
                    .getFireStore()
                    .collection("parents")
                    .document(uid)
                    .update(data)
                    .get();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteParent(String uid) {

        try {

            FirebaseConfig
                    .getFireStore()
                    .collection("parents")
                    .document(uid)
                    .delete()
                    .get();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<String, Object>> getAllVisits() {

        List<Map<String, Object>> visits = new ArrayList<>();

        try {

            QuerySnapshot snapshot = FirebaseConfig
                    .getFireStore()
                    .collection("visits")
                    .get()
                    .get();

            for (QueryDocumentSnapshot doc : snapshot.getDocuments()) {

                Map<String, Object> data = new HashMap<>(doc.getData());

                data.put("documentId", doc.getId());

                visits.add(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return visits;
    }

    public boolean updateVisitStatus(
            String visitId,
            String status,
            String rejectionReason) {

        try {

            Map<String, Object> data = new HashMap<>();

            data.put("status", status);

            data.put(
                    "updatedAt",
                    FieldValue.serverTimestamp());

            if ("Rejected".equalsIgnoreCase(status)) {

                data.put(
                        "rejectionReason",
                        rejectionReason == null
                                ? ""
                                : rejectionReason.trim());

            } else {

                data.put(
                        "rejectionReason",
                        "");
            }

            FirebaseConfig
                    .getFireStore()
                    .collection("visits")
                    .document(visitId)
                    .update(data)
                    .get();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
